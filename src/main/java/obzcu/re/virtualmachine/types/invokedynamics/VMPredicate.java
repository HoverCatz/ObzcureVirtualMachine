package obzcu.re.virtualmachine.types.invokedynamics;

import java.util.function.Predicate;

/**
 * @author HoverCatz
 * @created 19.01.2022
 * @url https://github.com/HoverCatz
 **/
public record VMPredicate(Predicate<Object> p) implements Predicate<Object>
{

    @Override
    public boolean test(Object o)
    {
        return p.test(o);
    }

    @Override
    public String toString()
    {
        return p.toString();
    }

}
