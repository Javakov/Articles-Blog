package org.articlesblog.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "gigaChatAuthClient", url = "https://ngw.devices.sberbank.ru:9443")
public interface GigaChatAuthFeignClient {

    @PostMapping(value = "/api/v2/oauth", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String getAccessToken(@RequestHeader("Authorization") String authorization, @RequestHeader("RqUID") String rqUID, @RequestBody String body);
}

