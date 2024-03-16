package obzcu.re.virtualmachine.types.invokedynamics;

import java.util.function.Supplier;

/**
 * @author HoverCatz
 * @created 12.02.2022
 * @url https://github.com/HoverCatz
 **/
public class VMSupplier implements Supplier<Object>
{

    private final Supplier<Object> s;

    public VMSupplier(Supplier<Object> s) {
        this.s = s;
    }

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
