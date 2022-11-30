package obzcu.re.virtualmachine.types.invokedynamics;

import java.util.function.Supplier;

/**
 * @author HoverCatz
 * @created 12.02.2022
 * @url https://github.com/HoverCatz
 **/
public record VMSupplier(Supplier<Object> s) implements Supplier<Object>
{

    @Override
    public Object get()
    {
        return s.get();
    }

    @Override
    public String toString()
    {
        return s.toString();
    }

}
