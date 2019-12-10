package es.uma.ingweb.coffeecar.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusStop {
    private int _id;
    private int codParada;
    private float codLinea;
    private float lat;
    private float lon;
}
