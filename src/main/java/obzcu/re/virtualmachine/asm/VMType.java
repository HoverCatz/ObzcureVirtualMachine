package obzcu.re.virtualmachine.asm;

import java.util.Arrays;

/**
 * @author HoverCatz
 * @created 10.01.2022
 * @url https://github.com/HoverCatz
 **/
public record VMType(String returnType, String[] argumentTypes)
{

    @Override
    public String toString() {
        return "VMType{" +
                "returnType='" + returnType + '\'' +
                ", argumentTypes=" + Arrays.toString(argumentTypes) +
                '}';
    }
}
