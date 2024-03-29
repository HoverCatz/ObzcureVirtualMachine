package obzcu.re.virtualmachine.types;

import obzcu.re.virtualmachine.VMStack;
import obzcu.re.virtualmachine.ObzcureVM;
import obzcu.re.virtualmachine.asm.VMHandle;
import obzcu.re.virtualmachine.asm.VMType;
import obzcu.re.virtualmachine.types.invokedynamics.*;


import java.lang.invoke.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;

/**
 * @author HoverCatz
 * @created 09.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMInvokeDynamicInsnNode extends VMNode
{

    public VMInvokeDynamicInsnNode(int opcode)
    {
        super(opcode);
    }

    @Override
    public void execute(ObzcureVM vm, VMStack stack) throws Throwable
    {
        super.execute(vm, stack);

        if (vm.debug) System.out.println("VMInvokeDynamicsInsnNode execute");

        MethodHandles.Lookup lookup = vm.getMethodHandlesLookup();
        if (lookup == null)
            throw new IllegalStateException("MethodHandles.Lookup not found! Can't proceed.");

        String which = getNextString();
        if (vm.debug)
            System.out.println("!!! which: " + which);
        switch (which)
        {
            case "StringConcatFactory": prepareStringConcat(vm, stack, lookup); break;
            case "Runnable": prepareRunnable(vm, stack, lookup); break;

            // Consumers
            case "Consumer": prepareConsumer(vm, stack, lookup, Object.class.getName()); break;
            case "IntConsumer": prepareConsumer(vm, stack, lookup, Integer.class.getName()); break;
            case "LongConsumer": prepareConsumer(vm, stack, lookup, Long.class.getName()); break;
            case "DoubleConsumer": prepareConsumer(vm, stack, lookup, Double.class.getName()); break;

            // Others
            case "Function": prepareFunction(vm, stack, lookup); break;
            case "Predicate": preparePredicate(vm, stack, lookup); break;
            case "Supplier": prepareSupplier(vm, stack, lookup); break;
            default: throw new RuntimeException("Invalid invokedynamics type: " + which);
        }

    }

    private void prepareStringConcat(ObzcureVM vm, VMStack stack, MethodHandles.Lookup lookup) throws Throwable
    {
        String string = getNextString();

        Object o = getNext();
        o = vm.cast(o, o.getClass(), String[].class);
        String[] argumentStrings = (String[]) o;
        Class<?>[] classes = getArgumentClasses(vm, argumentStrings);

        int index = -1, found = 0;
        while (true)
        {
            index = string.indexOf('\u0001', index);
            if (index == -1) break;
            found++;
            index++;
        }

        if (found != classes.length)
            throw new IllegalStateException("Unexpected found != classes.length. " +
                    found + " != " + classes.length);

        Object[] objects = new Object[found];
        for (int i = found - 1; i >= 0; i--)
        {
            Object pop = stack.pop();
            objects[i] = vm.cast(pop, pop.getClass(), classes[i]);
        }

        StringBuilder sb = new StringBuilder();

        int n = 0;
        for (int i = 0; i < string.length(); i++)
        {
            char c = string.charAt(i);
            if (c == '\u0001') sb.append(objects[n++]);
            else sb.append(c);
        }

        stack.push(sb.toString());
    }

    private void prepareSupplier(ObzcureVM vm, VMStack stack, MethodHandles.Lookup lookup) throws Throwable
    {
        int tag = getNextInt();
        if (vm.debug)
            System.out.println("!!! tag: " + tag);

        String owner = getNextString();
        String name = getNextString();

        String[] args = (String[]) getNext();
        Class<?>[] argsClasses = getArgumentClasses(vm, args);

        String[] arguments = (String[]) getNext();
        Class<?>[] argumentClasses = getArgumentClasses(vm, arguments);

        Class<?> clazz = vm.getClass(owner);
        if (clazz == null)
            throw new IllegalStateException("Invokedynamics owner class not found.");

//        final Method method = vm.getMethod(clazz, name, argsClasses);
//        if (method == null)
//            throw new IllegalStateException("Invokedynamics method not found.");
//
//        if (!method.trySetAccessible() && vm.debug)
//            System.err.println("Couldn't set method accessible. This may cause the method call to fail.");

        final MethodHandle mh = vm.getMethodHandle(clazz, name, argsClasses);
        if (mh == null)
            throw new IllegalStateException("Invokedynamics method not found.");

        if (vm.debug)
        {
            System.out.println("!!! stack: " + stack);
            System.out.println("!!! inputs3: " + Arrays.toString(args));
            System.out.println("!!! inputs4: " + Arrays.toString(arguments));
            System.out.println("!!! mh: " + mh);
        }

        Object[] popped = new Object[argumentClasses.length];
        for (int i = popped.length - 1; i >= 0; i--)
            popped[i] = stack.pop();

        if (vm.debug)
            System.out.println("!!! popped: " + Arrays.toString(popped));

        VMSupplier s = new VMSupplier(() ->
        {
            int argsCount = popped.length;
            try
            {
                // Supplier<o>#get returns T
                Object[] objects = new Object[argsCount + 1];
                if (popped.length > 0)
                    System.arraycopy(popped, 0, objects, 0, popped.length);
                if (vm.debug)
                {
                    System.out.println("!!! objects.length: " + objects.length);
                    System.out.println("objects: " + Arrays.stream(objects).map(o2 -> (o2 == null ? null : o2.getClass().getSimpleName())).collect(Collectors.toList()));
                }
                return mh.invokeWithArguments(objects);
            }
            catch (Throwable t)
            {
                throw new RuntimeException(t);
            }
        });
        stack.push(s);
    }

    private void preparePredicate(ObzcureVM vm, VMStack stack, MethodHandles.Lookup lookup) throws Throwable
    {
        int tag = getNextInt();
        if (vm.debug)
            System.out.println("!!! tag: " + tag);

        String owner = getNextString();
        String name = getNextString();

        String[] args = (String[]) getNext();
        Class<?>[] argsClasses = getArgumentClasses(vm, args);

        String[] arguments = (String[]) getNext();
        Class<?>[] argumentClasses = getArgumentClasses(vm, arguments);

        Class<?> clazz = vm.getClass(owner);
        if (clazz == null)
            throw new IllegalStateException("Invokedynamics owner class not found.");

//        final Method method = vm.getMethod(clazz, name, argsClasses);
//        if (method == null)
//            throw new IllegalStateException("Invokedynamics method not found.");
//
//        if (!method.trySetAccessible() && vm.debug)
//            System.err.println("Couldn't set method accessible. This may cause the method call to fail.");

        final MethodHandle mh = vm.getMethodHandle(clazz, name, argsClasses);
        if (mh == null)
            throw new IllegalStateException("Invokedynamics method not found.");

        if (vm.debug)
        {
            System.out.println("!!! stack: " + stack);
            System.out.println("!!! inputs3: " + Arrays.toString(args));
            System.out.println("!!! inputs4: " + Arrays.toString(arguments));
            System.out.println("!!! mh: " + mh);
        }

        Object[] popped = new Object[argumentClasses.length];
        for (int i = popped.length - 1; i >= 0; i--)
            popped[i] = stack.pop();

        if (vm.debug)
            System.out.println("!!! popped: " + Arrays.toString(popped));

        VMPredicate p = new VMPredicate(o ->
        {
            int argsCount = popped.length;
            try
            {
                // Predicate<o>#test returns boolean
                Object[] objects = new Object[argsCount + 1];
                objects[argsCount] = o;
                if (popped.length > 0)
                    System.arraycopy(popped, 0, objects, 0, popped.length);
                if (vm.debug)
                {
                    System.out.println("!!! objects.length: " + objects.length);
                    System.out.println("objects: " + Arrays.stream(objects).map(o2 -> (o2 == null ? null : o2.getClass().getSimpleName())).collect(Collectors.toList()));
                }
                return (boolean) mh.invokeWithArguments(objects);
            }
            catch (Throwable t)
            {
                throw new RuntimeException(t);
            }
        });
        stack.push(p);
    }

    private void prepareFunction(ObzcureVM vm, VMStack stack, MethodHandles.Lookup lookup) throws Throwable
    {
        int tag = getNextInt();
        if (vm.debug)
            System.out.println("!!! tag: " + tag);

        String owner = getNextString();
        String name = getNextString();

        String[] args = (String[]) getNext();
        Class<?>[] argsClasses = getArgumentClasses(vm, args);

        String[] arguments = (String[]) getNext();
        Class<?>[] argumentClasses = getArgumentClasses(vm, arguments);

        Class<?> clazz = vm.getClass(owner);
        if (clazz == null)
            throw new IllegalStateException("Invokedynamics owner class not found.");

//        final Method method = vm.getMethod(clazz, name, argsClasses);
//        if (method == null)
//            throw new IllegalStateException("Invokedynamics method not found.");
//
//        if (!method.trySetAccessible() && vm.debug)
//            System.err.println("Couldn't set method accessible. This may cause the method call to fail.");

        final MethodHandle mh = vm.getMethodHandle(clazz, name, argsClasses);
        if (mh == null)
            throw new IllegalStateException("Invokedynamics method not found.");

        if (vm.debug)
        {
            System.out.println("!!! stack: " + stack);
            System.out.println("!!! inputs3: " + Arrays.toString(args));
            System.out.println("!!! inputs4: " + Arrays.toString(arguments));
            System.out.println("!!! mh: " + mh);
        }

        Object[] popped = new Object[argumentClasses.length];
        for (int i = popped.length - 1; i >= 0; i--)
            popped[i] = stack.pop();

        if (vm.debug)
            System.out.println("!!! popped: " + Arrays.toString(popped));

        VMFunction f = new VMFunction(o ->
        {
            int argsCount = popped.length;
            try
            {
                // Function<T, R>#apply returns R
                Object[] objects = new Object[argsCount + 1];
                objects[argsCount] = o;
                if (popped.length > 0)
                    System.arraycopy(popped, 0, objects, 0, popped.length);
                if (vm.debug)
                {
                    System.out.println("!!! objects.length: " + objects.length);
                    System.out.println("objects: " + Arrays.stream(objects).map(o2 -> (o2 == null ? null : o2.getClass().getSimpleName())).collect(Collectors.toList()));
                }
                return mh.invokeWithArguments(objects);
            }
            catch (Throwable t)
            {
                throw new RuntimeException(t);
            }
        });
        stack.push(f);
    }

    private void prepareRunnable(ObzcureVM vm, VMStack stack, MethodHandles.Lookup lookup) throws Throwable
    {
        int tag = getNextInt();
        if (vm.debug)
            System.out.println("!!! tag: " + tag);

        String owner = getNextString();
        String name = getNextString();

        String[] args = (String[]) getNext();
        Class<?>[] argsClasses = getArgumentClasses(vm, args);

        String[] arguments = (String[]) getNext();
        if (tag == 5)
            arguments = Arrays.stream(Arrays.copyOfRange(arguments, 1, arguments.length)).toArray(String[]::new);
        Class<?>[] argumentClasses = getArgumentClasses(vm, arguments);

        Class<?> clazz = vm.getClass(owner);
        if (clazz == null)
            throw new IllegalStateException("Invokedynamics owner class not found.");

//        final Method method = vm.getMethod(clazz, name, argumentClasses);
//        if (method == null)
//            throw new IllegalStateException("Invokedynamics method not found.");
//
//        if (!method.trySetAccessible() && vm.debug)
//            System.err.println("Couldn't set method accessible. This may cause the method call to fail.");

        final MethodHandle mh = vm.getMethodHandle(clazz, name, argumentClasses);
        if (mh == null)
            throw new IllegalStateException("Invokedynamics method not found.");

        if (vm.debug)
        {
            System.out.println("!!! stack: " + stack);
            System.out.println("!!! inputs3: " + Arrays.toString(args));
            System.out.println("!!! inputs4: " + Arrays.toString(arguments));
            System.out.println("!!! mh: " + mh);
        }

        Object ref = tag == 5 ? stack.pop() : null;

        Object[] popped = new Object[argumentClasses.length];
        for (int i = popped.length - 1; i >= 0; i--)
            popped[i] = stack.pop();

        if (vm.debug)
            System.out.println("!!! popped: " + Arrays.toString(popped));

        VMRunnable r = new VMRunnable(() ->
        {
            try
            {
                // Runnable#run returns void
                if (vm.debug)
                {
                    System.out.println("!!! popped.length: " + popped.length);
                    System.out.println("popped: " + Arrays.stream(popped).map(o2 -> (o2 == null ? null : o2.getClass().getSimpleName())).collect(Collectors.toList()));
                }
                MethodHandle _mh = mh;
                if (tag == 5)
                    _mh = _mh.bindTo(ref);
                if (popped.length == 0)
                    _mh.invoke();
                else
                    _mh.invokeWithArguments(popped);
            }
            catch (Throwable t)
            {
                throw new RuntimeException(t);
            }
        });
        stack.push(r);
    }

    private void prepareConsumer(ObzcureVM vm, VMStack stack, MethodHandles.Lookup lookup, String type) throws Throwable
    {
        int tag = getNextInt();
        if (vm.debug)
            System.out.println("!!! tag: " + tag);

        String owner = getNextString();
        String name = getNextString();

        String[] args = (String[]) getNext();
        Class<?>[] argsClasses = getArgumentClasses(vm, args);

        String[] arguments = (String[]) getNext();
        Class<?>[] argumentClasses = getArgumentClasses(vm, arguments);

        Class<?> clazz = vm.getClass(owner);
        if (clazz == null)
            throw new IllegalStateException("Invokedynamics owner class not found.");

//        final Method method = vm.getMethod(clazz, name, argsClasses);
//        if (method == null)
//            throw new IllegalStateException("Invokedynamics method not found.");
//
//        if (!method.trySetAccessible() && vm.debug)
//            System.err.println("Couldn't set method accessible. This may cause the method call to fail.");

        final MethodHandle mh = vm.getMethodHandle(clazz, name, argsClasses);
        if (mh == null)
            throw new IllegalStateException("Invokedynamics method not found.");

        if (vm.debug)
        {
            System.out.println("!!! type: " + type);
            System.out.println("!!! tag: " + (tag == 5 ? "H_INVOKEVIRTUAL" : "H_INVOKESTATIC"));
            System.out.println("!!! stack: " + stack);
            System.out.println("!!! inputs3: " + Arrays.toString(args));
            System.out.println("!!! inputs4: " + Arrays.toString(arguments));
            System.out.println("!!! mh: " + mh);
        }

        Object[] popped = new Object[argumentClasses.length];
        for (int i = popped.length - 1; i >= 0; i--)
            popped[i] = stack.pop();
        if (vm.debug)
            System.out.println("!!! popped: " + Arrays.toString(popped));

        Class<?> typeClass = vm.getClass(type);

        Consumer<Object> consumer = (Consumer<Object>) arg ->
        {
            int argsCount = popped != null ? popped.length : 0;
            try
            {
                arg = vm.cast(arg, typeClass);
                // Consumer#accept returns void
                if (argsCount <= 0)
                    mh.invoke(arg);
                else
                {
                    Object[] objects = new Object[argsCount + 1];
                    objects[argsCount] = arg;
                    if (popped.length > 0)
                        System.arraycopy(popped, 0, objects, 0, popped.length);
                    if (vm.debug)
                    {
                        System.out.println("!!! objects.length: " + objects.length);
                        System.out.println("objects: " + Arrays.stream(objects).map(o2 -> (o2 == null ? null : o2.getClass().getSimpleName())).collect(Collectors.toList()));
                    }
                    mh.invokeWithArguments(objects);
                }
            }
            catch (Throwable t)
            {
                throw new RuntimeException(t);
            }
        };

        Object c;
        switch (type)
        {
            case "java.lang.Object": c = consumer; break;
            case "java.lang.Integer": c = (IntConsumer) consumer::accept; break;
            case "java.lang.Long": c = (LongConsumer) consumer::accept; break;
            case "java.lang.Double": c = (DoubleConsumer) consumer::accept; break;
            default: throw new IllegalStateException("Unexpected value: " + type);
        };

        stack.push(c);
    }

    private Object cast(Object bsmArg, Class<?> argumentType, MethodHandles.Lookup lookup, ObzcureVM vm) throws Throwable
    {
        if (bsmArg instanceof VMType && argumentType == MethodType.class)
        {
            VMType vmType = (VMType) bsmArg;
            return MethodType.methodType(vm.getClass(vmType.returnType), getArgumentClasses(vm, vmType.argumentTypes));
        }
        else
        if (bsmArg instanceof VMHandle && argumentType == MethodHandle.class)
        {
            VMHandle vmHandle = (VMHandle) bsmArg;
            if (vmHandle.tag == H_INVOKESTATIC)
            {
                Class<?> refClass = vm.getClass(vmHandle.owner);
                Class<?> returnType = vm.getClass(vmHandle.returnType);

                MethodType methodType = MethodType.methodType(returnType, getArgumentClasses(vm, vmHandle.argumentTypes));

                MethodHandle aStatic = lookup.findStatic(refClass, vmHandle.name, methodType);
                if (aStatic != null)
                    return aStatic;
            }
        }
        return bsmArg;
    }

    private Class<?>[] getArgumentClasses(ObzcureVM vm, String[] argumentTypes) throws ClassNotFoundException
    {
        Class<?>[] classes = new Class[argumentTypes.length];
        for (int i = 0; i < classes.length; i++)
            classes[i] = vm.getClass(argumentTypes[i]);
        return classes;
    }

}
