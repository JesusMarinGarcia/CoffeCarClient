package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.entities.Bus;
import javafx.scene.paint.Stop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StopConsumer {
    private static final String GET_ALL_STOP= "http://localhost:8080/getStops/all";
    private static final String GET_ALL_NEARBY_STOPS = "http://localhost:8080/getStops/near?";

    @Autowired
    private RestTemplate restTemplate;

    public List<Stop> getAll() {
        final ResponseEntity<PagedModel<Stop>> stopResponse = restTemplate
                .exchange(GET_ALL_STOP, HttpMethod.GET, null,
                        getParameterizedTypeReference()
                );
        return new ArrayList<>(Objects.requireNonNull(stopResponse.getBody()).getContent());
    }

    public List<Stop> getNearby(float lat, float lon) {
        final ResponseEntity<PagedModel<Stop>> stopResponse = restTemplate
                .exchange(GET_ALL_NEARBY_STOPS.concat("lat=" + lat + "&lon="+ lon),
                        HttpMethod.GET, null,
                        getParameterizedTypeReference()
                );
        return new ArrayList<>(Objects.requireNonNull(stopResponse.getBody()).getContent());
    }

    private static ParameterizedTypeReference<PagedModel<Stop>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {};
    }
}
