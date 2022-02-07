package obzcu.re.vm;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author HoverCatz
 * @created 14.01.2022
 * @url https://github.com/HoverCatz
 *
 * Dump JRE class files (they are packed into a single module file since Java 9)
 **/
public class Premain
{

    /* Folder to dump JRE classes into */
    public static final File folder = new File("dump");

    /* Modules to dump */
    private static final Set<String> CONSIDERED_MODULES = Set.of(
        "java.base", "java.compiler", "java.datatransfer", "java.desktop", "java.instrument", "java.logging", "java.management", "java.management.rmi", "java.naming", "java.net.http", "java.prefs", "java.rmi", "java.scripting", "java.se", "java.security.jgss", "java.security.sasl", "java.smartcardio", "java.sql", "java.sql.rowset", "java.transaction.xa", "java.xml", "java.xml.crypto"
    );

    private static final Map<String, String> moduleClassNames = new HashMap<>();
    private static void findClasses() throws IOException {
        File root = new File(System.getProperty("java.home"));
        File lib = new File(root, "lib");
        ZipFile srczip = new ZipFile(new File(lib, "src.zip"));
        Iterator<? extends ZipEntry> itr = srczip.entries().asIterator();
        while (itr.hasNext())
            extractFromEntry(itr.next());
    }
    private static void extractFromEntry(ZipEntry entry) {
        String name = entry.getName();
        if (!name.endsWith(".java"))
            return;
        int firstSlash = name.indexOf('/');
        String moduleName = name.substring(0, firstSlash);
        if (!CONSIDERED_MODULES.contains(moduleName))
            return;
        String fullyQualifiedName = name.substring(firstSlash + 1, name.length() - 5);
        if (fullyQualifiedName.endsWith("-info"))
            return; // module-info or package-info
        moduleClassNames.put(fullyQualifiedName, moduleName);
//        System.out.println(fullyQualifiedName);
    }
    public static void premain(String args, Instrumentation ins)
    {
        // Enable this to dump all JRE classes
        boolean runInstrumentation = false;
        if (!runInstrumentation)
            return;
        try
        {
            if (!folder.exists() && !folder.mkdirs())
                throw new Throwable("Dump folder doesn't exist and failed to create it: '" + folder.getAbsolutePath() + "'");
            ins.addTransformer(new ClassFileTransformer()
            {
                private final List<String> processedClasses = new ArrayList<>();
                @Override
                public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException
                {
                    if (!className.contains("$") && !processedClasses.contains(className) &&
                        moduleClassNames.containsKey(className))
                    {
                        processedClasses.add(className);

                        String fileFolderName = className.substring(0, className.lastIndexOf('/'));
                        File fileFolder = new File(folder, fileFolderName);
                        if (!fileFolder.exists())
                            fileFolder.mkdirs();

                        String fileName = className.substring(fileFolderName.length() + 1).replace("/", ".");
                        File outputFile = new File(fileFolder, fileName + ".class");
                        if (!outputFile.exists())
                            try {
                                Files.write(outputFile.toPath(), classfileBuffer);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                        System.out.println("[" + moduleName + "] " + className + " (" + classfileBuffer.length + ")");
                    }
                    return ClassFileTransformer.super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
                }
            }, true);
            findClasses();
            List<Class<?>> classes = new ArrayList<>();
            for (String className : moduleClassNames.keySet())
                try
                {
                    classes.add(Class.forName(className.replace("/", ".")));
                }
                catch (Throwable ignored)
                {
                }
            ins.retransformClasses(classes.toArray(new Class[0]));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

}
