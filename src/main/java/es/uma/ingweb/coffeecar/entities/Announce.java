package es.uma.ingweb.coffeecar.entities;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@Data
@JsonSerialize
@NoArgsConstructor
@AllArgsConstructor
public class Announce extends EntityModel<Announce> {
    private long id;
    private String title;
    private String departureTime;
    private String arrivalDate;
    private String description;
    private String arrival;
    private double arrivalLatitude;
    private double arrivalLongitude;
    private double departureLatitude;
    private double departureLongitude;
    private String imgLink;
    private int seats;
    private User driver;
    private List<User> passengers;
}
