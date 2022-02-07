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
public class Test_VMIincInsnNode
{

    @Test
    @Order(1)
    public void IINC() throws Throwable
    {
        var vm = createVM("VMIincInsnNode", VMOpcodes.IINC, 0, 123);
        vm.setLocal(0, 123);
        vm.execute();
        assertEquals(123 + 123, vm.getLocal(0));
        assertEquals(0, vm.stackSize());
    }

}
