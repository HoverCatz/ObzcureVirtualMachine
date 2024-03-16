package obzcu.re.virtualmachine.types.invokedynamics;

import java.util.function.Consumer;

/**
 * @author HoverCatz
 * @created 16.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMConsumer implements Consumer<Object>
{

    private final Consumer<Object> c;

    public VMConsumer(Consumer<Object> c) {
        this.c = c;
    }

    @Override
    public void accept(Object o)
    {
        c.accept(o);
    }

    @Override
    public String toString()
    {
        return c.toString();
    }

}
