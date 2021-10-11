package core.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import core.ITConfig;
import core.domain.Cat;
import core.domain.Purchase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/dbtest/db-data.xml")
public class CatRepositoryTest {
    @Autowired
    private CatRepository catRepository;

    @Test
    public void findCatByIdWithToys() {
        Optional<Cat> cat = catRepository.findCatByIdWithToys(1L);
        assertTrue(cat.isPresent());
        assertEquals("Cat 1 should have no toys", 0, cat.get().getToys().size());
        cat = catRepository.findCatByIdWithToys(2L);
        assertTrue(cat.isPresent());
        assertEquals("Cat 2 should have 1 toy", 1, cat.get().getToys().size());
        cat = catRepository.findCatByIdWithToys(10L);
        assertFalse("Cat 10 should not exist", cat.isPresent());
    }

    @Test
    public void findPurchasesOfCatByCatIdJPQL() {
        List<Purchase> purchases = catRepository.findPurchasesOfCatByCatIdJPQL(1L);
        assertEquals("Cat 1 should have one purchase", purchases.size(), 1);
        purchases = catRepository.findPurchasesOfCatByCatIdJPQL(2L);
        assertEquals("Cat 2 should have no purchases", purchases.size(), 0);
    }

    @Test
    public void findPurchasesOfCatByCatIdCriteria() {
        List<Purchase> purchases = catRepository.findPurchasesOfCatByCatIdCriteria(1L);
        assertEquals("Cat 1 should have one purchase", purchases.size(), 1);
        purchases = catRepository.findPurchasesOfCatByCatIdCriteria(2L);
        assertEquals("Cat 2 should have no purchases", purchases.size(), 0);
    }

    @Test
    public void findPurchasesOfCatByCatIdNative() {
        List<Purchase> purchases = catRepository.findPurchasesOfCatByCatIdNative(1L);
        assertEquals("Cat 1 should have one purchase", purchases.size(), 1);
        purchases = catRepository.findPurchasesOfCatByCatIdNative(2L);
        assertEquals("Cat 2 should have no purchases", purchases.size(), 0);
    }
}
