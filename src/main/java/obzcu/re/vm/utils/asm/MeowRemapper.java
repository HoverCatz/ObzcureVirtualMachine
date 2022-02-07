package obzcu.re.vm.utils.asm;

import obzcu.re.vm.Obzcure;
import org.objectweb.asm.commons.SimpleRemapper;

import java.util.Map;

/**
 * @authors ItzSomebody, HoverCatz
 * @url https://github.com/ItzSomebody
 * @url https://github.com/HoverCatz
 *
 * Made to rename only the internal VM classes (nothing else)
 */
public class MeowRemapper extends SimpleRemapper
{

    private final Obzcure obzcure;

    public MeowRemapper(Map<String, String> mapping, Obzcure obzcure)
    {
        super(mapping);
        this.obzcure = obzcure;
    }

    @Override
    public String mapType(String owner)
    {
        String remappedName = map(owner);
        return (remappedName != null) ? remappedName : owner;
    }

    @Override
    public String mapFieldName(String owner, String name, String desc)
    {
        String remappedName = map(owner + '.' + name + '.' + desc);
        if (remappedName != null)
            return remappedName;

        // Testing classes
        if (obzcure == null)
            return name;

        ClassWrapper ownerWrapper = obzcure.getClassWrapper(owner);
        if (ownerWrapper == null)
            return name;

        String superName = ownerWrapper.getSuperName();
        ClassWrapper superWrapper = obzcure.getClassWrapper(superName);
        if (superWrapper == null)
            return name;

        remappedName = map(superWrapper.getOriginalName() + '.' + name + '.' + desc);
        return (remappedName != null) ? remappedName : name;
    }

    @Override
    public String mapMethodName(String owner, String name, String desc)
    {
        String remappedName = map(owner + '.' + name + desc);
        if (remappedName != null)
            return remappedName;

        // Testing classes
        if (obzcure == null)
            return name;

        ClassWrapper ownerWrapper = obzcure.getClassWrapper(owner);
        if (ownerWrapper == null)
            return name;

        String superName = ownerWrapper.getSuperName();
        ClassWrapper superWrapper = obzcure.getClassWrapper(superName);
        if (superWrapper == null)
            return name;

        remappedName = map(superWrapper.getOriginalName() + '.' + name + desc);
        return (remappedName != null) ? remappedName : name;
    }

}
