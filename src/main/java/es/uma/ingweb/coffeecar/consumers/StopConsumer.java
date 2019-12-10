package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.entities.BusStop;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class StopConsumer {
    @Value("${server.url}")
    private String SERVER_URL;

    private final RestTemplate restTemplate;

    public StopConsumer(RestTemplate restTemplate) {
        this.restTemplate = new RestTemplate();
    }

    public List<BusStop> getAll() {
        final CollectionModel<BusStop> stopResponse = restTemplate
              .exchange(SERVER_URL+ "stops", HttpMethod.GET, null,
                    getParameterizedTypeReference()
              ).getBody();
        return new ArrayList<>(stopResponse.getContent());
    }

    public List<BusStop> getNearby(double lat, double lon) {
        final BusStop[] stopResponse = restTemplate
              .getForObject(SERVER_URL + "stops/search/findNearby?lat={lat}&lon={lon}",
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
