package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.VMStack;
import obzcu.re.virtualmachine.ObzcureVM;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMIincInsnNode extends VMNode
{

    public VMIincInsnNode(int opcode)
    {
        super(opcode);
    }

    @Override
    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        super.execute(vm, stack);
        int var = getNextInt();
        int incr = getNextInt();
        int number = (int) vm.cast(vm.getLocal(var), Integer.TYPE);
        vm.setLocal(var, number + incr);
    }

}
