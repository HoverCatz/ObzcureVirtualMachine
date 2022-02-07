package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.ObzcureVM;
import obzcu.re.virtualmachine.VMStack;

import java.util.Arrays;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMTableSwitchInsnNode extends VMNode
{

    public VMTableSwitchInsnNode(int opcode)
    {
        super(opcode);
    }

    @Override
    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        super.execute(vm, stack);
        vm.debug = false;

        if (vm.debug) System.out.println("VMTableSwitchInsnNode");

        int num = stack.popInt(vm);
        if (vm.debug) System.out.println("num: " + num);

        int min = getNextInt();
        if (vm.debug) System.out.println("min: " + min);

        int max = getNextInt();
        if (vm.debug) System.out.println("max: " + max);

        int defaultIndex = getNextInt();
        if (vm.debug) System.out.println("defaultIndex: " + defaultIndex);

        if (num < min)
        {
            if (vm.debug) System.out.println("Jump to defaultIndex (num < min)");
            if (vm.debug) System.out.println();
            vm.debug = false;

            vm.jump(defaultIndex);
            return;
        }

        // Offset max index
        max -= min;

        // Offset index
        num -= min;

        int[] labels = (int[]) getNext();
        if (vm.debug) System.out.println("labels: " + Arrays.toString(labels));
        if (vm.debug)
            for (int i = 0; i <= max; i++)
            {
                System.out.println(" [arg " + i + "] label: " + labels[i]);
            }

        for (int i = 0; i <= max; i++)
            if (i == num)
            {
                if (vm.debug) System.out.println(" [arg " + i + "] Jump to: " + labels[i]);
                if (vm.debug) System.out.println();
                vm.debug = false;

                vm.jump(labels[i]);
                return;
            }

        if (vm.debug) System.out.println("Jump to defaultIndex: " + defaultIndex);
        if (vm.debug) System.out.println();
        vm.debug = false;

        vm.jump(defaultIndex);
    }

}
