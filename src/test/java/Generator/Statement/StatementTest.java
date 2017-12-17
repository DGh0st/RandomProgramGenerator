package Generator.Statement;

import Generator.Variable;
import javassist.ClassPool;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import org.junit.Test;

import javax.naming.spi.StateFactory;

import static org.junit.Assert.*;

public class StatementTest {
    @Test
    public void testForStatement(){
        System.out.println("Testing For Statement");

        StatementFactory.setCtClass(CtPrimitiveType.booleanType);
        Statement forStatement = new ForStatement(5);
        assertNotNull(forStatement.getStatement());
        System.out.println(forStatement.getStatement() + "\n");
    }
    @Test
    public void testIfStatement(){
        System.out.println("Testing If Statement");

        StatementFactory.setCtClass(CtPrimitiveType.booleanType);
        Statement ifStatement = new IfStatement(5, Variable.getRandomPrimitivType(), "a", "b");
        assertNotNull(ifStatement.getStatement());
        System.out.println(ifStatement.getStatement() + "\n");
    }
    @Test
    public void testWhileStatement(){
        System.out.println("Testing While Statement");

        Statement whileStatement = new WhileStatement(5, Variable.getRandomPrimitivType());
        assertNotNull(whileStatement.getStatement());
        System.out.println(whileStatement.getStatement() + "\n");
    }

    @Test
    public void testTypeDeclarationStatement() {
        System.out.println("Testing Type Declaration Statement");

        Statement typeDeclarationStatement = new TypeDeclarationStatement(CtPrimitiveType.booleanType, "a");
        assertNotNull(typeDeclarationStatement.getStatement());
        System.out.print(typeDeclarationStatement.getStatement() + "\n");
        typeDeclarationStatement = new TypeDeclarationStatement(CtPrimitiveType.booleanType, "a", "true");
        assertNotNull(typeDeclarationStatement.getStatement() + "\n");
        System.out.println(typeDeclarationStatement.getStatement());
    }
    @Test
    public void testAssignmentStatement(){
        System.out.println("Testing Assignment Statement");

        Statement assignmentStatement = new AssignmentStatement(CtPrimitiveType.booleanType, "a","b");
        System.out.println(assignmentStatement.getStatement() + "\n");
        assertNotNull(assignmentStatement.getStatement());
    }
}