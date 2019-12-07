package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.RestTemplateProxy;
import es.uma.ingweb.coffeecar.entities.BusStop;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class StopConsumer {
    private static final String GET_ALL_STOP= "http://localhost:8080/getStops/all";
    private static final String GET_ALL_NEARBY_STOPS = "http://localhost:8080/getStops/near?";



    private RestTemplateProxy restTemplateProxy;

    public StopConsumer(RestTemplateProxy restTemplateProxy) {
        this.restTemplateProxy = restTemplateProxy;
    }

    public List<BusStop> getAll() {
        return restTemplateProxy.exchange(
                GET_ALL_STOP,
                HttpMethod.GET,
                null,
                getParameterizedTypeReference()
        ).map(HttpEntity::getBody).map(CollectionModel::getContent).map(Collection::stream).map(content -> content.collect(toList()))
                .orElse(Collections.emptyList());
    }

    public List<BusStop> getNearby(float lat, float lon) {
        return restTemplateProxy.exchange(
                GET_ALL_NEARBY_STOPS.concat("lat=" + lat + "&lon="+ lon),
                HttpMethod.GET,
                null,
                getParameterizedTypeReference()
        ).map(HttpEntity::getBody).map(CollectionModel::getContent)
                .map(Collection::stream).map(content -> content.collect(toList()))
                .orElse(Collections.emptyList());

    }

    private static ParameterizedTypeReference<PagedModel<BusStop>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {};
    }
}
