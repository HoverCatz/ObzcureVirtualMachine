package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.VMStack;
import obzcu.re.virtualmachine.ObzcureVM;
import obzcu.re.virtualmachine.asm.VMOpcodes;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public abstract class VMNode implements VMOpcodes
{

    public int opcode, currentInput = 0;

    public VMNode(int opcode)
    {
        this.opcode = opcode;
    }

    private boolean hasExecuted = false;
    public Object[] input = null, original = null;
    public Object retValue = null;
    public boolean doReturn = false;

    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        if (hasExecuted)
            reset(vm);
        else
            hasExecuted = true;
    }

    public void reset(ObzcureVM vm)
    {
        input = original == null ? null : vm.deepClone(input, original);
        currentInput = 0; // TODO: This may be the ONLY one required
        retValue = null;
        doReturn = false;
    }

    public void returnObject(Object obj)
    {
        retValue = obj;
        doReturn = true;
    }

    public Object getNext()
    {
        return input[currentInput++];
    }

    public int getNextInt()
    {
        return (int) input[currentInput++];
    }

    public String getNextString()
    {
        return (String) input[currentInput++];
    }

    public boolean getNextBoolean()
    {
        return (boolean) input[currentInput++];
    }

}
