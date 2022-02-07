package obzcu.re.testjar;

public class SecondClass extends AbsClass
{

    public SecondClass(Test test)
    {
        System.out.println(test.publicField);
        System.out.println(test.getPrivateField());
        System.out.println(test.getPublicField());
        System.out.println(testNumber);
    }

    public SecondClass(Test test, int i)
    {
        this(test);
    }

    public SecondClass(Test test, int i, double v)
    {
        super(test, i, v);
    }

    public char testBoolean()
    {
        return 1;
    }

    @Override
    public void testMethod()
    {
        super.testMethod();
        System.out.println("overridden testMethod");
    }

}
