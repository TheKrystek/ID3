package pl.swidurski.gp;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Author: Krystian Świdurski
 */
public class RandomHelper {

    private static final SecureRandom random = new SecureRandom();


    public static int getRandomInt() {
        return random.nextInt();
    }

    public static int getRandomPositiveInt() {
        return Math.abs(getRandomInt());
    }

    public static int getRandomIntLowerThan(int max) {
        return getRandomPositiveInt() % max;
    }

}
