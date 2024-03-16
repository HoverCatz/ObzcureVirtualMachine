package obzcu.re.virtualmachine.asm;

/**
 * @author HoverCatz
 * @created 11.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMTryCatch {

    public final int start;
    public final int end;
    public final int handler;
    public final Class<? extends Throwable> type;

    public VMTryCatch (int start, int end, int handler, Class<? extends Throwable> type) {
        this.start = start;
        this.end = end;
        this.handler = handler;
        this.type = type;
    }

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
