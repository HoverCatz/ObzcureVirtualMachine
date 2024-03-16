package obzcu.re.virtualmachine.asm;

import java.util.Arrays;

/**
 * @author HoverCatz
 * @created 10.01.2022
 * @url https://github.com/HoverCatz
 **/
public class VMType {

    public final String returnType;
    public final String[] argumentTypes;

    public VMType(String returnType, String[] argumentTypes) {
        this.returnType = returnType;
        this.argumentTypes = argumentTypes;
    }

    @Override
    public String toString() {
        return "VMType{" +
                "returnType='" + returnType + '\'' +
                ", argumentTypes=" + Arrays.toString(argumentTypes) +
                '}';
    }
}
