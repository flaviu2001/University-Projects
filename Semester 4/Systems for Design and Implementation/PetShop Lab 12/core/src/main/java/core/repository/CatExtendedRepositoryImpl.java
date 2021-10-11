package core.repository;

import core.domain.Purchase;
import core.domain.Purchase_;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.List;

public class CatExtendedRepositoryImpl extends CustomRepositorySupport implements CatExtendedRepository {
    @Override
    public List<Purchase> findPurchasesOfCatByCatIdJPQL(long catId) {
        System.out.println("jpql");
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("select distinct p from Purchase p left join fetch p.cat left join fetch p.customer where p.cat.id = ?1");
        query.setParameter(1, catId);
        //noinspection unchecked
        return (List<Purchase>) query.getResultList();
    }

    @Override
    public List<Purchase> findPurchasesOfCatByCatIdCriteria(long catId) {
        System.out.println("criteria");
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Purchase> query = criteriaBuilder.createQuery(Purchase.class);
        query.distinct(Boolean.TRUE);
        Root<Purchase> root = query.from(Purchase.class);
        root.fetch(Purchase_.cat, JoinType.LEFT);
        root.fetch(Purchase_.customer, JoinType.LEFT);
        query.where(criteriaBuilder.equal(root.get("cat"), catId));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    @Transactional
    public List<Purchase> findPurchasesOfCatByCatIdNative(long catId) {
        System.out.println("native");
        HibernateEntityManager hibernateEntityManager = getEntityManager().unwrap(HibernateEntityManager.class);
        Session session = hibernateEntityManager.getSession();

        org.hibernate.Query query = session.createSQLQuery("select distinct {p.*},{ca.*},{cu.*} " +
                "from purchase p " +
                "left join cat ca on p.catid=ca.id " +
                "left join customer cu on p.customerid=cu.id " + "" +
                "where ca.id = " + catId)
                .addEntity("p",Purchase.class)
                .addJoin("ca", "p.cat")
                .addJoin("cu", "p.customer")
                .addEntity("p",Purchase.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (List<Purchase>) query.getResultList();
    }
}
