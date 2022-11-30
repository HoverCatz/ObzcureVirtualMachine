package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.VMStack;
import obzcu.re.virtualmachine.ObzcureVM;


import java.lang.reflect.Array;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMTypeInsnNode extends VMNode
{

    public VMTypeInsnNode(int opcode)
    {
        super(opcode);
    }

    @Override
    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        super.execute(vm, stack);
        switch (opcode)
        {
            case NEW -> stack.push(vm.getClass(getNextString()));
            case ANEWARRAY -> stack.push(Array.newInstance(vm.getClass(getNextString()), stack.popInt()));
            case CHECKCAST -> {
                Object obj = stack.pop();
                Class<?> argumentClazz = vm.getClass(getNextString());
                vm.cast(obj, argumentClazz); // TODO: Do we push the casted object,
                stack.push(obj);             //       or the original object?
            }
            case INSTANCEOF -> {
                Object S = stack.pop();
                if (S == null)
                    stack.push(false);
                else
                {
                    Class<?> T = vm.getClass(getNextString());
                    if (T.isPrimitive())
                        stack.push(vm.isInstance(T, S));
                    else
                        stack.push(T.isInstance(S));
                }
            }
            default -> throw new IllegalStateException("Unexpected opcode: " + opcode);
        }
    }

}
