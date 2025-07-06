package cl.duoc.rodrcruz.perfumeinventorypurchase.controller.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class InventoryResponse {
    private Integer id;
    private String perfumeName;
    private Integer quantity;
    private double price;
    private String location;
    private LocalDate date;
}