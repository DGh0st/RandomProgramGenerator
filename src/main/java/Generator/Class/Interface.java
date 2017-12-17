package Generator.Class;

import Generator.MethodCreation;
import Generator.NameGenerator;
import Generator.Variable;
import javassist.*;

import java.util.ArrayList;

public class Interface extends Class {
    /*
     * Construct an interface
     */
    public Interface(String name, ArrayList<Class> otherInterfaces) {
        super(Modifier.PUBLIC, name, otherInterfaces, otherInterfaces);
    }

    /*
     * Override how the Class will initialize this interface so that it only adds only abstract methods and constant fields
     */
    @Override
    protected void initialize(ArrayList<Class> otherInterfaces, ArrayList<Class> interfaces) {
        boolean success = addSuperClassIfNeeded(interfaces, configurations.interfaceHierarchy.max);
        if (success) {
            // add random amount of constant fields
            int numConstants = configurations.interfaceConstants.min + randomizer.nextInt(configurations.interfaceConstants.max - configurations.interfaceConstants.min + 1);
            for (int i = 0; i < numConstants; i++) {
                String variableName = NameGenerator.Generate();
                Variable.addPrimitiveField(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL, currentCtClass, variableName, true);
            }

            // add random amount of abstract methods
            int numMethods = configurations.interfaceMethods.min + randomizer.nextInt(configurations.interfaceMethods.max - configurations.interfaceMethods.min + 1);
            for (int i = 0; i < numMethods; i++) {
                String methodName = NameGenerator.Generate();
                MethodCreation.addAbstractMethod(Modifier.PUBLIC | Modifier.ABSTRACT, new ArrayList<>(), methodName, currentCtClass);
            }
        }
    }

    /*
     * Override how interfaces are created via Javassist
     */
    @Override
    protected CtClass makeCtClass(String name) {
        return ClassPool.getDefault().makeInterface(name);
    }

    /*
     * Change what toString() does so that it parses through the CtClass (bytecode) object to generate source code
     *
     * We use this for generating source code from byte code which means our source code generated isn't natively generated
     */
    public String toString() {
        // create basic interface signature with name
        StringBuilder sb = new StringBuilder("");
        int modifiers = currentCtClass.getModifiers() & ~Modifier.ABSTRACT;
        sb.append(Modifier.toString(modifiers));
        sb.append(" ");
        sb.append(currentCtClass.getName());
        try {
            CtClass interfaces[] = currentCtClass.getInterfaces();
            if (interfaces.length > 0)
                sb.append(" extends ");
            for (int i = 0; i < interfaces.length; i++) {
                if (i > 0)
                    sb.append(", ");
                sb.append(interfaces[i].getSimpleName());
            }
        } catch (NotFoundException e) {
            // failed to get super class
        }
        sb.append(" {\n");

        // parse through the fields
        CtField fields[] = currentCtClass.getDeclaredFields();
        for (CtField field : fields) {
            sb.append("\t");
            int fieldModifier = field.getModifiers() & ~Modifier.PUBLIC & ~Modifier.STATIC & ~Modifier.FINAL;
            sb.append(Modifier.toString(fieldModifier));
            if (fieldModifier != 0)
                sb.append(" ");
            String constantSuffix = "";
            try {
                CtClass fieldType = field.getType();
                sb.append(fieldType.getSimpleName());
                if (fieldType.equals(CtPrimitiveType.longType))
                    constantSuffix = "L";
                else if (fieldType.equals(CtPrimitiveType.doubleType))
                    constantSuffix = "d";
                else if (fieldType.equals(CtPrimitiveType.floatType))
                    constantSuffix = "f";
            } catch (NotFoundException e) {
                // failed to get field type
                System.out.println("Failed to get field type for " + currentCtClass.getName());
            }
            sb.append(" ");
            sb.append(field.getName());
            sb.append(" = ");
            sb.append(field.getConstantValue());
            sb.append(constantSuffix);
            sb.append(";\n");
        }
        if (fields.length > 0)
            sb.append("\n");

        // parse through the methods
        CtMethod methods[] = currentCtClass.getDeclaredMethods();
        for (CtMethod method : methods) {
            sb.append("\t");
            int methodModifiers = method.getModifiers() & ~Modifier.PUBLIC & ~Modifier.ABSTRACT;
            sb.append(Modifier.toString(methodModifiers));
            if (methodModifiers != 0)
                sb.append(" ");
            try {
                sb.append(method.getReturnType().getSimpleName());
            } catch (NotFoundException e) {
                // failed to get method return type
                System.out.println("Failed to get method return type for " + currentCtClass.getName());
            }
            sb.append(" ");
            sb.append(method.getName());
            sb.append("(");
            try {
                CtClass parameters[] = method.getParameterTypes();
                for (int i = 0; i < parameters.length; i++) {
                    if (i > 0)
                        sb.append(", ");
                    sb.append(parameters[i].getSimpleName());
                    sb.append(" var");
                    sb.append(i + 1);
                }
            } catch (NotFoundException e) {
                // failed to get list of parameters
                System.out.println("Failed to get method parameters for " + currentCtClass.getName());
            }
            sb.append(");\n");
        }
        sb.append("}\n");
        return sb.toString();
    }
}
