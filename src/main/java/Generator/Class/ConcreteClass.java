package Generator.Class;

import Generator.MethodCreation;
import Generator.NameGenerator;
import Generator.Variable;
import javassist.*;

import java.util.ArrayList;

public class ConcreteClass extends Class {
    private static final boolean initializeClassFields = false;

    /*
     * Construct an concrete class
     */
    public ConcreteClass(String name, ArrayList<Class> otherClasses, ArrayList<Class> interfaces) {
        super(Modifier.PUBLIC, name, otherClasses, interfaces);
    }

    /*
     * Override how the Class will initialize this instance so it adds only concrete methods
     */
    @Override
    protected void initialize(ArrayList<Class> classes, ArrayList<Class> interfaces) {
        boolean success = addSuperClassIfNeeded(classes, configurations.inheritanceHierarchy.max);
        if (success) {
            // add interfaces and implement the methods
            if (addImplementsIfNeeded(interfaces)) {
                // failed to add implements so ignore it
            }

            // implement abstract methods from inherited abstract classes (if needed)
            try {
                CtClass superClass = currentCtClass.getSuperclass();
                if (Modifier.isAbstract(superClass.getModifiers())) {
                    implementAbstractMethods(superClass.getDeclaredMethods());
                }
            } catch (NotFoundException e) {
                // failed to find super class so ignore it
            }

            // add random amount of fields to the class
            int numFields = configurations.fields.min + randomizer.nextInt(configurations.fields.max - configurations.fields.min + 1);
            String constructorBody = "{\n";
            for (int i = 0; i < numFields; i++) {
                String fieldName = NameGenerator.Generate();
                if (randomizer.nextBoolean() || classes.size() == 0 || Modifier.isAbstract(classes.get(0).getCtClass().getModifiers()))
                    Variable.addPrimitiveField(Modifier.PRIVATE, currentCtClass, fieldName, false);
                else
                    Variable.addNonPrimitiveField(Modifier.PRIVATE, currentCtClass, fieldName, classes, initializeClassFields);
                try {
                    CtField field = currentCtClass.getField(fieldName);
                    CtClass returnType = field.getType();
                    constructorBody += "\t" + fieldName + " = " + Variable.generateLiteral(returnType) + ";\n";
                } catch (NotFoundException e) {
                    // failed to get field name
                }
            }

            // add constructor whose body just assigns values to instance variables of the class
            try {
                constructorBody += "}\n";
                CtConstructor constructor = CtNewConstructor.make(null, null, constructorBody, currentCtClass);
                methodBodies.put(constructor.getLongName(), constructorBody);
                currentCtClass.addConstructor(constructor);
            } catch (CannotCompileException e) {
                // failed to create constructor
            }

            // add random amount of concrete methods
            int numMethods = configurations.concreteMethods.min + randomizer.nextInt(configurations.concreteMethods.max - configurations.concreteMethods.min + 1);
            for (int i = 0; i < numMethods; i++) {
                String methodName = NameGenerator.Generate();
                MethodCreation.addConcreteMethod(Modifier.PUBLIC, new ArrayList<>(), methodName, currentCtClass, (CtMethod m, String body) -> {
                    methodBodies.put(m.getName(), body);
                });
            }
        }
    }

    /*
     * Override how classes are created via Javassist
     */
    @Override
    protected CtClass makeCtClass(String name) {
        return ClassPool.getDefault().makeClass(name);
    }

