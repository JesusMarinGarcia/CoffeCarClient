package es.uma.ingweb.coffeecar;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class RestTemplateProxy {
    private final RestTemplate restTemplate;

    public RestTemplateProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> Optional<ResponseEntity<T>> getForEntity(String url, Class<T> responseType, Object... uriVariables) {
        Optional<ResponseEntity<T>> entity = Optional.empty();
        try {
            entity = Optional.of(restTemplate.getForEntity(
                  url,
                  responseType,
                  uriVariables));
        } catch (RestClientException ignored) {
        }
        return entity;
    }

    public <T> Optional<ResponseEntity<T>> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity,
                                                    ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        Optional<ResponseEntity<T>> entity = Optional.empty();
        try {
            entity = Optional.of(restTemplate.exchange(
                  url,
                  method,
                  requestEntity,
                  responseType,
                  uriVariables));
        } catch (RestClientException ignored) {
        }
        return entity;
    }
}