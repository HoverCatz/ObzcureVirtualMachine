package obzcu.re.virtualmachine;

import java.util.Arrays;
import java.util.Stack;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMStack extends Stack<Object>
{

    private final int maxStack;

    public VMStack(int maxStack)
    {
        this.maxStack = maxStack;
    }

    @Override
    public synchronized boolean add(Object o)
    {
        if (elementCount == maxStack)
            throw new RuntimeException("Stack is full!");
        return super.add(o);
    }

    public void pop2()
    {
        pop();
        pop();
    }

    public void replace(Object obj)
    {
        pop();
        push(obj);
    }

    @Override
    public String toString()
    {
        Object[] objects = Arrays.copyOf(elementData, elementCount);
        for (int i = 0; i < objects.length; i++)
            if (objects[i] == null)
                objects[i] = "<null>";
            else
                objects[i] = "<" + objects[i].getClass().getSimpleName() + ">";
        return Arrays.toString(objects);
    }

    public int popInt(ObzcureVM vm)
    {
        Object pop = pop();
        return (int) vm.cast(pop, pop.getClass(), Integer.TYPE);
    }

    public byte popByte(ObzcureVM vm)
    {
        Object pop = pop();
        return (byte) vm.cast(pop, pop.getClass(), Byte.TYPE);
    }

    public char popChar(ObzcureVM vm)
    {
        Object pop = pop();
        return (char) vm.cast(pop, pop.getClass(), Character.TYPE);
    }

    public short popShort(ObzcureVM vm)
    {
        Object pop = pop();
        return (short) vm.cast(pop, pop.getClass(), Short.TYPE);
    }

    public double popDouble(ObzcureVM vm)
    {
        Object pop = pop();
        return (double) vm.cast(pop, pop.getClass(), Double.TYPE);
    }

    public float popFloat(ObzcureVM vm)
    {
        Object pop = pop();
        return (float) vm.cast(pop, pop.getClass(), Float.TYPE);
    }

    public long popLong(ObzcureVM vm)
    {
        Object pop = pop();
        return (long) vm.cast(pop, pop.getClass(), Long.TYPE);
    }

}
