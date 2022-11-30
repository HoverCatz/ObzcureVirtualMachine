package obzcu.re.vm.tests.tests.custom.dev.sim0n.evaluator.test.impl.annotation;

import obzcu.re.vm.tests.tests.custom.dev.sim0n.evaluator.Main;
import obzcu.re.vm.tests.tests.custom.dev.sim0n.evaluator.test.Test;

@TestAnnotation(string = "Test", doubleValue = 0.36, intValue = 36)
public class AnnotationTest implements Test {

    @Override
    public void handle() {
        Class<? extends AnnotationTest> clazz = AnnotationTest.class;

        if (clazz.isAnnotationPresent(TestAnnotation.class)) {
            TestAnnotation annotation = clazz.getAnnotation(TestAnnotation.class);

            String value = annotation.string();
            double doubleValue = annotation.doubleValue();
            int intValue = annotation.intValue();

            Main.LOG.println("Testing annotations");
            Main.LOG.println(String.format("%s, %s, %d", value, doubleValue, intValue));
        }
    }
}
