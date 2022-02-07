package obzcu.re.vm.utils.asm;

import obzcu.re.vm.Obzcure;
import org.objectweb.asm.tree.FieldNode;

/**
 * @name FieldWrapper.java
 * @authors ItzSomebody, HoverCatz
 * @url https://github.com/ItzSomebody
 * @url https://github.com/HoverCatz
 */
public class FieldWrapper
{

    private final Obzcure obzcure;
    private final FieldNode fieldNode;
    private final AccessHelper access;
    private final ClassWrapper owner;

    private final String originalName, originalDesc;

    public FieldWrapper(Obzcure obzcure, FieldNode fieldNode, ClassWrapper owner)
    {
        this.obzcure = obzcure;
        this.fieldNode = fieldNode;
        this.originalName = fieldNode.name;
        this.originalDesc = fieldNode.desc;
        this.access = new AccessHelper(fieldNode.access);
        this.owner = owner;
    }

    public AccessHelper getAccess()
    {
        return access;
    }

    public String getName()
    {
        return fieldNode.name;
    }

    public String getDesc()
    {
        return fieldNode.desc;
    }

    public String getOriginalName()
    {
        return originalName;
    }

    public String getOriginalDesc()
    {
        return originalDesc;
    }

    public ClassWrapper getOwner()
    {
        return owner;
    }

    public FieldNode getFieldNode()
    {
        return fieldNode;
    }

    public void remove()
    {
    }

}
