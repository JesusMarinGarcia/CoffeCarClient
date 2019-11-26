package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.entities.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BusCosumer {
    private static final String GET_ALL_BUSES_CURRENT_POS = "http://localhost:8080/getBuses/all";
    private static final String GET_ALL_BUSES_CURRENT_POS_BY_LINE = "http://localhost:8080/getBuses/byLine?line=";

    @Autowired
    private RestTemplate restTemplate;

    public List<Bus> getAll() {
        final ResponseEntity<PagedModel<Bus>> busResponse = restTemplate
                .exchange(GET_ALL_BUSES_CURRENT_POS, HttpMethod.GET, null,
                        getParameterizedTypeReference()
                );
        return new ArrayList<>(Objects.requireNonNull(busResponse.getBody()).getContent());
    }

    public List<Bus> getByLine(int codLine) {
        final ResponseEntity<PagedModel<Bus>> busResponse = restTemplate
                .exchange(GET_ALL_BUSES_CURRENT_POS.concat(String.valueOf(codLine)), HttpMethod.GET, null,
                        getParameterizedTypeReference()
                );
        return new ArrayList<>(Objects.requireNonNull(busResponse.getBody()).getContent());
    }

    private static ParameterizedTypeReference<PagedModel<Bus>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {};
    }
}
