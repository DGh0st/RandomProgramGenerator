package Generator.Expression;

import javassist.CtClass;
import javassist.CtPrimitiveType;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnaryExpressionTest {
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
    @Test
    public void test1Parameter(){
        System.out.println("\nTesting just the type parameter");
        Expression expression;
        for(CtClass type: primitives) {
            expression = new UnaryExpression(type);
            System.out.println(expression.getExpression());
        }
    }
    @Test
    public void test2Parameters(){
        System.out.println("\nTesting just the type parameter");
        Expression expression;
        for(CtClass type: primitives) {
            expression = new UnaryExpression("name",type);
            System.out.println(expression.getExpression());
        }

    }
}