package obzcu.re.vm;

import obzcu.re.vm.translator.Translator;
import obzcu.re.vm.utils.RandomUtils;
import obzcu.re.vm.utils.StringUtils;
import obzcu.re.vm.utils.asm.*;
import org.apache.commons.cli.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url <a href="https://github.com/HoverCatz">github.com/HoverCatz</a>
 **/
public class Obzcure
{

    public static void main(String[] args)
    {
        new Obzcure(args);
    }

    private final Map<String, ClassWrapper> nodes = new HashMap<>();
    private final Map<String, byte[]> resources = new HashMap<>();

    // ObzcureVM filename
    public String catsMeowFilename = "/obzcure/cats.meow";

    public Obzcure(String[] args)
    {
        try
        {

            File inputFile, outputFile;

            boolean removeFinal = false;
            boolean forcePublic = false;
            boolean renameVMClasses = false;
            boolean skipDebug = false;
            boolean rndMeow = false;
            boolean force = false;

            boolean localDevTesting = false;
            if (localDevTesting)
            {
                outputFile = new File("output", "out.jar");
                if (outputFile.exists() && !outputFile.delete())
                    throw new RuntimeException("Output file already exists, and failed to delete it.");

                inputFile = Objects.requireNonNull(new File("TestJar/target/").listFiles(f -> f.getName().endsWith(".jar")))[0];
//                inputFile = new File("C:\\Users\\" + System.getProperty("user.name") + "\\IdeaProjects\\ItzCounter\\build\\libs\\ItzCounter-1.0-SNAPSHOT.jar");

                rndMeow = true;
                removeFinal = true;
                force = true;
            }
            else
            {
                CommandLine cmd;
                Options options = new Options();
                HelpFormatter helpFormatter = new HelpFormatter();
                try
                {
                    options.addOption(Option.builder().argName("input").option("i").longOpt("input").hasArg().required(true).desc("Input jar file").build());
                    options.addOption(Option.builder().argName("output").option("o").longOpt("output").hasArg().required(true).desc("Output jar file").build());
                    options.addOption(new Option("rf", "removeFinal", false, "Force virtualization of final fields (removes final access)"));
                    options.addOption(new Option("fp", "forcePublic", false, "Make every field and method public (accessible from everywhere)"));
//                    options.addOption(new Option("r", "renaming", false, "Rename all VM classes/fields/methods"));
                    options.addOption(new Option("sd", "skipDebug", false, "Remove debugging information from all classes"));
                    options.addOption(new Option("rm", "rndMeow", false, "Random cats.meow filename"));
                    options.addOption(new Option("f", "force", false, "Force overwrite output file (if it exists)"));

                    CommandLineParser parser = new DefaultParser();
                    cmd = parser.parse(options, args);

                    inputFile = new File(cmd.getOptionValue("input"));
                    outputFile = new File(cmd.getOptionValue("output"));
                }
                catch (Throwable t)
                {
                    System.err.println(t.getMessage());
                    helpFormatter.printHelp("Usage:", options);
                    return;
                }

                if (!inputFile.exists())
                {
                    System.err.println("Input file doesn't exist: '" + inputFile.getAbsolutePath() + "'");
                    helpFormatter.printHelp("Usage:", options);
                    return;
                }

                removeFinal = cmd.hasOption("rf");
                forcePublic = cmd.hasOption("fp");
//                renameVMClasses = cmd.hasOption("r");
                skipDebug = cmd.hasOption("sd");
                rndMeow = cmd.hasOption("rm");
                force = cmd.hasOption("f");
            }

            if (outputFile.exists())
            {
                if (!force)
                {
                    System.out.println("Output file already exist. Do you want to overwrite? [Y/n]");
                    Scanner sc = new Scanner(System.in);
                    String s = sc.nextLine();
                    if (s == null)
                        return;
                    if (!s.isEmpty() && (s.equalsIgnoreCase("n") || s.equalsIgnoreCase("no")))
                        return;
                }
                if (!outputFile.delete())
                    throw new Throwable("Couldn't delete existing output file. Used by another process maybe?");
            }

            System.out.println();
            System.out.println("Welcome to Obzcure.");
            System.out.println();

            JarFile jar = new JarFile(inputFile);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements())
            {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.endsWith(".class") || name.endsWith(".class/") || skipClasses(name))
                {
                    InputStream is = jar.getInputStream(entry);
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;
                    byte[] data = new byte[0xffff];
                    while ((nRead = is.read(data, 0, data.length)) != -1)
                        buffer.write(data, 0, nRead);
                    resources.put(name, buffer.toByteArray());
                    continue;
                }
                ClassNode node = new ClassNode();
                ClassReader reader = new ClassReader(jar.getInputStream(entry));
                int flag = ClassReader.EXPAND_FRAMES;
                if (skipDebug)
                    flag |= ClassReader.SKIP_DEBUG; // Remove debug info
                reader.accept(node, flag);
                ClassWrapper wrapper = new ClassWrapper(this, node);
                nodes.put(node.name, wrapper);
                // Remove final access from every field and method.
                // This opens up for virtualization of more methods.
                if (removeFinal)
                {
                    AccessHelper classAccess = wrapper.getAccess();
                    // The Java spec allows only final static fields in interfaces, but this field/method is not final.
                    if (classAccess.isInterface())
                        continue;
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
            jar.close();

            // Add the runtime virtualmachine classes.
            addSelfVMClasses(nodes);

            // Build hierarchy
            nodes.values().forEach(wrapper -> buildHierarchy(wrapper, null));

            // Rename every internal JVM class
//            Remapper remapper;
//            if (renameVMClasses)
//                remapper = Renamer.process(nodes, this);
//            else
//                remapper = new SimpleRemapper(new HashMap<>());

            int resourceIndex = 0;
            Map<Integer, byte[]> translatedMethods = new HashMap<>();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            for (String className : nodes.keySet())
            {
                ClassWrapper node = nodes.get(className);
                if (node.isVMClass()) continue; // Don't virtualize VM classes
                if (!node.hasMethods()) continue;
                for (MethodWrapper method : node.getMethods())
                {
                    Translator translator = new Translator(node, method, this, forcePublic, removeFinal);
                    if (translator.translateInstructions(resourceIndex))
                        translatedMethods.put(resourceIndex++, translator.getResult());
                }
                MethodWrapper clinit = node.getOrCreateClinit();
                Translator.injectLookupField(node, clinit);
            }
            dos.writeInt(translatedMethods.size());
            for (int index : translatedMethods.keySet())
            {
                byte[] bytes = translatedMethods.get(index);
                dos.writeInt(bytes.length);
                dos.write(bytes);
            }
            dos.flush();
            if (rndMeow)
                catsMeowFilename = "/" + StringUtils.randomAlphaString(RandomUtils.getRandomInt(3, 9)) + "/" +
                        StringUtils.randomAlphaString(RandomUtils.getRandomInt(3, 9)) + "." + StringUtils.randomAlphaString(RandomUtils.getRandomInt(3, 5));
            resources.put(catsMeowFilename.substring(1), baos.toByteArray());
            dos.close();

            try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(outputFile)))
            {
                for (String className : nodes.keySet())
                {
                    ClassWrapper node = nodes.get(className);

                    JarEntry entry = new JarEntry(node.getName() + ".class");
                    entry.setCompressedSize(-1);
                    jos.putNextEntry(entry);

                    jos.write(node.getBytes());
                    jos.closeEntry();
                }
                for (String resource : resources.keySet())
                {
                    byte[] bytes = resources.get(resource);

                    try
                    {
                        JarEntry entry = new JarEntry(resource);
                        entry.setCompressedSize(-1);
                        jos.putNextEntry(entry);
//                        System.out.println("'" + resource + "' -> " + bytes.length);
                        jos.write(bytes);
                        jos.closeEntry();
                    }
                    catch (Throwable t)
                    {
                        t.printStackTrace();
                    }
                }
            }

        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    // Skip virtualization of these classes
    private boolean skipClasses(String name)
    {
        // TODO: Add more exclusions
        return name.startsWith("com/google/gson/");
    }

