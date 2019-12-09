package es.uma.ingweb.coffeecar.consumers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import es.uma.ingweb.coffeecar.RestTemplateProxy;
import es.uma.ingweb.coffeecar.entities.Announcement;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class AnnouncementConsumer {
    private static final String URL = "http://localhost:8080/announced";
    private static final String GET_ANNOUNCEMENTS_BY_DRIVER_URL = "http://localhost:8080/announced/search/findAnnouncesByDriver?user={user}";
    private static final String GET_ANNOUNCEMENTS_BY_PASSENGER_URL = "http://localhost:8080/announced/search/findAnnouncesByPassengers?user={user}";
    private static final String GET_ANNOUNCEMENTS_BY_ARRIVAL_DATE_URL = "http://localhost:8080/announced/search/findAnnouncesByArrivalDate?arrivalDate={arrivalDate}";
    private static final String GET_ANNOUNCEMENTS_BY_ARRIVAL_URL = "http://localhost:8080/announced/search/findAnnouncesByArrival?arrival={arrival}";

    private final RestTemplate restTemplate;
    @Autowired
    private static UserConsumer userConsumer;
    private final RestTemplateProxy restTemplateProxy;

    public AnnouncementConsumer(RestTemplate restTemplate, RestTemplateProxy restTemplateProxy) {
        this.restTemplate = restTemplate;
        this.restTemplateProxy = restTemplateProxy;
    }

    public Announcement getAnnouncementByURI(String uri){
        ResponseEntity<EntityModel<Announcement>> announcementResponseEntity = restTemplate
                .exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<EntityModel<Announcement>>() {}
                        );
        return Objects.requireNonNull(announcementResponseEntity.getBody()).getContent();
    }

    public List<Announcement> getAll() {
        return setParams(restTemplateProxy.exchange(
                URL,
                HttpMethod.GET,
                null,
                getParameterizedTypeReference()
        )
                .map(HttpEntity::getBody).map(CollectionModel::getContent)
                .map(Collection::stream).map(content -> content.collect(toList()))
                .orElseGet(Collections::emptyList));
    }
    }

    private static Announcement setParams(Announcement announcement){
        announcement.setDriver(userConsumer.getUser(announcement.getLink("driver").map(Link::getHref).get()));
        announcement.setPassengers(userConsumer.getPassengers(announcement.getLink("passenger").map(Link::getHref).get()));
        return announcement;
    }

    private List<Announcement> setParams(List<Announcement> announcements){
        return announcements.stream().map(AnnouncementConsumer::setParams).collect(Collectors.toList());
    }

    public List<Announcement> getJoinedAnnouncements(String uri){
        return setParams(restTemplateProxy.exchange(
                uri,
                HttpMethod.GET,
                null,
                getParameterizedTypeReference()
        )
                .map(HttpEntity::getBody).map(CollectionModel::getContent)
                .map(Collection::stream).map(content -> content.collect(toList()))
                .orElseGet(Collections::emptyList));
    }
    public List<Announcement> getOwnedAnnouncements(String uri){
        return setParams(restTemplateProxy.exchange(
                uri,
                HttpMethod.GET,
                null,
                getParameterizedTypeReference()
        )
                .map(HttpEntity::getBody).map(CollectionModel::getContent)
                .map(Collection::stream).map(content -> content.collect(toList()))
                .orElseGet(Collections::emptyList));
    }

    public List<Announcement> getAvailableAnnouncements(String email) {
        return setParams(restTemplateProxy.exchange(
              URL + "/search/findAvailableAnnounces?email={email}",
              HttpMethod.GET,
              null,
              getParameterizedTypeReference(),
              email
        )
              .map(HttpEntity::getBody).map(CollectionModel::getContent)
              .map(Collection::stream).map(content -> content.collect(toList()))
              .orElseGet(Collections::emptyList));
    }

    public List<Announcement> getByDriver(String email) {
        return setParams(restTemplateProxy.exchange(
              GET_ANNOUNCEMENTS_BY_DRIVER_URL,
              HttpMethod.GET,
              null,
              getParameterizedTypeReference(),
              email)
              .map(HttpEntity::getBody).map(CollectionModel::getContent)
              .map(Collection::stream).map(content -> content.collect(toList()))
              .orElse(Collections.emptyList()));
    }

    public List<Announcement> getByPassenger(String email) {
        return
              setParams(restTemplateProxy.exchange(
                    GET_ANNOUNCEMENTS_BY_PASSENGER_URL,
                    HttpMethod.GET,
                    null,
                    getParameterizedTypeReference(),
                    email)
                    .map(HttpEntity::getBody).map(CollectionModel::getContent)
                    .map(Collection::stream).map(content -> content.collect(toList()))
                    .orElse(Collections.emptyList()));
    }

    public List<Announcement> getMyTrips(String email) {
        return setParams(restTemplateProxy.exchange(
              URL + "/findUserTrips?email=email",
              HttpMethod.GET,
              null,
              getParameterizedTypeReference(),
              email)
              .map(HttpEntity::getBody).map(CollectionModel::getContent)
              .map(Collection::stream).map(content -> content.collect(toList()))
              .orElse(Collections.emptyList()));
    }

    private List<Announcement> sortByDepartureDate(List<Announcement> announcementList) {
        announcementList.sort(Comparator.comparing(Announcement::getDepartureTime));
        return announcementList;
    }

    public List<Announcement> getByArrivalDate(LocalDateTime arrivalDate) {
        final ResponseEntity<PagedModel<Announcement>> announcementResponse =
              restTemplate.exchange(
                    GET_ANNOUNCEMENTS_BY_ARRIVAL_DATE_URL,
                    HttpMethod.GET,
                    null,
                    getParameterizedTypeReference(),
                    arrivalDate
              );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
    }

    public List<Announcement> getByArrival(String arrival) {
        final ResponseEntity<PagedModel<Announcement>> announcementResponse =
              restTemplate.exchange(
                    GET_ANNOUNCEMENTS_BY_ARRIVAL_URL,
                    HttpMethod.GET,
                    null,
                    getParameterizedTypeReference(),
                    arrival
              );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
    }

    public void create(ObjectNode announcement) {
        restTemplate.postForLocation(URL, announcement);
    }

    public void delete(Announcement announcement) {
        restTemplate.delete(announcement.getLink("self").map(Link::getHref).get());
    }

    public void edit(Announcement announcement) {
        restTemplate.put(URL, announcement, Announcement.class);
    }

    private static ParameterizedTypeReference<PagedModel<Announcement>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
