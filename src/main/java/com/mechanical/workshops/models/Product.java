package com.mechanical.workshops.models;

import com.mechanical.workshops.auditable.AuditListener;
import com.mechanical.workshops.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@EntityListeners(AuditListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Version
    private Long version = 0L;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
