package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.VMStack;
import obzcu.re.virtualmachine.ObzcureVM;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMMethodInsnNode extends VMNode
{

    public VMMethodInsnNode(int opcode)
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

        int argumentCount = getNextInt();
        Class<?>[] argumentTypes = new Class<?>[argumentCount];

        for (int n = 0; n < argumentCount; n++)
            argumentTypes[n] = vm.getClass(getNextString());

        Class<?> returnType = vm.getClass(getNextString());

        final int len = argumentTypes.length;
        Object[] objects = new Object[len];
        for (int i = len - 1; i >= 0; i--)
        {
            Object pop = stack.pop();
            if (pop != null) pop = vm.cast(pop, pop.getClass(), argumentTypes[i]);
            objects[i] = pop;
        }

        int invokeType = getNextInt();
        boolean isStatic = invokeType == 0;

        // 0 = INVOKESTATIC
        // 1 = INVOKEVIRTUAL
        // 2 = INVOKESPECIAL
        // 3 = INVOKEINTERFACE

        if (!name.equals("<init>"))
        {
            Object value = isStatic ? null : stack.pop();
            if (!isStatic && value == null)
                throw new RuntimeException("Method invoke non-static, but value was null.");

//            System.out.println();
////            System.out.println("method: " + method);
//            System.out.println("value: " + value);
//            for (Object o : objects)
//                if (o == null)
//                    System.out.println("\tnull");
//                else
//                    System.out.println("\t" + (o.getClass().isPrimitive() ? o : "<object>") + " (" + o.getClass().getSimpleName() + ")");
//            System.out.println();

//            System.out.println();
//            System.out.println("VMMethodInsnNode");
//            System.out.println("invokeType: " + invokeType);
//            System.out.println("clazz: " + clazz);
//            System.out.println("name: " + name);
//            System.out.println("args: " + Arrays.toString(argumentTypes));
//            System.out.println("objects: " + Arrays.toString(objects));
//            System.out.println("returnType: " + returnType);
//            System.out.println("value: " + value);

            MethodHandle mh;

            // INVOKESPECIAL (inside extended class?)
            if (invokeType == 2)
                mh = vm.getSpecialHandle(clazz, name, returnType, argumentTypes, value.getClass());
            else
                mh = vm.getMethodHandle(clazz, name, argumentTypes);

            mh = MethodHandles.explicitCastArguments(value == null ? mh : mh.bindTo(value), MethodType.methodType(returnType, argumentTypes));

            Object invoke;
            if (value == null)
            {
                if (objects.length == 0)
                    invoke = mh.invoke();
                else {
                    invoke = mh.asFixedArity().invokeWithArguments(objects);
                }
            }
            else
            {
                if (objects.length == 0)
                    invoke = mh.invoke();
                else
                {
//                    System.out.println("mh: " + mh);
//                    System.out.println("objects.length: " + objects.length);
//                    System.out.println("objects: " + Arrays.toString(objects));
                    invoke = mh.invokeWithArguments(objects);
                }
            }

//            System.out.println("method: " + method);
//            System.out.println("mh: " + mh);
//            System.out.println();

            if (!returnType.equals(void.class))
                stack.push(invoke);

        }
        else
        {
//            System.out.println();
//            System.out.println("VMMethodInsnNode");
//            System.out.println("invokeType: " + invokeType);
//            System.out.println("clazz: " + clazz);
//            System.out.println("name: " + name);
//            System.out.println("args: " + Arrays.toString(argumentTypes));
//            System.out.println("objects: " + Arrays.toString(objects));
//            System.out.println("returnType: " + returnType);
//            System.out.println("stack: " + stack);

            Object ref = stack.pop();

//            System.out.println("ref: " + ref);

            Constructor<?> constructor = vm.getConstructor(clazz, argumentTypes);
//            Object invoke = constructor.newInstance(objects);

            // can't create enum objects with reflection

            MethodHandle mh = MethodHandles.lookup().unreflectConstructor(constructor);
            Object invoke = mh.invokeWithArguments(objects);

            stack.replace(invoke); // Replace top of stack with new instance
        }

    }

}
