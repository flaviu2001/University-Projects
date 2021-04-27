package core.repository;

import core.domain.Customer;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICustomerRepository extends IRepository<Customer, Long> {
    @Query(value = "select c.* as customerName, sum(p.price) as totalCash from customer c " +
            "inner join purchase p " +
            "on c.id = p.customerId " +
            "group by c.id " +
            "order by sum(p.price) desc", nativeQuery = true)
    List<Object[]> getCustomersSortedSpentCashInterface();
}
