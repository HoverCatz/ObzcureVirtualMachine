package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.VMStack;
import obzcu.re.virtualmachine.ObzcureVM;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMJumpInsnNode extends VMNode
{

    public VMJumpInsnNode(int opcode)
    {
        super(opcode);
    }

    @Override
    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        super.execute(vm, stack);
//        if (vm.debug) System.out.println("VMJumpInsnNode before execute! curr = " + vm.curr());

        int val = getNextInt();
        int jumpTo = switch (opcode)
        {
            case GOTO -> val;

            case IF_ICMPEQ -> {
                int value2 = stack.popInt();
                int value = stack.popInt();
                yield value == value2 ? val : -1;
            }
            case IF_ICMPNE -> {
                int value2 = stack.popInt();
                int value = stack.popInt();
                yield value != value2 ? val : -1;
            }
            case IF_ICMPGT -> {
                int value2 = stack.popInt();
                int value = stack.popInt();
                yield value > value2 ? val : -1;
            }
            case IF_ICMPLT -> {
                int value2 = stack.popInt();
                int value = stack.popInt();
                yield value < value2 ? val : -1;
            }
            case IF_ICMPGE -> {
                int value2 = stack.popInt();
                int value = stack.popInt();
                yield value >= value2 ? val : -1;
            }
            case IF_ICMPLE -> {
                int value2 = stack.popInt();
                int value = stack.popInt();
                yield value <= value2 ? val : -1;
            }

            case IF_ACMPEQ -> stack.pop() == stack.pop() ? val : -1;
            case IF_ACMPNE -> stack.pop() != stack.pop() ? val : -1;

            case IFEQ -> stack.popInt() == 0 ? val : -1;
            case IFNE -> stack.popInt() != 0 ? val : -1;
            case IFGT -> stack.popInt() > 0 ? val : -1;
            case IFLT -> stack.popInt() < 0 ? val : -1;
            case IFGE -> stack.popInt() >= 0 ? val : -1;
            case IFLE -> stack.popInt() <= 0 ? val : -1;

            case IFNONNULL -> stack.pop() != null ? val : -1;
            case IFNULL -> stack.pop() == null ? val : -1;

            default -> throw new IllegalStateException("Unexpected value: " + opcode);
        };

        if (jumpTo != -1)
            vm.jump(jumpTo);

//        if (vm.debug) System.out.println("VMJumpInsnNode after execute! curr = " + vm.curr());
    }

}
