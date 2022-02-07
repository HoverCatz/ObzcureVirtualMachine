package obzcu.re.vm.utils.asm;

import org.objectweb.asm.Opcodes;

/**
 * @author HoverCatz
 * @created 14.01.2022
 * @url https://github.com/HoverCatz
 **/
public class AccessHelper
{

    public int access;

    public AccessHelper(int access)
    {
        this.access = access;
    }

    public void update(int access)
    {
        this.access = access;
    }

    public void sync(ClassWrapper classWrapper)
    {
        classWrapper.getClassNode().access = access;
    }

    public void sync(FieldWrapper fieldWrapper)
    {
        fieldWrapper.getFieldNode().access = access;
    }

    public void sync(MethodWrapper methodWrapper)
    {
        methodWrapper.getMethodNode().access = access;
    }

    public boolean isPublic()
    {
        return (Opcodes.ACC_PUBLIC & access) != 0x0;
    }

    public boolean isPrivate()
    {
        return (Opcodes.ACC_PRIVATE & access) != 0x0;
    }

    public boolean isProtected()
    {
        return (Opcodes.ACC_PROTECTED & access) != 0x0;
    }

    public boolean isStatic()
    {
        return (Opcodes.ACC_STATIC & access) != 0x0;
    }

    public boolean isInterface()
    {
        return (Opcodes.ACC_INTERFACE & access) != 0x0;
    }

    public boolean isAbstract()
    {
        return (Opcodes.ACC_ABSTRACT & access) != 0x0;
    }

    public boolean isFinal()
    {
        return (Opcodes.ACC_FINAL & access) != 0x0;
    }

    public boolean isNative()
    {
        return (Opcodes.ACC_NATIVE & access) != 0x0;
    }

    public boolean isRecord()
    {
        return (Opcodes.ACC_RECORD & access) != 0x0;
    }

    public boolean isEnum()
    {
        return (Opcodes.ACC_ENUM & access) != 0x0;
    }

    public void setAccess(int access)
    {
        this.access = access;
    }

    public void addAccess(int access)
    {
        this.access |= access;
    }

    public void removeAccess(int access)
    {
        this.access &= ~access;
    }

}
