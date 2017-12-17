package Generator;

import Generator.Configurations;
import Generator.Statement.Statement;
import Generator.Statement.StatementFactory;
import Generator.Variable;
import javassist.*;

import java.util.ArrayList;
import java.util.Random;

public class MethodCreation{
    /*
     * interface used to send back body to the caller
     */
    public interface MethodCreationCallback {
        void methodCreationCompletion(CtMethod m, String body);
    }

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
    private static Random randomizer = new Random();

    /*
     * create and add abstract method to declaring class
     */
    public static void addAbstractMethod(int modifiers,
                                         ArrayList<CtClass> availableClassTypes,
                                         String name,
                                         CtClass declaring){
        try {
            Configurations configurations = Configurations.getConfigurations();
            CtClass returnType = getType(availableClassTypes, true);
            int numParameters = configurations.methodParameters.min + randomizer.nextInt(configurations.methodParameters.max - configurations.methodParameters.min + 1);
            CtClass parameters[] = new CtClass[numParameters];
            for (int i = 0; i < numParameters; i++) {
                parameters[i] = getType(availableClassTypes, false);
            }
            CtMethod m = CtNewMethod.abstractMethod(returnType, name, parameters, null, declaring);
            m.setModifiers(modifiers);
            declaring.addMethod(m);
        } catch (NotFoundException | CannotCompileException e) {
            // failed to create abstract method or add method to class
        }
    }

    /*
     * create and add concrete method with body to declaring class
     */
    public static void addConcreteMethod(int modifiers,
                                         ArrayList<CtClass> availableClassTypes,
                                         String name,
                                         CtClass declaring,
                                         MethodCreationCallback callback) {
        Configurations configurations = Configurations.getConfigurations();
        CtClass returnType = getType(availableClassTypes, true);
        int numParameters = configurations.methodParameters.min + randomizer.nextInt(configurations.methodParameters.max - configurations.methodParameters.min + 1);
        CtClass parameters[] = new CtClass[numParameters];
        for (int i = 0; i < numParameters; i++) {
            parameters[i] = getType(availableClassTypes, false);
        }
        // create return statement for method if needed
        String lastStatement = Variable.generateLiteral(returnType);
        if (lastStatement == null)
            lastStatement = "";
        else
            lastStatement = "\treturn " + lastStatement + ";\n";
        int totalMethods = declaring.getDeclaredMethods().length;
        if (totalMethods == 0)
            totalMethods = 5;
        StatementFactory.setCtClass(declaring);
        for (int j = 0; j < parameters.length; j++) { // add parameters to list of variables
            TrackScope.addToScope(parameters[j], "var" + (j + 1));
        }
        int currentLines = 0;
        int maxLines = Math.min(configurations.minLOCPerClass / totalMethods, 114);
        StringBuilder body = new StringBuilder("{\n");
        while (currentLines < maxLines) { // generate body
            Statement statement = StatementFactory.callStatement(maxLines - currentLines);
            body.append("\t");
            body.append(statement.getStatement().replace("\n", "\n\t"));
            body.append("\n");
            currentLines += statement.getCurrentLine();
        }
        body.append(lastStatement);
        body.append("}\n");
        String finalBody = body.toString();
        for (int j = 0; j < parameters.length; j++) { // replace var1, var2, var3, etc to $1, $2, $3, etc for Javassist
            finalBody = finalBody.replace("var" + (j + 1), "$" + (j + 1));
        }
        try {
            CtMethod m = CtNewMethod.make(returnType, name, parameters, null, finalBody, declaring);
            m.setModifiers(modifiers);
            declaring.addMethod(m);
            callback.methodCreationCompletion(m, body.toString());
        } catch (CannotCompileException e) {
            // failed to create method because generated body has some error
            System.err.println("===========================================");
            e.printStackTrace();
            System.err.println("===========================================");
            System.err.println(body.toString());
            System.err.println("===========================================");
        }
    }

    /*
     * generate a random type from list of types
     */
    private static CtClass getType(ArrayList<CtClass> types, boolean isVoidIncluded) {
        if (types.size() > 0 && randomizer.nextBoolean()) {
            if (isVoidIncluded && randomizer.nextBoolean()) // void
                return CtClass.voidType;
            else // classes
                return types.get(randomizer.nextInt(types.size()));
        } else {
            if (isVoidIncluded && randomizer.nextBoolean()) // void
                return CtClass.voidType;
            else // primitive
                return primitives[randomizer.nextInt(primitives.length)];
        }
    }
}



