package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.RestTemplateProxy;
import es.uma.ingweb.coffeecar.entities.Announcement;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class AnnouncementConsumer {
    private static final String URL ="http://localhost:8080/announced";
    private static final String GET_ANNOUNCEMENTS_BY_ARRIVAL_DATE_URL = "http://localhost:8080/announced/search/findAnnouncesByArrivalDate?arrivalDate={arrivalDate}";
    private static final String GET_ANNOUNCEMENTS_BY_ARRIVAL_URL = "http://localhost:8080/announced/search/findAnnouncesByArrival?arrival={arrival}";
    private static final String GET_ANNOUNCEMENTS_BY_DRIVER_URL = "http://localhost:8080/announced/search/findAnnouncesByDriver_Email?email={email}";


    private final RestTemplate restTemplate;
    private final RestTemplateProxy restTemplateProxy;

    public AnnouncementConsumer(RestTemplate restTemplate, RestTemplateProxy restTemplateProxy) {
        this.restTemplate = restTemplate;
        this.restTemplateProxy = restTemplateProxy;
    }

    public List<Announcement> getAll(){
        final ResponseEntity<PagedModel<Announcement>> announcementResponse =
              restTemplate.exchange(
                    URL,
                    HttpMethod.GET,
                    null,
                    getParameterizedTypeReference()
              );
        List<Announcement> allTrips = new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
        allTrips.removeAll(getMyTrips(user));
        return allTrips;
    }

    public List<Announcement> getByDriver(User user) {
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

    public List<Announcement> getByPassenger(User user) {
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

    public List<Announcement> getMyTrips(User user) {
        List<Announcement> allMyTrips = getByDriver(user);
        allMyTrips.addAll(getByPassenger(user));
        return sortByDepartureDate(allMyTrips);
    }

    private Boolean containsUser(Announcement announce, String email){
        return announce.getPassengers() != null && announce.getPassengers()
                .stream()
                .noneMatch(user -> user.getEmail().equals(email));
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
    public List<Announcement> getByArrival(String arrival){
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



    public void create(Announcement announcement) {
        restTemplate.postForEntity(URL, announcement, Announcement.class);
    }
    public void delete(Announcement announcement) {
        restTemplate.delete(URL, announcement, Announcement.class);
    }
    public void edit(Announcement announcement){
        restTemplate.put(URL, announcement, Announcement.class);
    }

    private static ParameterizedTypeReference<PagedModel<Announcement>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {};
    }
}
