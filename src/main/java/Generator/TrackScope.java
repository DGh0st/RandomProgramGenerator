package Generator;

import javassist.CtClass;

import java.util.ArrayList;
import java.util.HashMap;

public class TrackScope {

    private static int currentScope = 0;

    public static int getCurrentScope() {
        return currentScope;
    }

    public static void incrementScope() {
        currentScope++;
    }

    public static void decrementScope() {
        scope.remove(currentScope);
        currentScope--;
        if (currentScope < 0)
            currentScope = 0;
    }

    private static HashMap<Integer, HashMap<CtClass, ArrayList<String>>> scope = new HashMap<>();


    public static void addToScope(CtClass type, String name) {
        if (!scope.containsKey(currentScope)) {
            scope.put(currentScope, new HashMap<>());
            if (!scope.get(currentScope).containsKey(type)) {
                scope.get(currentScope).put(type, new ArrayList<>());
                scope.get(currentScope).get(type).add(name);
            }
        } else {
            if (!scope.get(currentScope).containsKey(type)) {
                scope.get(currentScope).put(type, new ArrayList<>());
                scope.get(currentScope).get(type).add(name);
            } else {
                scope.get(currentScope).get(type).add(name);
            }
        }
    }

    public static String[] getScopeVariables(CtClass type) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = currentScope; i >= 0; i--) {
            if (scope.containsKey(i) && scope.get(i).containsKey(type)) {
                list.addAll(scope.get(i).get(type));
            }
        }

        String[] names = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            names[i] = list.get(i);
        return names;
    }

    public static void reset() {
        currentScope = 0;
        scope = new HashMap<>();
    }

}
