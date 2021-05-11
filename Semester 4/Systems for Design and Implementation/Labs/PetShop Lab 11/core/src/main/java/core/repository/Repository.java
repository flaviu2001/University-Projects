package core.repository;

import core.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface Repository<T extends BaseEntity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {
}
