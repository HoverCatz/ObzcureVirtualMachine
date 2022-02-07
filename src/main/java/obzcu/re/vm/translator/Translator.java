package obzcu.re.vm.translator;

import obzcu.re.virtualmachine.ObzcureVM;
import obzcu.re.virtualmachine.VMLoader;
import obzcu.re.virtualmachine.asm.VMTryCatch;
import obzcu.re.vm.Obzcure;
import obzcu.re.vm.ObzcureException;
import obzcu.re.vm.utils.ASMUtils;
import obzcu.re.vm.utils.asm.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class Translator implements Opcodes
{

    private final Obzcure obzcure;
    private final ClassWrapper node;
    private final MethodWrapper method;
    private final AccessHelper classAccess, methodAccess;
    private final String className;
    private final String methodName;
    private final String methodDesc;
    private final boolean simulateReal;
    private final boolean forcePublic;
    private final boolean removeFinal;
    private final boolean isConstructor;
    private final boolean isStatic;

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final DataOutputStream writer = new DataOutputStream(baos);

    private byte[] result = null;
    private TranslateInvokeDynamics translateInvokeDynamics = null;

    public boolean debug = false;
    private final boolean debugPrettyPrint = false;
    private final boolean doStackAnalyze = false;

    public Translator(ClassWrapper node, MethodWrapper method, Obzcure obzcure, boolean forcePublic, boolean removeFinal)
    {
        this(node, method, obzcure, forcePublic, removeFinal, false);
    }

    public Translator(ClassWrapper node, MethodWrapper method, Obzcure obzcure, boolean forcePublic, boolean removeFinal, boolean simulateReal)
    {
        this.node = node;
        this.method = method;
        this.classAccess = node.getAccess();
        this.methodAccess = method.getAccess();
        this.className = node.getName();
        this.methodName = method.getName();
        this.methodDesc = method.getDesc();
        this.simulateReal = simulateReal;
        this.isConstructor = methodName.equals("<init>");
        this.isStatic = methodAccess.isStatic();
        this.obzcure = obzcure;
        this.forcePublic = forcePublic;
        this.removeFinal = removeFinal;
    }

    public static final String lookupName = "lookup$obzcure";

    public static void injectLookupField(ClassWrapper node, MethodWrapper clinit)
    {
        InsnList toAdd = new InsnList();
        toAdd.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(MethodHandles.class), "lookup", "()" + Type.getDescriptor(MethodHandles.Lookup.class)));
        toAdd.add(new FieldInsnNode(PUTSTATIC, node.getOriginalName(), lookupName, Type.getDescriptor(MethodHandles.Lookup.class)));

        InsnList insnList = clinit.getInstructions();
        insnList.insertBefore(insnList.getFirst(), toAdd);

        node.addField(ACC_PUBLIC | ACC_STATIC | ACC_FINAL | ACC_SYNTHETIC, lookupName, Type.getDescriptor(MethodHandles.Lookup.class));
    }

    public byte[] getResult()
    {
        return result;
    }

    private String insnToString(Printer printer, TraceMethodVisitor mp, AbstractInsnNode insn)
    {
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }

    private void translateInstructions() throws Throwable
    {
        Printer printer = new Textifier();
        TraceMethodVisitor mp = new TraceMethodVisitor(printer);

        if (doStackAnalyze)
        {
            Analyzer<ConstantTracker.ConstantValue> analyzer = new Analyzer<>(new ConstantTracker());
            Frame<ConstantTracker.ConstantValue>[] analyze = analyzer.analyze(node.getName(), method.getMethodNode());
            int _n = 0;
            AbstractInsnNode[] abcs = method.getInstructions().toArray();
            for (Frame<ConstantTracker.ConstantValue> frame : analyze)
            {
                if (frame == null) break;
                int stackCount = frame.getStackSize();
                StringJoiner sj = new StringJoiner(", ");
                for (int k = 0; k < stackCount; k++)
                {
                    ConstantTracker.ConstantValue stack = frame.getStack(k);
                    sj.add((stack.getValue() == null ? stack : ("(" + stack.getValue() + ")")).toString());
                }
                System.out.println("# " + _n + " [" + sj + "]" + " -> " +
                        insnToString(printer, mp, abcs[_n++]).trim());
            }
        }

        InsnList insnList = method.getInstructions();

        if (!removeFinal || classAccess.isInterface())
            for (AbstractInsnNode insn : insnList.toArray())
                if (insn instanceof FieldInsnNode field)
                    if (field.getOpcode() == Opcodes.PUTFIELD || field.getOpcode() == Opcodes.PUTSTATIC)
                        assertFieldNotFinal(field);

        // Pretty-print function
        if (debugPrettyPrint)
            for (AbstractInsnNode insn : insnList.toArray())
                System.out.print(insnToString(printer, mp, insn));

        // Begin translation! :)
        writer.writeUTF("Meow");

        writer.writeUTF(className);
        writer.writeUTF(methodName);
        writer.writeUTF(methodDesc);

        for (AbstractInsnNode insn : insnList.toArray())
        {
            int index = insnList.indexOf(insn);
            final int opcode = insn.getOpcode();
            if (debug) System.out.println();
            if (debug) System.out.println("curr: " + index + ", opcode: " + getOpcodeName(opcode));
            switch (insn)
            {
                case LabelNode labelNode -> {
                    if (debug) System.out.println("VMLabelInsnNode: " + opcode);
                    writer.writeUTF("VMLabelInsnNode");
                    writer.writeInt(opcode);

                    writer.writeInt(index);
                    if (debug) System.out.println("index: " + index);
                }
                case LineNumberNode lineNumberNode -> {
                    if (debug) System.out.println("VMLineNumberInsnNode: " + opcode);
                    writer.writeUTF("VMLineNumberInsnNode");
                    writer.writeInt(opcode);

                    writer.writeInt(lineNumberNode.line);
                    if (debug) System.out.println("lineNumberNode.line: " + lineNumberNode.line);
                }
                case JumpInsnNode jumpInsnNode -> {
                    if (debug) System.out.println("VMJumpInsnNode: " + opcode);
                    writer.writeUTF("VMJumpInsnNode");
                    writer.writeInt(opcode);

                    writer.writeInt(insnList.indexOf(jumpInsnNode.label));
                    if (debug) 
                        System.out.println("insnList.indexOf(jumpInsnNode.label): " + insnList.indexOf(jumpInsnNode.label));
                }
                case IntInsnNode intInsnNode -> {
                    if (debug) System.out.println(": " + opcode);
                    writer.writeUTF("VMIntInsnNode");
                    writer.writeInt(opcode);

                    writer.writeInt(intInsnNode.operand);
                    if (debug) System.out.println("intInsnNode.operand: " + intInsnNode.operand);
                }
                case InsnNode insnNode -> {
                    if (debug) System.out.println("VMInsnNode: " + opcode);
                    writer.writeUTF("VMInsnNode");
                    writer.writeInt(opcode);

                    // Nothing required, we use the opcode
                }
                case FieldInsnNode fieldInsnNode -> {
                    if (debug) System.out.println("VMFieldInsnNode: " + opcode);
                    writer.writeUTF("VMFieldInsnNode");
                    writer.writeInt(opcode);

                    if (debug) System.out.println("fieldInsnNode.owner: " + fieldInsnNode.owner.replace("/", "."));
                    writer.writeUTF(fieldInsnNode.owner.replace("/", "."));
                    if (debug) System.out.println("fieldInsnNode.name: " + fieldInsnNode.name);
                    writer.writeUTF(fieldInsnNode.name);
                    if (opcode == Opcodes.GETSTATIC) {
                        if (debug) System.out.println("fieldInsnNode.opcode: GETSTATIC");
                        writer.writeBoolean(true); // isStatic
                        writer.writeBoolean(false); // isPut
                    } else if (opcode == Opcodes.PUTSTATIC) {
                        writer.writeBoolean(true); // isStatic
                        writer.writeBoolean(true); // isPut
                    } else if (opcode == Opcodes.GETFIELD) {
                        if (debug) System.out.println("fieldInsnNode.opcode: GETFIELD");
                        writer.writeBoolean(false); // isStatic
                        writer.writeBoolean(false); // isPut
                    } else if (opcode == Opcodes.PUTFIELD) {
                        writer.writeBoolean(false); // isStatic
                        writer.writeBoolean(true); // isPut
                    }
                }
                case MethodInsnNode methodInsnNode -> {
                    if (debug) System.out.println("VMMethodInsnNode: " + opcode);
                    writer.writeUTF("VMMethodInsnNode");
                    writer.writeInt(opcode);

                    if (debug) System.out.println("methodInsnNode.owner: " + methodInsnNode.owner.replace("/", "."));
                    writer.writeUTF(methodInsnNode.owner.replace("/", "."));
                    if (debug) System.out.println("methodInsnNode.name: " +methodInsnNode.name);
                    writer.writeUTF(methodInsnNode.name);
                    Type[] argumentTypes = Type.getArgumentTypes(methodInsnNode.desc);
                    writer.writeInt(argumentTypes.length);
                    if (debug) System.out.println("argumentTypes.length: " + argumentTypes.length);
                    for (Type type : argumentTypes)
                    {
                        if (debug) System.out.println("arg.getInternalName(): " + type.getInternalName().replace("/", "."));
                        writer.writeUTF(type.getInternalName().replace("/", "."));
                    }
                    if (debug) System.out.println("methodInsnNode.returnType: " + Type.getReturnType(methodInsnNode.desc).getInternalName().replace("/", "."));
                    writer.writeUTF(Type.getReturnType(methodInsnNode.desc).getInternalName().replace("/", "."));

                    // invokeType
                    if (opcode == Opcodes.INVOKESTATIC)
                        writer.writeInt(0);
                    else if (opcode == Opcodes.INVOKEVIRTUAL)
                        writer.writeInt(1);
                    else if (opcode == Opcodes.INVOKESPECIAL)
                        writer.writeInt(2);
                    else if (opcode == Opcodes.INVOKEINTERFACE)
                        writer.writeInt(3);
                    else
                        throw new IllegalStateException("Unexpected opcode: " + opcode + " (" + getOpcodeName(opcode) + ")");
                }
                case TypeInsnNode typeInsnNode -> {
                    if (debug) System.out.println("VMTypeInsnNode: " + opcode);
                    writer.writeUTF("VMTypeInsnNode");
                    writer.writeInt(opcode);

                    if (debug) 
                        System.out.println("typeInsnNode.desc: " + Type.getObjectType(typeInsnNode.desc).getInternalName().replace("/", "."));
                    writer.writeUTF(Type.getObjectType(typeInsnNode.desc).getInternalName().replace("/", "."));
                }
                case LdcInsnNode ldcInsnNode -> {
                    if (debug) System.out.println("VMLdcInsnNode: " + opcode);
                    writer.writeUTF("VMLdcInsnNode");
                    writer.writeInt(opcode);

                    Object cst = ldcInsnNode.cst;
                    switch (cst) {
                        case String s -> {
                            writer.writeInt(0);
                            writer.writeUTF(s);
                        }
                        case Integer k -> {
                            writer.writeInt(1);
                            writer.writeInt(k);
                        }
                        case Long l -> {
                            writer.writeInt(2);
                            writer.writeLong(l);
                        }
                        case Float f -> {
                            writer.writeInt(3);
                            writer.writeFloat(f);
                        }
                        case Double d -> {
                            writer.writeInt(4);
                            writer.writeDouble(d);
                        }
                        case Type t -> {
                            writer.writeInt(5);
                            writer.writeUTF(t.getInternalName().replace("/", "."));
                        }
                        case null, default -> throw new IllegalStateException("Unexpected cst type: " + cst + " (" + cst.getClass().getSimpleName() + ")");
                    }
                    if (debug) System.out.println("cst: " + cst + " (" + cst.getClass().getSimpleName() + ")");
                }
                case VarInsnNode varInsnNode -> {
                    if (debug) System.out.println("VMVarInsnNode: " + opcode);
                    writer.writeUTF("VMVarInsnNode");
                    writer.writeInt(opcode);

                    if (debug) System.out.println("varInsnNode.var: " + varInsnNode.var);
                    writer.writeInt(varInsnNode.var);
                }
                case IincInsnNode iincInsnNode -> {
                    if (debug) System.out.println("VMIincInsnNode: " + opcode);
                    writer.writeUTF("VMIincInsnNode");
                    writer.writeInt(opcode);

                    if (debug) System.out.println("iincInsnNode.var: " + iincInsnNode.var);
                    writer.writeInt(iincInsnNode.var);
                    if (debug) System.out.println("iincInsnNode.incr: " + iincInsnNode.incr);
                    writer.writeInt(iincInsnNode.incr);
                }
                case InvokeDynamicInsnNode idin-> {
                    /* Try injecting invokedynamics,
                     * return false if we find something unsupported
                     */
                    boolean invokeDynamicsDebug = false;
                    if (translateInvokeDynamics == null)
                        translateInvokeDynamics = new TranslateInvokeDynamics(
                            node, classAccess,
                            method, methodAccess,
                            writer,
                            debug || invokeDynamicsDebug,
                            debugPrettyPrint,
                            doStackAnalyze
                        );
                    if (!translateInvokeDynamics.translate(idin))
                        throw new IllegalStateException("Invokedynamic instruction has unsupported arguments in: " + className + " " + methodName + methodDesc);
                }
                case LookupSwitchInsnNode lookup -> {
                    if (debug) System.out.println("VMLookupSwitchInsnNode: " + opcode);
                    writer.writeUTF("VMLookupSwitchInsnNode");
                    writer.writeInt(opcode);

                    int defaultIndex = insnList.indexOf(lookup.dflt);
                    writer.writeInt(defaultIndex);

                    List<Integer> keys = lookup.keys;
                    List<LabelNode> labels = lookup.labels;

                    int countKeys = keys.size();
                    int countLabels = labels.size();
                    if (countKeys != countLabels)
                        throw new IllegalStateException("countKeys = " + countKeys + ", " + "countLabels = " + countLabels + ". " +
                                "countKeys != countLabels");

                    writer.writeInt(countKeys);
                    for (int i = 0; i < countKeys; i++)
                    {
                        writer.writeInt(keys.get(i));
                        writer.writeInt(insnList.indexOf(labels.get(i)));
                    }
                }
                case TableSwitchInsnNode table -> {
                    if (debug) System.out.println("VMTableSwitchInsnNode: " + opcode);
                    writer.writeUTF("VMTableSwitchInsnNode");
                    writer.writeInt(opcode);

                    writer.writeInt(table.min); // min
                    writer.writeInt(table.max); // max
                    writer.writeInt(insnList.indexOf(table.dflt)); // defaultIndex

                    List<LabelNode> labels = table.labels;
                    writer.writeInt(labels.size());
                    for (LabelNode label : labels)
                        writer.writeInt(insnList.indexOf(label));
                }
                case FrameNode frameNode ->
                    // Ignore
                    writer.writeUTF("VMIgnore");

                default -> throw new ObzcureException("Instruction not implemented yet: " + insn.getClass().getSimpleName());
            }
        }
        writer.writeUTF("Meow");
    }

    private void assertFieldNotFinal(FieldInsnNode fieldInsnNode)
    {
        if (obzcure == null) return;
        String owner = fieldInsnNode.owner;
        ClassWrapper ownerNode = obzcure.getClassWrapper(owner);
        if (ownerNode == null) return;
        if (!ownerNode.hasFields()) return;
        String name = fieldInsnNode.name;
        String desc = fieldInsnNode.desc;
        Optional<FieldWrapper> first = ownerNode.getFields().stream().filter(f -> f.getName().equals(name) && f.getDesc().equals(desc)).findFirst();
        if (first.isEmpty()) return;
        FieldWrapper field = first.get();
        if (field.getAccess().isFinal())
            throw new IllegalStateException("Field " + owner + ' ' + name + '.' + desc + " is final, we can't PUT it.");
    }

    public boolean translateInstructions(int resourceIndex)
    {

        boolean isVirtualizationEnabled = true;
        if (!isVirtualizationEnabled)
            return false;

        // Ignore native and abstract methods
        if (methodAccess.isNative() || methodAccess.isAbstract())
            return false;

        // Ignore empty methods (no code attribute)
        if (!method.hasInstructions())
            return false;

        // TODO: Find a solution for constructors
        if (methodName.equals("<init>"))
            return false;

        // If tests, don't check for class/method names
        if (obzcure != null)
        {
            boolean useFilter = false;
            if (useFilter)
            {
                if (!className.startsWith("obzcu/re/"))
                    return false;
                if (!methodName.equals("<init>"))
                    return false;
            }
        }

        Type returnType = Type.getReturnType(method.getDesc());
        try
        {
            switch (returnType.getSort())
            {
                case Type.INT, Type.LONG, Type.FLOAT, Type.DOUBLE, Type.BYTE, Type.SHORT, Type.CHAR, Type.BOOLEAN, Type.OBJECT, Type.ARRAY, Type.VOID -> {}
                default -> throw new IllegalStateException("Unexpected value: " + returnType.getInternalName());
            }
        }
        catch (Throwable t)
        {
            System.err.println(t.getMessage());
            return false;
        }

        if (debug)
        {
            System.out.println();
            System.out.println("Translating " + node.getName() + " " + methodName + methodDesc);
        }

        try
        {
            // Translate instructions into binary
            translateInstructions();
        }
        catch (Throwable t)
        {
//            t.printStackTrace();
            System.out.println("Error: " + t.getMessage());
            return false;
        }

        // No errors! Good. Now, replace the instruction set with our own, calling new VirtualMachine(index, locals, stack)::execute();
        try
        {
            // Replace method with our VM-calls

            boolean hasTryCatches = method.hasTryCatchBlocks();

            int extraLocals = 0;
            Type[] argumentTypes = Type.getArgumentTypes(method.getDesc());
            for (Type arg : argumentTypes)
                switch (arg.getSort())
                {
                    case Type.DOUBLE, Type.LONG -> extraLocals++;
                }

            Type mainType = Type.getType(ObzcureVM.class);
            Type loaderType = Type.getType(VMLoader.class);
            Type tryCatchType = Type.getType(VMTryCatch.class);
            Type methodHandlesType = Type.getType(MethodHandles.Lookup.class);

            String mainClassName = mainType.getInternalName();
            String loaderClassName = loaderType.getInternalName();
            String tryCatchClassName = tryCatchType.getInternalName();
            String tryCatchTypeArrayDescriptor = "[L" + tryCatchClassName + ";";
            String methodHandlesClassName = methodHandlesType.getInternalName();
            String methodHandlesDescriptor = methodHandlesType.getDescriptor();

//            System.out.println("mainClassName: " + mainClassName);
//            System.out.println("loaderClassName: " + loaderClassName);
//            System.out.println("tryCatchClassName: " + tryCatchClassName);
//            System.out.println("tryCatchTypeArrayDescriptor: " + tryCatchTypeArrayDescriptor);

            InsnList insnList = new InsnList();
//            insnList.add(new TypeInsnNode(NEW, Type.getType(ObzcureVM.class).getInternalName()));
//            insnList.add(new InsnNode(DUP));
            insnList.add(ASMUtils.getNumberInsn(resourceIndex));
            insnList.add(ASMUtils.getNumberInsn(method.getMaxLocals() + extraLocals));
            insnList.add(ASMUtils.getNumberInsn(method.getMaxStack()));
            if (hasTryCatches)
            {
                Collection<TryCatchBlockNode> tryCatchBlocks = method.getTryCatchBlocks();
                int count = tryCatchBlocks.size();

                insnList.add(ASMUtils.getNumberInsn(count));
                insnList.add(new TypeInsnNode(ANEWARRAY, tryCatchClassName));

                int tryIndex = 0;
                for (TryCatchBlockNode tryCatch : tryCatchBlocks)
                {
                    String type = tryCatch.type;
                    if (type == null || type.equals("null"))
                        type = "java/lang/Throwable";

                    LabelNode start = tryCatch.start;
                    LabelNode end = tryCatch.end;
                    LabelNode handler = tryCatch.handler;

                    int startIndex = insnList.indexOf(start);
                    int endIndex = insnList.indexOf(end);
                    int handlerIndex = insnList.indexOf(handler);

                    insnList.add(new InsnNode(DUP));
                    insnList.add(ASMUtils.getNumberInsn(tryIndex++));
                    insnList.add(new TypeInsnNode(NEW, tryCatchClassName));
                    insnList.add(new InsnNode(DUP));

                    insnList.add(ASMUtils.getNumberInsn(startIndex));
                    insnList.add(ASMUtils.getNumberInsn(endIndex));
                    insnList.add(ASMUtils.getNumberInsn(handlerIndex));
                    insnList.add(new LdcInsnNode(Type.getType("L" + type + ";")));

                    insnList.add(new MethodInsnNode(INVOKESPECIAL, tryCatchClassName,
                            "<init>", "(IIILjava/lang/Class;)V"));
                    insnList.add(new InsnNode(AASTORE));
                }
            }

            // GETSTATIC obzcu/re/testjar/Test.lookup : Ljava/lang/invoke/MethodHandles$Lookup;
            insnList.add(new FieldInsnNode(GETSTATIC, className, lookupName, methodHandlesDescriptor));

            boolean injectBytes = obzcure == null && !simulateReal;
            if (injectBytes)
            {
                // GETSTATIC className.vmNodes : [B
                insnList.add(new FieldInsnNode(GETSTATIC, className, "vmNodes", "[B"));
            }

            String virtualizeMethodName = !injectBytes ? "virtualize" : "virtualizeTests";
            String executeMethodName = "execute";

            insnList.add(new MethodInsnNode(INVOKESTATIC, mainClassName, virtualizeMethodName,
                    "(III" + (hasTryCatches ? tryCatchTypeArrayDescriptor : "") +
                    methodHandlesDescriptor + (!injectBytes ? "" : "[B") +
            ")L" + mainClassName + ";"));

            int argsCount = argumentTypes.length;
            int storeIndex = isStatic ? 0 : 1;
            for (Type type : argumentTypes)
                storeIndex += switch (type.getSort()) {
                    case Type.LONG, Type.DOUBLE -> 2;
                    default -> 1;
                };

            if (hasTryCatches || argsCount > 0 || !isStatic)
                insnList.add(new VarInsnNode(ASMUtils.getVarOpcode(Type.getType(Object.class), true), storeIndex)); // vm =

            int currIndex = 0;

            // Add ALOAD_0
            if (!isStatic)
            {
                insnList.add(new VarInsnNode(ASMUtils.getVarOpcode(mainType, false), storeIndex)); // vm.
                insnList.add(ASMUtils.getNumberInsn(currIndex++));
                insnList.add(new VarInsnNode(ASMUtils.getVarOpcode(Type.getType(Object.class), false), 0));
                insnList.add(new MethodInsnNode(INVOKEVIRTUAL, mainClassName, "setLocal", "(ILjava/lang/Object;)V", false));
            }

            // Add arguments
            if (argsCount > 0)
            {
                for (Type type : argumentTypes)
                {
                    insnList.add(new VarInsnNode(ASMUtils.getVarOpcode(mainType, false), storeIndex)); // vm.
                    insnList.add(ASMUtils.getNumberInsn(currIndex));
                    insnList.add(new VarInsnNode(ASMUtils.getVarOpcode(type, false), currIndex));
                    currIndex += switch (type.getSort())
                    {
                        case Type.LONG, Type.DOUBLE -> 2;
                        default -> 1;
                    };
                    switch (type.getSort())
                    {
                        case Type.INT -> insnList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;"));
                        case Type.LONG -> insnList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;"));
                        case Type.FLOAT -> insnList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;"));
                        case Type.DOUBLE -> insnList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;"));
                        case Type.BYTE -> insnList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;"));
                        case Type.SHORT -> insnList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;"));
                        case Type.CHAR -> insnList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;"));
                        case Type.BOOLEAN -> insnList.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;"));
                    }
                    insnList.add(new MethodInsnNode(INVOKEVIRTUAL, mainClassName, "setLocal", "(ILjava/lang/Object;)V", false));
                }
            }

            LabelNode var1 = new LabelNode(), var2 = new LabelNode();
            insnList.add(var1);
            if (hasTryCatches || argsCount > 0 || !isStatic)
                insnList.add(new VarInsnNode(ASMUtils.getVarOpcode(mainType, false), storeIndex));
            insnList.add(var2);
            insnList.add(new MethodInsnNode(INVOKEVIRTUAL, mainClassName, executeMethodName, "()Ljava/lang/Object;", false));

            if (returnType == Type.VOID_TYPE)
            {
                insnList.add(new InsnNode(POP));
                insnList.add(new InsnNode(RETURN));
            }
            else
                switch (returnType.getSort())
                {
                    case Type.INT, Type.BOOLEAN, Type.BYTE, Type.CHAR, Type.SHORT -> {
                        insnList.add(new TypeInsnNode(CHECKCAST, "java/lang/Integer"));
                        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I"));
                        insnList.add(new InsnNode(IRETURN));
                    }
                    case Type.LONG -> {
                        insnList.add(new TypeInsnNode(CHECKCAST, "java/lang/Long"));
                        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J"));
                        insnList.add(new InsnNode(LRETURN));
                    }
                    case Type.FLOAT -> {
                        insnList.add(new TypeInsnNode(CHECKCAST, "java/lang/Float"));
                        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F"));
                        insnList.add(new InsnNode(FRETURN));
                    }
                    case Type.DOUBLE -> {
                        insnList.add(new TypeInsnNode(CHECKCAST, "java/lang/Double"));
                        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D"));
                        insnList.add(new InsnNode(DRETURN));
                    }
                    case Type.ARRAY, Type.OBJECT -> {
                        insnList.add(new TypeInsnNode(CHECKCAST, returnType.getInternalName()));
                        insnList.add(new InsnNode(ARETURN));
                    }
                }

            if (insnList.size() == 0)
                throw new ObzcureException("vmNode instruction set was empty");

            method.setInstructions(insnList);

            method.setMaxLocals(-1);
            method.setMaxStack(-1);

            List<LocalVariableNode> localVariables = method.getLocalVariables();
            if (localVariables == null)
                method.setLocalVariables(new ArrayList<>());
            else
                localVariables.clear();

            if (hasTryCatches)
                method.getTryCatchBlocks().clear();

            // Obfuscate numbers
//            InvokeDynamicsObfuscation.hideNumbers(method.getMethodNode());

            if (debug)
                System.out.println("[" + className + "] Successfully translated method '" + methodName + methodDesc + "'.");

        }
        catch (Throwable t)
        {
            t.printStackTrace();
            return false;
        }
        result = baos.toByteArray();
        return true;
    }


    private final String[] OPCODE_NAMES = {
            "nop", "aconst_null", "iconst_m1", "iconst_0", "iconst_1",
            "iconst_2", "iconst_3", "iconst_4", "iconst_5", "lconst_0",
            "lconst_1", "fconst_0", "fconst_1", "fconst_2", "dconst_0",
            "dconst_1", "bipush", "sipush", "ldc", "ldc_w", "ldc2_w", "iload",
            "lload", "fload", "dload", "aload", "iload_0", "iload_1", "iload_2",
            "iload_3", "lload_0", "lload_1", "lload_2", "lload_3", "fload_0",
            "fload_1", "fload_2", "fload_3", "dload_0", "dload_1", "dload_2",
            "dload_3", "aload_0", "aload_1", "aload_2", "aload_3", "iaload",
            "laload", "faload", "daload", "aaload", "baload", "caload", "saload",
            "istore", "lstore", "fstore", "dstore", "astore", "istore_0",
            "istore_1", "istore_2", "istore_3", "lstore_0", "lstore_1",
            "lstore_2", "lstore_3", "fstore_0", "fstore_1", "fstore_2",
            "fstore_3", "dstore_0", "dstore_1", "dstore_2", "dstore_3",
            "astore_0", "astore_1", "astore_2", "astore_3", "iastore", "lastore",
            "fastore", "dastore", "aastore", "bastore", "castore", "sastore",
            "pop", "pop2", "dup", "dup_x1", "dup_x2", "dup2", "dup2_x1",
            "dup2_x2", "swap", "iadd", "ladd", "fadd", "dadd", "isub", "lsub",
            "fsub", "dsub", "imul", "lmul", "fmul", "dmul", "idiv", "ldiv",
            "fdiv", "ddiv", "irem", "lrem", "frem", "drem", "ineg", "lneg",
            "fneg", "dneg", "ishl", "lshl", "ishr", "lshr", "iushr", "lushr",
            "iand", "land", "ior", "lor", "ixor", "lxor", "iinc", "i2l", "i2f",
            "i2d", "l2i", "l2f", "l2d", "f2i", "f2l", "f2d", "d2i", "d2l", "d2f",
            "i2b", "i2c", "i2s", "lcmp", "fcmpl", "fcmpg",
            "dcmpl", "dcmpg", "ifeq", "ifne", "iflt", "ifge", "ifgt", "ifle",
            "if_icmpeq", "if_icmpne", "if_icmplt", "if_icmpge", "if_icmpgt",
            "if_icmple", "if_acmpeq", "if_acmpne", "goto", "jsr", "ret",
            "tableswitch", "lookupswitch", "ireturn", "lreturn", "freturn",
            "dreturn", "areturn", "return", "getstatic", "putstatic", "getfield",
            "putfield", "invokevirtual", "invokespecial", "invokestatic",
            "invokeinterface", "invokedynamic", "new", "newarray", "anewarray",
            "arraylength", "athrow", "checkcast", "instanceof", "monitorenter",
            "monitorexit", "wide", "multianewarray", "ifnull", "ifnonnull",
            "goto_w", "jsr_w", "breakpoint"
    };
    public String getOpcodeName(final int index) {
        if (index == -1) return "-1";
        return OPCODE_NAMES[index];
    }

}
