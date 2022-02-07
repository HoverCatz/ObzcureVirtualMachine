package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.VMStack;
import obzcu.re.virtualmachine.ObzcureVM;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMLineNumberInsnNode extends VMNode
{

    public VMLineNumberInsnNode(int opcode)
    {
        super(opcode);
    }

    @Override
    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        super.execute(vm, stack);
        vm.setLineNumber(getNextInt());
    }

}
