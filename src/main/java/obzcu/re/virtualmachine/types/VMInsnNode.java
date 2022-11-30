package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.VMStack;
import obzcu.re.virtualmachine.ObzcureVM;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMInsnNode extends VMNode
{

    private final Map<Object, ReentrantLock> locks = new HashMap<>();

    public VMInsnNode(int opcode)
    {
        super(opcode);
    }

    @Override
    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        switch (opcode)
        {
            case NOP -> { } // Do nothing!
            case ACONST_NULL -> stack.push(null);

            case ICONST_M1 -> stack.push(-1);
            case ICONST_0 -> stack.push(0);
            case ICONST_1 -> stack.push(1);
            case ICONST_2 -> stack.push(2);
            case ICONST_3 -> stack.push(3);
            case ICONST_4 -> stack.push(4);
            case ICONST_5 -> stack.push(5);

            case LCONST_0 -> stack.push(0L);
            case LCONST_1 -> stack.push(1L);

            case FCONST_0 -> stack.push(0F);
            case FCONST_1 -> stack.push(1F);
            case FCONST_2 -> stack.push(2F);

            case DCONST_0 -> stack.push(0D);
            case DCONST_1 -> stack.push(1D);

            case IALOAD -> {
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                stack.push(Array.getInt(arrayRef, index));
            }
            case LALOAD -> {
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                stack.push(Array.getLong(arrayRef, index));
            }
            case FALOAD ->  {
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                stack.push(Array.getFloat(arrayRef, index));
            }
            case DALOAD -> {
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                stack.push(Array.getDouble(arrayRef, index));
            }
            case AALOAD -> {
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
//                System.out.println("arrayRef: " + arrayRef);
//                System.out.println("index: " + index);
//                System.out.println("count: " + Array.getLength(arrayRef));
//                if (index >= Array.getLength(arrayRef))
//                {
//                    System.out.println("!!! wtf");
//                    System.out.println("Curr: " + vm.getCurr());
//                    System.out.println("ClassName: " + vm.getClassName());
//                    System.out.println("MethodName: " + vm.getMethodName());
//                    System.out.println("LineNumber: " + vm.getLineNumber());
//                    System.out.println();
//                }
                stack.push(Array.get(arrayRef, index));
            }
            case BALOAD -> {
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                stack.push(Array.getByte(arrayRef, index));
            }
            case CALOAD -> {
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                stack.push(Array.getChar(arrayRef, index));
            }
            case SALOAD -> {
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                stack.push(Array.getShort(arrayRef, index));
            }

            case IASTORE -> {
                int value = stack.popInt(vm);
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                Array.setInt(arrayRef, index, value);
            }
            case LASTORE -> {
                long value = stack.popLong(vm);
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                Array.setLong(arrayRef, index, value);
            }
            case FASTORE -> {
                float value = stack.popFloat(vm);
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                Array.setFloat(arrayRef, index, value);
            }
            case DASTORE -> {
                double value = stack.popDouble(vm);
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                Array.setDouble(arrayRef, index, value);
            }
            case AASTORE -> {
                Object value = stack.pop();
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                Array.set(arrayRef, index, value);
            }
            case BASTORE -> {
                byte value = stack.popByte(vm);
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                Array.setByte(arrayRef, index, value);
            }
            case CASTORE -> {
                char value = stack.popChar(vm);
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                Array.setChar(arrayRef, index, value);
            }
            case SASTORE -> {
                short value = stack.popShort(vm);
                int index = stack.popInt(vm);
                Object arrayRef = stack.pop();
                Array.setShort(arrayRef, index, value);
            }

            case POP -> stack.pop();
            case POP2 ->
            {
                Object value = stack.pop();
                if (!(value instanceof Long || value instanceof Double))
                    stack.pop();
            }
            case DUP -> {
                Object value = stack.pop();
                stack.push(value);
                stack.push(value);
            }
            case DUP_X1 -> {
                Object value = stack.pop();
                Object value2 = stack.pop();
//                assertTypeNot(value2, 2, Double.class, Long.class);
//                assertTypeNot(value, 1, Double.class, Long.class);
                stack.push(value);
                stack.push(value2);
                stack.push(value);
            }
            case DUP_X2 -> {
                Object value = stack.pop();
                Object value2 = stack.pop();
                if (vm.isWide(value2)) {
                    stack.push(value);
                    stack.push(value2);
                    stack.push(value);
                } else {
                    Object value3 = stack.pop();
                    stack.push(value);
                    stack.push(value3);
                    stack.push(value2);
                    stack.push(value);
                }

            }
            case DUP2 -> {
                Object value = stack.pop();

                if (value instanceof Double || value instanceof Long)
                {
                    stack.push(value);
                    stack.push(value);
                }
                else
                {
                    Object value2 = stack.pop();
                    stack.push(value2);
                    stack.push(value);
                    stack.push(value2);
                    stack.push(value);
                }
            }
            case DUP2_X1 -> {
                Object value = stack.pop();
                Object value2 = stack.pop();

                if (vm.isWide(value)) {
                    stack.push(value);
                    stack.push(value2);
                    stack.push(value);
                }
                else
                {
                    Object value3 = stack.pop();
                    stack.push(value2);
                    stack.push(value);
                    stack.push(value3);
                    stack.push(value2);
                    stack.push(value);
                }
            }
            case DUP2_X2 -> {
                Object value = stack.pop();
                Object value2 = stack.pop();

                if (vm.isWide(value)) {
                    if (vm.isWide(value2)) {
                        stack.push(value);
                        stack.push(value2);
                        stack.push(value);
                    }
                    else
                    {
                        Object value3 = stack.pop();
                        stack.push(value);
                        stack.push(value3);
                        stack.push(value2);
                        stack.push(value);
                    }
                }
                else
                {
                    Object value3 = stack.pop();
                    if (vm.isWide(value3)) {
                        stack.push(value2);
                        stack.push(value);
                        stack.push(value3);
                        stack.push(value2);
                        stack.push(value);
                    }
                    else
                    {
                        Object value4 = stack.pop();
                        stack.push(value2);
                        stack.push(value);
                        stack.push(value4);
                        stack.push(value3);
                        stack.push(value2);
                        stack.push(value);
                    }
                }
            }
            case SWAP -> {
                Object value = stack.pop();
                Object value2 = stack.pop();
                stack.push(value);
                stack.push(value2);
            }
            case IADD -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Integer.TYPE);
                assertTypeNot(value, 1, Integer.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((int)value + (int)value2);
            }
            case LADD -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Long.TYPE);
                assertTypeNot(value, 1, Long.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((long)value + (long)value2);
            }
            case FADD -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Float.TYPE);
                assertTypeNot(value, 1, Float.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Float.TYPE);
                value = vm.cast(value, value.getClass(), Float.TYPE);
                stack.push((float)value + (float)value2);
            }
            case DADD -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Double.TYPE);
                assertTypeNot(value, 1, Double.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Double.TYPE);
                value = vm.cast(value, value.getClass(), Double.TYPE);
                stack.push((double)value + (double)value2);
            }
            case ISUB -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Integer.TYPE);
                assertTypeNot(value, 1, Integer.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((int)value - (int)value2);
            }
            case LSUB -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Long.TYPE);
                assertTypeNot(value, 1, Long.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((long)value - (long)value2);
            }
            case FSUB -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Float.TYPE);
                assertTypeNot(value, 1, Float.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Float.TYPE);
                value = vm.cast(value, value.getClass(), Float.TYPE);
                stack.push((float)value - (float)value2);
            }
            case DSUB -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Double.TYPE);
                assertTypeNot(value, 1, Double.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Double.TYPE);
                value = vm.cast(value, value.getClass(), Double.TYPE);
                stack.push((double)value - (double)value2);
            }
            case IMUL -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Integer.TYPE);
                assertTypeNot(value, 1, Integer.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((int)value * (int)value2);
            }
            case LMUL -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Long.TYPE);
                assertTypeNot(value, 1, Long.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((long)value * (long)value2);
            }
            case FMUL -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Float.TYPE);
                assertTypeNot(value, 1, Float.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Float.TYPE);
                value = vm.cast(value, value.getClass(), Float.TYPE);
                stack.push((float)value * (float)value2);
            }
            case DMUL -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Double.TYPE);
                assertTypeNot(value, 1, Double.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Double.TYPE);
                value = vm.cast(value, value.getClass(), Double.TYPE);
                stack.push((double)value * (double)value2);
            }
            case IDIV -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Integer.TYPE);
                assertTypeNot(value, 1, Integer.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((int)value / (int)value2);
            }
            case LDIV -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Long.TYPE);
                assertTypeNot(value, 1, Long.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((long)value / (long)value2);
            }
            case FDIV -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Float.TYPE);
                assertTypeNot(value, 1, Float.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Float.TYPE);
                value = vm.cast(value, value.getClass(), Float.TYPE);
                stack.push((float)value / (float)value2);
            }
            case DDIV -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Double.TYPE);
                assertTypeNot(value, 1, Double.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Double.TYPE);
                value = vm.cast(value, value.getClass(), Double.TYPE);
                stack.push((double)value / (double)value2);
            }
            case IREM -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Integer.TYPE);
                assertTypeNot(value, 1, Integer.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((int)value % (int)value2);
            }
            case LREM -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Long.TYPE);
                assertTypeNot(value, 1, Long.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((long)value % (long)value2);
            }
            case FREM -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Float.TYPE);
                assertTypeNot(value, 1, Float.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Float.TYPE);
                value = vm.cast(value, value.getClass(), Float.TYPE);
                stack.push((float)value % (float)value2);
            }
            case DREM -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Double.TYPE);
                assertTypeNot(value, 1, Double.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Double.TYPE);
                value = vm.cast(value, value.getClass(), Double.TYPE);
                stack.push((double)value % (double)value2);
            }
            case INEG -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push(-(int)value);
            }
            case LNEG -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push(-(long)value);
            }
            case FNEG -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Float.TYPE);
                value = vm.cast(value, value.getClass(), Float.TYPE);
                stack.push(-(float)value);
            }
            case DNEG -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Double.TYPE);
                value = vm.cast(value, value.getClass(), Double.TYPE);
                stack.push(-(double)value);
            }
            case ISHL -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Integer.TYPE);
                assertTypeNot(value, 1, Integer.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((int)value << (int)value2);
            }
            case LSHL -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Long.TYPE);
                assertTypeNot(value, 1, Long.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((long)value << (long)value2);
            }
            case ISHR -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Integer.TYPE);
                assertTypeNot(value, 1, Integer.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((int)value >> (int)value2);
            }
            case LSHR -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Long.TYPE);
                assertTypeNot(value, 1, Long.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((long)value >> (long)value2);
            }
            case IUSHR -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Integer.TYPE);
                assertTypeNot(value, 1, Integer.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((int)value >>> (int)value2);
            }
            case LUSHR -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Long.TYPE);
                assertTypeNot(value, 1, Long.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((long)value >>> (long)value2);
            }
            case IAND -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Integer.TYPE);
                assertTypeNot(value, 1, Integer.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((int)value & (int)value2);
            }
            case LAND -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Long.TYPE);
                assertTypeNot(value, 1, Long.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((long)value & (long)value2);
            }
            case IOR -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Integer.TYPE);
                assertTypeNot(value, 1, Integer.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((int)value | (int)value2);
            }
            case LOR -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Long.TYPE);
                assertTypeNot(value, 1, Long.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((long)value | (long)value2);
            }
            case IXOR -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Integer.TYPE);
                assertTypeNot(value, 1, Integer.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((int)value ^ (int)value2);
            }
            case LXOR -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Long.TYPE);
                assertTypeNot(value, 1, Long.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((long)value ^ (long)value2);
            }
            case I2L -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((long)(int)value);
            }
            case I2F -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((float)(int)value);
            }
            case I2D -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((double)(int)value);
            }
            case L2I -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((int)(long)value);
            }
            case L2F -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((float)(long)value);
            }
            case L2D -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push((double)(long)value);
            }
            case F2I -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Float.TYPE);
                value = vm.cast(value, value.getClass(), Float.TYPE);
                stack.push((int)(float)value);
            }
            case F2L -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Float.TYPE);
                value = vm.cast(value, value.getClass(), Float.TYPE);
                stack.push((long)(float)value);
            }
            case F2D -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Float.TYPE);
                value = vm.cast(value, value.getClass(), Float.TYPE);
                stack.push((double)(float)value);
            }
            case D2I -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Double.TYPE);
                value = vm.cast(value, value.getClass(), Double.TYPE);
                stack.push((int)(double)value);
            }
            case D2L -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Double.TYPE);
                value = vm.cast(value, value.getClass(), Double.TYPE);
                stack.push((long)(double)value);
            }
            case D2F -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Double.TYPE);
                value = vm.cast(value, value.getClass(), Double.TYPE);
                stack.push((float)(double)value);
            }
            case I2B -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((byte)(int)value);
            }
            case I2C -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((char)(int)value);
            }
            case I2S -> {
                Object value = stack.pop();
                assertTypeNot(value, 1, Integer.TYPE);
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                stack.push((short)(int)value);
            }
            case LCMP -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Long.TYPE);
                assertTypeNot(value, 1, Long.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Long.TYPE);
                value = vm.cast(value, value.getClass(), Long.TYPE);
                stack.push(Long.compare((long) value, (long) value2));
            }
            case FCMPG -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Float.TYPE);
                assertTypeNot(value, 1, Float.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Float.TYPE);
                value = vm.cast(value, value.getClass(), Float.TYPE);
                float v = (float) value;
                float v2 = (float) value2;
                if (Float.isNaN(v) || Float.isNaN(v2))
                    stack.push(1);
                else
                    stack.push(Float.compare(v, v2));
            }
            case FCMPL -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Float.TYPE);
                assertTypeNot(value, 1, Float.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Float.TYPE);
                value = vm.cast(value, value.getClass(), Float.TYPE);
                float v = (float) value;
                float v2 = (float) value2;
                if (Float.isNaN(v) || Float.isNaN(v2))
                    stack.push(-1);
                else
                    stack.push(Float.compare(v, v2));
            }
            case DCMPG -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Double.TYPE);
                assertTypeNot(value, 1, Double.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Double.TYPE);
                value = vm.cast(value, value.getClass(), Double.TYPE);
                double v = (double) value;
                double v2 = (double) value2;
                if (Double.isNaN(v) || Double.isNaN(v2))
                    stack.push(1);
                else
                    stack.push(Double.compare(v, v2));
            }
            case DCMPL -> {
                Object value2 = stack.pop();
                Object value = stack.pop();
                assertTypeNot(value2, 2, Double.TYPE);
                assertTypeNot(value, 1, Double.TYPE);
                value2 = vm.cast(value2, value2.getClass(), Double.TYPE);
                value = vm.cast(value, value.getClass(), Double.TYPE);
                double v = (double) value;
                double v2 = (double) value2;
                if (Double.isNaN(v) || Double.isNaN(v2))
                    stack.push(-1);
                else
                    stack.push(Double.compare(v, v2));
            }
            case IRETURN -> {
                Object value = stack.pop();
                value = vm.cast(value, value.getClass(), Integer.TYPE);
                returnObject(value);
            }
            case LRETURN -> {
                Object value = stack.pop();
                value = vm.cast(value, value.getClass(), Long.TYPE);
                returnObject(value);
            }
            case FRETURN -> {
                Object value = stack.pop();
                value = vm.cast(value, value.getClass(), Float.TYPE);
                returnObject(value);
            }
            case DRETURN -> {
                Object value = stack.pop();
                value = vm.cast(value, value.getClass(), Double.TYPE);
                returnObject(value);
            }
            case ARETURN -> returnObject(stack.pop());
            case RETURN -> vm.jump(-1);
            case ARRAYLENGTH -> stack.push(Array.getLength(stack.pop()));
            case ATHROW -> throw (Throwable)stack.pop();
            case MONITORENTER -> {
                Object value = stack.pop();
                if (locks.containsKey(value))
                {
                    ReentrantLock lock = locks.get(value);
                    lock.lock();
                }
                else
                {
                    ReentrantLock lock = new ReentrantLock();
                    lock.lock();
                    locks.put(value, lock);
                }
            }
            case MONITOREXIT -> {
                Object value = stack.pop();
                if (locks.containsKey(value))
                {
                    ReentrantLock lock = locks.remove(value);
                    lock.unlock();
                }
                // Ignore if it doesn't exist, shouldn't happen anyway
            }
        }
    }

    public void assertTypeNot(Object obj, int n, Class<?>... clazz)
    {
        for (Class<?> cls : clazz)
            if (cls.isInstance(obj))
                throw new RuntimeException("obj" + n + " was of type " + cls.getSimpleName() + ".");
    }

}
