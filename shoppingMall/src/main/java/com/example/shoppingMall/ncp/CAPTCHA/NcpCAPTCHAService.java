package com.example.shoppingMall.ncp.CAPTCHA;

import com.example.shoppingMall.ncp.CAPTCHA.CAPTCHAResponce;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Map;

public interface NcpCAPTCHAService {
    @GetExchange("")
    CAPTCHAResponce captcha(
            @RequestParam
            Map<String, Object> params
    );
}
