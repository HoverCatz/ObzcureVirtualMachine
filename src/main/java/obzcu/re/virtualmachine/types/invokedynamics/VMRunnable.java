package obzcu.re.virtualmachine.types.invokedynamics;

/**
 * @author HoverCatz
 * @created 16.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMRunnable implements Runnable
{

    private final Runnable r;

    public VMRunnable(Runnable r) {
        this.r = r;
    }

    @Override
    public void run()
    {
        r.run();
    }

    @Override
    public String toString()
    {
        return r.toString();
    }

}
