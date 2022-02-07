package obzcu.re.vm.utils.asm;

import obzcu.re.vm.Obzcure;
import obzcu.re.vm.utils.RandomUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.*;

/**
 * @name ClassWrapper.java
 * @authors ItzSomebody, HoverCatz
 * @url https://github.com/ItzSomebody
 * @url https://github.com/HoverCatz
 */
public class ClassWrapper
{

    // Sorry, this class is a mess. I'll clean it up later

    private final Obzcure obzcure;
    private ClassNode node;
    private AccessHelper access;

    private final Map<MethodNode, MethodWrapper> methodWrappers = new HashMap<>();
    private final Map<FieldNode, FieldWrapper> fieldWrappers = new HashMap<>();

    private final String originalName;

    public ClassWrapper(Obzcure obzcure, ClassNode node)
    {
        this.obzcure = obzcure;
        this.node = node;
        this.originalName = node.name;
        this.access = new AccessHelper(node.access);
        if (node.methods != null)
            node.methods.forEach(methodNode ->
                    methodWrappers.put(methodNode, new MethodWrapper(obzcure, methodNode, this)));
        if (node.fields != null)
            node.fields.forEach(fieldNode ->
                    fieldWrappers.put(fieldNode, new FieldWrapper(obzcure, fieldNode, this)));
    }

    public boolean hasMethods()
    {
        return !methodWrappers.isEmpty();
    }

    public boolean hasFields()
    {
        return !fieldWrappers.isEmpty();
    }

    public Collection<MethodWrapper> getMethods()
    {
        return methodWrappers.values();
    }

    public Collection<FieldWrapper> getFields()
    {
        return fieldWrappers.values();
    }

    public MethodWrapper getOrCreateClinit()
    {
        Optional<MethodWrapper> clinit = getMethodWrapper("<clinit>", "()V");
        if (clinit.isPresent()) return clinit.get();
        MethodNode mn = new MethodNode(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
        mn.instructions.add(new InsnNode(Opcodes.RETURN));
        node.methods.add(mn);
        MethodWrapper methodWrapper = new MethodWrapper(obzcure, mn, this);
        methodWrappers.put(mn, methodWrapper);
        return methodWrapper;
    }

    public Optional<MethodWrapper> getMethodWrapper(String name, String desc)
    {
        return methodWrappers.values().stream().filter(mn -> mn.getName().equals(name) && mn.getDesc().equals(desc)).findFirst();
    }

    private void removeMethod(String name, String desc)
    {
        for (MethodNode methodNode : methodWrappers.keySet().toArray(new MethodNode[0]))
        {
            if (methodNode.name.equals(name) && methodNode.desc.equals(desc))
            {
                methodWrappers.remove(methodNode);
                node.methods.remove(methodNode);
                break;
            }
            MethodWrapper methodWrapper = methodWrappers.get(methodNode);
            if (methodWrapper.getOriginalName().equals(name) && methodWrapper.getOriginalDesc().equals(desc))
            {
                methodWrappers.remove(methodNode);
                node.methods.remove(methodNode);
                break;
            }
        }
    }

    private void removeMethods(String name)
    {
        for (MethodNode methodNode : methodWrappers.keySet().toArray(new MethodNode[0]))
        {
            if (methodNode.name.equals(name))
            {
                methodWrappers.remove(methodNode);
                node.methods.remove(methodNode);
                break;
            }
            MethodWrapper methodWrapper = methodWrappers.get(methodNode);
            if (methodWrapper.getOriginalName().equals(name))
            {
                methodWrappers.remove(methodNode);
                node.methods.remove(methodNode);
                break;
            }
        }
    }

    public AccessHelper getAccess()
    {
        return access;
    }

    public String getName()
    {
        return node.name;
    }

    public String getOriginalName()
    {
        return originalName;
    }

    public String getSuperName()
    {
        return node.superName;
    }

    public List<String> getInterfaces()
    {
        return node.interfaces;
    }

    public void setClassNode(ClassNode node)
    {
        this.node = node;
        this.access = new AccessHelper(node.access);
        this.fieldWrappers.clear();
        this.methodWrappers.clear();
        if (node.methods != null)
            node.methods.forEach(methodNode ->
                    methodWrappers.put(methodNode, new MethodWrapper(obzcure, methodNode, this)));
        if (node.fields != null)
            node.fields.forEach(fieldNode ->
                    fieldWrappers.put(fieldNode, new FieldWrapper(obzcure, fieldNode, this)));
    }

    public void addField(int access, String name, String descriptor)
    {
        addField(access, name, descriptor, null);
    }

    public void addField(int access, String name, String descriptor, Object value)
    {
        FieldNode fieldNode = new FieldNode(access, name, descriptor, null, value);
        FieldWrapper field = new FieldWrapper(obzcure, fieldNode, this);
        fieldWrappers.put(fieldNode, field);
        node.fields.add(fieldNode);
    }

    public byte[] getBytes()
    {
        if (isVMClass())
        {
            if (isVMMain())
            {
                removeMethod("<init>", "(III[Lobzcu/re/virtualmachine/asm/VMTryCatch;Ljava/lang/invoke/MethodHandles$Lookup;[B)V");
                removeMethods("virtualizeTests");

                fixMeowCatsRename();
            }
            hideDebugInfo();
        }
        ClassWriter writer;
        try
        {
            writer = new CustomClassWriter(ClassWriter.COMPUTE_FRAMES, obzcure);
            node.accept(writer);
        }
        catch (Throwable ignored)
        {
            writer = new CustomClassWriter(ClassWriter.COMPUTE_MAXS, obzcure);
            node.accept(writer);
        }
        return writer.toByteArray();
    }

    private void fixMeowCatsRename()
    {
        MethodWrapper clinit = getOrCreateClinit();
        for (AbstractInsnNode insn : clinit.getInstructions())
            if (insn instanceof LdcInsnNode ldc &&
                    ldc.cst instanceof String str &&
                    str.equals("/obzcure/cats.meow"))
            {
                ldc.cst = obzcure.catsMeowFilename;
                return;
            }
    }

    public boolean isVMClass()
    {
        return originalName.startsWith("obzcu/re/virtualmachine/");
    }

    public boolean isVMMain()
    {
        return originalName.equals("obzcu/re/virtualmachine/ObzcureVM");
    }

    // VM Class
    private void hideDebugInfo()
    {
        boolean hideStuff = false;
        if (!hideStuff) return;
        node.signature = "\u0000";
        node.sourceDebug = node.name;
        node.sourceFile = node.name + ".java";
        node.methods.forEach(method ->
            method.instructions.forEach(insn ->
            {
                if (insn instanceof LineNumberNode line)
                    line.line = RandomUtils.getRandomInt(1, 9999);
            }));
    }

    public ClassNode getClassNode()
    {
        return node;
    }

}
