package com.mechanical.workshops.models;

import com.mechanical.workshops.auditable.AuditListener;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditListener.class)
public class Sale {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Person client;

    @Column(nullable = false)
    private ZonedDateTime date;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private String status;
}
