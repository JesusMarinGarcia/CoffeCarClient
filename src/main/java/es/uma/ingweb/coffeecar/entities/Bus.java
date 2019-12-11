package es.uma.ingweb.coffeecar.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonSerialize
@AllArgsConstructor
public class Bus {
    private int _id;
    private int codBus;
    private float codLinea;
    private float lat;
    private float lon;
}
