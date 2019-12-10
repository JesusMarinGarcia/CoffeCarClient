package es.uma.ingweb.coffeecar.consumers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.uma.ingweb.coffeecar.entities.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BusConsumer {
    @Value("${server.url}")
    private String SERVER_URL;
    private String CURRENT_POS_FROM_ALL_BUSES_URL = SERVER_URL + "buses";
    private String CURRENT_POS_BY_LINE_URL = SERVER_URL + "buses/search/findByLine?line=";

    private final RestTemplate restTemplate;

    public BusConsumer(RestTemplate restTemplate) {
        this.restTemplate = new RestTemplate();
    }

    public List<Bus> getAll() {
        final ResponseEntity<PagedModel<Bus>> busResponse = restTemplate
                .exchange(CURRENT_POS_FROM_ALL_BUSES_URL, HttpMethod.GET, null,
                        getParameterizedTypeReference()
                );
        return new ArrayList<>(Objects.requireNonNull(busResponse.getBody()).getContent());
    }

    public List<Bus> getByLine(int codLine) {
        final ResponseEntity<PagedModel<Bus>> busResponse = restTemplate
                .exchange(CURRENT_POS_BY_LINE_URL.concat(String.valueOf(codLine)), HttpMethod.GET, null,
                        getParameterizedTypeReference()
                );
        return new ArrayList<>(Objects.requireNonNull(busResponse.getBody()).getContent());
    }

    private static ParameterizedTypeReference<PagedModel<Bus>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {};
    }
}
