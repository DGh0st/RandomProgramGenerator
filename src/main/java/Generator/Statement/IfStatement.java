package Generator.Statement;

import Generator.Expression.Expression;
import Generator.Expression.ExpressionFactory;
import Generator.TrackScope;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import Generator.Statement.*;

import java.util.Random;

public class IfStatement extends Statement {
    /**
     *
     * @param maxLines
     * @param dataType
     * @param variableNames possible variable names for Expression to go inside If statement
     */
    public IfStatement(int maxLines, CtClass dataType, String ... variableNames){
        super();
        maxLines = Math.min(maxLines, 10);
        currentLine = 2;
        Random random = new Random();
        boolean divide = random.nextBoolean();
        Expression expression = ExpressionFactory.make(CtPrimitiveType.booleanType, dataType, variableNames);

        StringBuilder builder = new StringBuilder("if (" + expression.getExpression() + ") {\n");
        for(int i =0; i< (divide ? maxLines/2 : maxLines); i++){
           Statement s1 = StatementFactory.callStatement(maxLines- currentLine);
           //Formatting for if statements
           if(s1.getStatement() !=null) {
               String s = s1.getStatement();
               builder.append("\t");
               if (s1.currentLine > 1)
                   builder.append(s.replace("\n", "\n\t"));
               else
                   builder.append(s);
               builder.append("\n");
               currentLine += s1.currentLine;
           }
           else
              break;
        }
        builder.append("} ");
        if(divide){
            //to track scope when "{" added or "}" removed
            TrackScope.decrementScope();
            TrackScope.incrementScope();
            //add else if with 25% probability of this happening
            if(random.nextInt(10) >= 7){
                builder.append("else if (" + expression.getExpression() + "){\n");
            }
            //add else with 25% probability of this happening
            else {
                builder.append("else {\n");
            }
            currentLine +=2;
            //Add more statements to go inside else or else if until a random max is reached
            for(int i =0; i< (divide ? maxLines/2 : maxLines); i++){
                Statement s1 = StatementFactory.callStatement(maxLines- currentLine);
                String s = s1.getStatement();
                builder.append("\t");
                if (s1.currentLine > 1)
                    builder.append(s.replace("\n", "\n\t"));
                else
                    builder.append(s);
                builder.append("\n");
                currentLine += s1.currentLine;
            }
            builder.append("} ");
        }
        setStatement(builder.toString());
    }
}
