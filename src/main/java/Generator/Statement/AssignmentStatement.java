package Generator.Statement;

import Generator.Expression.AssignmentExpression;
import Generator.Expression.Expression;
import javassist.CtClass;

public class AssignmentStatement extends Statement{
    /**
     * Getting assignment statements by making use of assignment Expression, fulfilling requirements of a statement
     */
    Expression expression;
    public AssignmentStatement(CtClass type, String ... names){
        super();
        currentLine = 1;
        //Getting left hand side and rest is for assignment expression of right hand side
        String newNames[] = new String[names.length - 1];
        for (int i = 0; i < newNames.length; i++) {
            newNames[i] = names[i + 1];
        }
        expression = new AssignmentExpression(type, names[0], newNames);
        setStatement(expression.getExpression()+";");
    }
}
