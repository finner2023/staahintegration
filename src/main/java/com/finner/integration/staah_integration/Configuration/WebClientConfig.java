package com.finner.integration.staah_integration.Configuration;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;


@Configuration
public class WebClientConfig {
   @Value("${staah.reservations.endpoint}")
    private String staahBaseUrl;
 @Value("${staah.api.timeout:5}")
   private int timeoutSeconds;
// @Value("${pms.api.base-url}")
 private String pmsBaseUrl;
 @Bean
   public HttpClient reactorHttpClient(){
     return  HttpClient.create()
               .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,timeoutSeconds*1000)
               .doOnConnected(conn->conn
                       .addHandlerLast(new ReadTimeoutHandler(timeoutSeconds, TimeUnit.SECONDS))
                       .addHandlerLast(new WriteTimeoutHandler(timeoutSeconds,TimeUnit.SECONDS))
               );
   }
   @Bean
   public WebClient staahWebClient(@Qualifier("reactorHttpClient") HttpClient httpClient){

    return WebClient.builder()
            .baseUrl(staahBaseUrl)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
   }
   @Bean
   public WebClient pmsWebClient(@Qualifier("reactorHttpClient") HttpClient httpClient){
       return WebClient.builder()
               .baseUrl(pmsBaseUrl)
               .clientConnector(new ReactorClientHttpConnector(httpClient))
               .build();
   }
}
