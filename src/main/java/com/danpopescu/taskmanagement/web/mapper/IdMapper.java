package com.danpopescu.taskmanagement.web.mapper;

import com.danpopescu.taskmanagement.domain.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class IdMapper {

    private final EntityManager entityManager;

    public <T extends BaseEntity> T resolve(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    public Long toId(BaseEntity entity) {
        return entity != null ? entity.getId() : null;
    }
}
