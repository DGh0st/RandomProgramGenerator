package Generator.Expression;

import Generator.Variable;
import javassist.CtClass;
import javassist.CtPrimitiveType;

import java.util.Random;

public class AssignmentExpression extends Expression {

    /**
     * Specific to assignment expressions
     * example: a = a+b
     * @param leftHandSideName, a from before = goes here
     * @param names, names for random expression, like a and b from a+b
     */
    public AssignmentExpression(CtClass returnType, String leftHandSideName , String ... names){
        super();
        //edge case when example: a = b or c = 4, which involves no Arithmetic logic
        if (names.length <= 1) {
            int numNames = new Random().nextInt(2) + 2;
            String newNames[] = new String[numNames];
            int i = 0;
            while (i < numNames) {
                newNames[i] = Variable.generateLiteral(returnType);
                i++;
            }
            while (i < names.length) {
                newNames[i] = names[i];
                i++;
            }
            Expression expression;
            if (new Random().nextBoolean() && returnType.equals(CtPrimitiveType.booleanType)) {
                expression = createConditionalExpression(returnType, newNames);
            } else {
                expression = new ArithmeticExpression(returnType, newNames);
            }
            setExpression(leftHandSideName + " = (" + returnType.getSimpleName() + ") " + expression.getExpression());
        } else {
            Expression expression;
            if (new Random().nextBoolean() && returnType.equals(CtPrimitiveType.booleanType)) {
                expression = createConditionalExpression(returnType, names);
            } else {
                expression = new ArithmeticExpression(returnType, names);
            }
            setExpression(leftHandSideName + " = (" + returnType.getSimpleName() + ") " + expression.getExpression());
        }
    }

    /*
     * create a conditional expression with data
     */
    private Expression createConditionalExpression(CtClass returnType, String ... names) {
        if (names.length == 0)
            return new ConditionalExpression(returnType, Variable.generateLiteral(returnType), Variable.generateLiteral(returnType));
        else if (names.length == 1)
            return new ConditionalExpression(returnType, names[0], Variable.generateLiteral(returnType));
        else if (names.length == 2)
            return new ConditionalExpression(returnType, names[0], names[1]);
        String firstHalf[] = new String[names.length / 2];
        String secondHalf[] = new String[names.length / 2];
        for (int i = 0; i < names.length / 2; i++) {
            firstHalf[i] = names[i];
            secondHalf[i] = names[names.length / 2 + i];
        }
        Expression firstExpression = createConditionalExpression(returnType, firstHalf);
        Expression secondExpression = createConditionalExpression(returnType, secondHalf);
        return new ConditionalExpression(returnType, firstExpression.getExpression(), secondExpression.getExpression());
    }
}
