package com.mechanical.workshops.auditable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mechanical.workshops.repository.AuditLogRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.UUID;

@Component
public class AuditListener {

    private static AuditLogRepository auditLogRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper(); // Para convertir objetos a JSON

    @Autowired
    public void setAuditLogRepository(AuditLogRepository repository) {
        auditLogRepository = repository;
    }

    @PrePersist
    public void prePersist(Object entity) {
        saveAuditLog(entity, "INSERT", null);
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        saveAuditLog(entity, "UPDATE", entity);
    }

    @PreRemove
    public void preRemove(Object entity) {
        saveAuditLog(entity, "DELETE", entity);
    }

    private void saveAuditLog(Object entity, String operation, Object oldEntity) {
        try {
            AuditLog audit = new AuditLog();
            audit.setTableName(entity.getClass().getSimpleName());
            audit.setOperation(operation);
            audit.setRecordId(getEntityId(entity));
            audit.setOldData(oldEntity != null ? objectMapper.writeValueAsString(oldEntity) : null);
            audit.setNewData(operation.equals("INSERT") ? objectMapper.writeValueAsString(entity) : null);
            audit.setChangedBy("SYSTEM"); // Aquí puedes obtener el usuario autenticado
            audit.setChangedAt(Instant.now());

            auditLogRepository.save(audit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private UUID getEntityId(Object entity) {
        try {
            return UUID.fromString(entity.getClass().getMethod("getId").invoke(entity).toString());
        } catch (Exception e) {
            return UUID.randomUUID();
        }
    }
}
