package cl.duoc.rodrcruz.perfumeinventorypurchase.repository;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Inventario")
public class InventoryDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "perfume_id", nullable = false)
    private PerfumeDB perfume;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "price",nullable = false)
    private double price;
    @Column(name = "location", nullable = false)
    private String location;
    @Column(name = "date", nullable = false)
    private LocalDate date;


}
