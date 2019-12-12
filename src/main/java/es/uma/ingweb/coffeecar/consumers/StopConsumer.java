package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.entities.BusStop;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StopConsumer {
    @Value("${server.url}")
    private String SERVER_URL;

    private final RestTemplate restTemplate;

    public StopConsumer(RestTemplate restTemplate) {
        this.restTemplate = new RestTemplate();
    }

    public List<BusStop> getLines(BusStop stop) {
        final BusStop[] stopResponse = restTemplate
                .getForObject(SERVER_URL + "stops/search/getLines?codParada={cod}",
                        BusStop[].class,
                        Map.of("cod", stop.getCodParada())
                );
        return Arrays.asList(stopResponse);
    }

    private BusStop setLines(BusStop stop){
        List<Float> lines = getLines(stop)
                .stream()
                .map(BusStop::getCodLinea)
                .collect(Collectors.toList());

        stop.setLineas(StringUtils.join(lines, "<br>"));

        return stop;
    }

    public List<BusStop> getNearby(double lat, double lon) {
        final BusStop[] stopResponse = restTemplate
              .getForObject(SERVER_URL + "stops/search/findNearby?lat={lat}&lon={lon}",
                    BusStop[].class,
                    Map.of("lat", lat,
                          "lon", lon)
              );

        List<BusStop> lala = Arrays.asList(stopResponse).stream().map(this::setLines).collect(Collectors.toList());
        return lala;
    }

    private static ParameterizedTypeReference<CollectionModel<BusStop>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
