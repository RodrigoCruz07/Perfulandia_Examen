package cl.duoc.rodrcruz.perfumeinventorypurchase.controller.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseResponse {
    private Integer id;
    private int userId;
    private int sellerid;
    private String perfumeName;
    private Integer quantity;
    private Double price;
    private LocalDateTime purchaseDate;
}