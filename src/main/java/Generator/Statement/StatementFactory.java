package Generator.Statement;

import Generator.Configurations;
import Generator.NameGenerator;
import Generator.TrackScope;
import Generator.Variable;
import javafx.util.Pair;
import javassist.*;

import java.util.*;

public class StatementFactory {
    private final static CtClass primitives[] = {
            CtPrimitiveType.booleanType,
            CtPrimitiveType.shortType,
            CtPrimitiveType.longType,
            CtPrimitiveType.intType,
            CtPrimitiveType.floatType,
            CtPrimitiveType.doubleType,
            CtPrimitiveType.charType,
            CtPrimitiveType.byteType
    };
    private static CtClass ct = null;
    private static Random randomizer = new Random();
    private static String lastAssignmentVariable = ""; // to remove same variable being assigned multiple times

    public static void setCtClass(CtClass c){
        ct = c;
        TrackScope.reset();
        for(CtField field : ct.getFields()){
            try {
                String name = field.getName();
                CtClass type = field.getType();
                if (Modifier.isFinal(type.getModifiers()) || !field.visibleFrom(ct))
                    continue;
                TrackScope.addToScope(type, name);
            } catch (NotFoundException e) {
                // failed to get type of field
            }
        }
        TrackScope.incrementScope();
    }

    public static Statement callStatement(int maxLines){
        if(maxLines ==0) {
            return typeDeclarationStatement();
        }
        else if(maxLines ==1){
            if(randomizer.nextBoolean()) return assignmentStatement();
            else return typeDeclarationStatement();
        }

        if (TrackScope.getCurrentScope() < 5) {
            int random = randomizer.nextInt(10000);
            if (random >= 0 && random <= 1000) return ifStatement(maxLines);
            else if (random > 1000 && random <= 1500) return forStatement(maxLines);
            else if (random > 1500 && random <= 2000) return whileStatement(maxLines);
            else if (random > 2000 && random <= 2500) return methodInvocationStatement();
            else if (random > 2500 && random <= 9250) return assignmentStatement();
            else if (random > 9250 && random <= 10000) return typeDeclarationStatement();
            else return assignmentStatement();
        } else {
            int random = randomizer.nextInt(3);
            if (random == 0) return assignmentStatement();
            else if (random == 1) return methodInvocationStatement();
            else if (random == 2) return typeDeclarationStatement();
            else return methodInvocationStatement();
        }
    }

    private static Statement ifStatement(int maxLines ){
        TrackScope.incrementScope();
        Pair<CtClass, String[]> names = generateListOfNames();
        Statement statement;
        if (names == null) {
            CtClass type = CtPrimitiveType.booleanType;
            statement = new IfStatement(maxLines, type, Variable.generateLiteral(type));
        } else {
            statement = new IfStatement(maxLines, names.getKey(), names.getValue());
        }
        TrackScope.decrementScope();
        return statement;
    }
    private static Statement forStatement(int maxLines){
        TrackScope.incrementScope();
        Statement statement = new ForStatement( maxLines);
        TrackScope.decrementScope();
        return statement;
    }
    private static Statement whileStatement(int maxLines){
        TrackScope.incrementScope();
        Pair<CtClass, String[]> names = generateListOfNames();
        Statement statement;
        if (names == null)
            statement = new WhileStatement(maxLines);
        else
            statement = new WhileStatement(maxLines, names.getKey(), names.getValue());
        TrackScope.decrementScope();
        return statement;
    }
    private static Statement assignmentStatement(){
        Pair<CtClass, String[]> names = generateListOfNames();
        if (names == null)
            return typeDeclarationStatement();
        String values[] = names.getValue();
        if (values.length > 0) {
            while (values[0].equals(lastAssignmentVariable)) {
                shuffle(values);
            }
            lastAssignmentVariable = values[0];
        }
        Statement statement = new AssignmentStatement(names.getKey(), values);
        return statement;
    }
    private static Statement typeDeclarationStatement(){
        CtClass type = Variable.getRandomPrimitiveType();
        String name = NameGenerator.Generate();
        Statement statement;
        Configurations configurations = Configurations.getConfigurations();
        if (configurations.maxArraySize > 0 && randomizer.nextInt(10) > 7) { // array
            int arraySize;
            do {
                arraySize = randomizer.nextInt(configurations.maxArraySize);
            } while (arraySize == 0);
            statement = new TypeDeclarationStatement(type, name, arraySize);
            for (int i = 0; i < arraySize; i++)
                TrackScope.addToScope(type, name + "[" + i + "]");
        } else { // primitive
            statement = new TypeDeclarationStatement(type, name, Variable.generateLiteral(type));
            TrackScope.addToScope(type, name);
        }
        return statement;
    }
    private  static Statement methodInvocationStatement(){
        CtMethod[] methods= ct.getDeclaredMethods();
        if(methods.length != 0){
            Statement statement = new MethodInvocationStatement(methods[randomizer.nextInt(methods.length)]);
            return statement;
        }
        return typeDeclarationStatement();
    }

    private static Pair<CtClass, String[]> generateListOfNames(){
        int shuffleTimes = randomizer.nextInt(primitives.length);
        for (int i = 0; i < shuffleTimes; i++)
            shuffle(primitives);
        for (int i = 0; i < primitives.length; i++) {
            String returnArray[] = TrackScope.getScopeVariables(primitives[i]);
            if (returnArray.length >= 2)
                return new Pair<>(primitives[i], returnArray);
        }
        return null;
    }

    private static void shuffle(Object list[]) {
        for (int i = 0; i < list.length; i++) {
            int index = randomizer.nextInt(i + 1);
            Object temp = list[index];
            list[index] = list[i];
            list[i] = temp;
        }
    }
}
