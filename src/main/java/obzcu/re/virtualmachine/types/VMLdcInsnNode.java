package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.VMStack;
import obzcu.re.virtualmachine.ObzcureVM;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMLdcInsnNode extends VMNode
{

    public VMLdcInsnNode(int opcode)
    {
        super(opcode);
    }

    @Override
    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        super.execute(vm, stack);
        int type = getNextInt();
        if (type == 5) // Type
            stack.push(vm.getClass(getNextString()));
        else
            stack.push(getNext());
    }

}
