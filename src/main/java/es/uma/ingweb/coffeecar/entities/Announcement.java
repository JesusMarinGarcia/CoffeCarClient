package es.uma.ingweb.coffeecar.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    private long id;
    private String title;
    private Date departureTime;
    private Date arrivalDate;
    private String description;
    private String arrival;
    private double arrivalLatitude;
    private double arrivalLongitude;
    private double departureLatitude;
    private double getDepartureLongitude;
    private String imgLink;
    private int seats;
    private User driver;
    private List<User> passengers;
}
