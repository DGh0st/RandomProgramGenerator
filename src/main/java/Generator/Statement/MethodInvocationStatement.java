package Generator.Statement;

import Generator.TrackScope;
import Generator.Variable;
import javafx.util.Pair;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.lang.reflect.Method;
import java.util.Random;

public class MethodInvocationStatement extends Statement {

    /**
     * @param method
     */
    public MethodInvocationStatement(CtMethod method){
        super();
        StringBuilder builder = new StringBuilder();
        try {
            String name = method.getName();

            // figure out if return type to a method exists, then try to get a left hand side variable from current class
            if (method.getReturnType() != CtClass.voidType){
                String[] scopeVariables = TrackScope.getScopeVariables(method.getReturnType());
                if (scopeVariables.length != 0){
                    builder.append(scopeVariables[new Random().nextInt(scopeVariables.length)]);
                    builder.append(" = ");
                }
            }

            //Right hand side or the actual method invocation, get parameter list and find variables matching that parameter in the current class
            // if no such variables exists then create a random literal of that parameter type
            CtClass[] parameterList = method.getParameterTypes();
            builder.append(method.getName());
            if(parameterList.length == 0){
                builder.append("();");
                setStatement(builder.toString());
            }else {
                builder.append("( ");
                //Get parameter names from given class
                for (int i = 0; i < parameterList.length; i++) {
                    CtClass type = parameterList[i];
                    String[] scopeVariables = TrackScope.getScopeVariables(type);
                    if (scopeVariables.length != 0){
                        builder.append(scopeVariables[new Random().nextInt(scopeVariables.length)]);
                        builder.append(", ");
                    }
                    else {
                        builder.append(Variable.generateLiteral(type));
                        builder.append(", ");
                    }
                }
                int replace = builder.lastIndexOf(", ");
                builder.replace(replace, replace+1, " ");
                builder.append(" );");
                setStatement(builder.toString());
            }

        } catch (NotFoundException e) {
            System.err.println("Error Producing Method Invocation Statement");
            e.printStackTrace();
        }

    }
}
