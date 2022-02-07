package obzcu.re.virtualmachine.types.invokedynamics;

/**
 * @author HoverCatz
 * @created 16.01.2022
 * @url https://github.com/HoverCatz
 **/
public record VMRunnable(Runnable r) implements Runnable
{

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
