package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.ObzcureVM;
import obzcu.re.virtualmachine.VMStack;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMLookupSwitchInsnNode extends VMNode
{

    public VMLookupSwitchInsnNode(int opcode)
    {
        super(opcode);
    }

    @Override
    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        super.execute(vm, stack);

        int hashCode = stack.popInt();

        int defaultIndex = getNextInt();
        int[] keys = (int[]) getNext();
        int[] labels = (int[]) getNext();

        for (int i = 0; i < keys.length; i++)
            if (hashCode == keys[i])
            {
                vm.jump(labels[i]);
                return;
            }

        vm.jump(defaultIndex);
    }

}
