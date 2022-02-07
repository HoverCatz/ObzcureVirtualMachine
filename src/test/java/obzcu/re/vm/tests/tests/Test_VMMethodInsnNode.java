package obzcu.re.vm.tests.tests;

import obzcu.re.virtualmachine.asm.VMOpcodes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.lang.invoke.MethodHandles;

import static obzcu.re.vm.tests.VMTests.createVM;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HoverCatz
 * @created 21.01.2022
 * @url https://github.com/HoverCatz
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_VMMethodInsnNode
{

    private final MethodHandles.Lookup lookup = MethodHandles.lookup();

    private static boolean testStaticBoolean = false;
    private static void testStatic() { testStaticBoolean = true; }

    @Test
    @Order(1)
    public void INVOKESTATIC() throws Throwable
    {
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKESTATIC, lookup,
                new MethodInsnNode(VMOpcodes.INVOKESTATIC,
                        getClass().getName(), "testStatic", "()V")
        );
        vm.execute();
        assertTrue(testStaticBoolean);
        assertEquals(0, vm.stackSize());
    }

    private boolean testVirtualBoolean = false;
    private void testVirtual() { testVirtualBoolean = true; }

    @Test
    @Order(2)
    public void INVOKEVIRTUAL() throws Throwable
    {
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKEVIRTUAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKEVIRTUAL,
                        getClass().getName(), "testVirtual", "()V")
        );
        vm.push(this);
        vm.execute();
        assertTrue(testVirtualBoolean);
        assertEquals(0, vm.stackSize());
    }

    private static boolean innerClassTestBoolean = false;
    static class InnerClassTest
    {
        public InnerClassTest() { innerClassTestBoolean = true; }
        public InnerClassTest(boolean z) { innerClassTestBoolean = z; }
    }

    @Test
    @Order(3)
    public void INVOKESPECIAL() throws Throwable
    {
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKESPECIAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKESPECIAL,
                        InnerClassTest.class.getName(), "<init>", "()V")
        );
        vm.push(InnerClassTest.class);
        vm.push(InnerClassTest.class);
        vm.execute();
        assertTrue(innerClassTestBoolean);
        assertEquals(1, vm.stackSize()); // vm.pop() required for `new InnerClassTest();`
    }

    @Test
    @Order(4)
    public void INVOKESPECIAL_InnerClassTest() throws Throwable
    {
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKESPECIAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKESPECIAL,
                        InnerClassTest.class.getName(), "<init>", "(Z)V")
        );
        vm.push(InnerClassTest.class);
        vm.push(InnerClassTest.class);
        vm.push(false);
        vm.execute();
        assertFalse(innerClassTestBoolean);
        assertEquals(1, vm.stackSize()); // vm.pop() required for `new InnerClassTest();`
    }

    private static int innerClassTestTwoInteger = 1;
    private static double innerClassTestTwoDouble = 1D;
    private static String innerClassTestTwoString = "Wow";
    static class InnerClassTestTwo
    {
        public InnerClassTestTwo() { innerClassTestTwoInteger = 2; }
        public InnerClassTestTwo(int i) { innerClassTestTwoInteger = i; }
        public InnerClassTestTwo(int i, double d, String s) {
            innerClassTestTwoInteger = i;
            innerClassTestTwoDouble = d;
            innerClassTestTwoString = s;
        }
        public long testLong()
        {
            return 1337L;
        }
        public String testArguments(int i, short s, char c, String str, double d, long e)
        {
            return "Hello, " + i + ", " + s + ", " + c + ", " + str + ", " + d + ", " + e;
        }
        public static String testArgumentsStatic(int i, short s, char c, String str, double d, long e)
        {
            return "Hello, " + i + ", " + s + ", " + c + ", " + str + ", " + d + ", " + e;
        }
    }

    @Test
    @Order(5)
    public void INVOKESPECIAL_InnerClassTestTwo() throws Throwable
    {
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKESPECIAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKESPECIAL,
                        InnerClassTestTwo.class.getName(), "<init>", "()V")
        );
        vm.push(InnerClassTest.class);
        vm.push(InnerClassTest.class);
        vm.execute();
        assertEquals(2, innerClassTestTwoInteger);
        assertEquals(1, vm.stackSize()); // vm.pop() required for `new InnerClassTest();`
    }

    @Test
    @Order(6)
    public void INVOKESPECIAL_InnerClassTestTwo_I() throws Throwable
    {
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKESPECIAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKESPECIAL,
                        InnerClassTestTwo.class.getName(), "<init>", "(I)V")
        );
        vm.push(InnerClassTest.class);
        vm.push(InnerClassTest.class);
        vm.push(1337);
        vm.execute();
        assertEquals(1337, innerClassTestTwoInteger);
        assertEquals(1, vm.stackSize()); // vm.pop() required for `new InnerClassTest();`
    }

    @Test
    @Order(7)
    public void INVOKESPECIAL_InnerClassTestTwo_IDString() throws Throwable
    {
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKESPECIAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKESPECIAL,
                        InnerClassTestTwo.class.getName(), "<init>", "(IDLjava/lang/String;)V")
        );
        vm.push(InnerClassTest.class);
        vm.push(InnerClassTest.class);
        vm.push(1234);
        vm.push(50.5D);
        vm.push("Hello world");
        vm.execute();
        assertEquals(1234, innerClassTestTwoInteger);
        assertEquals(50.5D, innerClassTestTwoDouble);
        assertEquals("Hello world", innerClassTestTwoString);
        assertEquals(1, vm.stackSize()); // vm.pop() required for `new InnerClassTest();`
    }

    @Test
    @Order(8)
    public void INVOKESPECIAL_InnerClassTestTwo_I_Long() throws Throwable
    {
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKESPECIAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKESPECIAL,
                        InnerClassTestTwo.class.getName(), "<init>", "(I)V")
        );
        vm.push(InnerClassTest.class);
        vm.push(InnerClassTest.class);
        vm.push(1337);
        vm.execute();
        assertEquals(1337, innerClassTestTwoInteger);
        assertEquals(1, vm.stackSize()); // vm.pop() required for `new InnerClassTest();`
        Object pop = vm.pop();
        assertInstanceOf(InnerClassTestTwo.class, pop);

        InnerClassTestTwo inner = (InnerClassTestTwo) pop;
        var vm2 = createVM("VMMethodInsnNode", VMOpcodes.INVOKEVIRTUAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKEVIRTUAL, InnerClassTestTwo.class.getName(),
                        "testLong", "()J")
        );
        vm2.push(inner);
        vm2.execute();
        assertEquals(1, vm2.stackSize());
        Object l = vm2.pop();
        assertInstanceOf(Long.class, l);
        assertEquals(1337L, l);
    }

    @Test
    @Order(9)
    public void INVOKESPECIAL_InnerClassTestTwo_I_Long_Arguments() throws Throwable
    {
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKESPECIAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKESPECIAL,
                        InnerClassTestTwo.class.getName(), "<init>", "(I)V")
        );
        vm.push(InnerClassTest.class);
        vm.push(InnerClassTest.class);
        vm.push(1337);
        vm.execute();
        assertEquals(1337, innerClassTestTwoInteger);
        assertEquals(1, vm.stackSize()); // vm.pop() required for `new InnerClassTest();`
        Object pop = vm.pop();
        assertInstanceOf(InnerClassTestTwo.class, pop);

        InnerClassTestTwo inner = (InnerClassTestTwo) pop;
        var vm2 = createVM("VMMethodInsnNode", VMOpcodes.INVOKEVIRTUAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKEVIRTUAL, InnerClassTestTwo.class.getName(),
                        "testArguments", "(ISCLjava/lang/String;DJ)Ljava/lang/String;")
        );
        vm2.push(inner);
        vm2.push(1);
        vm2.push((short)2);
        vm2.push('3');
        vm2.push("4");
        vm2.push(5.9999999999999D);
        vm2.push(Long.MAX_VALUE);
        vm2.execute();
        assertEquals(1, vm2.stackSize());
        Object obj = vm2.pop();
        assertInstanceOf(String.class, obj);
        assertEquals("Hello, 1, 2, 3, 4, " + 5.9999999999999D + ", " + Long.MAX_VALUE, obj);
    }

    @Test
    @Order(10)
    public void INVOKESPECIAL_InnerClassTestTwo_I_Long_Arguments_Static() throws Throwable
    {
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKESTATIC, lookup,
                new MethodInsnNode(VMOpcodes.INVOKESTATIC, InnerClassTestTwo.class.getName(),
                        "testArgumentsStatic", "(ISCLjava/lang/String;DJ)Ljava/lang/String;")
        );
        vm.push(1);
        vm.push((short)2);
        vm.push('3');
        vm.push("4");
        vm.push(5.9999999999999D);
        vm.push(Long.MAX_VALUE);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object obj = vm.pop();
        assertInstanceOf(String.class, obj);
        assertEquals("Hello, 1, 2, 3, 4, " + 5.9999999999999D + ", " + Long.MAX_VALUE, obj);
    }

    interface InnerClassTestThreeInterface
    {
        void test();
    }
    private static boolean innerClassTestThreeBoolean = false;
    static class InnerClassTestThree implements InnerClassTestThreeInterface
    {
        @Override
        public void test()
        {
            innerClassTestThreeBoolean = true;
        }
    }

    @Test
    @Order(11)
    public void INVOKESPECIAL_InnerClassTestThree() throws Throwable
    {
        InnerClassTestThree three = new InnerClassTestThree();
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKEVIRTUAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKEVIRTUAL, InnerClassTestThree.class.getName(),
                        "test", "()V")
        );
        vm.push(three);
        vm.execute();
        assertEquals(0, vm.stackSize());
        assertTrue(innerClassTestThreeBoolean);
    }

    private static int innerClassTestFourthInteger = 1;
    private static int innerClassTestFourthInteger2 = 1;
    abstract static class InnerClassTestFourthAbstract
    {
        public void test()
        {
            innerClassTestFourthInteger = 2;
        }
        public void test2()
        {
            innerClassTestFourthInteger2 = 2;
        }
    }
    static class InnerClassTestFourth extends InnerClassTestFourthAbstract
    {
        @Override
        public void test()
        {
            super.test();
            innerClassTestFourthInteger = 3;
        }
        @Override
        public void test2()
        {
            innerClassTestFourthInteger = 3;
            super.test2();
        }
    }

    @Test
    @Order(12)
    public void INVOKESPECIAL_InnerClassTestFourth() throws Throwable
    {
        InnerClassTestFourth fourth = new InnerClassTestFourth();
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKEVIRTUAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKEVIRTUAL, InnerClassTestFourth.class.getName(),
                        "test", "()V")
        );
        vm.push(fourth);
        vm.execute();
        assertEquals(0, vm.stackSize());
        assertEquals(3, innerClassTestFourthInteger);
    }

    @Test
    @Order(13)
    public void INVOKESPECIAL_InnerClassTestFourth_2() throws Throwable
    {
        InnerClassTestFourth fourth = new InnerClassTestFourth();
        var vm = createVM("VMMethodInsnNode", VMOpcodes.INVOKEVIRTUAL, lookup,
                new MethodInsnNode(VMOpcodes.INVOKEVIRTUAL, InnerClassTestFourth.class.getName(),
                        "test2", "()V")
        );
        vm.push(fourth);
        vm.execute();
        assertEquals(0, vm.stackSize());
        assertEquals(2, innerClassTestFourthInteger2);
    }

}
