package obzcu.re.vm.utils.asm;

import obzcu.re.vm.Obzcure;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

import java.util.Collection;
import java.util.List;

/**
 * @name MethodWrapper.java
 * @authors ItzSomebody, HoverCatz
 * @url https://github.com/ItzSomebody
 * @url https://github.com/HoverCatz
 */
public class MethodWrapper
{

    private final Obzcure obzcure;
    private final MethodNode methodNode;
    private final AccessHelper access;
    private final ClassWrapper owner;

    private final String originalName, originalDesc;

    public MethodWrapper(Obzcure obzcure, MethodNode methodNode, ClassWrapper owner)
    {
        this.obzcure = obzcure;
        this.methodNode = methodNode;
        this.originalName = methodNode.name;
        this.originalDesc = methodNode.desc;
        this.access = new AccessHelper(methodNode.access);
        this.owner = owner;
    }

    public MethodNode getMethodNode()
    {
        return methodNode;
    }

    public InsnList getInstructions()
    {
        return methodNode.instructions;
    }

    public AccessHelper getAccess()
    {
        return access;
    }

    public String getName()
    {
        return methodNode.name;
    }

    public String getDesc()
    {
        return methodNode.desc;
    }

    public String getOriginalName()
    {
        return originalName;
    }

    public String getOriginalDesc()
    {
        return originalDesc;
    }

    public boolean hasInstructions()
    {
        return methodNode.instructions != null;
    }

    public void setInstructions(InsnList insnList)
    {
        methodNode.instructions = insnList;
    }

    public boolean hasTryCatchBlocks()
    {
        return methodNode.tryCatchBlocks != null && !methodNode.tryCatchBlocks.isEmpty();
    }

    public Collection<TryCatchBlockNode> getTryCatchBlocks()
    {
        return methodNode.tryCatchBlocks;
    }

    public int getMaxLocals()
    {
        return methodNode.maxLocals;
    }

    public void setMaxLocals(int i)
    {
        methodNode.maxLocals = i;
    }

    public int getMaxStack()
    {
        return methodNode.maxStack;
    }

    public void setMaxStack(int i)
    {
        methodNode.maxStack = i;
    }

    public List<LocalVariableNode> getLocalVariables()
    {
        return methodNode.localVariables;
    }

    public void setLocalVariables(List<LocalVariableNode> localVariableNodes)
    {
        methodNode.localVariables = localVariableNodes;
    }

    public ClassWrapper getOwner()
    {
        return owner;
    }

}
