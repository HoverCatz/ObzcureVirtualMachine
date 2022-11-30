package obzcu.re.vm.tests.tests;

import obzcu.re.virtualmachine.ObzcureVM;
import obzcu.re.virtualmachine.asm.VMOpcodes;
import obzcu.re.virtualmachine.types.invokedynamics.VMConsumer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static obzcu.re.vm.tests.VMTests.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HoverCatz
 * @created 21.01.2022
 * @url https://github.com/HoverCatz
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_VMInvokeDynamicsInsnNode
{

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    private static boolean lambdaBoolean = false;
    private static void testLambdaMethod()
    {
        lambdaBoolean = true;
    }

    @Test
    @Order(1)
    public void RUNNABLE() throws Throwable
    {
        Object[] inputs = {
            "Runnable",             // which
            VMOpcodes.H_INVOKESTATIC, //tag
            getClass().getName(),   // owner
            "testLambdaMethod",     // name
            0,                      // args count
            0,                      // arguments count
        };
        var vm = createVM("VMInvokeDynamicInsnNode",
                VMOpcodes.H_INVOKESTATIC, lookup, inputs);
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        Object pop = vm.pop();
        assertInstanceOf(Runnable.class, pop);
        ((Runnable)pop).run();
        assertTrue(lambdaBoolean);
        assertEquals(0, vm.stackSize());
    }

    private static boolean lambdaBoolean2 = false;
    private static void testLambdaMethod2(int i)
    {
        lambdaBoolean2 = i == 1337;
    }

    @Test
    @Order(2)
    public void RUNNABLE_I() throws Throwable
    {
        Object[] inputs = {
            "Runnable",             // which
            VMOpcodes.H_INVOKESTATIC, //tag
            getClass().getName(),   // owner
            "testLambdaMethod2",    // name
            0,                      // args count
            1,                      // arguments count
            "I",                    // arguments
        };
        var vm = createVM("VMInvokeDynamicInsnNode",
                VMOpcodes.H_INVOKESTATIC, lookup, inputs);
        vm.push(1337);
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        Object pop = vm.pop();
        assertInstanceOf(Runnable.class, pop);
        ((Runnable)pop).run();
        assertTrue(lambdaBoolean2);
        assertEquals(0, vm.stackSize());
    }

    private static boolean lambdaBoolean3 = false;
    private static void testLambdaMethod3(int i, int n)
    {
        lambdaBoolean3 = (i + n) == 1337;
    }

    @Test
    @Order(3)
    public void RUNNABLE_II() throws Throwable
    {
        Object[] inputs = {
            "Runnable",             // which
            VMOpcodes.H_INVOKESTATIC, //tag
            getClass().getName(),   // owner
            "testLambdaMethod3",    // name
            2,                      // args count
            "I", "I",               // args
            2,                      // arguments count
            "I", "I",               // arguments
        };
        var vm = createVM("VMInvokeDynamicInsnNode",
                VMOpcodes.H_INVOKESTATIC, lookup, inputs);
        vm.push(1335);
        vm.push(2);
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        Object pop = vm.pop();
        assertInstanceOf(Runnable.class, pop);
        ((Runnable)pop).run();
        assertTrue(lambdaBoolean3);
        assertEquals(0, vm.stackSize());
    }

    private static boolean lambdaBoolean4 = false;
    private static void testLambdaMethod4(int i, String str)
    {
        lambdaBoolean4 = (i + str.length()) == 1337;
    }

    @Test
    @Order(4)
    public void RUNNABLE_IString() throws Throwable
    {
        Object[] inputs = {
            "Runnable",             // which
            VMOpcodes.H_INVOKESTATIC, //tag
            getClass().getName(),   // owner
            "testLambdaMethod4",    // name
            0,                      // args count
            2,                      // arguments count
            "I","java.lang.String"  // arguments
        };
        var vm = createVM("VMInvokeDynamicInsnNode",
                VMOpcodes.H_INVOKESTATIC, lookup, inputs);
        vm.push(1334);
        vm.push("abc");
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        Object pop = vm.pop();
        assertInstanceOf(Runnable.class, pop);
        ((Runnable)pop).run();
        assertTrue(lambdaBoolean4);
        assertEquals(0, vm.stackSize());
    }

    private static boolean lambdaBoolean5 = false;
    private static void testLambdaMethod5(int i, Runnable r)
    {
        lambdaBoolean5 = i == 1337;
        r.run();
    }

    @Test
    @Order(5)
    public void RUNNABLE_IRunnable() throws Throwable
    {
        Object[] inputs = {
            "Runnable",             // which
            VMOpcodes.H_INVOKESTATIC, //tag
            getClass().getName(),   // owner
            "testLambdaMethod5",    // name
            0,                      // args count
            2,                      // arguments count
            "I","java.lang.Runnable",// arguments
        };
        var vm = createVM("VMInvokeDynamicInsnNode",
                VMOpcodes.H_INVOKESTATIC, lookup, inputs);
        vm.push(1337);
        vm.push((Runnable) () -> {});
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        Object pop = vm.pop();
        assertInstanceOf(Runnable.class, pop);
        ((Runnable)pop).run();
        assertTrue(lambdaBoolean5);
        assertEquals(0, vm.stackSize());
    }

    private static int lambdaInt6 = 0;
    private static void testLambdaMethod6(Runnable r, int i, Runnable r2)
    {
        lambdaInt6 += i;
        r.run();
        lambdaInt6++;
        r2.run();
    }

    @Test
    @Order(6)
    public void RUNNABLE_RunnableIRunnable() throws Throwable
    {
        Object[] inputs = {
            "Runnable",             // which
            VMOpcodes.H_INVOKESTATIC, //tag
            getClass().getName(),   // owner
            "testLambdaMethod6",    // name
            0,                      // args count
            3,                      // arguments count
                "java.lang.Runnable",
                "I",
                "java.lang.Runnable",// arguments
        };
        var vm = createVM("VMInvokeDynamicInsnNode",
                VMOpcodes.H_INVOKESTATIC, lookup, inputs);
        vm.push((Runnable) () -> lambdaInt6++);
        vm.push(1337);
        vm.push((Runnable) () -> lambdaInt6++);
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        Object pop = vm.pop();
        assertInstanceOf(Runnable.class, pop);
        ((Runnable)pop).run();
        assertEquals(1340, lambdaInt6);
        assertEquals(0, vm.stackSize());
    }

    private static String testConsumerString = "";
    private static void testConsumerMethod(String arg)
    {
        testConsumerString = arg + "!";
    }

    @Test
    @Order(100)
    public void CONSUMER() throws Throwable
    {
        Object[] inputs = {
                "Consumer",             // which
                VMOpcodes.H_INVOKESTATIC, //tag
                getClass().getName(),   // owner
                "testConsumerMethod",   // name
                1,                      // args count
                    "java.lang.String",
                0,                      // arguments count
        };
        var vm = createVM("VMInvokeDynamicInsnNode",
                VMOpcodes.H_INVOKESTATIC, lookup, inputs);
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        Object pop = vm.pop();
        assertTrue(pop instanceof Consumer);
        ((Consumer<String>)pop).accept("Hello");
        assertEquals("Hello!", testConsumerString);
        assertEquals(0, vm.stackSize());
    }

    private static double testConsumerDouble2 = 5D;
    private static void testConsumerMethod2(double d)
    {
        testConsumerDouble2 += d + 0.125D;
    }

    private static double TESTME_CONSUMER_D()
    {
        //noinspection Convert2MethodRef
        Consumer<Double> c = arg -> testConsumerMethod2(arg);
        c.accept(7.25D);
        return testConsumerDouble2;
    }

    @Test
    @Order(101)
    public void CONSUMER_D() throws Throwable
    {
        double expected = 12.375D;

        // Test directly
        double actual = TESTME_CONSUMER_D();
        assertEquals(expected, actual);

        // Reset values
        testConsumerDouble2 = 5D;

        // Test virtualized method
        Method method = vmStaticMethod("TESTME_CONSUMER_D", "()D");
        double response = (double) assertDoesNotThrow(() -> method.invoke(null));
        assertEquals(expected, response);
    }

    public static void TESTME_LAMBDAS()
    {
        Thread t = new Thread(() -> System.out.println("Inside thread"));

        Runnable r = () -> System.out.println("Hello runnable");

        Consumer<String> test2 = str -> System.out.println("Consumed str: " + str);
        test2.accept("test2");

        Function<String, Void> func = str -> {
            System.out.println("Hello, " + str);
            r.run();
            test2.accept("oof");
            return null;
        };

        Predicate<String> predicate = str -> str.equals("def");
        predicate = predicate.or(str -> {
            System.out.println("if false, we can change it here");
            t.start();
            func.apply("From predicate!");
            return true;
        });
        predicate = predicate.and(str -> {
            System.out.println("if true, we can change it here");
            r.run();
            Object apply = func.apply("world");
            System.out.println(apply);
            return true;
        });
        boolean abc = predicate.test("abc"); // returns either true of false
        System.out.println(abc);

        Runnable r2 = () ->
        {
            System.out.println("Hello world 2");
            func.apply("From runnable! " + abc);
        };
        r2.run();

        Consumer<String> test3 = str -> System.out.println("Consumed str: " + str);
        test3 = test3.andThen(str -> System.out.println("And then: " + str));
        test3.accept("test");

        Consumer<Integer> test4 = str -> System.out.println("Consumed int: " + str);
        test4.accept(3);

        final Consumer<String> finalTest = test3;
        Consumer<Runnable> c = a -> {
            a.run();
            r.run();
            finalTest.accept("Test");
            func.apply("From consumer!");
        };
        c.accept(r);

        Consumer<Consumer<Integer>> test5 = c2 -> {
            System.out.println("Consumed consumer: " + c2);
            c2.accept(69);
            r.run();
            r2.run();
        };
        test5.accept(test4);

        IntStream intStream = IntStream.range(1, 10);
        intStream.forEach(System.out::print);
        IntStream.range(1, 10).forEach(i -> System.out.print(i));

        System.out.println();

        LongStream longStream = LongStream.range(1, 10);
        longStream.forEach(System.out::print);
        LongStream.range(1, 10).forEach(l -> System.out.print(l));

        System.out.println();

        DoubleStream doubleStream = IntStream.range(1, 10).asDoubleStream();
        doubleStream.forEach(System.out::print);
        IntStream.range(1, 10).asDoubleStream().forEach(i -> System.out.print(i));

    }

    @Test
    @Order(102)
    public void LAMBDAS() throws Throwable
    {
        // Test virtualized method
        Method method = vmStaticMethod("TESTME_LAMBDAS", "()V");
        assertDoesNotThrow(() -> method.invoke(null));
    }

    public void testVirtualConsumer(String in)
    {
        System.out.println("in: " + in);
    }

    public String testVirtualFunction(String in)
    {
        System.out.println("in: " + in);
        return in + "!";
    }

    public boolean testVirtualPredicate(String in)
    {
        System.out.println("in: " + in);
        return true;
    }

    private void testVirtualThread()
    {
        System.out.println("Hello world 1");
    }

    private void testVirtualRunnable()
    {
        System.out.println("Hello world 5");
    }

    public void TESTME_Virtual()
    {
        Thread test6 = new Thread(this::testVirtualThread);
        test6.start();

        Consumer<String> test7 = this::testVirtualConsumer;
        test7.accept("Hello world 2");

        Function<String, String> test8 = this::testVirtualFunction;
        test8.apply("Hello world 3");

        Predicate<String> test9 = this::testVirtualPredicate;
        test9.and(this::testVirtualPredicate);
        test9.test("Hello world 4");

        Runnable test10 = this::testVirtualRunnable;
        test10.run();
    }

    @Test
    @Order(103)
    public void LAMBDAS_Virtual() throws Throwable
    {
        // Test virtualized method
        byte[] result = vmVirtualMethod("TESTME_Virtual", "()V");
        ObzcureVM vm = ObzcureVM.virtualizeTests(0, 0, 0, null, lookup, result);
        vm.setLocal(0, this);
        assertDoesNotThrow(vm::execute);
    }

}
