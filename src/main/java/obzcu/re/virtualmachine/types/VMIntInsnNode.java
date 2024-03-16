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
            int count = stack.popInt();
            Class<?> arrayType;
            switch (getNextInt())
            {
                case T_BOOLEAN: arrayType = Boolean.TYPE; break;
                case T_CHAR: arrayType = Character.TYPE; break;
                case T_FLOAT: arrayType = Float.TYPE; break;
                case T_DOUBLE: arrayType = Double.TYPE; break;
                case T_BYTE: arrayType = Byte.TYPE; break;
                case T_SHORT: arrayType = Short.TYPE; break;
                case T_INT: arrayType = Integer.TYPE; break;
                case T_LONG: arrayType = Long.TYPE; break;
                default: throw new RuntimeException("Invalid NEWARRAY type: '" + input[0] + "'");
            };
            stack.push(Array.newInstance(arrayType, count));
        }
    }

}
