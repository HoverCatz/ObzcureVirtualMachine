package obzcu.re.vm.tests.tests.custom;

import obzcu.re.virtualmachine.ObzcureVM;
import obzcu.re.vm.tests.tests.custom.dev.sim0n.evaluator.Main;
import obzcu.re.vm.tests.tests.custom.testjar.MainTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.BufferedReader;
import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.StringJoiner;

import static obzcu.re.vm.tests.VMTests.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HoverCatz
 * @created 29.01.2022
 * @url https://github.com/HoverCatz
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_CustomJars
{

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    @Test
    @Order(1)
    public void VERSION()
    {
        ObzcureVM.Duo<String, String> output = assertDoesNotThrow(() ->
        {
            File javaExecutable = getJavaExecutable();
            Process p = Runtime.getRuntime().exec(new String[] { javaExecutable.getAbsolutePath(), "-version" });
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

        print("Output", output.a);
        System.out.println();
        error("Error", output.b);

        assertTrue(output.a.isEmpty());
        assertFalse(output.b.isEmpty());
    }

    @Test
    @Order(2)
    public void TESTJAR()
    {
        System.out.println("BEFORE TRANSLATION (ORIGINAL): ");
        // BEFORE TRANSLATION
        File tempFile = processJar(MainTest.class, "/obzcu/re/vm/tests/tests/custom/testjar/", false);
        assertDoesNotThrow(() -> {
            File folder = new File("src/test/java/obzcu/re/vm/tests/tests/jarfiles/");
            if (!folder.exists()) folder.mkdirs();
            Files.copy(tempFile.toPath(), new File(folder, "testjar-before.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
        });

        ObzcureVM.Duo<String, String> output = assertDoesNotThrow(() -> executeJar(tempFile));
        print("Output", output.a);
        System.out.println();
        error("Error", output.b);
        System.out.println("\n\n\n");

        System.out.println("AFTER TRANSLATION (VM OUTPUT):");
        // AFTER TRANSLATION
        File tempFile2 = processJar(MainTest.class, "/obzcu/re/vm/tests/tests/custom/testjar/", true);
        assertDoesNotThrow(() -> {
            File folder = new File("src/test/java/obzcu/re/vm/tests/tests/jarfiles/");
            if (!folder.exists()) folder.mkdirs();
            Files.copy(tempFile2.toPath(), new File(folder, "testjar-after.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
        });

        ObzcureVM.Duo<String, String> output2 = assertDoesNotThrow(() -> executeJar(tempFile2));
        ObzcureVM.Duo<String, String> duo = fixVMExceptions(output.b, output2.b);
        print("Output", output2.a);
        System.out.println();
        error("Error (vm errors)", output2.b);
        output2.b = duo.b;
        error("Error (fixed)", output2.b);
        System.out.println();

        assertEquals(duo.a, output2.b); // Test errors first
        assertEquals(output.a, output2.a); // Then test output
    }

    @Test
    @Order(3)
    /* https://github.com/terminalsin/Evaluator */
    public void TESTJAR_Evaluator()
    {
        System.out.println("BEFORE TRANSLATION (ORIGINAL): ");
        // BEFORE TRANSLATION
        File tempFile = processJar(Main.class, "/obzcu/re/vm/tests/tests/custom/dev/sim0n/evaluator/", false);
        assertDoesNotThrow(() -> {
            File folder = new File("src/test/java/obzcu/re/vm/tests/tests/jarfiles/");
            if (!folder.exists()) folder.mkdirs();
            Files.copy(tempFile.toPath(), new File(folder, "evaluator-before.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
        });

        ObzcureVM.Duo<String, String> output = assertDoesNotThrow(() -> executeJar(tempFile));
        print("Output", output.a);
        System.out.println();
        error("Error", output.b);
        System.out.println("\n\n\n");

        System.out.println("AFTER TRANSLATION (VM OUTPUT):");
        // AFTER TRANSLATION
        File tempFile2 = processJar(Main.class, "/obzcu/re/vm/tests/tests/custom/dev/sim0n/evaluator/", true, true, true);
        assertDoesNotThrow(() -> {
            File folder = new File("src/test/java/obzcu/re/vm/tests/tests/jarfiles/");
            if (!folder.exists()) folder.mkdirs();
            Files.copy(tempFile2.toPath(), new File(folder, "evaluator-after.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
        });

        ObzcureVM.Duo<String, String> output2 = assertDoesNotThrow(() -> executeJar(tempFile2));
        ObzcureVM.Duo<String, String> duo = fixVMExceptions(output.b, output2.b);
        print("Output", output2.a);
        System.out.println();
        error("Error (vm errors)", output2.b);
        output2.b = duo.b;
        error("Error (fixed)", output2.b);
        System.out.println();

        assertEquals(duo.a, output2.b); // Test errors first
        assertEquals(output.a, output2.a); // Then test output
    }

}
