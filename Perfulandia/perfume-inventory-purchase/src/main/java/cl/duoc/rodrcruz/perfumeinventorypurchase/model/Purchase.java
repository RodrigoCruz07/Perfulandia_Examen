package cl.duoc.rodrcruz.perfumeinventorypurchase.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Purchase {
    private int id;
    private int userid;
    private int perfumeid;
    private String perfumename;
    private int quantity;
    private double price;
    private Date purchasedate;


}
