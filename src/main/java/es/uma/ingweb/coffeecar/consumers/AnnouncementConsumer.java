package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.entities.Announcement;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AnnouncementConsumer {
    private static final String URL = "http://localhost:8080/announced";
    private static final String GET_ANNOUNCEMENTS_BY_DRIVER_URL = "http://localhost:8080/announced/search/findAnnouncesByDriver?user={user}";
    private static final String GET_ANNOUNCEMENTS_BY_PASSENGER_URL = "http://localhost:8080/announced/search/findAnnouncesByPassengers?user={user}";
    private static final String GET_ANNOUNCEMENTS_BY_ARRIVAL_DATE_URL = "http://localhost:8080/announced/search/findAnnouncesByArrivalDate?arrivalDate={arrivalDate}";
    private static final String GET_ANNOUNCEMENTS_BY_ARRIVAL_URL = "http://localhost:8080/announced/search/findAnnouncesByArrival?arrival={arrival}";
    private static final String GET_ANNOUNCEMENTS_BY_DRIVER_URL = "http://localhost:8080/announced/search/findAnnouncesByDriver_Email?email={email}";
    private static final String  GET_ANNOUNCEMENTS_BY_PASSENGER_URL = "http://localhost:8080/announced/search/findAnnouncesByPassengers?user={user}";


    private final RestTemplate restTemplate;

    public AnnouncementConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Announcement> getAvailableAnnouncements(User user){
        final ResponseEntity<PagedModel<Announcement>> announcementResponse =
              restTemplate.exchange(
                    URL,
                    HttpMethod.GET,
                    null,
                    getParameterizedTypeReference()
              );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
    }

    public List<Announcement> getAvailableAnnouncements(String email) {
        return restTemplateProxy.exchange(
              URL + "/findAvailableAnnounces?email={email}",
              HttpMethod.GET,
              null,
              getParameterizedTypeReference(),
              email
        )
              .map(HttpEntity::getBody).map(CollectionModel::getContent)
              .map(Collection::stream).map(content -> content.collect(toList()))
              .orElse(Collections.emptyList());
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
    
    public List<Announcement> getByDriver(User user){
        final ResponseEntity<PagedModel<Announcement>> announcementResponse =
                restTemplate.exchange(
                        GET_ANNOUNCEMENTS_BY_DRIVER_URL,
                        HttpMethod.GET,
                        null,
                        getParameterizedTypeReference(),
                        user
                );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
    }
    public List<Announcement> getByPassenger(User user){
        final ResponseEntity<PagedModel<Announcement>> announcementResponse =
                restTemplate.exchange(
                        GET_ANNOUNCEMENTS_BY_PASSENGER_URL,
                        HttpMethod.GET,
                        null,
                        getParameterizedTypeReference(),
                        user
                );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
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

    public List<Announcement> getMyTrips(String email) {
        return restTemplateProxy.exchange(
              URL + "/findUserTrips?email=email",
              HttpMethod.GET,
              null,
              getParameterizedTypeReference(),
              email)
              .map(HttpEntity::getBody).map(CollectionModel::getContent)
              .map(Collection::stream).map(content -> content.collect(toList()))
              .orElse(Collections.emptyList());

    public List<Announcement> getMyTrips(User user){
        List<Announcement> allMyTrips = getByDriver(user);
        allMyTrips.addAll(getByPassenger(user));
        return sortByDepartureDate(allMyTrips);

    }
    private List<Announcement> sortByDepartureDate(List<Announcement> announcementList){
        announcementList.sort(Comparator.comparing(Announcement::getDepartureTime));
        return announcementList;
    }
    public List<Announcement> getByArrivalDate(LocalDateTime arrivalDate){
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
        return restTemplateProxy.exchange(
                GET_ANNOUNCEMENTS_BY_ARRIVAL_URL,
                HttpMethod.GET,
                null,
                getParameterizedTypeReference(),
                arrival)
                .map(HttpEntity::getBody).map(CollectionModel::getContent)
                .map(Collection::stream).map(content -> content.collect(toList()))
                .orElse(Collections.emptyList());
    }



    public void create(Announcement announcement) {
        restTemplateProxy.exchange(URL, HttpMethod.POST, new HttpEntity<>(announcement), getParameterizedTypeReference());
    }

    public void delete(Announcement announcement) {
        restTemplateProxy.exchange(URL, HttpMethod.DELETE, new HttpEntity<>(announcement), getParameterizedTypeReference());
    }

    public void edit(Announcement announcement) {
        restTemplateProxy.exchange(URL, HttpMethod.PUT, new HttpEntity<>(announcement), getParameterizedTypeReference());
    }

    private static ParameterizedTypeReference<PagedModel<Announcement>> getParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
