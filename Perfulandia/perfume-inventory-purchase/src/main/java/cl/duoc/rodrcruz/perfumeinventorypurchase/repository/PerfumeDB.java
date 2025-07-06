package cl.duoc.rodrcruz.perfumeinventorypurchase.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "perfume")
public class PerfumeDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "size", nullable = false)
    private Integer size;
    @Column(name = "brand", nullable = false)
    private String brand;
}
