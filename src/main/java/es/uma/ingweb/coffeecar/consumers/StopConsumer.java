package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.entities.BusStop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class StopConsumer {
    @Value("${server.url}")
    private String SERVER_URL;
    private String GET_ALL_STOP;
    private final String GET_ALL_NEARBY_STOPS;

    private final RestTemplate restTemplate;

    public StopConsumer(RestTemplate restTemplate) {
        this.restTemplate = new RestTemplate();
        GET_ALL_STOP = SERVER_URL + "getStops/all";
        GET_ALL_NEARBY_STOPS = SERVER_URL + "getStops/near?lat={lat}&lon={lon}";
    }

    public List<BusStop> getAll() {
        final CollectionModel<BusStop> stopResponse = restTemplate
              .exchange(GET_ALL_STOP, HttpMethod.GET, null,
                    getParameterizedTypeReference()
              ).getBody();
        return new ArrayList<>(stopResponse.getContent());
    }

    public List<BusStop> getNearby(double lat, double lon) {
        final BusStop[] stopResponse = restTemplate
              .getForObject(SERVER_URL + "stops/near?lat={lat}&lon={lon}",
                    BusStop[].class,
                    Map.of("lat", lat,
                          "lon", lon)
              );
        return Arrays.asList(stopResponse);
    }

    private static ParameterizedTypeReference<CollectionModel<BusStop>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
