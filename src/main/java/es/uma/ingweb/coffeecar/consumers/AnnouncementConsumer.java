package es.uma.ingweb.coffeecar.consumers;

import com.fasterxml.jackson.databind.JsonNode;
import es.uma.ingweb.coffeecar.entities.Announce;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.hateoas.MediaTypes.HAL_JSON;

@Service
public class AnnouncementConsumer {
    @Value("${server.url}")
    private String SERVER_URL;

    private final RestTemplate restTemplate;
    private final Traverson traverson;


    public AnnouncementConsumer(RestTemplate restTemplate, Traverson traverson) {
        this.restTemplate = restTemplate;
        this.traverson = traverson;
    }

    public Announce getAnnouncementByURI(String uri) {
        ResponseEntity<EntityModel<Announce>> announcementResponseEntity = restTemplate
              .exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                      getAnnounceEntityModelParameterizedTypeReference()
              );

        Announce announce = Objects.requireNonNull(announcementResponseEntity.getBody()).getContent();
        announce.add(announcementResponseEntity.getBody().getLinks());

        return setParams(announce);
    }

    public List<Announce> getAvailableAnnouncements(String email) {
        Traverson.TraversalBuilder traversalBuilder = traverson
                .follow("announces")
                .follow("search")
                .follow("findAvailableAnnounces")
                .withTemplateParameters(Map.of("email", email));

        PagedModel<Announce> announces = traversalBuilder.toObject(getParameterizedTypeReference());

        return setParams(new ArrayList<>(Objects.requireNonNull(announces).getContent()));
    }

    public List<Announce> getMyTrips(String email) {
        Traverson.TraversalBuilder traversalBuilder = traverson
                .follow("announces")
                .follow("search")
                .follow("findUserTrips")
                .withTemplateParameters(Map.of("email", email));

        CollectionModel<Announce> announces = traversalBuilder.toObject(getParameterizedTypeReference());

        return new ArrayList<>(Objects.requireNonNull(announces).getContent());
    }

    private Announce setParams(Announce announcement){
        String driver = announcement.getLink("driver").map(Link::getHref).get();

        announcement.setDriver(getDriver(URI.create(driver)));

        return announcement;
    }

    private List<Announce> setParams(List<Announce> announcements){
        List<Announce> result = new ArrayList<>();
        announcements.forEach(announce -> result.add(setParams(announce)));

        return result;
    }

    public User getDriver(URI uri){
        return Objects.requireNonNull(new Traverson(uri, HAL_JSON)
                .follow("self")
                .toObject(getUserEntityModelParameterizedTypeReference())).getContent();
    }

    public URI create(JsonNode announcement) {
        return restTemplate.postForLocation(SERVER_URL + "announces", announcement);
    }

    public void edit(Announce announce) {
        restTemplate.put(announce.getLink("self").map(Link::getHref).get(), new HttpEntity(announce), Announce.class
                , announce.getId());
    }
    public void delete(String uri) {
          restTemplate.delete(uri);
    }


    private static ParameterizedTypeReference<PagedModel<Announce>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }

    private static ParameterizedTypeReference<EntityModel<Announce>> getAnnounceEntityModelParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }

    private static ParameterizedTypeReference<EntityModel<User>> getUserEntityModelParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
