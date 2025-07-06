package cl.duoc.rodrcruz.perfumeinventorypurchase.controller.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PurchaseRequest {
    private int userid;
    private Integer perfumeid;
    private Integer quantity;
    private double price;
    private int sellerid;
}
