package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.entities.Announcement;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnnouncementConsumer {
    private static final String GET_ALL_ANNOUNCEMENTS_URL = "http://localhost:8080/announced";
    private static final String GET_MY_ANNOUNCEMENTS_URL = "http://localhost:8080/announced/search";

    @Autowired
    private RestTemplate restTemplate;

    public List<Announcement> getAll(){
        final ResponseEntity<PagedModel<Announcement>> announcementResponse =
                restTemplate.exchange(
                        GET_ALL_ANNOUNCEMENTS_URL,
                        HttpMethod.GET,
                        null,
                        getParameterizedTypeReference()
                );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
    }

    private static ParameterizedTypeReference<PagedModel<Announcement>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {};
    }
}
