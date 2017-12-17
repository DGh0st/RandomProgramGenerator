package Generator.Expression;


import javassist.CtClass;
import javassist.CtPrimitiveType;

import java.util.Random;

public class ConditionalExpression extends Expression {
    /**
     * ==      equal to
     !=      not equal to
     >       greater than
     >=      greater than or equal to
     <       less than
     <=      less than or equal to
     */
    private String operator;
    private static String [] regularOperators = new String []{"==", "!=", ">", ">=", "<", "<="};
    private static String [] booleanOperators = new String []{"==", "!="};

    public ConditionalExpression(CtClass dataType, String leftHandSide, String rightHandSide){
        super();
        StringBuilder builder = new StringBuilder();
        builder.append("("+ leftHandSide + " ");
        if(dataType.equals(CtPrimitiveType.booleanType)) // generate an conditional operator for required type
            builder.append(getBooleanOperator());
        else
            builder.append(getRegularOperator());
        builder.append(" "+ rightHandSide + ")");
        setExpression(builder.toString());
    }

    private String getRegularOperator() {
        operator = regularOperators[new Random().nextInt(regularOperators.length)];
        return operator;
    }
    private String getBooleanOperator() {
        operator = booleanOperators[new Random().nextInt(booleanOperators.length)];
        return operator;
    }


}
