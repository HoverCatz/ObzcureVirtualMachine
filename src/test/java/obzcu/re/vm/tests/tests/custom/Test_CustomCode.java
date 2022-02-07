package obzcu.re.vm.tests.tests.custom;

import obzcu.re.virtualmachine.ObzcureVM;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.concurrent.ThreadLocalRandom;

import static obzcu.re.vm.tests.VMTests.vmStaticMethod;
import static obzcu.re.vm.tests.VMTests.vmVirtualMethod;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HoverCatz
 * @created 26.01.2022
 * @url https://github.com/HoverCatz
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_CustomCode
{

    private static final MethodHandles.Lookup lookup$obzcure = MethodHandles.lookup();

    public static String getArg()
    {
        return "test";
    }
    private static String foundHello()
    {
        return "hello";
    }
    private static String foundWorld()
    {
        return "world";
    }
    private static String foundTest()
    {
        return "test";
    }
    private static String foundDefault()
    {
        return "default";
    }

    public static String TESTME_customTests() throws Throwable
    {
        String arg = getArg();
        return switch (arg)
        {
            case "hello" -> foundHello();
            case "test" -> {
                System.out.println("owo test");
                yield foundTest();
            }
            case "world" -> foundWorld();
            default -> foundDefault();
        };
    }

    @Test
    @Order(1)
    public void customTests() throws Throwable
    {
        // Test virtualized method
        Method method = vmStaticMethod("TESTME_customTests", "()Ljava/lang/String;");
        String result = (String) assertDoesNotThrow(() -> method.invoke(null));
        assertEquals("test", result);
    }

    public static String TESTME_customTestsDefault() throws Throwable
    {
        String arg = "Meow";
        return switch (arg)
        {
            case "hello" -> foundHello();
            case "test" -> {
                System.out.println("owo test");
                yield foundTest();
            }
            case "world" -> foundWorld();
            default -> foundDefault();
        };
    }

    @Test
    @Order(2)
    public void customTests_Default() throws Throwable
    {
        // Test virtualized method
        Method method = vmStaticMethod("TESTME_customTestsDefault", "()Ljava/lang/String;");
        String result = (String) assertDoesNotThrow(() -> method.invoke(null));
        assertEquals("default", result);
    }

    public static String TESTME_customTestsRandom(String arg) throws Throwable
    {
        return switch (arg)
        {
            case "hello" -> foundHello();
            case "test" -> {
                System.out.println("owo test");
                yield foundTest();
            }
            case "world" -> foundWorld();
            default -> foundDefault();
        };
    }

    @Test
    @Order(3)
    public void customTests_Random() throws Throwable
    {
        String[] words = { "hello", "test", "world", "default" };
        String word = words[ThreadLocalRandom.current().nextInt(words.length)];
        // Test virtualized method
        Method method = vmStaticMethod("TESTME_customTestsRandom", "(Ljava/lang/String;)Ljava/lang/String;");
        String result = (String) assertDoesNotThrow(() -> method.invoke(null, word));
        assertEquals(word, result);
    }

    public boolean virtualBoolean = false;
    public void TESTME_virtual()
    {
        virtualBoolean = true;
    }

    @Test
    @Order(4)
    public void customTests_virtual() throws Throwable
    {
        byte[] result = vmVirtualMethod(Test_CustomCode.class,
                "TESTME_virtual", "()V");

        assertFalse(virtualBoolean);

        ObzcureVM vm = ObzcureVM.virtualizeTests(0, 0, 0, null, lookup$obzcure, result);
        vm.setLocal(0, this);
        assertDoesNotThrow(vm::execute);

        assertTrue(virtualBoolean);
    }

}
