package Generator.Expression;

import Generator.Variable;
import javassist.CtClass;
import javassist.CtPrimitiveType;

import java.util.Random;

public class ArithmeticExpression extends Expression {

    /**
     +	Additive operator (also used for String concatenation)
     -	Subtraction operator
     *	Multiplication operator
     /	Division operator
     %	Remainder operator
     */
    private static String [] numerics = new String []{"+", "-", "*", "/", "%"};
    private static String [] booleans = new String []{"&", "|", "^"};
    private static String [] bytes = new String []{"+", "-", "*", "/", "%"};
    private static String [] chars = new String []{"+", "*", "/", "%"};

    /**
     * example: a+b+13 is a arithmetic expression
     * @param names
     */
    public ArithmeticExpression(CtClass dataType, String ... names){
        super();
        String localExpression = "(";
        int length = names.length;
        if (names.length >= 2) {
            for (int i = 0; i < length - 1; i++) {
                localExpression += names[i] + " " + getOperator(dataType) + " ";
            }
            localExpression += names[length - 1];
        } else if (names.length == 1){
            localExpression += "(" + dataType.getSimpleName() + ") " + names[0] + " " + getOperator(dataType) + " " + Variable.generateLiteral(dataType);
        } else {
            localExpression += "(" + dataType.getSimpleName() + ") " +Variable.generateLiteral(dataType) + " " + getOperator(dataType) + " " + Variable.generateLiteral(dataType);
        }
        localExpression += ")";
        setExpression(localExpression);
    }

    /*
     * generate a random operator for specified type
     */
    private String getOperator(CtClass returnType) {
        if (returnType.equals(CtPrimitiveType.booleanType)) {
            return getOperator(booleans);
        } else if (returnType.equals(CtPrimitiveType.byteType)) {
            return getOperator(bytes);
        } else if (returnType.equals(CtPrimitiveType.charType)) {
            return getOperator(chars);
        } else if (returnType.equals(CtPrimitiveType.intType) || returnType.equals(CtPrimitiveType.longType) || returnType.equals(CtPrimitiveType.shortType) || returnType.equals(CtPrimitiveType.floatType) || returnType.equals(CtPrimitiveType.doubleType)) {
            return getOperator(numerics);
        } else {
            return " ";
        }
    }

    /*
     * select a random operator from an array
     */
    private String getOperator(String operators[]) {
        return operators[new Random().nextInt(operators.length)];
    }
}
