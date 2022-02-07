package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.VMStack;
import obzcu.re.virtualmachine.ObzcureVM;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMFieldInsnNode extends VMNode
{

    public VMFieldInsnNode(int opcode)
    {
        super(opcode);
    }

    @Override
    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        super.execute(vm, stack);

        String owner = getNextString();
        Class<?> clazz = vm.getClass(owner);

        String name = getNextString();
//        Field field = vm.getField(clazz, name);

        boolean isStatic = getNextBoolean();
        boolean isPut = getNextBoolean();

        ObzcureVM.Duo<Field, MethodHandle> field =
                isPut ?
                vm.getFieldSetter(clazz, name) :
                vm.getFieldGetter(clazz, name);

//        System.out.println();
//        System.out.println("VMFieldInsnNode");
//        System.out.println("isPut: " + isPut);
//        System.out.println("clazz: " + clazz);
//        System.out.println("field.k: " + field.k);
//        System.out.println("field.t: " + field.t);

        if (isPut)
        {
            Object value = stack.pop();
            Object ref = isStatic ? null : stack.pop();

            int modifiers = field.a.getModifiers();
            if (Modifier.isFinal(modifiers))
                throw new RuntimeException("Can't set a final field through reflection.");

            if (value != null)
                value = vm.cast(value, value.getClass(), field.a.getType());

            MethodHandle fieldSetter = field.b;
            if (ref == null)
                fieldSetter
                        .invoke(value);
            else
                fieldSetter
                        .bindTo(ref)
                        .invoke(value);
        }
        else
        {

            Object ref = isStatic ? null : stack.pop();
            Object invoke;

            MethodHandle fieldGetter = field.b;
            if (ref == null)
                invoke = fieldGetter
                        .invoke();
            else
                invoke = fieldGetter
                        .bindTo(ref)
                        .invoke();
            stack.push(invoke);
        }

    }

}
