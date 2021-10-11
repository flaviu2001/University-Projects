package core.repository;

import lombok.Getter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Getter
public class CustomRepositorySupport {
    @PersistenceContext
    private EntityManager entityManager;
}
