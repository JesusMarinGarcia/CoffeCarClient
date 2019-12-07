package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.RestTemplateProxy;
import es.uma.ingweb.coffeecar.entities.Bus;
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
public class BusConsumer {
    private static final String CURRENT_POS_FROM_ALL_BUSES_URL = "http://localhost:8080/getBuses/all";
    private static final String CURRENT_POS_BY_LINE_URL = "http://localhost:8080/getBuses/byLine?line=";


    private RestTemplateProxy restTemplateProxy;

    public BusConsumer(RestTemplateProxy restTemplateProxy) {
        this.restTemplateProxy = restTemplateProxy;
    }

    public List<Bus> getAll() {
        return restTemplateProxy.exchange(
                CURRENT_POS_FROM_ALL_BUSES_URL,
                HttpMethod.GET,
                null,
                getParameterizedTypeReference()
        ).map(HttpEntity::getBody).map(CollectionModel::getContent)
                .map(Collection::stream).map(content -> content.collect(toList()))
                .orElse(Collections.emptyList());
    }

    public List<Bus> getByLine(int codLine) {
        return restTemplateProxy.exchange(
                CURRENT_POS_BY_LINE_URL,
                HttpMethod.GET,
                null,
                getParameterizedTypeReference()
        ).map(HttpEntity::getBody).map(CollectionModel::getContent)
                .map(Collection::stream).map(content -> content.collect(toList()))
                .orElse(Collections.emptyList());
    }

    private static ParameterizedTypeReference<PagedModel<Bus>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {};
    }
}
