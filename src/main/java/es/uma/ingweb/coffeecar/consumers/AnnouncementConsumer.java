package es.uma.ingweb.coffeecar.consumers;

import com.fasterxml.jackson.databind.JsonNode;
import es.uma.ingweb.coffeecar.RestTemplateProxy;
import es.uma.ingweb.coffeecar.entities.Announce;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class AnnouncementConsumer {
    private static final String URL = "http://localhost:8080/announced";
    private static final String GET_ANNOUNCEMENTS_BY_DRIVER_URL = "http://localhost:8080/announced/search/findAnnouncesByDriver?user={user}";
    private static final String GET_ANNOUNCEMENTS_BY_PASSENGER_URL = "http://localhost:8080/announced/search/findAnnouncesByPassengers?user={user}";
    private static final String GET_ANNOUNCEMENTS_BY_ARRIVAL_DATE_URL = "http://localhost:8080/announced/search/findAnnouncesByArrivalDate?arrivalDate={arrivalDate}";
    private static final String GET_ANNOUNCEMENTS_BY_ARRIVAL_URL = "http://localhost:8080/announced/search/findAnnouncesByArrival?arrival={arrival}";

    private final RestTemplate restTemplate;
    private final RestTemplateProxy restTemplateProxy;

    public AnnouncementConsumer(RestTemplate restTemplate, RestTemplateProxy restTemplateProxy) {
        this.restTemplate = restTemplate;
        this.restTemplateProxy = restTemplateProxy;
    }

    public Announce getAnnouncementByURI(String uri) {
        ResponseEntity<EntityModel<Announce>> announcementResponseEntity = restTemplate
              .exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<EntityModel<Announce>>() {
                    }
              );
        return setURIs(announcementResponseEntity.getBody());
    }

    private Announce setURIs(EntityModel<Announce> resourceAnnouncement) {
        Announce announce = Objects.requireNonNull(resourceAnnouncement).getContent();
        Optional<Link> driver = Objects.requireNonNull(resourceAnnouncement).getLink("driver");
        Optional<Link> passengers = Objects.requireNonNull(resourceAnnouncement).getLink("passengers");

        Objects.requireNonNull(announce).setDriverURI(driver.map(Link::getHref).get());
        Objects.requireNonNull(announce).setPassengersURI(passengers.map(Link::getHref).get());

        return announce;
    }

    public List<Announce> getAll() {
        final ResponseEntity<PagedModel<Announce>> announcementResponse =
              restTemplate.exchange(
                    URL,
                    HttpMethod.GET,
                    null,
                    getParameterizedTypeReference()
              );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
    }

    public List<Announce> getAvailableAnnouncements(String email) {
        return restTemplateProxy.exchange(
              URL + "/search/findAvailableAnnounces?email={email}",
              HttpMethod.GET,
              null,
              getParameterizedTypeReference(),
              email
        )
              .map(HttpEntity::getBody).map(CollectionModel::getContent)
              .map(Collection::stream).map(content -> content.collect(toList()))
              .orElseGet(Collections::emptyList);
    }

    public List<Announce> getByDriver(User user) {
        return restTemplateProxy.exchange(
              GET_ANNOUNCEMENTS_BY_DRIVER_URL,
              HttpMethod.GET,
              null,
              getParameterizedTypeReference(),
              user)
              .map(HttpEntity::getBody).map(CollectionModel::getContent)
              .map(Collection::stream).map(content -> content.collect(toList()))
              .orElse(Collections.emptyList());
    }

    public List<Announce> getByPassenger(User user) {
        return
              restTemplateProxy.exchange(
                    GET_ANNOUNCEMENTS_BY_PASSENGER_URL,
                    HttpMethod.GET,
                    null,
                    getParameterizedTypeReference(),
                    user)
                    .map(HttpEntity::getBody).map(CollectionModel::getContent)
                    .map(Collection::stream).map(content -> content.collect(toList()))
                    .orElse(Collections.emptyList());
    }

    public List<Announce> getMyTrips(String email) {
        return restTemplateProxy.exchange(
              URL + "/findUserTrips?email=email",
              HttpMethod.GET,
              null,
              getParameterizedTypeReference(),
              email)
              .map(HttpEntity::getBody).map(CollectionModel::getContent)
              .map(Collection::stream).map(content -> content.collect(toList()))
              .orElse(Collections.emptyList());
    }

    private List<Announce> sortByDepartureDate(List<Announce> announceList) {
        announceList.sort(Comparator.comparing(Announce::getDepartureTime));
        return announceList;
    }

    public List<Announce> getByArrivalDate(LocalDateTime arrivalDate) {
        final ResponseEntity<PagedModel<Announce>> announcementResponse =
              restTemplate.exchange(
                    GET_ANNOUNCEMENTS_BY_ARRIVAL_DATE_URL,
                    HttpMethod.GET,
                    null,
                    getParameterizedTypeReference(),
                    arrivalDate
              );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
    }

    public List<Announce> getByArrival(String arrival) {
        final ResponseEntity<PagedModel<Announce>> announcementResponse =
              restTemplate.exchange(
                    GET_ANNOUNCEMENTS_BY_ARRIVAL_URL,
                    HttpMethod.GET,
                    null,
                    getParameterizedTypeReference(),
                    arrival
              );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
    }

    public String create(JsonNode announcement) {
        URI uri = restTemplate.postForLocation(URL, announcement);
        return Objects.requireNonNull(uri).getPath();
    }

    public void delete(Announce announce) {
        restTemplate.delete(announce.getSelfURI());
    }

    public void edit(Announce announce) {
        restTemplate.put(URL, announce, Announce.class);
    }

    private static ParameterizedTypeReference<PagedModel<Announce>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
