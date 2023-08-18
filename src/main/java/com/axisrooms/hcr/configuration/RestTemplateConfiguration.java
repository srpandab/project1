package com.axisrooms.hcr.configuration;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Configuration
public class RestTemplateConfiguration {

    @Value("${max.total.connections}")
    private Integer maxTotalConnections;

    @Value("${max.connections.per.route}")
    private Integer maxConnectionsPerRoute;

    @Value("${connection.timeout.milliseconds}")
    private Integer connectionTimeoutMilliSeconds;

    @Value("${socket.timeout.milliseconds}")
    private Integer socketTimeoutMilliSeconds;

    @Bean
    public RestTemplate restTemplate() {

        try {

            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();

            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(maxTotalConnections);
            connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
            RequestConfig requestConfig = RequestConfig
                    .custom()
                    .setConnectTimeout(connectionTimeoutMilliSeconds)
                    .setConnectionRequestTimeout(connectionTimeoutMilliSeconds)
                    .setSocketTimeout(socketTimeoutMilliSeconds)
                    .build();
            CloseableHttpClient httpClient = HttpClientBuilder
                    .create()
                    .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
                    .setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            return new RestTemplateBuilder()
                    .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
                    .errorHandler(new ResponseErrorHandler() {
                                      @Override
                                      public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
                                          return (httpResponse.getStatusCode().series() == CLIENT_ERROR ||
                                                  httpResponse.getStatusCode().series() == SERVER_ERROR);
                                      }

                                      @Override
                                      public void handleError(ClientHttpResponse httpResponse) {
                                      }
                    }).build();

        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
            return new RestTemplate();
        }
    }
}
