package obzcu.re.testjar;

public abstract class AbsClass
{

    int testNumber = 0;

    public AbsClass() {

    }

    public AbsClass(int i)
    {
        this();
    }

    public AbsClass(int i, double v)
    {
        this(i);
    }

    void testMethod()
    {
        System.out.println("default testMethod");
    }

    public AbsClass(Test test, int i, double v)
    {
        this(i, v);
    }

}
