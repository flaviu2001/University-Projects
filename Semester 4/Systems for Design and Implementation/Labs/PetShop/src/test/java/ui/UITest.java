package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class UITest {
    private final PrintStream standardOutput = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private final InputStream standardInput = System.in;
    private ByteArrayInputStream inputStream;
    private final UI ui = new UI();

    private final String chooseApplicationMenuString = """
                Welcome to the Wild Cats Pet Shop
                Choose type of storage
                1. Memory
                2. File
                3. Database - in progress
                4. XML File
                0. Exit""";

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOutput);
        System.setIn(standardInput);
    }

    @Test
    public void testPrintStorageTypes() {
        UI.printStorageTypes();

        Assertions.assertEquals(chooseApplicationMenuString.trim(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void testChooseApplicationStyle() {
        inputStream = new ByteArrayInputStream("1".getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
        ui.chooseApplicationStyle();

        Assertions.assertEquals(chooseApplicationMenuString.trim(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void testPrintMenu() {
        UI.printMenu();
        String menuString = """
                Welcome to the Wild Cats Pet Shop
                1. Add a cat
                2. Add food
                3. Feed a cat
                4. Add a customer
                5. Make a purchase
                6. Show all cats
                7. Show all the food
                8. Show all cat food pairs
                9. Show all customers
                10. Show all purchases
                11. Delete cat
                12. Delete food
                13. Stop feeding a cat
                14. Delete a customer
                15. Return a purchase
                16. Update cat
                17. Update food
                18. Update cat food
                19. Update customer
                20. Change purchase review
                21. Filter cats that eat a certain food
                22. Filter customers that bought at least a cat of a certain breed
                23. Filter purchases based on the minimum number of stars
                24. Report customers and their spent cash sorted by that amount
                0. Exit
                """;
        Assertions.assertEquals(menuString.trim(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void testAddCat() {
        inputStream = new ByteArrayInputStream("1".getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
        ui.chooseApplicationStyle();
        inputStream = new ByteArrayInputStream("George b1 3".getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
        ui.addCat();
        outputStreamCaptor.reset();
        ui.showCats();
        Assertions.assertEquals(outputStreamCaptor.toString().trim(), "BaseEntity{id=0} Cat{name: George; breed: b1; catYears: 3}");
    }

    @Test
    public void testRemoveCat() {
        inputStream = new ByteArrayInputStream("1".getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
        ui.chooseApplicationStyle();
        inputStream = new ByteArrayInputStream("George b1 3".getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
        ui.addCat();
        inputStream = new ByteArrayInputStream("0".getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
        ui.deleteCat();
        outputStreamCaptor.reset();
        ui.showCats();
        Assertions.assertEquals(outputStreamCaptor.toString().trim(), "");
    }

    @Test
    public void testUpdateCat() {
        inputStream = new ByteArrayInputStream("1".getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
        ui.chooseApplicationStyle();
        inputStream = new ByteArrayInputStream("George b1 3".getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
        ui.addCat();
        inputStream = new ByteArrayInputStream("0 Georgian b2 3".getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
        ui.updateCat();
        outputStreamCaptor.reset();
        ui.showCats();
        Assertions.assertEquals(outputStreamCaptor.toString().trim(), "BaseEntity{id=0} Cat{name: Georgian; breed: b2; catYears: 3}");
    }



}