    private void recursiveFolder(String folderName, File folder, Map<String, ClassWrapper> classes) throws Throwable
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
                ClassReader reader = new ClassReader(new FileInputStream(file));
                ClassNode node = new ClassNode();
                reader.accept(node, ClassReader.EXPAND_FRAMES
//                    | ClassReader.SKIP_DEBUG
                );
                ClassWrapper wrapper = new ClassWrapper(this, node);
                buildHierarchy(wrapper, null);
                classes.put(node.name, wrapper);
            }
        }
    }

    private void addSelfVMClasses(Map<String, ClassWrapper> classes) throws Throwable
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
                ClassReader reader = new ClassReader(is);
                ClassNode node = new ClassNode();
                reader.accept(node, ClassReader.EXPAND_FRAMES
//                    | ClassReader.SKIP_DEBUG
                );
                ClassWrapper wrapper = new ClassWrapper(this, node);
                buildHierarchy(wrapper, null);
                classes.put(node.name, wrapper);
            }
            ourJar.close();
        }
    }

    // All hierarchy code made by ItzSomebody (https://github.com/ItzSomebody)
    private final Map<String, ClassTree> hierarchy = new LinkedHashMap<>();

    // Made by ItzSomebody (https://github.com/ItzSomebody)
    public ClassTree getTree(String ref)
    {
        if (!hierarchy.containsKey(ref))
        {
            ClassWrapper wrapper = getClassWrapper(ref);
            buildHierarchy(wrapper, null);
        }
        return hierarchy.get(ref);
    }

    // Made by ItzSomebody (https://github.com/ItzSomebody)
    public void buildHierarchy(ClassWrapper wrapper, ClassWrapper sub)
    {
        if (wrapper == null)
            return;

        if (hierarchy.get(wrapper.getName()) == null)
        {
            ClassTree tree = new ClassTree(wrapper);

            if (wrapper.getSuperName() != null)
            {
                tree.getParentClasses().add(wrapper.getSuperName());

                buildHierarchy(getClassWrapper(wrapper.getSuperName()), wrapper);
            }
            if (wrapper.getInterfaces() != null)
                wrapper.getInterfaces().forEach(s ->
                {
                    tree.getParentClasses().add(s);

                    buildHierarchy(getClassWrapper(s), wrapper);
                });

//            System.out.println("built hierarchy for " + wrapper.getName());
            hierarchy.put(wrapper.getName(), tree);
        }

        if (sub != null)
            hierarchy.get(wrapper.getName()).getSubClasses().add(sub.getName());
    }

    // Made by ItzSomebody (https://github.com/ItzSomebody)
    public boolean isAssignableFrom(final String type1, final String type2)
    {
        if ("java/lang/Object".equals(type1))
            return true;
        if (type1.equals(type2))
            return true;

        getClassWrapper(type1);
        getClassWrapper(type2);

        ClassTree firstTree = getTree(type1);
        if (firstTree == null)
            throw new MissingTypeException("Could not find " + type1 + " in the built class hierarchy");

        Set<String>   allChildren = new HashSet<>();
        Deque<String> toProcess   = new ArrayDeque<>(firstTree.getSubClasses());
        while (!toProcess.isEmpty())
        {
            String s = toProcess.poll();

            if (allChildren.add(s))
            {
                getClassWrapper(s);
                ClassTree tempTree = getTree(s);
                toProcess.addAll(tempTree.getSubClasses());
            }
        }
        return allChildren.contains(type2);
    }

    public ClassWrapper getClassWrapper(String className)
    {
        if (className == null)
            return null;
        ClassWrapper wrapper = nodes.get(className);
        if (wrapper != null)
            return wrapper;
        for (ClassWrapper cw : nodes.values())
            if (cw.getOriginalName().equals(className))
                return cw;
        for (ClassWrapper cw : nodes.values())
            if (cw.getName().equals(className))
                return cw;
        return null;
    }

    public void addClassWrapper(ClassWrapper wrapper)
    {
        nodes.put(wrapper.getName(), wrapper);
    }

    public void removeClassWrapper(String className)
    {
        nodes.remove(className);
    }

}
