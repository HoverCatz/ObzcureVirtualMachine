package obzcu.re.testjar;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Test
{

    private static final boolean field1 = new Random().nextBoolean();
    private static final boolean field2 = new Random().nextBoolean();
    private final boolean field3 = new Random().nextBoolean();
    private static final boolean field4 = new Random().nextBoolean();

    static int test1 = 3;

    static int i = 0;
    static int a = 3;

    static int classCounter = 0;

    public boolean publicField = true;
    private boolean privateField = false;
    protected boolean protectedField = true;

    public boolean getPublicField()
    {
        return publicField;
    }
    public boolean getPrivateField()
    {
        return privateField;
    }

    private static void testPrivateMethod()
    {
        System.out.println("Hello world from private method");
    }

    public void testVirtualConsumer(String in)
    {
        System.out.println("in: " + in);
    }

    public String testVirtualFunction(String in)
    {
        System.out.println("in: " + in);
        return in + "!";
    }

    public boolean testVirtualPredicate(String in)
    {
        System.out.println("in: " + in);
        return true;
    }

    private void testVirtualThread()
    {
        System.out.println("Hello world 1");
    }

    private void testVirtualRunnable()
    {
        System.out.println("Hello world 5");
    }

    public Test(int i)
    {
        this();
    }

    static int k;
    public Test(int i, double d)
    {
        this(k = i);
    }

    static int getTestInt()
    {
        return 1337;
    }

    static String testVarargs(String format, String... args)
    {
        return new Formatter().format(format, args).toString();
    }

    public static void main(String[] args) throws Throwable
    {

        // TODO: Unsupported returnType: java/awt/event/ActionListener

        // TODO: Unsupported returnType: I
        //       Error: Invokedynamic instruction has unsupported arguments in: obzcu/re/testjar/Test$TestLineNumberNode hashCode()I

        // TODO: Error: Invokedynamic instruction has unsupported arguments in: obzcu/re/testjar/Test$TestLineNumberNode toString()Ljava/lang/String;

        // TODO: Unsupported returnType: Z
        //       Error: Invokedynamic instruction has unsupported arguments in: obzcu/re/testjar/Test$TestLineNumberNode equals(Ljava/lang/Object;)Z

        System.out.println(String.format("Today's date is %s", LocalDate.now()));

//        AbsClass absClass = new SecondClass(new Test());
//        System.out.println(((SecondClass) absClass).testBoolean());
//
//        System.out.println(testVarargs("Test %s", "string"));
//
//        Consumer<Object> objectConsumer = o -> System.out.println(o);
//        objectConsumer.accept("Hello world");
//
//        Consumer<Object> objectConsumer2 = System.out::println;
//        objectConsumer2.accept("Hello world 2");
//
//        IntStream intStream = IntStream.range(1, 10);
//        intStream.forEach(System.out::print);
//        IntStream.range(1, 10).forEach(i -> System.out.print(i));
//
//        System.out.println();
//
//        LongStream longStream = LongStream.range(1, 10);
//        longStream.forEach(System.out::print);
//        LongStream.range(1, 10).forEach(l -> System.out.print(l));
//
//        System.out.println();
//
//        DoubleStream doubleStream = IntStream.range(1, 10).asDoubleStream();
//        doubleStream.forEach(System.out::print);
//        IntStream.range(1, 10).asDoubleStream().forEach(i -> System.out.print(i));
//
//        Supplier<String> s = () -> "Hello world";
//        System.out.println(s.get());
//
//        Test t = new Test(getTestInt(), 2D);
//        System.out.println(k);
//
//        new SecondClass(t, getTestInt(), 2D);
//
//        Test t = new Test();
//
//        Thread test = new Thread(t::testVirtualThread);
//        test.start();
//
//        Consumer<String> test1 = t::testVirtualConsumer;
//        test1.accept("Hello world 2");
//
//        Function<String, String> test2 = t::testVirtualFunction;
//        test2.apply("Hello world 3");
//
//        Predicate<String> test3 = t::testVirtualPredicate;
//        test3.and(t::testVirtualPredicate);
//        test3.test("Hello world 4");
//
//        Runnable test4 = t::testVirtualRunnable;
//        test4.run();

        Test test = new Test();

        testPrivateMethod();

        new SecondClass(test).testMethod();
//
//        test.publicField = true;
//        test.privateField = false;
//        test.protectedField = true;
//        System.out.println(test.publicField);
//        System.out.println(test.privateField);
//        System.out.println(test.protectedField);
//
//        // Boolean is actually an INTEGER (a number) in bytecode
//        // And we can't set a boolean field to a number value
//        // so we need to cast it! (int -> boolean) before setting the field
//        test.protectedTest = true;
//
//        test.protectedChar = 'a';
//        test.protectedShort = 6;
//        test.protectedShort = (short)6;
//        test.protectedByte = 3;
//        test.protectedByte = (byte)3;
//
//        System.out.println(test.protectedTest);
//        System.out.println(test.protectedChar);
//        System.out.println(test.protectedShort);
//        System.out.println(test.protectedByte);
//
//        System.out.println(correctCaller());
//
//        System.out.println(lmao());
//
//        System.out.println(Arrays.toString(testRetArray("D")));
//        for (byte b = 0; b < 19; b++)
//        {
//            System.out.print(b + ": ");
//            switch (b)
//            {
//                case 3 -> System.out.println("3");
//                case 4 -> System.out.println("4");
//                case 5 -> System.out.println("5");
//                case 6 -> System.out.println("6");
//                case 7 -> System.out.println("7");
//                case 8 -> System.out.println("8");
//                case 16 -> System.out.println("16");
//                case 15 -> System.out.println("15");
//                case 17 -> System.out.println("17");
//                default -> System.out.println("default");
//            }
//        }
//
//        byte[] bytes = { 3, 4, 5, 6, 7, 8, 16, 15, 17 };
//        int offset = 1;
//
//        System.out.print("offset: ");
//        System.out.println(Byte.valueOf(bytes[offset - 1]));
//
//        switch (bytes[offset - 1])
//        {
//            case 3 -> System.out.println("3");
//            case 4 -> System.out.println("4");
//            case 5 -> System.out.println("5");
//            case 6 -> System.out.println("6");
//            case 7 -> System.out.println("7");
//            case 8 -> System.out.println("8");
//            case 16 -> System.out.println("16");
//            case 15 -> System.out.println("15");
//            case 17 -> System.out.println("17");
//        }
//
//        a();
//        try
//        {
//            b();
//            int a = 3;
//            int b = 0;
//            System.out.println(a / b);
//            c();
//        }
//        catch (Throwable t2)
//        {
//            System.out.println(t2.getMessage());
//            d();
//        }
//        finally
//        {
//            System.out.println("Finally!");
//            e();
//        }
//
//        {
//            int a = 1;
//            boolean b = true;
//            double c = 69.1337d;
//            byte d = 5;
//            String abc2 = a + " hello " + b + " world" + c + " " + d;
//            System.out.println(abc2);
//        }
//
//        abc(1);
//        new Test().test();
//
//        long time = System.currentTimeMillis();
//        process(new File("C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\test.jar"));
//        System.out.println("That took " + readable(System.currentTimeMillis() - time));
//
//        Class<?> clazz = Class.forName("java.lang.System");
//        Field out = clazz.getField("out");
//        Object printStream = out.get(null);
//
//        Class<?> printStreamClazz = printStream.getClass();
//        Method println = printStreamClazz.getMethod("println", String.class);
//
//        println.invoke(printStream, "Hello world :)");
//
//        testDouble(1, 3D, 5F, 7L, true, (short)9, (byte)12, (char)15, ":)", new Object());
//        System.out.println("before try");
//
//        try
//        {
//            System.out.println("before testException try");
//            try
//            {
//                System.out.println("before testException throw");
//                try
//                {
//                    throw new Throwable("throw new Throwable! >:o");
//                }
//                catch (Exception e)
//                {
//                    System.out.println("inside testException handler (2), before printStackTrace");
//                    e.printStackTrace();
//                    System.out.println("inside testException handler (2), after printStackTrace");
//                    throw new TestException(e.getMessage());
//                }
//            }
//            catch (TestException t2)
//            {
//                System.out.println("inside testException handler, before printStackTrace");
//                t2.printStackTrace();
//                System.out.println("inside testException handler, after printStackTrace");
//            }
//            System.out.println("after testException try");
//            System.out.println("before math");
//            System.out.println(a / i);
//            System.out.println("after math");
//        }
//        catch (Throwable t2)
//        {
//            System.out.println("inside handler, before printStackTrace");
//            t2.printStackTrace();
//            System.out.println("inside handler, after printStackTrace");
//        }
//        System.out.println("after try");
//
//        Test thisTest = new Test();
//        thisTest.add(1, 2);
//        thisTest.sub(1, 2);
//        thisTest.multiply(1, 2);
//        thisTest.divide(1, 2);
//
//        thisTest.add(1L, 2L);
//        thisTest.sub(1L, 2L);
//        thisTest.multiply(1L, 2L);
//        thisTest.divide(1L, 2L);
//
//        thisTest.add(1f, 2f);
//        thisTest.sub(1f, 2f);
//        thisTest.multiply(1f, 2f);
//        thisTest.divide(1f, 2f);
//
//        thisTest.add(1D, 2D);
//        thisTest.sub(1D, 2D);
//        thisTest.multiply(1D, 2D);
//        thisTest.divide(1D, 2D);
//
//        thisTest.add(1f, 2f);
    }

    public static SecondClass lmao()
    {
        SecondClass second = new SecondClass(new Test());
        return second.testBoolean() == 1 ? second : null;
    }

    private static boolean correctCaller()
    {
        return "a".equals("b");
    }

    protected boolean protectedTest;
    protected char protectedChar;
    protected short protectedShort;
    protected byte protectedByte;

    private static void process(final File jarFile) throws Throwable {
//        System.out.println(jarFile);
        final ZipFile zipFile = new ZipFile(jarFile);
//        System.out.println(zipFile);
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();
//        System.out.println(entries);
        try {
            while (entries.hasMoreElements()) {
//                System.out.println("hasMoreElements");
                final ZipEntry entry = entries.nextElement();
//                System.out.println(entry);
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
//                    System.out.println("not dir and ends with .class: " + entry.getName());
                    try (final InputStream in = zipFile.getInputStream(entry)) {
//                        System.out.println(in);
                        int m = 69;
                        int f = 1337;
//                        System.out.println("Done. Methods = " + m + ", Fields = " + f);
                    }
                    ++Test.classCounter;
                }
                else
                {
//                    System.out.println("is dir, or doesn't end with .class: " + entry.getName());
                }
            }
//            System.out.println("done with entries loop");
        }
        finally {
//            System.out.println("finally");
        }
        System.out.println("Found " + classCounter + " classes");
    }

    static void a() {
        System.out.println("a");
    }
    static void b() {
        System.out.println("b");
    }
    static void c() {
        System.out.println("c");
    }
    static void d() {
        System.out.println("d");
    }
    static void e() {
        System.out.println("e");
    }

    static String[] testRetArray(String D)
    {
        return new String[] { "A", "B", "C", D };
    }

    private static void abc(int i)
    {
        System.out.println(i);
        if (i == 1)
        {
            System.out.println("first!");
        }
        if (i == 5)
        {
            System.out.println("half way");
            abc(i + 1);
        }
        if (i < 10)
            abc(i + 1);
        else
            System.out.println("last print!");
    }

    List<TestLineNumberNode> instructions = new ArrayList<>();

    void test()
    {
        visitLineNumber(69, new Object());
//        System.out.println("size: " + instructions.size());
    }
    public void visitLineNumber(final int line, final Object start) {
        instructions.add(new TestLineNumberNode(line, start));
    }
    record TestLineNumberNode(int line, Object label) { }

    static String testString()
    {
        StringBuilder sb = new StringBuilder("a");
        sb.append('b');
        sb.append("c");
        return sb.toString();
    }

    private static String readable(long time)
    {
        int secs = (int) (time / 1000) % 60 ;
        int mins = (int) ((time / (1000*60)) % 60);
        int hours = (int) ((time / (1000*60*60)) % 24);
        int days = (int) ((time / (1000*60*60*24)) % 24);
        String result = (days  > 0 ? (days  + "d, ") : "") +
                (hours > 0 ? (hours + "h, ") : "") +
                (mins  > 0 ? (mins  + "m, ") : "") +
                (secs  > 0 ? (secs  + "s")   : "");
        if (result.endsWith(", "))
        {
            if ((days > 0 || hours > 0 || mins > 0) && secs <= 0)
                result += time + "ms";
            else
                result = result.substring(0, result.length() - 2);
        }
        return result.isEmpty() ? time + "ms" : result;
    }

    static void testDouble(int i, double d, float f, long l, boolean z, short s, byte b, char c, String s1, Object o)
    {
        System.out.println(i);
        System.out.println(d);
        System.out.println(f);
        System.out.println(l);
        System.out.println(z);
        System.out.println(s);
        System.out.println(b);
        System.out.println(c);
        System.out.println(s1);
        System.out.println(o);
    }

    public String main2(String[] args)
    {
        if (field2)
            return "Got test2 :)";
        else
        {
            System.out.println(":o");
            return "No Test2 :(";
        }
    }

    private Test()
    {
        System.out.println("Hello world :) owo");
    }

    int add(int i, int n) {
        return i + n;
    }
    int sub(int i, int n) {
        return i - n;
    }
    int multiply(int i, int n) {
        return i * n;
    }
    int divide(int i, int n) {
        return i / n;
    }

    float add(float i, float n) {
        return i + n;
    }
    float sub(float i, float n) {
        return i - n;
    }
    float multiply(float i, float n) {
        return i * n;
    }
    float divide(float i, float n) {
        return i / n;
    }

    long add(long i, long n) {
        return i + n;
    }
    long sub(long i, long n) {
        return i - n;
    }
    long multiply(long i, long n) {
        return i * n;
    }
    long divide(long i, long n) {
        return i / n;
    }

    double add(double i, double n) {
        return i + n;
    }
    double sub(double i, double n) {
        return i - n;
    }
    double multiply(double i, double n) {
        return i * n;
    }
    double divide(double i, double n) {
        return i / n;
    }

}
