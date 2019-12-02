package es.uma.ingweb.coffeecar.consumers;

import es.uma.ingweb.coffeecar.entities.Announcement;
import es.uma.ingweb.coffeecar.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AnnouncementConsumer {
    private static final String URL ="http://localhost:8080/announced";
    private static final String GET_AVAILABLE_ANNOUNCEMETS_URL = "http://localhost:8080/announced/search/findAnnouncesByDriverNotAndPassengersNot{mail}";
    private static final String GET_ANNOUNCEMENTS_BY_DRIVER_URL = "http://localhost:8080/announced/search/findAnnouncesByDriver_Mail{mail}";
    private static final String  GET_ANNOUNCEMENTS_BY_PASSANGER_URL = "http://localhost:8080/announced/search/findAnnouncesByPassengers{mail}";
    private static final String GET_ANNOUNCEMETS_BY_ARRIVAL_DATE_URL = "http://localhost:8080/announced/search/findAnnouncesByArrivalDate{arrivalDate}";
    private static final String GET_ANNOUNCEMETS_BY_ARRIVAL_URL = "http://localhost:8080/announced/search/findAnnouncesByArrival{arrival}";

    @Autowired
    private RestTemplate restTemplate;

    public List<Announcement> getAvailableAnnouncements(String mail){
        final ResponseEntity<PagedModel<Announcement>> announcementResponse =
                restTemplate.exchange(
                        GET_AVAILABLE_ANNOUNCEMETS_URL,
                        HttpMethod.GET,
                        null,
                        getParameterizedTypeReference(),
                        Map.of("mail", mail)
                );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
    }
    public List<Announcement> getByDriver(String mail){
        final ResponseEntity<PagedModel<Announcement>> announcementResponse =
                restTemplate.exchange(
                        GET_ANNOUNCEMENTS_BY_DRIVER_URL,
                        HttpMethod.GET,
                        null,
                        getParameterizedTypeReference(),
                        Map.of("mail", mail)
                );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
    }
    public List<Announcement> getByPassenger(String mail){
        final ResponseEntity<PagedModel<Announcement>> announcementResponse =
                restTemplate.exchange(
                        GET_ANNOUNCEMENTS_BY_PASSANGER_URL,
                        HttpMethod.GET,
                        null,
                        getParameterizedTypeReference(),
                        Map.of("mail", mail)
                );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
    }
    public List<Announcement> getMyTrips(String mail){
        List<Announcement> allMyTrips = getByDriver(mail);
        allMyTrips.addAll(getByPassenger(mail));
        return sortByDepartureDate(allMyTrips);

    }
    private List<Announcement> sortByDepartureDate(List announcementList){
        Collections.sort(announcementList, new Comparator<Announcement>(){
            @Override
            public int compare(Announcement announcement, Announcement t1) {
                return announcement.getDepartureTime().compareTo(t1.getDepartureTime());
            }
        });
        return announcementList;
    }
    public List<Announcement> getByArrivalDate(String mail){
        final ResponseEntity<PagedModel<Announcement>> announcementResponse =
                restTemplate.exchange(
                        GET_ANNOUNCEMETS_BY_ARRIVAL_DATE_URL,
                        HttpMethod.GET,
                        null,
                        getParameterizedTypeReference(),
                        Map.of("arrivalDate", mail)
                );
        return new ArrayList<>(Objects.requireNonNull(announcementResponse.getBody()).getContent());
    }
    public List<Announcement> getByArrival(String mail){
        final ResponseEntity<PagedModel<Announcement>> announcementResponse =
                restTemplate.exchange(
                        GET_ANNOUNCEMETS_BY_ARRIVAL_URL,
                        HttpMethod.GET,
                        null,
                        getParameterizedTypeReference(),
                        Map.of("arrival", mail)
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
