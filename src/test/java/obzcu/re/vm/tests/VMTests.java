package obzcu.re.vm.tests;

import obzcu.re.virtualmachine.ObzcureVM;
import obzcu.re.vm.Obzcure;
import obzcu.re.vm.tests.tests.Test_VMInvokeDynamicsInsnNode;
import obzcu.re.vm.translator.Translator;
import obzcu.re.vm.utils.asm.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HoverCatz
 * @created 21.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMTests
{

    protected static Map<Integer, byte[]> meowData = null;
    protected static AtomicInteger meowIndex = new AtomicInteger(0);

    static
    {
        try
        {
            Field f = ObzcureVM.class.getDeclaredField("meowData");
            f.setAccessible(true);
            meowData = (Map<Integer, byte[]>) f.get(null);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    public static ObzcureVM createVM(String which, int opcode, Object... inputs) throws Throwable
    {
        return createVM(which, opcode, null, inputs);
    }

    public static ObzcureVM createVM(String which, int opcode, MethodHandles.Lookup lookup, Object... inputs) throws Throwable
    {
        int index = meowIndex.getAndIncrement();
        meowData.put(index, TestGenerators.generate(which, opcode, inputs));
        return new ObzcureVM(index, 0, Integer.MAX_VALUE, null, lookup);
    }

    // Virtual call
    public static byte[] vmVirtualMethod(String name, String desc) throws Throwable
    {
        String owner = new Exception().getStackTrace()[1].getClassName().replace(".", "/");
        return vmVirtualMethod(owner, name, desc);
    }

    public static byte[] vmVirtualMethod(Class<?> owner, String name, String desc) throws Throwable
    {
        return vmVirtualMethod(owner.getName().replace(".", "/"), name, desc);
    }

    public static byte[] vmVirtualMethod(String owner, String name, String desc) throws Throwable
    {
        InputStream is = Test_VMInvokeDynamicsInsnNode.class.getResourceAsStream("/" + owner + ".class");
        assertNotNull(is);

        ClassReader reader = new ClassReader(is);
        ClassNode node = new ClassNode();
        reader.accept(node, ClassReader.EXPAND_FRAMES);
        ClassWrapper wrapper = new ClassWrapper(null, node);
        //noinspection OptionalGetWithoutIsPresent
        MethodWrapper methodWrapper = wrapper.getMethodWrapper(name, desc).get();
        assertNotNull(methodWrapper);

        Translator translator = new Translator(wrapper, methodWrapper, null, false, false);
        translator.debug = false;
        boolean success = translator.translateInstructions(0);
        translator.debug = false;
        assertTrue(success);

        Optional<FieldWrapper> lookup = wrapper.getFields().stream().filter(f -> f.getOriginalName().equals(Translator.lookupName)).findFirst();
        if (lookup.isEmpty())
            Translator.injectLookupField(wrapper, wrapper.getOrCreateClinit());
        else
        {
            FieldWrapper field = lookup.get();
            AccessHelper access = field.getAccess();
            if (!access.isPublic() || !access.isStatic())
                access.setAccess(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC);
        }

        byte[] result = translator.getResult();
        assertNotNull(result);
        assertNotEquals(0, result.length);

        File classBytesFolder = new File("src/test/java/obzcu/re/vm/tests/tests/classBytes");
        if (!classBytesFolder.exists()) classBytesFolder.mkdirs();
        Files.write(new File(classBytesFolder,
                owner.replace("/", ".") + "_" + name + ".result").toPath(), result);

        return result;
    }

    // Static call
    public static Method vmStaticMethod(String name, String desc) throws Throwable
    {
        String owner = new Exception().getStackTrace()[1].getClassName().replace(".", "/");
        return vmStaticMethod(owner, name, desc);
    }

    public static Method vmStaticMethod(String owner, String name, String desc) throws Throwable
    {
        InputStream is = Test_VMInvokeDynamicsInsnNode.class.getResourceAsStream("/" + owner + ".class");
        assertNotNull(is);

        ClassReader reader = new ClassReader(is);
        ClassNode node = new ClassNode();
        reader.accept(node, ClassReader.EXPAND_FRAMES);
        ClassWrapper wrapper = new ClassWrapper(null, node);
        //noinspection OptionalGetWithoutIsPresent
        MethodWrapper methodWrapper = wrapper.getMethodWrapper(name, desc).get();
        assertNotNull(methodWrapper);

        Translator translator = new Translator(wrapper, methodWrapper, null, false, false);
        translator.debug = false;
        boolean success = translator.translateInstructions(0);
        assertTrue(success);

        Optional<FieldWrapper> lookup = wrapper.getFields().stream().filter(f -> f.getOriginalName().equals(Translator.lookupName)).findFirst();
        if (lookup.isEmpty())
            Translator.injectLookupField(wrapper, wrapper.getOrCreateClinit());
        else
        {
            FieldWrapper field = lookup.get();
            AccessHelper access = field.getAccess();
            if (!access.isPublic() || !access.isStatic())
                access.setAccess(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC);
        }

        byte[] result = translator.getResult();
        assertNotEquals(0, result.length);

        wrapper.addField(Opcodes.ACC_STATIC, "vmNodes", "[B");

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        byte[] classBytes = writer.toByteArray();
        assertNotEquals(0, classBytes.length);

        File classBytesFolder = new File("src/test/java/obzcu/re/vm/tests/tests/classBytes");
        if (!classBytesFolder.exists()) classBytesFolder.mkdirs();
        Files.write(new File(classBytesFolder,
                owner.replace("/", ".") + "_" + name + ".class").toPath(), classBytes);

        class CustomClassLoader extends ClassLoader
        {
            @Override
            public Class<?> findClass(String name)
            {
                return defineClass(name.replace("/", "."), classBytes, 0, classBytes.length);
            }
        }
        CustomClassLoader ccl = new CustomClassLoader();
        Class<?> clazz = ccl.findClass(node.name);
        assertNotNull(clazz);

        Field vmNodes = clazz.getDeclaredField("vmNodes");
        assertNotNull(vmNodes);
        assertTrue(vmNodes.trySetAccessible());

        Type[] arguments = Type.getArgumentTypes(desc);
        Class<?>[] clazzes = getArgumentClasses(arguments);

        Method method = clazz.getDeclaredMethod(name, clazzes);
        assertNotNull(method);
        assertTrue(method.trySetAccessible());

        vmNodes.set(null, result);

        return method;
    }

    private static Class<?>[] getArgumentClasses(Type[] argumentTypes) throws ClassNotFoundException
    {
        Class<?>[] classes = new Class[argumentTypes.length];
        for (int i = 0; i < classes.length; i++)
            classes[i] = Class.forName(argumentTypes[i].getInternalName().replace("/", "."));
        return classes;
    }

    public static Map<String, byte[]> getClassBytesFromPackage(String pkg) throws Throwable
    {
        Map<String, byte[]> classes = new HashMap<>();
        File ourFile = new File(Obzcure.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        if (ourFile.isDirectory())
        {
            ourFile = new File(ourFile.getParent(), "test-classes");
            if (!ourFile.exists())
                throw new IllegalStateException("VMTests test-classes folder doesn't exist: '" + ourFile.getAbsolutePath() + "'");
            File folder = new File(ourFile, pkg);
            recursiveFolder(pkg, folder, classes);
        }
        else
            throw new IllegalStateException("Illegal file type (not a directory)!");
        return classes;
    }

    private static void recursiveFolder(String folderName, File folder, Map<String, byte[]> classes) throws Throwable
    {
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File file : files)
        {
            final String name = file.getName();
            if (file.isDirectory())
                recursiveFolder(folderName + name + "/", file, classes);
            else
            if (name.endsWith(".class"))
            {
                InputStream is = new FileInputStream(file);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[0xffff];
                while ((nRead = is.read(data, 0, data.length)) != -1)
                    buffer.write(data, 0, nRead);
                classes.put(file.getAbsolutePath(), buffer.toByteArray());
            }
        }
    }

    public static BufferedReader getOutput(Process p) {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }
    public static BufferedReader getError(Process p) {
        return new BufferedReader(new InputStreamReader(p.getErrorStream()));
    }

    public static File getJavaExecutable()
    {
        String java_path = System.getProperty("java.home");
        File javaPath = new File(java_path);
        assertTrue(javaPath.exists());

        File binPath = new File(javaPath, "bin");
        assertTrue(binPath.exists());

        // Change this for OS's other than windows
        File javaExecutable = new File(binPath, "java.exe");
        assertTrue(javaExecutable.exists());

        return javaExecutable;
    }

    public static File processJar(Class<?> clazz, String pkg, boolean translate)
    {
        return processJar(clazz, pkg, translate, false, false);
    }

    public static File processJar(Class<?> clazz, String pkg, boolean translate, boolean fp, boolean rf)
    {
        File tempFile = assertDoesNotThrow(() ->
        {
            String mainClass = clazz.getName();
            String mainClassSlashes = mainClass.replace(".", "/");
            System.out.println("mainClass: " + mainClassSlashes + ".class");

            Map<String, byte[]> classes = getClassBytesFromPackage(pkg);
            assertNotEquals(0, classes.size());

            Manifest manifest = new Manifest();
            Attributes mainAttribs = manifest.getMainAttributes();
            mainAttribs.put(Attributes.Name.MANIFEST_VERSION, "1.0");
            mainAttribs.put(Attributes.Name.MAIN_CLASS, mainClass);
            mainAttribs.put(Attributes.Name.SEALED, "true");

            File tmp = File.createTempFile("obzcure", mainClass);
            try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(tmp), manifest))
            {
                AtomicInteger resourceIndex = null;
                Map<Integer, byte[]> translatedMethods = null;
                ByteArrayOutputStream baos = null;
                DataOutputStream dos = null;

                if (translate)
                {
                    resourceIndex = new AtomicInteger(0);
                    translatedMethods = new HashMap<>();
                    baos = new ByteArrayOutputStream();
                    dos = new DataOutputStream(baos);
                }

                for (String name : classes.keySet())
                {
                    byte[] bytes = classes.get(name);
                    String entryName = name.substring(name.indexOf("test-classes") + 13).replace("\\", "/");
                    if (translate)
                    {
                        ObzcureVM.Duo<Boolean, byte[]> result = translateClass(bytes, translatedMethods, resourceIndex, fp, rf);
                        if (result.a)
                            bytes = result.b;
                    }
                    System.out.println("[" + (entryName.equals(mainClassSlashes + ".class") ? "true " : "false") + "] " + entryName + " -> " + bytes.length);
                    JarEntry entry = new JarEntry(entryName);
                    entry.setCompressedSize(-1);
                    jos.putNextEntry(entry);
                    jos.write(bytes);
                    jos.closeEntry();
                }

                if (translate)
                {
                    dos.writeInt(translatedMethods.size());
                    for (int index : translatedMethods.keySet())
                    {
                        byte[] bytes = translatedMethods.get(index);
                        dos.writeInt(bytes.length);
                        dos.write(bytes);
                    }
                    dos.flush();

                    JarEntry entry = new JarEntry("obzcure/cats.meow");
                    entry.setCompressedSize(-1);
                    jos.putNextEntry(entry);
                    jos.write(baos.toByteArray());
                    jos.closeEntry();

                    dos.close();
                }

                classes.clear();
                addSelfVMClasses(classes);
                for (String name : classes.keySet())
                {
                    byte[] bytes = classes.get(name);
                    String entryName = name.substring(name.indexOf("classes") + 8).replace("\\", "/");
                    JarEntry entry = new JarEntry(entryName);
                    entry.setCompressedSize(-1);
                    jos.putNextEntry(entry);
                    jos.write(bytes);
                    jos.closeEntry();
                }
            }

            return tmp;
        });

        assertTrue(tempFile.exists());

        // Delete temp file when the JVM exits
        tempFile.deleteOnExit();

        return tempFile;
    }

    private static ObzcureVM.Duo<Boolean, byte[]> translateClass(byte[] bytes, Map<Integer, byte[]> translatedMethods, AtomicInteger resourceIndex)
    {
        return translateClass(bytes, translatedMethods, resourceIndex, false, false);
    }

    private static ObzcureVM.Duo<Boolean, byte[]> translateClass(byte[] bytes, Map<Integer, byte[]> translatedMethods, AtomicInteger resourceIndex, boolean fp, boolean rf)
    {
        AtomicBoolean success = new AtomicBoolean(false);

        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, ClassReader.EXPAND_FRAMES);

        ClassWrapper wrapper = new ClassWrapper(null, node);
        wrapper.getMethods().forEach(m ->
        {
            Translator translator = new Translator(wrapper, m, null, fp, rf, true);
            if (translator.translateInstructions(resourceIndex.get()))
            {
                translatedMethods.put(resourceIndex.getAndIncrement(), translator.getResult());
                success.set(true);
            }
        });

        if (success.get())
            Translator.injectLookupField(wrapper, wrapper.getOrCreateClinit());

        // Remove final access from every field and method.
        // This opens up for virtualization of more methods.
        if (rf)
        {
            AccessHelper classAccess = wrapper.getAccess();
            // The Java spec allows only final static fields in interfaces, but this field/method is not final.
            if (!classAccess.isInterface())
            {
                wrapper.getFields().forEach(f -> {
                    AccessHelper access = f.getAccess();
                    if (access.isFinal())
                    {
                        access.removeAccess(Opcodes.ACC_FINAL);
                        access.sync(f);
                    }
                });
                wrapper.getMethods().forEach(m -> {
                    AccessHelper access = m.getAccess();
                    if (access.isFinal())
                    {
                        access.removeAccess(Opcodes.ACC_FINAL);
                        access.sync(m);
                    }
                });
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        node.accept(writer);
        return new ObzcureVM.Duo<>(success.get(), writer.toByteArray());
    }

    public static ObzcureVM.Duo<String, String> executeJar(File file)
    {
        return assertDoesNotThrow(() ->
        {
            File javaExecutable = getJavaExecutable();
            Process p = Runtime.getRuntime().exec(new String[] { javaExecutable.getAbsolutePath(), "--enable-preview", "-jar", file.getAbsolutePath() });
            BufferedReader br = getOutput(p);
            String line;
            StringJoiner lines = new StringJoiner("\n");
            while ((line = br.readLine()) != null)
                lines.add(line);
            BufferedReader error = getError(p);
            String err;
            StringJoiner errors = new StringJoiner("\n");
            while ((err = error.readLine()) != null)
                errors.add(err);
            return new ObzcureVM.Duo<>(lines.toString(), errors.toString());
        });
    }

    private static void addSelfVMClasses(Map<String, byte[]> classes) throws Throwable
    {
        File ourFile = new File(Obzcure.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        if (ourFile.isDirectory())
        {
            File folder = new File(ourFile, "obzcu/re/virtualmachine/");
            recursiveFolder("obzcu/re/virtualmachine/", folder, classes);
        }
        else
        {
            JarFile ourJar = new JarFile(ourFile);
            Enumeration<JarEntry> ourEntries = ourJar.entries();
            while (ourEntries.hasMoreElements())
            {
                JarEntry jarEntry = ourEntries.nextElement();
                if (jarEntry.isDirectory()) continue;
                String name = jarEntry.getName();
                if (!name.startsWith("obzcu/re/virtualmachine/")) continue;
                if (!name.endsWith(".class")) continue;
                InputStream is = ourJar.getInputStream(jarEntry);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[0xffff];
                while ((nRead = is.read(data, 0, data.length)) != -1)
                    buffer.write(data, 0, nRead);
                classes.put(name, buffer.toByteArray());
            }
            ourJar.close();
        }
    }

    public static ObzcureVM.Duo<String, String> fixVMExceptions(String in, String out)
    {
        List<String> inList = new ArrayList<>();
        List<String> outList = new ArrayList<>();
        String[] inSplit = in.split("\n");
        String[] outSplit = out.split("\n");
        for (String outLine : outSplit)
        {
            if (outLine.startsWith("\tat obzcu.re.virtualmachine.")) continue;
            // TODO: Do we need to remove '    at java.base/' from inSplit too?
            if (outLine.startsWith("\tat java.base/")) continue;
            if (outLine.contains("<init>")) outLine = removeLineNumber(outLine);
            outList.add(outLine);
        }
        for (String inLine : inSplit)
        {
            if (inLine.contains("<init>")) inLine = removeLineNumber(inLine);
            inList.add(inLine);
        }
        if (inList.size() != outList.size())
            assertEquals(String.join("\n", inList), String.join("\n", outList));
        for (int i = 0; i < inList.size(); i++)
        {
            String inLine = inList.get(i);
            int index = inLine.lastIndexOf(':'); // Does the original output even have a linenumber?
            if (index == -1) continue; // Nope, so ignore

            String outLine = outList.get(i);
            int index2 = outLine.lastIndexOf(':'); // Does the vm output have a line number?
            if (index2 >= 0) continue; // Yes, so ignore

            index2 = outLine.lastIndexOf(')');
            String sub = outLine.substring(0, index2); // Insert missing line-number from the original output
            outList.set(i, sub + ":" + inLine.substring(index + 1, inLine.lastIndexOf(')')) + ")");
        }
        return new ObzcureVM.Duo<>(String.join("\n", inList), String.join("\n", outList));
    }

    private static String removeLineNumber(String str)
    {
        int index = str.lastIndexOf(':');
        if (index == -1) return str;
        //    at obzcu.re.vm.tests.tests.custom.testjar.MainTest.<init>(MainTest.java:19)
        return str.substring(0, index) + ")";
    }

    public static void print(String title, String message)
    {
        System.out.println("===== " + title + " =====");
        System.out.println(message);
        System.out.println("======" + "=".repeat(title.length()) + "======");
        System.out.flush();
    }

    public static void error(String title, String error)
    {
        System.out.println("===== " + title + " =====");
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.flush();
        System.err.println(error);
        System.err.flush();
        try {
            // Amazingly gross fix for out-of-sync messages
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("======" + "=".repeat(title.length()) + "======");
    }

}
