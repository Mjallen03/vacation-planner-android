package com.example.vacationplanner;

import com.example.vacationplanner.data.model.Vacation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExampleUnitTest {
    // Test #1 - vacation date validation
    @Test
    public void testDateValidation() {
        Vacation v = new Vacation();
        v.setStartDate("01/10/2026");
        v.setEndDate("01/05/2026");

        boolean isValid = v.getEndDate().compareTo(v.getStartDate()) >= 0;

        assertFalse("End date should not be before start date", isValid);
    }
    // Test #2 - Search filters return correct vacation
    @Test
    public void testSearchFilter() {
        Vacation v = new Vacation();
        v.setName("London Trip");
        v.setHotel("Le Chic");
        v.setLocation("England");
        // simulate a simple search
        String search = "london";

        boolean matches =
                v.getName().toLowerCase().contains(search) ||
                        v.getHotel().toLowerCase().contains(search) ||
                        v.getLocation().toLowerCase().contains(search);

        assertTrue("Search should match vacation name", matches);
    }
}
