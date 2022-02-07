package obzcu.re.vm.utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @name RandomUtils.java
 * @author ItzSomebody
 * @url https://github.com/ItzSomebody
 */
public class RandomUtils extends StringUtils {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    public static int getRandomInt() {
        return random.nextInt();
    }

    public static int getRandomInt(int min, int max) {
        int j = Math.min(min, max);
        int k = Math.max(min, max);
        min = j; max = k;
        return random.nextInt(min, max + 1);
    }

    public static int getRandomIntNoOrigin(int bounds) {
        return random.nextInt(bounds);
    }

    public static long getRandomLong() {
        return random.nextLong();
    }

    public static long getRandomLong(long bounds) {
        return random.nextLong(bounds);
    }

    public static long getRandomLong(long min, long max) {
        return random.nextLong(min, max);
    }

    public static float getRandomFloat() {
        return random.nextFloat();
    }

    public static float getRandomFloat(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    public static double getRandomDouble() {
        return random.nextDouble();
    }

    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public static <T> T getRandomElement(List<T>list)
    {
        return list.get(getRandomIntNoOrigin(list.size()));
    }

}