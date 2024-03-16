package obzcu.re.virtualmachine.types.invokedynamics;

import java.util.function.Predicate;

/**
 * @author HoverCatz
 * @created 19.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMPredicate implements Predicate<Object>
{

    private final Predicate<Object> p;

    public VMPredicate(Predicate<Object> p) {
        this.p = p;
    }

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
