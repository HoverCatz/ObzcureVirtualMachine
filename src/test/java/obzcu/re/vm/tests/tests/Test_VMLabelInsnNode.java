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
public class Test_VMLabelInsnNode
{

    @Test
    @Order(1)
    public void LABEL_INDEX() throws Throwable
    {
        var vm = createVM("VMLabelInsnNode", -1, 0);
        assertEquals(0, vm.getPointer()); // Label index should be the same as the curr
        vm.execute();
        assertEquals(1, vm.getPointer());
        assertEquals(0, vm.stackSize());
    }

}
