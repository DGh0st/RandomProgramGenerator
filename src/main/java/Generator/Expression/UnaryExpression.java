package Generator.Expression;

import Generator.Variable;
import javassist.CtClass;

import java.util.Random;

public class UnaryExpression extends Expression {

    /**
     +	Unary plus operator; indicates positive value (numbers are positive without this, however)
     -	Unary minus operator; negates an expression
     ++	Increment operator; increments a value by 1
     --	Decrement operator; decrements a value by 1
     !	Logical complement operator; inverts the value of a boolean
     */
    private static String [] numericOperators = new String[]{"-", "+", "--", "++", "~"};
    private static String [] decimalOperators = new String[]{"--", "++", "-", "+"};
    private static String [] booleanOperators = new String[]{"!"};

    public UnaryExpression(CtClass type){
        super();
        String name = Variable.generateLiteral(type);
        if (new Random().nextInt(10) > 3) {
            if (type.equals(CtClass.booleanType)) {
                setUnary(getBooleanOperator(), name);
            } else if (type.equals(CtClass.intType) || type.equals(CtClass.longType) || type.equals(CtClass.shortType)) {
                setExpression(numericOperators[new Random().nextInt(2)] + name);
            } else if (type.equals(CtClass.byteType)) {
                setExpression(name);
            } else if (type.equals(CtClass.charType)) {
                setExpression(name);
            } else if (type.equals(CtClass.doubleType) || type.equals(CtClass.floatType)) {
                setExpression(numericOperators[new Random().nextInt(2)] + name);
            }
        } else {
            setExpression(name);
        }
    }

    public UnaryExpression(String name, CtClass type) {
        super();
        if (new Random().nextInt(10) > 3) {
            if (type.equals(CtClass.booleanType)) {
                setUnary(getBooleanOperator(), name);
            } else if (type.equals(CtClass.intType) || type.equals(CtClass.longType) || type.equals(CtClass.shortType)) {
                setUnary(getNumericOperator(), name);
            } else if (type.equals(CtClass.byteType)) {
                setExpression(decimalOperators[new Random().nextInt(2)] + name);
            } else if (type.equals(CtClass.charType)) {
                setExpression(decimalOperators[new Random().nextInt(2)] + name);
            } else if (type.equals(CtClass.doubleType) || type.equals(CtClass.floatType)) {
                setUnary(getDecimalOperator(), name);
            }
        } else {
            setExpression(name);
        }
    }
    private void setUnary(String operator, String name){
        if (operator == "--" || operator == "++") {
            if (new Random().nextBoolean()) setExpression(operator + name);
            else this.setExpression(name + operator);
        } else {
            this.setExpression(operator + name);
        }
    }

    private String getNumericOperator() {
        return numericOperators[new Random().nextInt(numericOperators.length)];
    }
    private String getDecimalOperator() {
        return numericOperators[new Random().nextInt(decimalOperators.length)];
    }
    private String getBooleanOperator() {
        return booleanOperators[new Random().nextInt(booleanOperators.length)];
    }
}
