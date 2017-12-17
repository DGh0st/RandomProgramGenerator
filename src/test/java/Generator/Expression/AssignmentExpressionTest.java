package Generator.Expression;

import javassist.CtClass;
import javassist.CtPrimitiveType;
import org.junit.Test;

public class AssignmentExpressionTest {
    @Test
    public void getAssignmentExpression(){
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
            Expression expression = new AssignmentExpression(primitive, "a", "b", "c", "d", "e");
            System.out.println(primitive.getName() + " " + expression.getExpression() + "\n");
            Expression expression1 = new AssignmentExpression(primitive, "a", "b");
            System.out.println(primitive.getName() + " " + expression1.getExpression() + "\n");
            Expression expression2 = new AssignmentExpression(primitive, "a");
            System.out.println(primitive.getName() + " " + expression2.getExpression() + "\n");
        }
    }
}
