package obzcu.re.virtualmachine.types.invokedynamics;

import java.util.function.Consumer;

/**
 * @author HoverCatz
 * @created 16.01.2022
 * @url https://github.com/HoverCatz
 **/
public record VMConsumer(Consumer<Object> c) implements Consumer<Object>
{

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
