package obzcu.re.vm.tests.tests.custom.testjar;

/**
 * @author HoverCatz
 * @created 29.01.2022
 * @url https://github.com/HoverCatz
 **/
public class MainTest
{

    public static void main(String[] args) {
        new MainTest();
    }

    private MainTest()
    {
        doit();
    }

    private void doit()
    {
        System.out.println("Hello world from testjar's MainTest!");
        SecondClass secondClass = new SecondClass()
        {
            @Override
            public void testAbstract() throws Throwable
            {
                System.out.println("Hello world from abstract method!");
                throw new Throwable("Test exception???");
            }
        };
        try
        {
            secondClass.testThrow();
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        try
        {
            secondClass.testAbstract();
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

}
