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

import java.util.*;

@Service
public class BusConsumer {
    @Value("${server.url}")
    private String SERVER_URL;

    private final RestTemplate restTemplate;

    public BusConsumer(RestTemplate restTemplate) {
        this.restTemplate = new RestTemplate();
    }

    public List<Bus> getAll() {
        final ResponseEntity<PagedModel<Bus>> busResponse = restTemplate
                .exchange(SERVER_URL+ "buses", HttpMethod.GET, null,
                        getParameterizedTypeReference()
                );
        return new ArrayList<>(Objects.requireNonNull(busResponse.getBody()).getContent());
    }

    public List<Bus> getByLine(int codLine) {
        final Bus[] busResponse = restTemplate
                .getForObject(SERVER_URL+ "buses/search/findByLine?line={line}", Bus[].class,
                        Map.of("line", codLine));
        return Arrays.asList(busResponse);
    }

    private static ParameterizedTypeReference<PagedModel<Bus>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {};
    }
}
