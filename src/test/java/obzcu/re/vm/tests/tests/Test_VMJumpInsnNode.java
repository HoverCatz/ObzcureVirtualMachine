package obzcu.re.vm.tests.tests;

import obzcu.re.virtualmachine.asm.VMOpcodes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static obzcu.re.vm.tests.VMTests.createVM;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HoverCatz
 * @created 21.01.2022
 * @url https://github.com/HoverCatz
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_VMJumpInsnNode
{

    @Test
    @Order(1)
    public void GOTO() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.GOTO, 123);
        vm.execute();
        assertEquals(123, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(2)
    public void IF_ICMPEQ() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IF_ICMPEQ, 123);
        vm.push(5, 10);
        vm.execute();
        assertEquals(1, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(3)
    public void IF_ICMPNE() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IF_ICMPNE, 123);
        vm.push(5, 10);
        vm.execute();
        assertEquals(123, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(4)
    public void IF_ICMPGT() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IF_ICMPGT, 123);
        vm.push(5, 10); // 5 > 10 = false
        vm.execute();
        assertEquals(1, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(5)
    public void IF_ICMPLT() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IF_ICMPLT, 123);
        vm.push(5, 10); // 5 < 10 = true
        vm.execute();
        assertEquals(123, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(6)
    public void IF_ICMPGE() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IF_ICMPGE, 123);
        vm.push(5, 10); // 5 >= 10 = false
        vm.execute();
        assertEquals(1, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(7)
    public void IF_ICMPLE() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IF_ICMPLE, 123);
        vm.push(5, 10); // 5 <= 10 = true
        vm.execute();
        assertEquals(123, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(8)
    public void IF_ACMPEQ() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IF_ACMPEQ, 123);
        vm.push(5, 10); // 5 == 10 = false
        vm.execute();
        assertEquals(1, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(9)
    public void IF_ACMPNE() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IF_ACMPNE, 123);
        vm.push(5, 10); // 5 != 10 = true
        vm.execute();
        assertEquals(123, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(10)
    public void IFEQ() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IFEQ, 123);
        vm.push(5); // 5 == 0 = false
        vm.execute();
        assertEquals(1, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(11)
    public void IFNE() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IFNE, 123);
        vm.push(5); // 5 != 0 = true
        vm.execute();
        assertEquals(123, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(12)
    public void IFGT() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IFGT, 123);
        vm.push(5); // 5 > 0 = true
        vm.execute();
        assertEquals(123, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(13)
    public void IFLT() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IFLT, 123);
        vm.push(5); // 5 < 0 = false
        vm.execute();
        assertEquals(1, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(14)
    public void IFGE() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IFGE, 123);
        vm.push(5); // 5 >= 0 = true
        vm.execute();
        assertEquals(123, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(15)
    public void IFLE() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IFLE, 123);
        vm.push(5); // 5 <= 0 = false
        vm.execute();
        assertEquals(1, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(15)
    public void IFNONNULL() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IFNONNULL, 123);
        vm.push("Some object"); // Obviously not null
        vm.execute();
        assertEquals(123, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(16)
    public void IFNONNULL_2() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IFNONNULL, 123);
        vm.push((Object)null); // Obviously null
        vm.execute();
        assertEquals(1, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(17)
    public void IFNULL() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IFNULL, 123);
        vm.push("Some object"); // Obviously not null
        vm.execute();
        assertEquals(1, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(18)
    public void IFNULL_2() throws Throwable
    {
        var vm = createVM("VMJumpInsnNode", VMOpcodes.IFNULL, 123);
        vm.push((Object)null); // Obviously null
        vm.execute();
        assertEquals(123, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

}
