package com.mechanical.workshops.auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "table_name", nullable = false)
    private String tableName; // Tabla afectada

    @Column(name = "operation", nullable = false)
    private String operation; // INSERT, UPDATE, DELETE

    @Column(name = "record_id", nullable = false)
    private UUID recordId; // ID del registro afectado

    @Column(name = "old_data", columnDefinition = "TEXT")
    private String oldData; // Datos antes del cambio (JSON)

    @Column(name = "new_data", columnDefinition = "TEXT")
    private String newData; // Datos después del cambio (JSON)

    @Column(name = "changed_by")
    private String changedBy; // Usuario que hizo el cambio

    @Column(name = "changed_at", nullable = false)
    private Instant changedAt; // Fecha del cambio

}
