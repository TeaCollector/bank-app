package ru.alex.msdeal.service;

import java.util.Random;
import org.junit.jupiter.api.Test;


public class DealServiceTest {

    @Test
    void testSesCode() {
        var number = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            number.append(new Random().nextInt(0, 9));
        }

        System.out.println(Integer.parseInt(number.toString()));
    }
}
