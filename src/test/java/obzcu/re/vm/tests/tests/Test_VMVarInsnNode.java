package obzcu.re.vm.tests.tests;

import obzcu.re.virtualmachine.asm.VMOpcodes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.objectweb.asm.Type;

import static obzcu.re.vm.tests.VMTests.createVM;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author HoverCatz
 * @created 21.01.2022
 * @url https://github.com/HoverCatz
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_VMVarInsnNode
{

    @Test
    @Order(1)
    public void ILOAD() throws Throwable
    {
        var vm = createVM("VMVarInsnNode", VMOpcodes.ILOAD, 0);
        vm.setLocal(0, 123);
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(123, vm.pop());
    }

    @Test
    @Order(2)
    public void LLOAD() throws Throwable
    {
        var vm = createVM("VMVarInsnNode", VMOpcodes.LLOAD, 0);
        vm.setLocal(0, 123L);
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(123L, vm.pop());
    }

    @Test
    @Order(3)
    public void FLOAD() throws Throwable
    {
        var vm = createVM("VMVarInsnNode", VMOpcodes.FLOAD, 0);
        vm.setLocal(0, 123F);
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(123F, vm.pop());
    }

    @Test
    @Order(4)
    public void DLOAD() throws Throwable
    {
        var vm = createVM("VMVarInsnNode", VMOpcodes.DLOAD, 0);
        vm.setLocal(0, 123D);
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(123D, vm.pop());
    }

    @Test
    @Order(5)
    public void ALOAD() throws Throwable
    {
        var vm = createVM("VMVarInsnNode", VMOpcodes.ALOAD, 0);
        vm.setLocal(0, "Some string");
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals("Some string", vm.pop());
    }

    @Test
    @Order(6)
    public void ISTORE() throws Throwable
    {
        var vm = createVM("VMVarInsnNode", VMOpcodes.ISTORE, 0);
        vm.push(123);
        vm.execute();
        assertEquals(0, vm.stackSize());
        assertEquals(123, vm.getLocal(0));
    }

    @Test
    @Order(7)
    public void LSTORE() throws Throwable
    {
        var vm = createVM("VMVarInsnNode", VMOpcodes.LSTORE, 0);
        vm.push(123L);
        vm.execute();
        assertEquals(0, vm.stackSize());
        assertEquals(123L, vm.getLocal(0));
    }

    @Test
    @Order(8)
    public void FSTORE() throws Throwable
    {
        var vm = createVM("VMVarInsnNode", VMOpcodes.FSTORE, 0);
        vm.push(123F);
        vm.execute();
        assertEquals(0, vm.stackSize());
        assertEquals(123F, vm.getLocal(0));
    }

    @Test
    @Order(9)
    public void DSTORE() throws Throwable
    {
        var vm = createVM("VMVarInsnNode", VMOpcodes.DSTORE, 0);
        vm.push(123D);
        vm.execute();
        assertEquals(0, vm.stackSize());
        assertEquals(123D, vm.getLocal(0));
    }

    @Test
    @Order(10)
    public void ASTORE() throws Throwable
    {
        var vm = createVM("VMVarInsnNode", VMOpcodes.ASTORE, 0);
        vm.push("A string!");
        vm.execute();
        assertEquals(0, vm.stackSize());
        assertEquals("A string!", vm.getLocal(0));
    }

}
