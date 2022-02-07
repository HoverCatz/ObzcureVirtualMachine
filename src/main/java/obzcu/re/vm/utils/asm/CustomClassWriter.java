package obzcu.re.vm.utils.asm;

import obzcu.re.vm.Obzcure;
import org.objectweb.asm.ClassWriter;

/**
 * @name CustomClassWriter.java
 * @author ItzSomebody
 * @url https://github.com/ItzSomebody
 */
public class CustomClassWriter extends ClassWriter
{

    private final Obzcure obzcure;

    public CustomClassWriter(int flags, Obzcure obzcure)
    {
        super(flags);
        this.obzcure = obzcure;
    }

    @Override
    protected String getCommonSuperClass(final String type1, final String type2)
    {
        if ("java/lang/Object".equals(type1) || "java/lang/Object".equals(type2))
            return "java/lang/Object";

        String first = deriveCommonSuperName(type1, type2);
        String second = deriveCommonSuperName(type2, type1);

        if (!"java/lang/Object".equals(first))
            return first;

        if (!"java/lang/Object".equals(second))
            return second;

        return getCommonSuperClass(obzcure.getClassWrapper(type1).getSuperName(), obzcure.getClassWrapper(type2).getSuperName());
    }

    private String deriveCommonSuperName(final String type1, final String type2)
    {
        if (obzcure.isAssignableFrom(type1, type2))
            return type1;
        else if (obzcure.isAssignableFrom(type2, type1))
            return type2;
        ClassWrapper first  = obzcure.getClassWrapper(type1);
        ClassWrapper second = obzcure.getClassWrapper(type2);
        String temp;
        do
        {
            temp = first.getSuperName();
            first = obzcure.getClassWrapper(temp);
        } while (!obzcure.isAssignableFrom(temp, type2));
        return temp;
    }

}
