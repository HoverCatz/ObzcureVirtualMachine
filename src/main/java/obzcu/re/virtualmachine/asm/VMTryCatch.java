package obzcu.re.virtualmachine.asm;

/**
 * @author HoverCatz
 * @created 11.01.2022
 * @url https://github.com/HoverCatz
 **/
public record VMTryCatch(int start, int end, int handler, Class<? extends Throwable> type)
{

    public boolean isWithin(int curr)
    {
        return curr >= start && curr <= end;
    }

    @Override
    public String toString()
    {
        return "VMTryCatch{" +
                    "start=" + start +
                    ", end=" + end +
                    ", handler=" + handler +
                    ", type=" + type +
                '}';
    }

}
