package cl.duoc.rodrcruz.perfumeinventorypurchase.controller.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class InventoryRequest {
    private Integer perfumeId;
    private Integer quantity;
    private double price;
    private String location;
    private LocalDate date;
}

