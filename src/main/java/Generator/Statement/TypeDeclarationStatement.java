package Generator.Statement;

import javassist.CtClass;

/**
 * Randomly picking a new primitive type and getting a Type Declaration Statement
 */
public class TypeDeclarationStatement extends Statement {
    /**
     *
     * @param type takes a randomly picked type
     * @param name takes a randomly generated name
     */
    public TypeDeclarationStatement(CtClass type, String name) {
        super();
        currentLine = 1;

        setStatement(type.getName() + " " + name + ";");
    }

    public TypeDeclarationStatement(CtClass type, String name, String initialValue) {
        super();
        currentLine = 1;

        setStatement(type.getName() + " " + name + " = " + initialValue + ";");
    }

    public TypeDeclarationStatement(CtClass type, String name, int arraySize) {
        super();
        currentLine = 1;

        setStatement(type.getName() + " " + name + "[] = new " + type.getName() + "[" + arraySize + "];");
    }
}
