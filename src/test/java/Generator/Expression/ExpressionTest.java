package Generator.Expression;

import Generator.Variable;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExpressionTest {
    /**
     * Not so much of testing because our methods randomize things
     * Hard to test randomizing methods
     */

    @Test
    public void testArithmeticExpression(){
        System.out.println("Testing Arithmetic Expressions");

        Expression expression = new ArithmeticExpression(CtPrimitiveType.booleanType, new String[] {"a", "b", "c", "d"});
        System.out.println(expression.getExpression());
        assertNotNull(expression.getExpression());

        expression = new ArithmeticExpression(CtPrimitiveType.intType, new String[] {"12", "right"});
        System.out.println(expression.getExpression());
        assertNotNull(expression.getExpression());

        expression = new ArithmeticExpression(CtPrimitiveType.charType, new String[] {"Solo"});
        System.out.println(expression.getExpression() + "\n");
        assertNotNull(expression.getExpression());
    }

    @Test
    public void testAssignmentExpression(){
        System.out.println("Testing Assignment Expressions");

        Expression expression = new AssignmentExpression(CtPrimitiveType.intType,"Left", "a","b","Left");
        System.out.println(expression.getExpression() + "\n");
        assertNotNull(expression.getExpression());
    }

    @Test
    public void testConditionalExpression(){
        System.out.println("Testing Conditional Expression");

        Expression expression = new ConditionalExpression(Variable.getRandomPrimitivType(),"left", "right");
        System.out.println(expression.getExpression() + "\n");
        assertNotNull(expression.getExpression());
    }

}