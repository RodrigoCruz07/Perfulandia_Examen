package cl.duoc.rodrcruz.perfumeinventorypurchase.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Inventory {
    private int id;
    private String location;
    private LocalDateTime lastRestockDate;
}
