package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.VMStack;
import obzcu.re.virtualmachine.ObzcureVM;

import java.lang.reflect.Array;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMIntInsnNode extends VMNode
{

    public VMIntInsnNode(int opcode)
    {
        super(opcode);
    }

    @Override
    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        super.execute(vm, stack);
        if (opcode == BIPUSH)
        {
            stack.push((byte)getNextInt());
        }
        else
        if (opcode == SIPUSH)
        {
            stack.push((short)getNextInt());
        }
        else
        if (opcode == NEWARRAY)
        {
            int count = stack.popInt(vm);
            Class<?> arrayType = switch (getNextInt())
            {
                case T_BOOLEAN -> Boolean.TYPE;
                case T_CHAR -> Character.TYPE;
                case T_FLOAT -> Float.TYPE;
                case T_DOUBLE -> Double.TYPE;
                case T_BYTE -> Byte.TYPE;
                case T_SHORT -> Short.TYPE;
                case T_INT -> Integer.TYPE;
                case T_LONG -> Long.TYPE;
                default -> throw new RuntimeException("Invalid NEWARRAY type: '" + input[0] + "'");
            };
            stack.push(Array.newInstance(arrayType, count));
        }
    }

}
