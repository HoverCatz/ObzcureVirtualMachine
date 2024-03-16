package obzcu.re.virtualmachine.types.invokedynamics;

import java.util.function.Function;

/**
 * @author HoverCatz
 * @created 17.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMFunction implements Function<Object, Object>
{

    private final Function<Object, Object> f;

    public VMFunction(Function<Object, Object> f) {
        this.f = f;
    }

    @Override
    public Object apply(Object o)
    {
        return f.apply(o);
    }

    @Override
    public String toString()
    {
        return f.toString();
    }

}
