package org.articlesblog.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "gigaChatGenClient", url = "https://gigachat.devices.sberbank.ru")
public interface GigaChatGenFeignClient {

    @GetMapping(value = "/api/v1/models")
    String getModel(@RequestHeader("Authorization") String token);

    @PostMapping(value = "/api/v1/chat/completions")
    String getAnswer(@RequestHeader("Authorization") String token, @RequestBody String jsonPayload);
}
