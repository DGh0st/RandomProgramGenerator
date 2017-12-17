package Generator.Statement;

import Generator.Expression.ConditionalExpression;
import Generator.Expression.Expression;
import Generator.Expression.ExpressionFactory;
import Generator.NameGenerator;
import Generator.Variable;
import javassist.CtClass;
import javassist.CtPrimitiveType;

import java.util.Random;

public class WhileStatement extends Statement{

    public WhileStatement(int maxLines, CtClass dataType, String ... variables){
        super();
        maxLines = Math.min(maxLines, 10);
        currentLine = 2;
        Expression expression = ExpressionFactory.make(CtClass.booleanType, dataType, variables);

        StringBuilder builder = new StringBuilder("while (" + expression.getExpression() + ") {\n");
        while (currentLine < maxLines) {
            Statement s1 = StatementFactory.callStatement(maxLines - currentLine);
            String s = s1.getStatement();
            if (s != null) {
                builder.append("\t");
                builder.append(s.replace("\n", "\n\t"));
                builder.append("\n");
                currentLine += s1.currentLine;
            }
        }
        builder.append("}");
        setStatement(builder.toString());
    }

    public WhileStatement(int maxLines) {
        super();
        maxLines = Math.min(maxLines, 10);
        currentLine = 2;
        CtClass type = CtPrimitiveType.booleanType;
        String name = NameGenerator.Generate();
        boolean initialValue = new Random().nextBoolean();
        TypeDeclarationStatement typeDeclarationStatement= new TypeDeclarationStatement(type, name, initialValue ? "true" : "false");

        StringBuilder builder = new StringBuilder(typeDeclarationStatement.getStatement());
        builder.append("\nwhile (");
        builder.append(name);
        builder.append(" == ");
        builder.append(initialValue ? "false" : "true");
        builder.append(") {\n");
        while (currentLine < maxLines) {
            Statement s1 = StatementFactory.callStatement(maxLines - currentLine);
            String s = s1.getStatement();
            if (s != null) {
                builder.append("\t");
                builder.append(s.replace("\n", "\n\t"));
                builder.append("\n");
                currentLine += s1.currentLine;
            }
        }
        builder.append("\t");
        builder.append(name);
        builder.append(" = ");
        builder.append(initialValue ? "false" : "true");
        builder.append(";\n}");
        setStatement(builder.toString());
    }
}
