package obzcu.re.vm.tests.tests;

import obzcu.re.virtualmachine.asm.VMOpcodes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.objectweb.asm.Type;

import static obzcu.re.vm.tests.VMTests.createVM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author HoverCatz
 * @created 21.01.2022
 * @url https://github.com/HoverCatz
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_VMLdcInsnNode
{

    @Test
    @Order(1)
    public void STRING() throws Throwable
    {
        var vm = createVM("VMLdcInsnNode", VMOpcodes.LDC,
                "A string");
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals("A string", vm.pop());
    }

    @Test
    @Order(2)
    public void INT() throws Throwable
    {
        var vm = createVM("VMLdcInsnNode", VMOpcodes.LDC,
                25);
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(25, vm.pop());
    }

    @Test
    @Order(3)
    public void LONG() throws Throwable
    {
        var vm = createVM("VMLdcInsnNode", VMOpcodes.LDC,
                25L);
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(25L, vm.pop());
    }

    @Test
    @Order(4)
    public void FLOAT() throws Throwable
    {
        var vm = createVM("VMLdcInsnNode", VMOpcodes.LDC,
                25F);
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(25F, vm.pop());
    }

    @Test
    @Order(5)
    public void DOUBLE() throws Throwable
    {
        var vm = createVM("VMLdcInsnNode", VMOpcodes.LDC,
                25D);
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(25D, vm.pop());
    }

    @Test
    @Order(6)
    public void TYPE() throws Throwable
    {
        var vm = createVM("VMLdcInsnNode", VMOpcodes.LDC,
                Type.getType(Object.class));
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(Object.class, vm.pop());
    }

    @Test
    @Order(7)
    public void TYPE_2() throws Throwable
    {
        var vm = createVM("VMLdcInsnNode", VMOpcodes.LDC,
                Type.getType(String.class));
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertEquals(String.class, vm.pop());
    }

}
