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
public class Test_VMTypeInsnNode
{

    @Test
    @Order(1)
    public void NEW() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.NEW, "java.lang.String");
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(String.class, vm.pop());
    }

    @Test
    @Order(2)
    public void NEW_Test_VMTypeInsnNode() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.NEW, Test_VMTypeInsnNode.class.getName());
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(Test_VMTypeInsnNode.class, vm.pop());
    }

    @Test
    @Order(3)
    public void NEW_Test_I() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.NEW, "I");
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(Integer.TYPE, vm.pop());
    }

    @Test
    @Order(4)
    public void NEW_Test_Integer() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.NEW, Integer.class.getName());
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(Integer.class, vm.pop());
    }

    @Test
    @Order(5)
    public void ANEWARRAY() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.ANEWARRAY, Integer.class.getName());
        vm.push(3); // count
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertInstanceOf(Integer[].class, vm.pop());
    }

    @Test
    @Order(6)
    public void ANEWARRAY_I() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.ANEWARRAY, "I");
        vm.push(5); // count
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertInstanceOf(int[].class, vm.pop());
    }

    @Test
    @Order(7)
    public void ANEWARRAY_String() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.ANEWARRAY, String.class.getName());
        vm.push(2); // count
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertInstanceOf(String[].class, vm.pop());
    }

    @Test
    @Order(8)
    public void CHECKCAST() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.CHECKCAST, String.class.getName());
        vm.push("Hello world");
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        assertInstanceOf(String.class, vm.pop());
    }

    @Test
    @Order(9)
    public void CHECKCAST_Throw() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.CHECKCAST, String.class.getName());
        vm.push(13.37D);
        assertThrows(ClassCastException.class, vm::execute);
    }

    @Test
    @Order(10)
    public void CHECKCAST_I() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.CHECKCAST, "I");
        vm.push(1);
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        assertInstanceOf(Integer.class, vm.pop());
    }

    @Test
    @Order(11)
    public void INSTANCEOF() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.INSTANCEOF, "I");
        vm.push(1);
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        assertEquals(true, vm.pop());
    }

    @Test
    @Order(12)
    public void INSTANCEOF_I() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.INSTANCEOF, "int");
        vm.push(1);
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        assertEquals(true, vm.pop());
    }

    @Test
    @Order(13)
    public void INSTANCEOF_Integer() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.INSTANCEOF, "java.lang.Integer");
        vm.push(1);
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        assertEquals(true, vm.pop());
    }

    @Test
    @Order(14)
    public void INSTANCEOF_False() throws Throwable
    {
        var vm = createVM("VMTypeInsnNode", VMOpcodes.INSTANCEOF, "I");
        vm.push(1D);
        assertDoesNotThrow(vm::execute);
        assertEquals(1, vm.stackSize());
        assertEquals(false, vm.pop());
    }

}
