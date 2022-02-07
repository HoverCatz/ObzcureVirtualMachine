package obzcu.re.vm.tests.tests.custom.testjar;

/**
 * @author HoverCatz
 * @created 30.01.2022
 * @url https://github.com/HoverCatz
 **/
public abstract class SecondClass
{

    SecondClass()
    {
        System.out.println("Hello world from SecondClass");
    }

    public SecondClass(int i)
    {
        this();
    }

    public SecondClass(int i, double v)
    {
        this(i);
        System.out.println("Hello world from SecondClass(I, D)");
    }

    public void testThrow() throws Throwable
    {
        int a = 0;
        int b = 3 / a;
    }

    public abstract void testAbstract() throws Throwable;

}
