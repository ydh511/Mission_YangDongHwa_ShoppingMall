package com.example.shoppingMall.config;


import com.example.shoppingMall.ncp.CAPTCHA.NcpCAPTCHAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@Configuration
public class NcpClientConfig {
    // NCP Map API Rest Client
    private static final String NCP_APIGW_KEY_ID = "X-NCP-APIGW-API-KEY-ID";
    private static final String NCP_APIGW_KEY = "X-NCP-APIGW-API-KEY";
    @Value("${ncp.api.client-id}")
    private String ncpClientId;
    @Value("${ncp.api.client-secret}")
    private String ncpClientSecret;

    // GET CAPTCHA key, compare CAPTCHA key
    @Bean
    public RestClient ncpCaptchaClient() {
        return RestClient.builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/")
                .defaultHeader(NCP_APIGW_KEY_ID, ncpClientId)
                .defaultHeader(NCP_APIGW_KEY, ncpClientSecret)
                .build();
    }
    @Bean
    public NcpCAPTCHAService CaptchaApiService() {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(ncpCaptchaClient()))
                .build()
                .createClient(NcpCAPTCHAService.class);
    }

    // GET CAPTCHA img
    @Bean
    public RestClient ncpCaptchaImgClient() {
        return RestClient.builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/")
                .defaultHeader(NCP_APIGW_KEY_ID, ncpClientId)
                .defaultHeader(NCP_APIGW_KEY, ncpClientSecret)
                .build();
    }
    @Bean
    public NcpCAPTCHAService CaptchaImgApiService() {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(ncpCaptchaImgClient()))
                .build()
                .createClient(NcpCAPTCHAService.class);
    }
}

















