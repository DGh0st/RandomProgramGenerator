package Generator.Expression;

import static org.junit.Assert.assertEquals;

import javassist.CtClass;
import javassist.CtPrimitiveType;
import org.junit.Test;

public class ArithmeticExpressionTest {

    @Test
    public void getArithmeticExpression(){
        CtClass primitives[] = {
                CtPrimitiveType.booleanType,
                CtPrimitiveType.shortType,
                CtPrimitiveType.longType,
                CtPrimitiveType.intType,
                CtPrimitiveType.floatType,
                CtPrimitiveType.doubleType,
                CtPrimitiveType.charType,
                CtPrimitiveType.byteType
        };

        for (CtClass primitive : primitives) {
            Expression expression = new ArithmeticExpression(primitive, "a", "b", "c", "d");
            System.out.println(primitive.getName() + " " + expression.getExpression() + "\n");
            Expression expression1 = new ArithmeticExpression(primitive, "a");
            System.out.println(primitive.getName() + " " + expression1.getExpression() + "\n");
            Expression expression2 = new ArithmeticExpression(primitive);
            System.out.println(primitive.getName() + " " + expression2.getExpression() + "\n");
        }
    }
}