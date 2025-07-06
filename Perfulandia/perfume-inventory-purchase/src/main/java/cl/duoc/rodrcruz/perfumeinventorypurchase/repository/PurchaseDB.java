package cl.duoc.rodrcruz.perfumeinventorypurchase.repository;
import cl.duoc.rodrcruz.usersellermodule.repository.UserDB;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "purchases")
public class PurchaseDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @Column(name="userid", nullable = false, updatable = false)
    private int userid;
    @Column(name="sellerid", nullable = false, updatable = false)
    private int sellerid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="perfumeid",nullable = false)
    private PerfumeDB perfume;
    @Column(name = "perfumename", updatable = false, nullable = false)
    private String perfumename;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "purchasedate", updatable = false, nullable = false)
    private LocalDateTime purchasedate;
}
