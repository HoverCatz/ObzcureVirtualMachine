package obzcu.re.vm.tests.tests;

import obzcu.re.virtualmachine.asm.VMOpcodes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;
import static obzcu.re.vm.tests.VMTests.createVM;

/**
 * @author HoverCatz
 * @created 21.01.2022
 * @url https://github.com/HoverCatz
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_VMInsnNode
{

    @Test
    @Order(1)
    public void NOP() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.NOP);
        vm.execute();
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(2)
    public void ACONST_NULL() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ACONST_NULL);
        vm.execute();
        assertNull(vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(3)
    public void ICONST_M1() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ICONST_M1);
        vm.execute();
        assertEquals(-1, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(4)
    public void ICONST_0() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ICONST_0);
        vm.execute();
        assertEquals(0, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(5)
    public void ICONST_1() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ICONST_1);
        vm.execute();
        assertEquals(1, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(6)
    public void ICONST_2() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ICONST_2);
        vm.execute();
        assertEquals(2, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(7)
    public void ICONST_3() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ICONST_3);
        vm.execute();
        assertEquals(3, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(8)
    public void ICONST_4() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ICONST_4);
        vm.execute();
        assertEquals(4, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(9)
    public void ICONST_5() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ICONST_5);
        vm.execute();
        assertEquals(5, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(10)
    public void LCONST_0() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LCONST_0);
        vm.execute();
        assertEquals(0L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(11)
    public void LCONST_1() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LCONST_1);
        vm.execute();
        assertEquals(1L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(12)
    public void FCONST_0() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FCONST_0);
        vm.execute();
        assertEquals(0F, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(13)
    public void FCONST_1() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FCONST_1);
        vm.execute();
        assertEquals(1F, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(14)
    public void FCONST_2() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FCONST_2);
        vm.execute();
        assertEquals(2F, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(15)
    public void DCONST_0() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DCONST_0);
        vm.execute();
        assertEquals(0D, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(16)
    public void DCONST_1() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DCONST_1);
        vm.execute();
        assertEquals(1D, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(17)
    public void IALOAD() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.IALOAD);
        vm.push(new int[] { 1337 }); // arrayRef
        vm.push(0); // index
        vm.execute();
        assertEquals(1337, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(18)
    public void LALOAD() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LALOAD);
        vm.push(new long[] { 1337L }); // arrayRef
        vm.push(0); // index
        vm.execute();
        assertEquals(1337L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(19)
    public void FALOAD() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FALOAD);
        vm.push(new float[]{ 1337F }); // arrayRef
        vm.push(0); // index
        vm.execute();
        assertEquals(1337F, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(20)
    public void DALOAD() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DALOAD);
        vm.push(new double[]{ 1337D }); // arrayRef
        vm.push(0); // index
        vm.execute();
        assertEquals(1337D, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(21)
    public void AALOAD() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.AALOAD);
        vm.push((Object)new Object[]{ "Hello world" }); // arrayRef
        vm.push(0); // index
        vm.execute();
        assertEquals("Hello world", vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(22)
    public void BALOAD() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.BALOAD);
        vm.push(new byte[]{ (byte)3 }); // arrayRef
        vm.push(0); // index
        vm.execute();
        assertEquals((byte)3, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(23)
    public void CALOAD() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.CALOAD);
        vm.push(new char[]{ 'x' }); // arrayRef
        vm.push(0); // index
        vm.execute();
        assertEquals('x', vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(24)
    public void SALOAD() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.SALOAD);
        vm.push(new short[]{ (short)25 }); // arrayRef
        vm.push(0); // index
        vm.execute();
        assertEquals((short)25, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(25)
    public void IASTORE() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.IASTORE);
        int[] arrayRef = new int[1];
        vm.push(arrayRef, 0, 1337); // value
        vm.execute();
        assertEquals(1337, arrayRef[0]);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(26)
    public void LASTORE() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LASTORE);
        long[] arrayRef = new long[1];
        vm.push(arrayRef, 0, 1337L); // value
        vm.execute();
        assertEquals(1337L, arrayRef[0]);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(27)
    public void FASTORE() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FASTORE);
        float[] arrayRef = new float[1];
        vm.push(arrayRef, 0, 1337F);
        vm.execute();
        assertEquals(1337F, arrayRef[0]);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(28)
    public void DASTORE() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DASTORE);
        double[] arrayRef = new double[1];
        vm.push(arrayRef, 0, 1337D);
        vm.execute();
        assertEquals(1337D, arrayRef[0]);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(29)
    public void AASTORE() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.AASTORE);
        Object[] arrayRef = new Object[1];
        vm.push(arrayRef, 0, "Hello world");
        vm.execute();
        assertEquals("Hello world", arrayRef[0]);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(30)
    public void BASTORE() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.BASTORE);
        byte[] arrayRef = new byte[1];
        vm.push(arrayRef, 0, (byte)30);
        vm.execute();
        assertEquals((byte)30, arrayRef[0]);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(31)
    public void CASTORE() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.CASTORE);
        char[] arrayRef = new char[1];
        vm.push(arrayRef, 0, 'x');
        vm.execute();
        assertEquals('x', arrayRef[0]);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(32)
    public void SASTORE() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.SASTORE);
        short[] arrayRef = new short[1];
        vm.push(arrayRef, 0, (short)35);
        vm.execute();
        assertEquals((short)35, arrayRef[0]);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(33)
    public void POP() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.POP);
        vm.push("Some object");
        vm.execute();
        assertEquals(0, vm.stackSize());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(34)
    public void POP2() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.POP2);
        vm.push("Some object");
        vm.push("Some other object");
        vm.execute();
        assertEquals(0, vm.stackSize());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(35)
    public void DUP() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DUP);
        Object value = "Some object";
        vm.push(value);
        vm.execute();
        assertEquals(2, vm.stackSize());
        assertEquals(value, vm.pop());
        assertEquals(value, vm.pop());
    }

    @Test
    @Order(36)
    public void DUP_X1() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DUP_X1);
        Object value = "Some object";
        Object value2 = "Some other object";
        vm.push(value2, value);
        vm.execute();
        assertEquals(3, vm.stackSize());
        assertSame(value, vm.pop());
        assertSame(value2, vm.pop());
        assertSame(value, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(37)
    public void DUP_X2() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DUP_X2);
        Object value = "Some object";
        Object value2 = "Some other object";
        Object value3 = "Some third object";
        vm.push(value3, value2, value);
        vm.execute();
        assertEquals(4, vm.stackSize());
        assertSame(value, vm.pop());
        assertSame(value2, vm.pop());
        assertSame(value3, vm.pop());
        assertSame(value, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(38)
    public void DUP2() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DUP2);
        Object value = "Some object";
        Object value2 = "Some other object";
        vm.push(value2, value);
        vm.execute();
        assertEquals(4, vm.stackSize());
        assertSame(value, vm.pop());
        assertSame(value2, vm.pop());
        assertSame(value, vm.pop());
        assertSame(value2, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(39)
    public void DUP2_X1() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DUP2_X1);
        Object value = "Some object";
        Object value2 = "Some other object";
        Object value3 = "Some third object";
        vm.push(value3, value2, value);
        vm.execute();
        assertEquals(5, vm.stackSize());
        assertSame(value, vm.pop());
        assertSame(value2, vm.pop());
        assertSame(value3, vm.pop());
        assertSame(value, vm.pop());
        assertSame(value2, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(40)
    public void DUP2_X2() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DUP2_X2);
        Object value = "Some object";
        Object value2 = "Some other object";
        Object value3 = "Some third object";
        Object value4 = "Some fourth object";
        vm.push(value4, value3, value2, value);
        vm.execute();
        assertEquals(6, vm.stackSize());
        assertSame(value, vm.pop());
        assertSame(value2, vm.pop());
        assertSame(value3, vm.pop());
        assertSame(value4, vm.pop());
        assertSame(value, vm.pop());
        assertSame(value2, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(41)
    public void SWAP() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.SWAP);
        Object value = "Some object";
        Object value2 = "Some other object";
        vm.push(value2, value);
        vm.execute();
        assertEquals(2, vm.stackSize());
        assertSame(value2, vm.pop());
        assertSame(value, vm.pop()); // Swapped
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(42)
    public void IADD() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.IADD);
        vm.push(2, 3);
        vm.execute();
        assertEquals(2 + 3, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(43)
    public void LADD() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LADD);
        vm.push(2L, 3L);
        vm.execute();
        assertEquals(2L + 3L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(44)
    public void FADD() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FADD);
        vm.push(2F, 3F);
        vm.execute();
        assertEquals(2F + 3F, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(45)
    public void DADD() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DADD);
        vm.push(2D, 3D);
        vm.execute();
        assertEquals(2D + 3D, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(46)
    public void ISUB() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ISUB);
        vm.push(5, 3);
        vm.execute();
        assertEquals(5 - 3, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(47)
    public void LSUB() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LSUB);
        vm.push(5L, 3L);
        vm.execute();
        assertEquals(5L - 3L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(48)
    public void FSUB() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FSUB);
        vm.push(5F, 3F);
        vm.execute();
        assertEquals(5F - 3F, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(49)
    public void DSUB() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DSUB);
        vm.push(5D, 3D);
        vm.execute();
        assertEquals(5D - 3D, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(50)
    public void IMUL() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.IMUL);
        vm.push(5, 2);
        vm.execute();
        assertEquals(5 * 2, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(51)
    public void LMUL() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LMUL);
        vm.push(5L, 2L);
        vm.execute();
        assertEquals(5L * 2L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(52)
    public void FMUL() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FMUL);
        vm.push(5F, 2F);
        vm.execute();
        assertEquals(5F * 2F, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(53)
    public void DMUL() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DMUL);
        vm.push(5D, 2D);
        vm.execute();
        assertEquals(5D * 2D, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(54)
    public void IDIV() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.IDIV);
        vm.push(10, 2);
        vm.execute();
        assertEquals(10 / 2, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(55)
    public void LDIV() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LDIV);
        vm.push(10L, 2L);
        vm.execute();
        assertEquals(10L / 2L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(56)
    public void FDIV() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FDIV);
        vm.push(10F, 2F);
        vm.execute();
        assertEquals(10F / 2F, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(57)
    public void DDIV() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DDIV);
        vm.push(10D, 2D);
        vm.execute();
        assertEquals(10D / 2D, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(58)
    public void IREM() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.IREM);
        vm.push(10, 2);
        vm.execute();
        assertEquals(10 % 2, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(59)
    public void LREM() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LREM);
        vm.push(10L, 2L);
        vm.execute();
        assertEquals(10L % 2L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(60)
    public void FREM() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FREM);
        vm.push(10F, 2F);
        vm.execute();
        assertEquals(10F % 2F, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(61)
    public void DREM() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DREM);
        vm.push(10D, 2D);
        vm.execute();
        assertEquals(10D % 2D, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(62)
    public void INEG() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.INEG);
        vm.push(10);
        vm.execute();
        assertEquals(-10, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(63)
    public void LNEG() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LNEG);
        vm.push(10L);
        vm.execute();
        assertEquals(-10L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(64)
    public void FNEG() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FNEG);
        vm.push(10F);
        vm.execute();
        assertEquals(-10F, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(65)
    public void DNEG() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DNEG);
        vm.push(10D);
        vm.execute();
        assertEquals(-10D, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(66)
    public void ISHL() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ISHL);
        vm.push(10, 5);
        vm.execute();
        assertEquals(10 << 5, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(67)
    public void LSHL() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LSHL);
        vm.push(10L, 5L);
        vm.execute();
        assertEquals(10L << 5L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(68)
    public void ISHR() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ISHR);
        vm.push(10, 5);
        vm.execute();
        assertEquals(10 >> 5, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(69)
    public void LSHR() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LSHR);
        vm.push(10L, 5L);
        vm.execute();
        assertEquals(10L >> 5L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(70)
    public void IUSHR() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.IUSHR);
        vm.push(10, 5);
        vm.execute();
        assertEquals(10 >>> 5, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(71)
    public void LUSHR() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LUSHR);
        vm.push(10L, 5L);
        vm.execute();
        assertEquals(10L >>> 5L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(72)
    public void IAND() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.IAND);
        vm.push(10, 5);
        vm.execute();
        assertEquals(10 & 5, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(73)
    public void LAND() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LAND);
        vm.push(10L, 5L);
        vm.execute();
        assertEquals(10L & 5L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(74)
    public void IOR() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.IOR);
        vm.push(10, 5);
        vm.execute();
        assertEquals(10 | 5, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(75)
    public void LOR() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LOR);
        vm.push(10L, 5L);
        vm.execute();
        assertEquals(10L | 5L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(76)
    public void IXOR() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.IXOR);
        vm.push(10, 5);
        vm.execute();
        assertEquals(10 ^ 5, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(77)
    public void LXOR() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LXOR);
        vm.push(10L, 5L);
        vm.execute();
        assertEquals(10L ^ 5L, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(78)
    public void I2L() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.I2L);
        vm.push(15);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Long.class, pop);
        assertEquals(15L, vm.cast(pop, Long.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(79)
    public void I2F() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.I2F);
        vm.push(15);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Float.class, pop);
        assertEquals(15F, vm.cast(pop, Float.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(80)
    public void I2D() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.I2D);
        vm.push(15);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Double.class, pop);
        assertEquals(15D, vm.cast(pop, Double.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(81)
    public void L2I() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.L2I);
        vm.push(15L);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Integer.class, pop);
        assertEquals(15, vm.cast(pop, Integer.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(82)
    public void L2F() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.L2F);
        vm.push(15L);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Float.class, pop);
        assertEquals(15F, vm.cast(pop, Float.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(83)
    public void L2D() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.L2D);
        vm.push(15L);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Double.class, pop);
        assertEquals(15D, vm.cast(pop, Double.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(84)
    public void F2I() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.F2I);
        vm.push(15F);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Integer.class, pop);
        assertEquals(15, vm.cast(pop, Integer.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(85)
    public void F2L() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.F2L);
        vm.push(15F);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Long.class, pop);
        assertEquals(15L, vm.cast(pop, Long.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(86)
    public void F2D() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.F2D);
        vm.push(15F);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Double.class, pop);
        assertEquals(15D, vm.cast(pop, Double.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(87)
    public void D2I() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.D2I);
        vm.push(15D);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Integer.class, pop);
        assertEquals(15, vm.cast(pop, Integer.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(88)
    public void D2L() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.D2L);
        vm.push(15D);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Long.class, pop);
        assertEquals(15L, vm.cast(pop, Long.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(89)
    public void D2F() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.D2F);
        vm.push(15D);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Float.class, pop);
        assertEquals(15F, vm.cast(pop, Float.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(90)
    public void I2B() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.I2B);
        vm.push(15);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Byte.class, pop);
        assertEquals((byte)15, vm.cast(pop, Byte.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(91)
    public void I2C() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.I2C);
        vm.push(15);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Character.class, pop);
        assertEquals((char)15, vm.cast(pop, Character.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(92)
    public void I2S() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.I2S);
        vm.push(15);
        vm.execute();
        Object pop = vm.pop();
        assertInstanceOf(Short.class, pop);
        assertEquals((short)15, vm.cast(pop, Short.TYPE));
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(93)
    public void LCMP() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LCMP);
        vm.push(10L, 5L);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(Long.compare(10L, 5L), value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(94)
    public void FCMPG() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FCMPG);
        vm.push(10F, 5F);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(Float.compare(10L, 5L), value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(95)
    public void FCMPG_NaN() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FCMPG);
        vm.push(10F, Float.NaN);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(1, value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(96)
    public void FCMPG_NaN2() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FCMPG);
        vm.push(Float.NaN, 5F);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(1, value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(97)
    public void FCMPL() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FCMPL);
        vm.push(10F, 5F);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(Float.compare(10L, 5L), value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(98)
    public void FCMPL_NaN() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FCMPL);
        vm.push(10F, Float.NaN);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(-1, value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(99)
    public void FCMPL_NaN2() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FCMPL);
        vm.push(Float.NaN, 5F);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(-1, value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(100)
    public void DCMPG() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DCMPG);
        vm.push(10D, 5D);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(Double.compare(10D, 5D), value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(101)
    public void DCMPG_NaN() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DCMPG);
        vm.push(10D, Double.NaN);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(1, value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(102)
    public void DCMPG_NaN2() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DCMPG);
        vm.push(Double.NaN, 5D);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(1, value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(103)
    public void DCMPL() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DCMPL);
        vm.push(10D, 5D);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(Double.compare(10D, 5D), value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(104)
    public void DCMPL_NaN() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DCMPL);
        vm.push(10D, Double.NaN);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(-1, value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(105)
    public void DCMPL_NaN2() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DCMPL);
        vm.push(Double.NaN, 5D);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object value = vm.pop();
        assertInstanceOf(Integer.class, value);
        assertEquals(-1, value);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(106)
    public void IRETURN() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.IRETURN);
        vm.push(1337);
        assertEquals(1337, vm.execute());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(107)
    public void LRETURN() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.LRETURN);
        vm.push(1337L);
        assertEquals(1337L, vm.execute());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(108)
    public void FRETURN() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.FRETURN);
        vm.push(1337F);
        assertEquals(1337F, vm.execute());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(109)
    public void DRETURN() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.DRETURN);
        vm.push(1337D);
        assertEquals(1337D, vm.execute());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(110)
    public void ARETURN() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ARETURN);
        vm.push("Hello world");
        assertEquals("Hello world", vm.execute());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(111)
    public void RETURN() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.RETURN);
        assertNull(vm.execute());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(112)
    public void ARRAYLENGTH() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ARRAYLENGTH);
        vm.push((Object)new Object[3]);
        vm.execute();
        assertEquals(3, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(113)
    public void ATHROW() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.ATHROW);
        vm.push(new Throwable("Test exception"));
        assertThrows(Throwable.class, vm::execute);
        assertEquals(0, vm.stackSize());
    }

    /* For tests: 114, 115 */
    private Object monitorTest;

    @Test
    @Order(114)
    public void MONITORENTER() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.MONITORENTER);
        monitorTest = new Object();
        vm.push(monitorTest);
        vm.execute();
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(115)
    public void MONITOREXIT() throws Throwable
    {
        var vm = createVM("VMInsnNode", VMOpcodes.MONITOREXIT);
        vm.push(monitorTest);
        vm.execute();
        assertEquals(0, vm.stackSize());
    }

}
