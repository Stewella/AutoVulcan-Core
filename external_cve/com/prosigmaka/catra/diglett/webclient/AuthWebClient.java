package com.prosigmaka.catra.diglett.webclient;

import com.prosigmaka.catra.diglett.configuration.DefaultResponse;
import com.prosigmaka.catra.diglett.exception.CommonException;
import com.prosigmaka.catra.diglett.model.dto.request.LoginRequestDto;
import com.prosigmaka.catra.diglett.model.dto.response.MetapodAccessTokenResponse;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.persistence.Access;

@RestController
@PropertySource("classpath:clients.properties")
@RequestMapping("/auth")
public class AuthWebClient {

    @Value("${config.uri.metapod}")
    private String clientAuthUrl;

    @PostMapping(path = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public DefaultResponse<AccessTokenResponse> getToken(LoginRequestDto loginRequestDto){
        WebClient client = WebClient.create(clientAuthUrl);

        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("username", loginRequestDto.getUsername());
        bodyMap.add("password", loginRequestDto.getPassword());
        Mono<MetapodAccessTokenResponse> result = client.post()
                .uri("/auth/token")
                .body(BodyInserters.fromFormData(bodyMap))
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(MetapodAccessTokenResponse.class);
                    } else {
                        throw new CommonException("Username / Password Salah");
                    }
                });
        MetapodAccessTokenResponse metapodAccessTokenResponse = result.block();
        return DefaultResponse.ok(metapodAccessTokenResponse.getData());
    }
}
