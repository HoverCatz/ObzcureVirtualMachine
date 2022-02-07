package obzcu.re.vm.tests.tests;

import obzcu.re.virtualmachine.asm.VMOpcodes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.objectweb.asm.tree.FieldInsnNode;

import java.io.PrintStream;
import java.lang.invoke.MethodHandles;

import static obzcu.re.vm.tests.VMTests.createVM;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HoverCatz
 * @created 21.01.2022
 * @url https://github.com/HoverCatz
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_VMFieldInsnNode
{

    private final MethodHandles.Lookup lookup = MethodHandles.lookup();

    @Test
    @Order(1)
    public void GETSTATIC() throws Throwable
    {
        Object[] inputs = {
            new FieldInsnNode(VMOpcodes.GETSTATIC,
                "java.lang.System", "out", null),
            true /* isStatic */, false /* isPut */
        };
        var vm = createVM("VMFieldInsnNode", VMOpcodes.GETSTATIC, lookup, inputs);
        vm.execute();
        assertEquals(1, vm.stackSize());
        assertInstanceOf(PrintStream.class, vm.pop());
        assertEquals(0, vm.stackSize());
    }

    /* Used in: GETSTATIC_2 */
    private final static boolean testFieldStatic = false;

    @Test
    @Order(2)
    public void GETSTATIC_2() throws Throwable
    {
        Object[] inputs = {
            new FieldInsnNode(VMOpcodes.GETSTATIC,
                    getClass().getName(), "testFieldStatic", null),
            true /* isStatic */, false /* isPut */
        };
        var vm = createVM("VMFieldInsnNode", VMOpcodes.GETSTATIC, lookup, inputs);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object pop = vm.pop();
        assertInstanceOf(Boolean.class, pop);
        assertEquals(false, pop);
        assertEquals(0, vm.stackSize());
    }

    /* Used in: GETFIELD */
    private final boolean testField = false;

    @Test
    @Order(3)
    public void GETFIELD() throws Throwable
    {
        Object[] inputs = {
            new FieldInsnNode(VMOpcodes.GETFIELD,
                getClass().getName(), "testField", null),
            false /* isStatic */, false /* isPut */
        };
        var vm = createVM("VMFieldInsnNode", VMOpcodes.GETFIELD, lookup, inputs);
        vm.push(this);
        vm.execute();
        assertEquals(1, vm.stackSize());
        Object pop = vm.pop();
        assertInstanceOf(Boolean.class, pop);
        assertEquals(false, pop);
        assertEquals(0, vm.stackSize());
    }

    /* Used in: PUTSTATIC */
    private static boolean testFieldPutStatic;

    @Test
    @Order(4)
    public void PUTSTATIC() throws Throwable
    {
        Object[] inputs = {
            new FieldInsnNode(VMOpcodes.PUTSTATIC,
                getClass().getName(), "testFieldPutStatic", null),
            true /* isStatic */, true /* isPut */
        };
        var vm = createVM("VMFieldInsnNode", VMOpcodes.PUTSTATIC, lookup, inputs);
        vm.push(true);
        vm.execute();
        assertTrue(testFieldPutStatic);
        assertEquals(0, vm.stackSize());
    }

    /* Used in: PUTFIELD */
    private boolean testFieldPutField;

    @Test
    @Order(5)
    public void PUTFIELD() throws Throwable
    {
        Object[] inputs = {
            new FieldInsnNode(VMOpcodes.PUTFIELD,
                getClass().getName(), "testFieldPutField", null),
            false /* isStatic */, true /* isPut */
        };
        var vm = createVM("VMFieldInsnNode", VMOpcodes.PUTFIELD, lookup, inputs);
        vm.push(this, true);
        vm.execute();
        assertTrue(testFieldPutField);
        assertEquals(0, vm.stackSize());
    }

    /* Used in: PUTFINAL */
    private final boolean testFieldPutFinal = true;

    @Test
    @Order(6)
    public void PUTFINAL() throws Throwable
    {
        Object[] inputs = {
            new FieldInsnNode(VMOpcodes.PUTFIELD,
                getClass().getName(), "testFieldPutFinal", null),
            false /* isStatic */, true /* isPut */
        };
        var vm = createVM("VMFieldInsnNode", VMOpcodes.PUTFIELD, lookup, inputs);
        vm.push(this, false);
        assertThrows(RuntimeException.class, vm::execute);
    }

}
