package Generator;

import javassist.*;

import Generator.Class.Class;
import java.util.ArrayList;
import java.util.Random;

public class Variable {
    private final static CtClass primitives[] = {
            CtPrimitiveType.booleanType,
            CtPrimitiveType.shortType,
            CtPrimitiveType.longType,
            CtPrimitiveType.intType,
            CtPrimitiveType.floatType,
            CtPrimitiveType.doubleType,
            CtPrimitiveType.charType,
            CtPrimitiveType.byteType
    };

    /*
     * Add primitive field to class with modifiers and name (initialize to a value if specified)
     */
    public static void addPrimitiveField(int additionalModifiers, CtClass currentClass, String variableName, boolean initalize) {
        try {
            CtField field;
            Random randomizer = new Random();
            int primitiveType = randomizer.nextInt(8);
            switch (primitiveType) {
                case 0: // boolean
                    field = CtField.make(generateVariableLine(CtPrimitiveType.booleanType, variableName), currentClass);
                    field.setModifiers(additionalModifiers);
                    if (initalize)
                        currentClass.addField(field, CtField.Initializer.constant(randomizer.nextBoolean()));
                    else
                        currentClass.addField(field);
                    break;
                case 1: // byte
                    byte[] bytes = new byte[10];
                    randomizer.nextBytes(bytes);
                    field = CtField.make(generateVariableLine(CtPrimitiveType.byteType, variableName), currentClass);
                    field.setModifiers(additionalModifiers);
                    if (initalize)
                        currentClass.addField(field, CtField.Initializer.constant(bytes[randomizer.nextInt(bytes.length)]));
                    else
                        currentClass.addField(field);
                    break;
                case 2: // char
                    field = CtField.make(generateVariableLine(CtPrimitiveType.charType, variableName), currentClass);
                    field.setModifiers(additionalModifiers);
                    char offset = 'a'; // lower case
                    if (randomizer.nextBoolean()) // upper case
                        offset = 'A';
                    if (initalize)
                        currentClass.addField(field, CtField.Initializer.constant(randomizer.nextInt(26) + offset));
                    else
                        currentClass.addField(field);
                    break;
                case 3: // double
                    field = CtField.make(generateVariableLine(CtPrimitiveType.doubleType, variableName), currentClass);
                    field.setModifiers(additionalModifiers);
                    if (initalize)
                        currentClass.addField(field, CtField.Initializer.constant(randomizer.nextDouble()));
                    else
                        currentClass.addField(field);
                    break;
                case 4: // float
                    field = CtField.make(generateVariableLine(CtPrimitiveType.floatType, variableName), currentClass);
                    field.setModifiers(additionalModifiers);
                    if (initalize)
                        currentClass.addField(field, CtField.Initializer.constant(randomizer.nextFloat()));
                    else
                        currentClass.addField(field);
                    break;
                case 5: // int
                    field = CtField.make(generateVariableLine(CtPrimitiveType.intType, variableName), currentClass);
                    field.setModifiers(additionalModifiers);
                    if (initalize)
                        currentClass.addField(field, CtField.Initializer.constant(randomizer.nextInt()));
                    else
                        currentClass.addField(field);
                    break;
                case 6: // long
                    field = CtField.make(generateVariableLine(CtPrimitiveType.longType, variableName), currentClass);
                    field.setModifiers(additionalModifiers);
                    if (initalize)
                        currentClass.addField(field, CtField.Initializer.constant(randomizer.nextLong()));
                    else
                        currentClass.addField(field);
                    break;
                case 7: // short
                    field = CtField.make(generateVariableLine(CtPrimitiveType.shortType, variableName), currentClass);
                    field.setModifiers(additionalModifiers);
                    if (initalize)
                        currentClass.addField(field, CtField.Initializer.constant(randomizer.nextInt(Short.MAX_VALUE)));
                    else
                        currentClass.addField(field);
                    break;
                default: // boolean
                    field = CtField.make(generateVariableLine(CtPrimitiveType.booleanType, variableName), currentClass);
                    field.setModifiers(additionalModifiers);
                    if (initalize)
                        currentClass.addField(field, CtField.Initializer.constant(randomizer.nextBoolean()));
                    else
                        currentClass.addField(field);
                    break;
            }
        } catch (CannotCompileException e) {
            // Failed to make field for class
        }
    }

