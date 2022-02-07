package obzcu.re.vm.tests.tests;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static obzcu.re.vm.tests.VMTests.createVM;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author HoverCatz
 * @created 21.01.2022
 * @url https://github.com/HoverCatz
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_VMLineNumberInsnNode
{

    @Test
    @Order(1)
    public void LINE_NUMBER() throws Throwable
    {
        var vm = createVM("VMLineNumberInsnNode", -1, 1234);
        vm.execute();
        assertEquals(1234, vm.getLineNumber());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(2)
    public void LINE_NUMBER_NEGATIVE() throws Throwable
    {
        var vm = createVM("VMLineNumberInsnNode", -1, -999);
        vm.execute();
        assertEquals(-999, vm.getLineNumber());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(3)
    public void LINE_NUMBER_MAX_INT() throws Throwable
    {
        var vm = createVM("VMLineNumberInsnNode", -1, Integer.MAX_VALUE);
        vm.execute();
        assertEquals(Integer.MAX_VALUE, vm.getLineNumber());
        assertEquals(0, vm.stackSize());
    }

    @Test
    @Order(4)
    public void LINE_NUMBER_MIN_INT() throws Throwable
    {
        var vm = createVM("VMLineNumberInsnNode", -1, Integer.MIN_VALUE);
        vm.execute();
        assertEquals(Integer.MIN_VALUE, vm.getLineNumber());
        assertEquals(0, vm.stackSize());
    }

}
