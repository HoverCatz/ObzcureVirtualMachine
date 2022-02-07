package obzcu.re.virtualmachine.asm;

import java.util.Arrays;

/**
 * @author HoverCatz
 * @created 10.01.2022
 * @url https://github.com/HoverCatz
 **/
public record VMHandle(int tag, String owner, String name, String returnType, String[] argumentTypes) {

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
