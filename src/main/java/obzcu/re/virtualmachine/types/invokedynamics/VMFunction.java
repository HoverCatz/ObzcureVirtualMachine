package obzcu.re.virtualmachine.types.invokedynamics;

import java.util.function.Function;

/**
 * @author HoverCatz
 * @created 17.01.2022
 * @url https://github.com/HoverCatz
 **/
public record VMFunction(Function<Object, Object> f) implements Function<Object, Object>
{

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
