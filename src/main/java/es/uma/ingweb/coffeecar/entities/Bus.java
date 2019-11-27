package es.uma.ingweb.coffeecar.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bus {
    private int _id;
    private int codBus;
    private float codLinea;
    private float lat;
    private float lon;
}
