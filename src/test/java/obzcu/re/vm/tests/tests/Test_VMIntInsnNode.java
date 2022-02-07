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
public class Test_VMIntInsnNode
{

    @Test
    @Order(1)
    public void BIPUSH() throws Throwable
    {
        var vm = createVM("VMIntInsnNode", VMOpcodes.BIPUSH, 1337);
        vm.execute();
        assertEquals((byte)1337, vm.pop()); // 1337b -> 57b (cuz byte overflow)
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(2)
    public void SIPUSH() throws Throwable
    {
        var vm = createVM("VMIntInsnNode", VMOpcodes.SIPUSH, 1337);
        vm.execute();
        assertEquals((short) 1337, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(3)
    public void NEWARRAY_BOOLEAN_3() throws Throwable
    {
        var vm = createVM("VMIntInsnNode", VMOpcodes.NEWARRAY, VMOpcodes.T_BOOLEAN);
        vm.push(3); // array count
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object pop = vm.pop();
        assertInstanceOf(boolean[].class, pop);
        assertEquals(3, ((boolean[])pop).length);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(4)
    public void NEWARRAY_BYTE_7() throws Throwable
    {
        var vm = createVM("VMIntInsnNode", VMOpcodes.NEWARRAY, VMOpcodes.T_BYTE);
        vm.push(7); // array count
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object pop = vm.pop();
        assertInstanceOf(byte[].class, pop);
        assertEquals(7, ((byte[])pop).length);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(5)
    public void NEWARRAY_DOUBLE_EMPTY() throws Throwable
    {
        var vm = createVM("VMIntInsnNode", VMOpcodes.NEWARRAY, VMOpcodes.T_DOUBLE);
        vm.push(0); // array count
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object pop = vm.pop();
        assertInstanceOf(double[].class, pop);
        assertEquals(0, ((double[])pop).length);
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(6)
    public void NEWARRAY_INVALID_TYPE() throws Throwable
    {
        var vm = createVM("VMIntInsnNode", VMOpcodes.NEWARRAY, 123); // Valid: [4..11]
        vm.push(5); // array count
        assertThrows(RuntimeException.class, vm::execute);
        assertEquals(0, vm.stackSize());
    }

}
