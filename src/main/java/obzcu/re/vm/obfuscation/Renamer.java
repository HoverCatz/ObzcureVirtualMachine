package obzcu.re.vm.obfuscation;

import obzcu.re.vm.Obzcure;
import obzcu.re.vm.utils.asm.*;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HoverCatz
 * @created 17.01.2022
 * @url https://github.com/HoverCatz
 **/
public class Renamer
{

    // TODO: Fix this :D Doesn't work rn.

    public static Map<String, String> mappings = new HashMap<>();

    // Rename VM classes
    public static Remapper process(Map<String, ClassWrapper> classes, Obzcure obzcure)
    {

        boolean doRenaming = true;
        if (!doRenaming)
            return new SimpleRemapper(mappings);

        List<ClassWrapper> values = new ArrayList<>(classes.values());
        Collections.shuffle(values);
        int numClasses = 0;
        for (ClassWrapper wrapper : values)
        {
            if (!wrapper.isVMClass())
                continue;
            process(numClasses++, wrapper, classes, obzcure);
        }

        if (mappings.isEmpty())
        {
            System.out.println("Skipping renaming.");
            return new SimpleRemapper(mappings);
        }

        Remapper remapper = new MeowRemapper(mappings, obzcure);
        for (ClassWrapper wrapper : classes.values())
        {
            ClassNode clone = new ClassNode();
            ClassNode node = wrapper.getClassNode();
            node.accept(new ClassRemapper(clone, remapper));

            clone.innerClasses.clear();
            wrapper.setClassNode(clone);
        }
        return remapper;
    }

    private static void process(int numClasses, ClassWrapper wrapper, Map<String, ClassWrapper> classes, Obzcure obzcure)
    {
        String className = wrapper.getOriginalName();
        String newClassName = getMeow(1 + numClasses);
        System.out.println("Renaming '" + className + "' to 'meow/" + newClassName + "'!");
        mappings.put(className, "meow/" + newClassName);

        ClassWrapper topMostClass = getTopmostClass(wrapper, obzcure);
        String owner = topMostClass.getOriginalName();
        if (wrapper.hasFields())
        {
            AtomicInteger numFieldsRenamed = new AtomicInteger(0);
            wrapper.getFields().forEach(fieldWrapper ->
            {
                String key = owner + '.' + fieldWrapper.getOriginalName() + '.' + fieldWrapper.getOriginalDesc();
                if (mappings.containsKey(key)) return;
                String newName = getMeow(numFieldsRenamed.incrementAndGet());
                mappings.put(key, newName);
                System.out.println("Renaming field '" + key + "' -> " + newName);
            });
        }
        if (wrapper.hasMethods())
        {
            AtomicInteger numMethodsRenamed = new AtomicInteger(0);
            wrapper.getMethods().forEach(methodWrapper ->
            {
                renameLocalVariables(methodWrapper.getLocalVariables());
                String name = methodWrapper.getOriginalName();
                if (name.equals("<init>") || name.equals("<clinit>") || name.equals("premain")) return;
                String desc = methodWrapper.getOriginalDesc();
                if (name.equals("toString") && desc.equals("()Ljava/lang/String;")) return;
                if (name.equals("main") && desc.equals("([Ljava/lang/String;)V")) return;
                if (cannotRenameMethod(wrapper, topMostClass, name, desc, obzcure)) return;
                String key = owner + '.' + name + desc;
                if (mappings.containsKey(key)) return;
                String newName = getMeow(numMethodsRenamed.incrementAndGet());
                mappings.put(key, newName);
                System.out.println("Renaming method '" + key + "' -> " + newName);
            });
        }

    }

    private static boolean cannotRenameMethod(ClassWrapper wrapper, ClassWrapper topMostClass, String name, String desc, Obzcure obzcure)
    {
        List<String> interfaces = topMostClass.getInterfaces();
        if (interfaces == null || interfaces.isEmpty())
            return false;
        for (String clazz : interfaces)
        {
            switch (clazz)
            {
                // TODO: Add more lambda methods here
                case "java/lang/Runnable" -> {
                    if (name.equals("run") &&
                        desc.equals("()V"))
                    return true;
                }
                default -> {
                    ClassWrapper itfWrapper = obzcure.getClassWrapper(clazz);
                    if (itfWrapper != null)
                    {
                        boolean cannotRename = cannotRenameMethod(wrapper, itfWrapper, name, desc, obzcure);
                        if (cannotRename) return true;
                    }
                }
            }
        }
        return false;
    }

    private static ClassWrapper getTopmostClass(ClassWrapper wrapper, Obzcure obzcure)
    {
        String superName = wrapper.getSuperName();
        if (superName == null) return wrapper;
        ClassWrapper superWrapper = obzcure.getClassWrapper(superName);
        if (superWrapper == null) return wrapper;
        return getTopmostClass(superWrapper, obzcure);
    }

    private static void renameLocalVariables(List<LocalVariableNode> localVariables)
    {
        if (localVariables == null || localVariables.isEmpty()) return;
        for (LocalVariableNode var : localVariables)
            var.name = getMeow(1);
    }

    private static final char[] MEOW = { 'M', 'e', 'o', 'w' };
    private static String getMeow(int i)
    {
        boolean simpleMeow = true;
        if (simpleMeow)
            return "Meow".repeat(i);
        StringBuilder sb = new StringBuilder("Meow");
        for (int n = 0; n < i; n++)
            sb.append(MEOW[n % 4]);
        return sb.toString();
    }

}
