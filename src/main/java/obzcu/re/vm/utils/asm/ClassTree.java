package obzcu.re.vm.utils.asm;

import java.util.HashSet;
import java.util.Set;

/**
 * @name ClassTree.java
 * @author ItzSomebody
 * @url https://github.com/ItzSomebody
 */
public class ClassTree
{

    private final ClassWrapper classWrapper;
    private final Set<String> parentClasses = new HashSet<>();
    private final Set<String>  subClasses = new HashSet<>();

    /**
     * Creates a ClassTree object.
     *
     * @param classWrapper the ClassWraper attached to this ClassTree.
     */
    public ClassTree(ClassWrapper classWrapper) {
        this.classWrapper = classWrapper;
    }

    /**
     * Attached ClassWrapper.
     */
    public ClassWrapper getClassWrapper() {
        return classWrapper;
    }

    /**
     * Names of classes this represented class inherits from.
     */
    public Set<String> getParentClasses() {
        return parentClasses;
    }

    /**
     * Names of classes this represented class is inherited by.
     */
    public Set<String> getSubClasses() {
        return subClasses;
    }

}
