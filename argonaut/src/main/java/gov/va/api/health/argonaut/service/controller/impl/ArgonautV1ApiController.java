package gov.va.api.health.argonaut.service.controller.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class ArgonautV1ApiController {

    @RequestMapping(
            value = {"/v1/hello"},
            produces = {"application/json"},
            method = RequestMethod.GET
    )
    @SneakyThrows
    public Map<String, String> hello(ServerWebExchange exchange) {
        Map<String, String> payload = new HashMap<>();
        payload.put("message", "Hello World!");
        return payload;
    }

}
