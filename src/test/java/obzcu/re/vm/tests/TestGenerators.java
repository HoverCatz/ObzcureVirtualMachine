package obzcu.re.vm.tests;

import obzcu.re.virtualmachine.asm.VMOpcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * @author HoverCatz
 * @created 21.01.2022
 * @url https://github.com/HoverCatz
 **/
public class TestGenerators
{

    public static byte[] generate(String which, int opcode, Object... inputs) throws Throwable
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        injectBefore(dos, which, opcode);
        switch (which)
        {
            case "VMLabelInsnNode" -> dos.writeInt((int)inputs[0]); // label index
            case "VMLineNumberInsnNode" -> dos.writeInt((int)inputs[0]); // line number
            case "VMJumpInsnNode" -> dos.writeInt((int)inputs[0]); // Jump target index
            case "VMIntInsnNode" -> dos.writeInt((int)inputs[0]); // operator
            case "VMInsnNode" -> { } // Nothing
            case "VMFieldInsnNode" -> {
                FieldInsnNode field = (FieldInsnNode) inputs[0];
                dos.writeUTF(field.owner);
                dos.writeUTF(field.name);

                dos.writeBoolean((boolean) inputs[1]); // isStatic
                dos.writeBoolean((boolean) inputs[2]); // isPut
            }
            case "VMMethodInsnNode" -> {
                MethodInsnNode invoke = (MethodInsnNode) inputs[0];
                dos.writeUTF(invoke.owner);
                dos.writeUTF(invoke.name);
                Type[] argumentTypes = Type.getArgumentTypes(invoke.desc);
                dos.writeInt(argumentTypes.length);
                for (Type type : argumentTypes)
                    dos.writeUTF(type.getInternalName().replace("/", "."));
                dos.writeUTF(Type.getReturnType(invoke.desc).getInternalName().replace("/", "."));

                if (opcode == VMOpcodes.INVOKESTATIC)
                    dos.writeInt(0);
                else if (opcode == VMOpcodes.INVOKEVIRTUAL)
                    dos.writeInt(1);
                else if (opcode == VMOpcodes.INVOKESPECIAL)
                    dos.writeInt(2);
                else if (opcode == VMOpcodes.INVOKEINTERFACE)
                    dos.writeInt(3);
            }
            case "VMTypeInsnNode" -> dos.writeUTF((String)inputs[0]); // Object type
            case "VMLdcInsnNode" -> {
                switch (inputs[0]) {
                    case String s -> {
                        dos.writeInt(0);
                        dos.writeUTF(s);
                    }
                    case Integer k -> {
                        dos.writeInt(1);
                        dos.writeInt(k);
                    }
                    case Long l -> {
                        dos.writeInt(2);
                        dos.writeLong(l);
                    }
                    case Float f -> {
                        dos.writeInt(3);
                        dos.writeFloat(f);
                    }
                    case Double d -> {
                        dos.writeInt(4);
                        dos.writeDouble(d);
                    }
                    case Type t -> {
                        dos.writeInt(5);
                        dos.writeUTF(t.getInternalName().replace("/", "."));
                    }
                    case null, default -> throw new IllegalStateException("Unexpected cst type: " + inputs[0] + " (" + inputs[0].getClass().getSimpleName() + ")");
                }
            }
            case "VMVarInsnNode" -> dos.writeInt((int) inputs[0]); // var
            case "VMIincInsnNode" -> {
                dos.writeInt((int) inputs[0]); // var
                dos.writeInt((int) inputs[1]); // incr
            }
            case "VMInvokeDynamicInsnNode" -> {
                int n = 0;
                dos.writeUTF((String) inputs[n++]); // which
                dos.writeInt((int) inputs[n++]); // which
                dos.writeUTF((String) inputs[n++]); // owner
                dos.writeUTF((String) inputs[n++]); // name
                int argsCount = (int) inputs[n++];
                dos.writeInt(argsCount);
                for (int i = 0; i < argsCount; i++)
                    dos.writeUTF((String) inputs[n++]);
                int argumentsCount = (int) inputs[n++];
                dos.writeInt(argumentsCount);
                for (int i = 0; i < argumentsCount; i++)
                    dos.writeUTF((String) inputs[n++]);
            }
            case "VMLookupSwitchInsnNode" -> { } // TODO: Later
            case "VMTableSwitchInsnNode" -> { } // TODO: Later
            case "VMFrameNode" -> { } // Ignore
            default -> throw new RuntimeException("Unexpected type: " + which);
        }
        injectAfter(dos);
        return baos.toByteArray();
    }

    private static void injectBefore(DataOutputStream dos, String which, int opcode) throws Throwable {
        dos.writeUTF("Meow");
        dos.writeUTF("SomeClassName");
        dos.writeUTF("SomeMethodName");
        dos.writeUTF("SomeMethodDesc");
        dos.writeUTF(which);
        dos.writeInt(opcode);
    }
    private static void injectAfter(DataOutputStream dos) throws Throwable {
        dos.writeUTF("Meow");
    }

}
