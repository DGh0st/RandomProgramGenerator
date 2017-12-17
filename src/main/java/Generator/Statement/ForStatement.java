package Generator.Statement;

import Generator.Expression.Expression;
import Generator.NameGenerator;
import Generator.TrackScope;
import javassist.CtClass;
import javassist.CtPrimitiveType;

import java.util.Random;

public class ForStatement extends Statement {

    public ForStatement(int maxLines){
        super();
        maxLines = Math.min(maxLines, 10);
        currentLine = 2;
        StringBuilder builder = new StringBuilder("for (" + getForLoopExpression() + ") {\n");
        while (currentLine < maxLines) {
            //Generate random statements to go inside this particular for loop
            Statement s1 = StatementFactory.callStatement(maxLines - currentLine);
            String s = s1.getStatement();
            //formatting for loop
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

    /**
     * @return for loop statement is created here and returned as string
     */
    private String getForLoopExpression(){
        String variableName = NameGenerator.Generate();
        StringBuilder builder = new StringBuilder("int ");
        builder.append(variableName);
        builder.append(" = ");
        Random random = new Random();
        int number = random.nextInt(100);
        int insideNumber;

        //Generate a random for loop for positive iterations
        if(random.nextBoolean()){
            insideNumber = random.nextInt(10)+number;
            builder.append(number);
            builder.append("; ");
            builder.append(variableName);
            builder.append(" < ");
            builder.append(insideNumber);
            builder.append("; ");
            builder.append(variableName);
            builder.append("++");
        }
        //Generate a random for loop for negative iterations
        else{
            insideNumber = number - random.nextInt(10);
            builder.append(number);
            builder.append("; ");
            builder.append(variableName);
            builder.append(" > ");
            builder.append(insideNumber);
            builder.append("; ");
            builder.append(variableName);
            builder.append("--");
        }
        return builder.toString();
    }
}