    /*
     * add a non primitive field to class and initialize if needed
     */
    public static void addNonPrimitiveField(int additionalModifiers, CtClass currentClass, String variableName, ArrayList<Class> types, boolean initialize) {
        try {
            Random randomizer = new Random();
            int type = randomizer.nextInt(types.size());
            CtClass randomType = types.get(type).getCtClass();
            if (randomType != null) {
                if (randomType.subclassOf(currentClass) || randomType.subtypeOf(currentClass))
                    return; // skip
                CtConstructor[] constructors = randomType.getDeclaredConstructors();
                // create initialize expression
                String initialValue = "new " + randomType.getName() + "(";
                String parameterStrings[];
                if (constructors.length == 0) {
                    initialValue += ")";
                    parameterStrings = new String[] {};
                } else {
                    int randomConstructor = randomizer.nextInt(constructors.length);
                    CtConstructor constructor = constructors[randomConstructor];
                    CtClass parameters[] = constructor.getParameterTypes();
                    parameterStrings = new String[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        if (i > 0)
                            initialValue += ", ";
                        parameterStrings[i] = generateLiteral(parameters[i]);
                        initialValue += parameterStrings[i];
                    }
                    initialValue += ")";
                }
                String variableLine;
                if (initialize)
                    variableLine = generateVariableLine(randomType, variableName, initialValue);
                else
                    variableLine = generateVariableLine(randomType, variableName);
                // create and add field to class
                CtField field = CtField.make(variableLine, currentClass);
                field.setModifiers(additionalModifiers);
                currentClass.addField(field, CtField.Initializer.byNew(randomType, parameterStrings));
            }
        } catch (CannotCompileException | NotFoundException e) {
            // failed to make field for class
        }
    }

    /*
     * generate a random literal for specified type
     */
    public static String generateLiteral(CtClass literalType) {
        Random randomizer = new Random();
        if (literalType.equals(CtPrimitiveType.voidType)) {
            return null;
        } else if (literalType.equals(CtPrimitiveType.booleanType)) {
            return randomizer.nextBoolean() ? "true" : "false";
        } else if (literalType.equals(CtPrimitiveType.byteType)) {
            byte[] bytes = new byte[10];
            randomizer.nextBytes(bytes);
            return "((byte) " + bytes[randomizer.nextInt(bytes.length)] + ")";
        } else if (literalType.equals(CtPrimitiveType.charType)) {
            char offset = 'a'; // lower case
            if (randomizer.nextBoolean()) // upper case
                offset = 'A';
            return "((char) " + (offset + randomizer.nextInt(26)) + ")";
        } else if (literalType.equals(CtPrimitiveType.doubleType)) {
            return randomizer.nextDouble() + "d";
        } else if (literalType.equals(CtPrimitiveType.floatType)) {
            return randomizer.nextFloat() + "f";
        } else if (literalType.equals(CtPrimitiveType.intType)) {
            return String.valueOf(randomizer.nextInt());
        } else if (literalType.equals(CtPrimitiveType.longType)) {
            return randomizer.nextLong() + "L";
        } else if (literalType.equals(CtPrimitiveType.shortType)) {
            return "((short) " + (randomizer.nextInt(Short.MAX_VALUE - Short.MIN_VALUE) - Short.MIN_VALUE) + ")";
        } else {
            for (CtConstructor constructor : literalType.getDeclaredConstructors()) {
                try {
                    if (constructor.getParameterTypes().length == 0) {
                        return "new " + literalType.getName() + "()";
                    }
                } catch (NotFoundException e) {
                    // parameter types not found so skip
                }
            }
            return "null";
        }
    }

    /*
     * generate a variable line helper method for primitive fields
     */
    public static String generateVariableLine(CtClass variableClass, String variableName) {
        return variableClass.getName() + " " + variableName + ";"; // ex: int i;
    }

    /*
     * generate a variable line helper method for primitive fields with value
     */
    public static String generateVariableLine(CtClass variableClass, String variableName, String initialValue) {
        return variableClass.getName() + " " + variableName + " = " + initialValue + ";"; // ex: int i = 0;
    }

    /*
     * return true if specified type is a primitive type
     */
    public static boolean isPrimitive(CtClass type) {
        for (int i = 0; i < primitives.length; i++)
            if (primitives[i].equals(type))
                return true;
        return false;
    }

    /*
     * generate a random primitive type
     */
    public static CtClass getRandomPrimitiveType(){
        return primitives[new Random().nextInt(primitives.length)];
    }
}