    /*
     * Change what toString() does so that it parses through the CtClass (bytecode) object to generate source code
     *
     * We use this for generating source code from byte code which means our source code generated isn't natively generated
     */
    public String toString() {
        // create basic class with name
        StringBuilder sb = new StringBuilder("");
        int modifiers = currentCtClass.getModifiers();
        sb.append(Modifier.toString(modifiers));
        sb.append(" class ");
        sb.append(currentCtClass.getName());
        try {
            CtClass superClass = currentCtClass.getSuperclass();
            String superClassName = superClass.getSimpleName();
            if (!superClassName.equals("Object")) {
                sb.append(" extends ");
                sb.append(superClassName);
                sb.append(" ");
            }
        } catch (NotFoundException e) {
            // failed to get super class
        }

        // parse through the interfaces
        try {
            CtClass interfaces[] = currentCtClass.getInterfaces();
            if (interfaces.length > 0)
                sb.append(" implements ");
            for (int i = 0; i < interfaces.length; i++) {
                if (i > 0)
                    sb.append(", ");
                sb.append(interfaces[i].getSimpleName());
            }
        } catch (NotFoundException e) {
            // failed to get interfaces
        }
        sb.append(" {\n");

        // parse through the fields
        CtField fields[] = currentCtClass.getDeclaredFields();
        for (CtField field : fields) {
            sb.append("\t");
            int fieldModifier = field.getModifiers();
            sb.append(Modifier.toString(fieldModifier));
            if (fieldModifier != 0)
                sb.append(" ");
            boolean isPrimitive = false;
            try {
                CtClass type = field.getType();
                isPrimitive = Variable.isPrimitive(type);
                sb.append(type.getSimpleName());
            } catch (NotFoundException e) {
                // failed to get field type
                System.out.println("Failed to get field type for " + currentCtClass.getName());
            }
            sb.append(" ");
            sb.append(field.getName());
            Object constantValue = field.getConstantValue();
            String initializerSignature = field.getSignature();
            if (constantValue != null) {
                sb.append(" = ");
                sb.append(constantValue);
            } else if (!isPrimitive && initializerSignature != null && initializeClassFields){
                sb.append(" = ");
                sb.append("new ");
                sb.append(initializerSignature.replaceFirst("L", "").replace(";", ""));
                sb.append("()");
                // TODO: Add support for multiple parameters
            }
            sb.append(";\n");
        }

        // parse through the constructor
        CtConstructor constructors[] = currentCtClass.getDeclaredConstructors();
        if (fields.length > 0 && constructors.length > 0) {
            sb.append("\n");
        }
        for (CtConstructor constructor : constructors) {
            sb.append("\t");
            int constructorModifier = constructor.getModifiers();
            sb.append(Modifier.toString(constructorModifier));
            if (constructorModifier != 0)
                sb.append(" ");
            sb.append(constructor.getName());
            sb.append("(");
            try {
                CtClass parameters[] = constructor.getParameterTypes();
                for (int i = 0; i < parameters.length; i++) {
                    if (i > 0)
                        sb.append(", ");
                    sb.append(parameters[i].getSimpleName());
                    sb.append(" var");
                    sb.append(i + 1);
                }
            } catch (NotFoundException e) {
                // failed to get list of parameters
                System.out.println("Failed to get constructor parameters for " + constructor.getLongName());
            }
            sb.append(")");
            String body = methodBodies.get(constructor.getLongName());
            if (body != null)
                sb.append(body.replace("\n", "\n\t"));
            else
                sb.append("{}");
            sb.append("\n");
        }

        // parse through the methods
        CtMethod methods[] = currentCtClass.getDeclaredMethods();
        if (constructors.length > 0 && methods.length > 0) {
            sb.append("\n");
        }
        for (int i = 0; i < methods.length; i++) {
            sb.append("\t");
            CtMethod method = methods[i];
            int methodModifiers = method.getModifiers();
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
                for (int j = 0; j < parameters.length; j++) {
                    if (j > 0)
                        sb.append(", ");
                    sb.append(parameters[j].getSimpleName());
                    sb.append(" var");
                    sb.append(j + 1);
                }
            } catch (NotFoundException e) {
                // failed to get list of parameters
                System.out.println("Failed to get method parameters for " + currentCtClass.getName());
            }
            sb.append(")");
            String body = methodBodies.get(method.getName());
            if (body != null)
                sb.append(body.replace("\n", "\n\t"));
            else
                sb.append("{}");
            if (i < methods.length - 1)
                sb.append("\n");
            else
                sb.deleteCharAt(sb.lastIndexOf("\t"));
        }
        sb.append("}\n");
        return sb.toString();
    }
}
