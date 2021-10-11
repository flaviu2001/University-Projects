package core.repository;

import core.domain.Purchase;
import core.domain.Purchase_;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;

public class CustomerExtendedRepositoryImpl extends CustomRepositorySupport implements CustomerExtendedRepository{
    @Override
    public List<Purchase> findPurchasesOfCustomerByCustomerIdJPQL(long customerId) {
        System.out.println("jpql");
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("select distinct p from Purchase p left join fetch p.cat left join fetch p.customer where p.customer.id = ?1");
        query.setParameter(1, customerId);
        //noinspection unchecked
        return (List<Purchase>) query.getResultList();
    }

    @Override
    public List<Purchase> findPurchasesOfCustomerByCustomerIdCriteria(long customerId) {
        System.out.println("criteria");
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Purchase> query = criteriaBuilder.createQuery(Purchase.class);
        query.distinct(Boolean.TRUE);
        Root<Purchase> root = query.from(Purchase.class);
        root.fetch(Purchase_.cat, JoinType.LEFT);
        root.fetch(Purchase_.customer, JoinType.LEFT);
        query.where(criteriaBuilder.equal(root.get("customer"), customerId));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    @Transactional
    public List<Purchase> findPurchasesOfCustomerByCustomerIdNative(long customerId) {
        System.out.println("native");
        HibernateEntityManager hibernateEntityManager = getEntityManager().unwrap(HibernateEntityManager.class);
        Session session = hibernateEntityManager.getSession();

        org.hibernate.Query query = session.createSQLQuery("select distinct {p.*},{ca.*},{cu.*} " +
                "from purchase p " +
                "left join cat ca on p.catid=ca.id " +
                "left join customer cu on p.customerid=cu.id " + "" +
                "where cu.id = " + customerId)
                .addEntity("p",Purchase.class)
                .addJoin("ca", "p.cat")
                .addJoin("cu", "p.customer")
                .addEntity("p",Purchase.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return (List<Purchase>) query.getResultList();
    }
}
