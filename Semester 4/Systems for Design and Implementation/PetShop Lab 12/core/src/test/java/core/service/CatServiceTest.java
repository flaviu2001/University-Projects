package core.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import core.ITConfig;
import core.domain.Cat;
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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/dbtest/db-data.xml")
public class CatServiceTest {
    @Autowired
    private CatService catService;

    @Test
    public void getCatsFromRepository() {
        List<Cat> cats = catService.getCatsFromRepository();
        assertEquals("there should be four cats", 4, cats.size());
    }

    @Test
    public void getCatsByBreed() {
        assertEquals("there should be two cats of breed b1", 2, catService.getCatsByBreed("b1").size());
        assertEquals("there should be no cats of breed c1", 0, catService.getCatsByBreed("c1").size());
    }

    @Test
    public void addCat() {
        catService.addCat("kitty", "purrful", 2);
        assertEquals("there should now be 5 cats", 5, catService.getCatsFromRepository().size());
    }

    @Test
    public void deleteCat() {
        catService.deleteCat(4L);
        assertEquals("there should now be 3 cats", 3, catService.getCatsFromRepository().size());
    }

    @Test
    public void updateCat() {
        catService.updateCat(1L, "kittay", "purrfowl", 3);
        for (Cat cat : catService.getCatsFromRepository())
            if (cat.getId() == 1L) {
                assertEquals(cat.getName(), "kittay");
                assertEquals(cat.getBreed(), "purrfowl");
                assertEquals(cat.getCatYears(), new Integer(3));
                return;
            }
        fail();
    }

    @Test
    public void findPurchasesOfCatByCatId() {
        assertEquals("The first cat should have been purchased once", 1, catService.findPurchasesOfCatByCatId(1).size());
        assertEquals("The second cat should have never been purchased", 0, catService.findPurchasesOfCatByCatId(2).size());
    }
}