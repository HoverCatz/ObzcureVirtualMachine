package obzcu.re.virtualmachine;

import obzcu.re.virtualmachine.asm.VMOpcodes;
import obzcu.re.virtualmachine.types.*;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMLoader
{

    private static final boolean debug = false;

    public static VMNode[] load(ObzcureVM vm, DataInputStream dis) throws Throwable
    {
        if (!dis.readUTF().equals("Meow")) // Meow!
            throw new RuntimeException("Invalid meow file! Meow meow!");
        vm.setClassName(dis.readUTF()); // className
        vm.setMethodName(dis.readUTF()); // methodName
        vm.setMethodDesc(dis.readUTF()); // methodDesc
        List<VMNode> nodes = new ArrayList<>();
        boolean done = false;
        int curr = 0;
        while (!done)
        {
            if (debug) System.err.println();
            if (debug) System.err.println("\033[91mdis.available(): " + dis.available());
            String className = dis.readUTF();
            if (debug) System.err.println("\033[91mclassName: '" + className + "'");
            if (debug && !className.equals("Meow"))
                System.out.println("curr: " + curr++);
            switch (className)
            {
                case "VMLabelInsnNode": {
                    VMLabelInsnNode node = new VMLabelInsnNode(dis.readInt());
                    if (debug) System.err.println("\033[91mopcode: " + node.opcode);
                    int index = dis.readInt();
                    if (debug) System.err.println("\033[91mindex: " + index);
                    setInput(vm, node, index);
                    nodes.add(node);
                } break;
                case "VMLineNumberInsnNode": {
                    VMLineNumberInsnNode node = new VMLineNumberInsnNode(dis.readInt());
                    if (debug) System.err.println("\033[91mopcode: " + node.opcode);
                    int lineNumber = dis.readInt();
                    if (debug) System.err.println("\033[91mlineNumber: " + lineNumber);
                    setInput(vm, node, lineNumber);
                    nodes.add(node);
                } break;
                case "VMJumpInsnNode": {
                    VMJumpInsnNode node = new VMJumpInsnNode(dis.readInt());
                    if (debug) System.err.println("\033[91mopcode: " + node.opcode);
                    int index = dis.readInt();
                    if (debug) System.err.println("\033[91mindex: " + index);
                    setInput(vm, node, index);
                    nodes.add(node);
                } break;
                case "VMIntInsnNode": {
                    VMIntInsnNode node = new VMIntInsnNode(dis.readInt());
                    if (debug) System.err.println("\033[91mopcode: " + node.opcode);
                    int operand = dis.readInt();
                    if (debug) System.err.println("\033[91moperand: " + operand);
                    setInput(vm, node, operand);
                    nodes.add(node);
                } break;
                case "VMInsnNode": {
                    VMInsnNode node = new VMInsnNode(dis.readInt());
                    if (debug) System.err.println("\033[91mopcode: " + node.opcode);
                    // No inputs, we use the opcode
                    nodes.add(node);
                } break;
                case "VMFieldInsnNode": {
                    VMFieldInsnNode node = new VMFieldInsnNode(dis.readInt());
                    if (debug) System.err.println("\033[91mopcode: " + node.opcode);
                    setInput(vm, node,
                        dis.readUTF(), // owner
                        dis.readUTF(), // name
                        dis.readBoolean(), // isStatic
                        dis.readBoolean()  // PUT=true, GET=false
                    );
                    if (debug) System.err.println("\033[91mobjects: " + Arrays.toString(node.original));
                    nodes.add(node);
                } break;
                case "VMMethodInsnNode": {
                    VMMethodInsnNode node = new VMMethodInsnNode(dis.readInt());
                    if (debug) System.err.println("\033[91mopcode: " + node.opcode);
                    List<Object> inputs = new ArrayList<>();
                    inputs.add(dis.readUTF()); // owner
                    inputs.add(dis.readUTF()); // name
                    int argumentsCount = dis.readInt();
                    inputs.add(argumentsCount);
                    for (int i = 0; i < argumentsCount; i++)
                        inputs.add(dis.readUTF()); // argument types
                    inputs.add(dis.readUTF()); // returnType
                    inputs.add(dis.readInt()); // invokeType
                    setInput(vm, node, inputs.toArray(new Object[0]));
                    if (debug) System.err.println("\033[91mobjects: " + Arrays.toString(node.original));
                    nodes.add(node);
                } break;
                case "VMTypeInsnNode": {
                    VMTypeInsnNode node = new VMTypeInsnNode(dis.readInt());
                    if (debug) System.err.println("\033[91mopcode: " + node.opcode);
                    String desc = dis.readUTF();
                    setInput(vm, node, desc);
                    if (debug) System.err.println("\033[91mobjects: " + Arrays.toString(node.original));
                    nodes.add(node);
                } break;
                case "VMLdcInsnNode": {
                    VMLdcInsnNode node = new VMLdcInsnNode(dis.readInt());
                    if (debug) System.err.println("\033[91mopcode: " + node.opcode);
                    int type = dis.readInt();
                    if (debug) System.err.println("\033[91mtype: " + type);
                    List<Object> inputs = new ArrayList<>();
                    inputs.add(type);
                    switch (type)
                    {
                        case 0:
                        case 5: inputs.add(dis.readUTF()); break;
                        case 1: inputs.add(dis.readInt()); break;
                        case 2: inputs.add(dis.readLong()); break;
                        case 3: inputs.add(dis.readFloat()); break;
                        case 4: inputs.add(dis.readDouble()); break;
                        default:  throw new IllegalStateException("Unexpected value: " + type);
                    }
                    setInput(vm, node, inputs.toArray(new Object[0]));
                    if (debug) System.err.println("\033[91mobjects: " + Arrays.toString(node.original));
                    nodes.add(node);
                } break;
                case "VMVarInsnNode": {
                    VMVarInsnNode node = new VMVarInsnNode(dis.readInt());
                    if (debug) System.err.println("\033[91mopcode: " + node.opcode);
                    int var = dis.readInt();
                    if (debug) System.err.println("\033[91mvar: " + var);
                    setInput(vm, node, var);
                    nodes.add(node);
                } break;
                case "VMIincInsnNode": {
                    VMIincInsnNode node = new VMIincInsnNode(dis.readInt());
                    if (debug) System.err.println("\033[91mopcode: " + node.opcode);
                    int var = dis.readInt();
                    int incr = dis.readInt();
                    if (debug) System.err.println("\033[91mvar: " + var);
                    if (debug) System.err.println("\033[91mincr: " + incr);
                    setInput(vm, node, var, incr);
                    nodes.add(node);
                } break;
                case "VMInvokeDynamicInsnNode": {
                    boolean unsupported = false;
                    if (unsupported)
                        continue;
                    VMInvokeDynamicInsnNode node = new VMInvokeDynamicInsnNode(dis.readInt());
                    if (debug) System.err.println("\033[91mopcode: " + node.opcode);
                    List<Object> inputs = new ArrayList<>();
                    if (!readInvokeDynamics(dis, inputs))
                        throw new IllegalStateException("Unsupported invokedynamics instruction reached.");
                    setInput(vm, node, inputs.toArray(new Object[0]));
                    nodes.add(node);
                } break;
                case "VMLookupSwitchInsnNode": {
                    VMLookupSwitchInsnNode node = new VMLookupSwitchInsnNode(dis.readInt());
                    int defaultIndex = dis.readInt();
                    int count = dis.readInt();
                    int[] keys = new int[count];
                    int[] labels = new int[count];
                    for (int i = 0; i < count; i++)
                    {
                        keys[i] = dis.readInt();
                        labels[i] = dis.readInt();
                    }
                    setInput(vm, node, defaultIndex, keys, labels);
                    nodes.add(node);
                } break;
                case "VMTableSwitchInsnNode": {
                    VMTableSwitchInsnNode node = new VMTableSwitchInsnNode(dis.readInt());
                    int min = dis.readInt();
                    int max = dis.readInt();
                    int defaultIndex = dis.readInt();
                    int count = dis.readInt();
                    int[] labels = new int[count];
                    for (int i = 0; i < count; i++)
                        labels[i] = dis.readInt();
                    setInput(vm, node, min, max, defaultIndex, labels);
                    nodes.add(node);
                } break;
                case "VMFrameNode":
                case "VMIgnore":  // Ignore
                    nodes.add(new VMInsnNode(VMOpcodes.NOP));
                    break;

                case "Meow": done = true;
                    break;
                default: throw new IllegalStateException("Unexpected value: " + className);
            }
        }
        dis.close();
        return nodes.toArray(new VMNode[0]);
    }

    private static boolean readInvokeDynamics(DataInputStream dis, List<Object> inputs) throws Throwable
    {
        String which = dis.readUTF(); // which
        switch (which)
        {
            case "StringConcatFactory": {
                inputs.add(which);
                inputs.add(dis.readUTF()); // string
                int argsLength = dis.readInt();
                String[] args = new String[argsLength];
                for (int i = 0; i < argsLength; i++)
                    args[i] = dis.readUTF(); // arg
                inputs.add(args);
                return true;
            }
            case "Runnable":

                // Consumers
            case "Consumer": 
            case "IntConsumer": 
            case "LongConsumer": 
            case "DoubleConsumer": 

                // Others
            case "Function": 
            case "Predicate": 
            case "Supplier": {
                inputs.add(which);
                inputs.add(dis.readInt()); // tag
                inputs.add(dis.readUTF()); // owner
                inputs.add(dis.readUTF()); // name
                int argsLength = dis.readInt();
                String[] args = new String[argsLength];
                for (int i = 0; i < argsLength; i++)
                    args[i] = dis.readUTF(); // arg
                inputs.add(args);
                int argumentLength = dis.readInt();
                String[] arguments = new String[argumentLength];
                for (int i = 0; i < argumentLength; i++)
                    arguments[i] = dis.readUTF(); // argument
                inputs.add(arguments);
                return true;
            }
            default: return false;
        }
    }

    private static void setInput(ObzcureVM vm, VMNode vmNode, Object... obj)
    {
        vmNode.original = vm.deepClone(vmNode.input, obj);
        vmNode.input = obj;
    }

}
