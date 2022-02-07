package obzcu.re.vm.utils;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class StringUtils
{

    private static final char[] ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] NUM = "0123456789".toCharArray();

    public static String randomAlphaString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append(ALPHA[RandomUtils.getRandomIntNoOrigin(ALPHA.length)]);
        return sb.toString();
    }

    public static String randomAlphaNumericString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append(ALPHA_NUM[RandomUtils.getRandomIntNoOrigin(ALPHA_NUM.length)]);
        return sb.toString();
    }

    public static String randomNumericString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append(NUM[RandomUtils.getRandomIntNoOrigin(NUM.length)]);
        return sb.toString();
    }

    public static String randomInvalidString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append((char) (RandomUtils.getRandomIntNoOrigin(8) + '\ua6ac'));
        return sb.toString();
    }

}
