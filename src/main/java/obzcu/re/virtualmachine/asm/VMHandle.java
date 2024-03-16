package obzcu.re.virtualmachine.asm;

import java.util.Arrays;

/**
 * @author HoverCatz
 * @created 10.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMHandle {

    public final int tag;
    public final String owner;
    public final String name;
    public final String returnType;
    public final String[] argumentTypes;

    public VMHandle(int tag, String owner, String name, String returnType, String[] argumentTypes) {
        this.tag = tag;
        this.owner = owner;
        this.name = name;
        this.returnType = returnType;
        this.argumentTypes = argumentTypes;
    }

    @Override
    public String toString() {
        return "VMHandle{" +
                "tag=" + tag +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", argumentTypes=" + Arrays.toString(argumentTypes) +
                '}';
    }
}
