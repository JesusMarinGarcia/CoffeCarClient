package es.uma.ingweb.coffeecar.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
@AllArgsConstructor
public class Announcement extends RepresentationModel<Announcement>{
    private long id;
    private String title;
    private String departureTime;
    private String arrivalDate;
    private String description;
    private String arrival;

   /* private String selfURI;
    private String driverURI;
    private String passengersURI;*/

    private double arrivalLatitude;
    private double arrivalLongitude;
    private double departureLatitude;
    private double getDepartureLongitude;
    private String imgLink;
    private int seats;
    private User driver;
    private List<User> passengers;
}
