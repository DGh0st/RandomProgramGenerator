package Generator.Expression;

import javassist.CtClass;
import javassist.CtPrimitiveType;

public class ExpressionFactory {
    /*
     * generate a new expression based on return and data types
     */
    public static Expression make(CtClass returnType, CtClass dataType, String ... variableNames) {
        boolean isBooleanType = returnType.equals(CtPrimitiveType.booleanType);
        // generate unary expression if there is 0 or 1 variables
        if (variableNames.length == 0) {
            return new UnaryExpression(dataType);
        } else if (variableNames.length == 1) {
            return new UnaryExpression(variableNames[0], dataType);
        } else {
            // generate different expression types based on return type
            if (isBooleanType) {
                String firstHalf[] = new String[variableNames.length / 2];
                String secondHalf[] = new String[variableNames.length / 2];
                for (int i = 0; i < variableNames.length / 2; i++) {
                    firstHalf[i] = variableNames[i];
                    secondHalf[i] = variableNames[variableNames.length / 2 + i];
                }
                Expression firstExpression = make(dataType, dataType, firstHalf);
                Expression secondExpression = make(dataType, dataType, secondHalf);
                if (firstExpression == null || secondExpression == null)
                    return new ConditionalExpression(dataType, variableNames[0], variableNames[1]);
                else
                    return new ConditionalExpression(dataType, firstExpression.getExpression(), secondExpression.getExpression());
            } else if (returnType.equals(CtPrimitiveType.shortType) || returnType.equals(CtPrimitiveType.longType) || returnType.equals(CtPrimitiveType.intType) || returnType.equals(CtPrimitiveType.floatType) || returnType.equals(CtPrimitiveType.doubleType)) {
                return new ArithmeticExpression(dataType, variableNames);
            } else if (returnType.equals(CtPrimitiveType.charType)) {
                return new ArithmeticExpression(dataType, variableNames);
            } else if (returnType.equals(CtPrimitiveType.byteType)) {
                return new ArithmeticExpression(dataType, variableNames);
            }
        }
        return null;
    }
}
