package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.VMStack;
import obzcu.re.virtualmachine.ObzcureVM;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMVarInsnNode extends VMNode
{

    public VMVarInsnNode(int opcode)
    {
        super(opcode);
    }

    @Override
    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        super.execute(vm, stack);
        switch (opcode)
        {
            case ILOAD, LLOAD, FLOAD, DLOAD, ALOAD -> stack.push(vm.getLocal(getNextInt()));
            case ISTORE, LSTORE, FSTORE, DSTORE, ASTORE -> vm.setLocal(getNextInt(), stack.pop());
            default -> throw new IllegalStateException("Unexpected value: " + opcode);
        }
    }

}
