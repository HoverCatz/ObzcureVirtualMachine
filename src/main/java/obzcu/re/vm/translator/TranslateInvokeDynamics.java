package obzcu.re.vm.translator;

import obzcu.re.vm.utils.asm.AccessHelper;
import obzcu.re.vm.utils.asm.ClassWrapper;
import obzcu.re.vm.utils.asm.MethodWrapper;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;

import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HoverCatz
 * @created 16.01.2022
 * @url https://github.com/HoverCatz
 **/
public record TranslateInvokeDynamics(ClassWrapper node,
                                      AccessHelper classAccess,
                                      MethodWrapper method,
                                      AccessHelper methodAccess, DataOutputStream writer, boolean debug,
                                      boolean debugPrettyPrint, boolean doStackAnalyze)
{

    public boolean translate(InvokeDynamicInsnNode idin) throws Throwable
    {
        // Temporarily filter
        boolean unsupported = false;
        if (unsupported)
            return false;

        Type returnType = Type.getReturnType(idin.desc);
        return switch (returnType.getInternalName())
        {
            case "java/lang/Runnable" -> inject(idin, "Runnable");

            // Consumers
            case "java/util/function/Consumer" -> inject(idin, "Consumer");
            case "java/util/function/IntConsumer" -> inject(idin, "IntConsumer");
            case "java/util/function/LongConsumer" -> inject(idin, "LongConsumer");
            case "java/util/function/DoubleConsumer" -> inject(idin, "DoubleConsumer");

            // Others
            case "java/util/function/Function" -> inject(idin, "Function");
            case "java/util/function/Predicate" -> inject(idin, "Predicate");
            case "java/util/function/Supplier" -> inject(idin, "Supplier");
            // TODO: Implement support for every single class inside the package `java.util.function`

            case "java/lang/String" -> injectStringConcat(idin);
            default -> {
                System.out.println("Unsupported returnType: " + returnType.getInternalName());
                yield false;
            }
        };
    }

    private boolean injectStringConcat(InvokeDynamicInsnNode idin) throws Throwable
    {
        int opcode = idin.getOpcode();
        String name = idin.name;
        Type returnType = Type.getReturnType(idin.desc);
        Type[] arguments = Type.getArgumentTypes(idin.desc);
        Handle bsm = idin.bsm;
        Object[] bsmArgs = idin.bsmArgs;

        if (debug)
        {
            System.out.println(name);
            System.out.println(returnType.getInternalName());
            System.out.println(Arrays.toString(arguments));
            System.out.println(bsm);
            System.out.println(bsmArgs.length);
            System.out.println(Arrays.toString(bsmArgs));
        }

        // Filter name
        if (!name.equals("makeConcatWithConstants"))
            return false;

        // Filter returnType
        if (!returnType.getInternalName().equals("java/lang/String"))
            return false;

        // Filter bsm
        if (bsm == null)
            return false;
        else
        {
            String bsmOwner = bsm.getOwner();
            // Filter bsm owner
            if (!bsmOwner.equals("java/lang/invoke/StringConcatFactory"))
                return false;

            String bsmName = bsm.getName();
            // Filter bsm name
            if (!bsmName.equals("makeConcatWithConstants"))
                return false;

            String bsmDesc = bsm.getDesc();
            // Filter bsm desc
            if (!bsmDesc.equals("(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;"))
                return false;
        }

        // Filter bsmArgs length
        if (bsmArgs.length != 1)
            return false;

        String string = (String) bsmArgs[0];

        if (debug) System.out.println("VMInvokeDynamicInsnNode: " + opcode);
        writer.writeUTF("VMInvokeDynamicInsnNode");
        writer.writeInt(opcode);

        if (debug) System.out.println("which: StringConcatFactory");
        writer.writeUTF("StringConcatFactory"); // which

        if (debug) System.out.println("string: " + string);
        writer.writeUTF(string);

        String[] args = getArgumentStrings(arguments);
        if (debug) System.out.println("args.length: " + args.length);
        writer.writeInt(args.length);

        for (String arg : args)
        {
            if (debug) System.out.println("\targ: " + arg);
            writer.writeUTF(arg);
        }

        return true;
    }

    private static final Map<String, String> nameMap = new HashMap<>()
    {{
        put("Runnable", "run");

        // Consumers
        put("Consumer", "accept");
        put("IntConsumer", "accept");
        put("LongConsumer", "accept");
        put("DoubleConsumer", "accept");

        // Others
        put("Function", "apply");
        put("Predicate", "test");
        put("Supplier", "get");
    }};

    private boolean inject(InvokeDynamicInsnNode idin, String which) throws Throwable
    {
        int opcode = idin.getOpcode();
        String name = idin.name;
        Type[] arguments = Type.getArgumentTypes(idin.desc);
        Handle bsm = idin.bsm;
        Object[] bsmArgs = idin.bsmArgs;

        if (debug)
        {
            System.out.println(which);
            System.out.println(name);
            System.out.println(Arrays.toString(arguments));
            System.out.println(bsm);
            System.out.println(bsmArgs.length);
            System.out.println(Arrays.toString(bsmArgs));
        }

        // Filter name
        if (!name.equals(nameMap.get(which)))
        {
            if (debug) System.out.println("Failed name filter check");
            return false;
        }

        // Filter bsm
        if (bsm == null)
        {
            if (debug) System.out.println("Failed bsm filter check");
            return false;
        }
        else
        {
            String bsmOwner = bsm.getOwner();
            // Filter bsm owner
            if (!bsmOwner.equals("java/lang/invoke/LambdaMetafactory"))
            {
                if (debug) System.out.println("Failed bsm owner check");
                return false;
            }

            String bsmName = bsm.getName();
            // Filter bsm name
            if (!bsmName.equals("metafactory"))
            {
                if (debug) System.out.println("Failed bsm name check");
                return false;
            }

            String bsmDesc = bsm.getDesc();
            // Filter bsm desc
            if (!bsmDesc.equals("(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;"))
            {
                if (debug) System.out.println("Failed bsm desc check");
                return false;
            }
        }

        // Filter bsmArgs count
        if (bsmArgs.length != 3)
        {
            if (debug) System.out.println("Failed bsmArgs length check");
            return false;
        }

        Object bsmArg1 = bsmArgs[1];

        // Filter arg1 type
        if (!(bsmArg1 instanceof Handle handle))
        {
            if (debug) System.out.println("Failed bsmArg1 handle check");
            return false;
        }

        // Filter handle tag
        // (5 = H_INVOKEVIRTUAL)
        // (6 = H_INVOKESTATIC)
        int tag = handle.getTag();
        if (tag < Opcodes.H_INVOKEVIRTUAL || tag > Opcodes.H_INVOKESTATIC)
        {
            if (debug) System.out.println("Failed bsm tag check");
            return false;
        }

        boolean doPrint = false;
        if (doPrint)
        {
            System.out.println();
            System.out.println("! inject");
            System.out.println("which: " + which);
            System.out.println("name: " + name);
            System.out.println("arguments: " + Arrays.toString(arguments));
            System.out.println("bsm: " + bsm);
            System.out.println("bsmArgsCount: " + bsmArgs.length);
            System.out.println("bsmArgs: " + Arrays.toString(bsmArgs));
            System.out.println("handle: " + handle);
            System.out.println("handle.getTag: " + tag);
            System.out.println();
        }

        if (debug) System.out.println("VMInvokeDynamicInsnNode: " + opcode);
        writer.writeUTF("VMInvokeDynamicInsnNode");
        writer.writeInt(opcode);

        if (debug) System.out.println("which: " + which);
        writer.writeUTF(which); // which

        if (debug) System.out.println("tag: " + tag);
        writer.writeInt(tag); // tag

        if (debug) System.out.println("owner: " + handle.getOwner().replace("/", "."));
        writer.writeUTF(handle.getOwner().replace("/", "."));

        if (debug) System.out.println("name: " + handle.getName());
        writer.writeUTF(handle.getName());

        String[] args = getArgumentStrings(Type.getArgumentTypes(handle.getDesc()));
        if (debug) System.out.println("args.length: " + args.length);
        writer.writeInt(args.length);

        for (String arg : args)
        {
            if (debug) System.out.println("\targ: " + arg);
            writer.writeUTF(arg);
        }

        String[] argumentStrings = getArgumentStrings(arguments);
        if (debug) System.out.println("arguments.length: " + argumentStrings.length);
        writer.writeInt(argumentStrings.length);

        for (String arg : argumentStrings)
        {
            if (debug) System.out.println("\targ: " + arg);
            writer.writeUTF(arg);
        }

        return true;
    }

    private String[] getArgumentStrings(Type[] argumentTypes)
    {
        String[] strings = new String[argumentTypes.length];
        for (int i = 0; i < strings.length; i++)
            strings[i] = argumentTypes[i].getInternalName().replace("/", ".");
        return strings;
    }

}
