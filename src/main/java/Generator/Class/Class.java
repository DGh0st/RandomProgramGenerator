package Generator.Class;

import Generator.Configurations;
import Generator.Statement.Statement;
import Generator.Statement.StatementFactory;
import Generator.TrackScope;
import Generator.Variable;
import javassist.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class Class {
    protected static Configurations configurations = null;
    protected static Random randomizer = new Random();
    protected CtClass currentCtClass = null;
    protected HashMap<String, String> methodBodies = new HashMap<>();

    /*
     * Construct a concrete, abstract or interface class with name and randomly add implements/extends
     */
    public Class(int additionalModifiers, String name, ArrayList<Class> otherClasses, ArrayList<Class>interfaces) {
        currentCtClass = makeCtClass(name);
        currentCtClass.setModifiers(currentCtClass.getModifiers() | additionalModifiers);
        initialize(otherClasses, interfaces);
    }

    /*
     * abstract methods used for creating and initialize the currentCtClass
     */
    protected abstract void initialize(ArrayList<Class> otherClasses, ArrayList<Class> interfaces);
    protected abstract CtClass makeCtClass(String name);

    /*
     * Set the configurations so that it can be used later.
     * We can't initialize it on declaration line as that would mess up the configuration singleton.
     */
    public static void setConfigurations(Configurations newConfigurations) {
        configurations = newConfigurations;
    }

    /*
     * return the current CtClass which has all the code
     */
    public CtClass getCtClass() {
        return currentCtClass;
    }

    /*
     * randomly add a super class to the current class
     */
    protected boolean addSuperClassIfNeeded(ArrayList<Class> classes, int maxHierarchy) {
        if (classes.size() > 0 && randomizer.nextBoolean()) {
            int randomClass = randomizer.nextInt(classes.size());
            CtClass randomCtClass = classes.get(randomClass).currentCtClass;
            int hierarchy = 0;
            CtClass currentSuperClass = randomCtClass;
            while (currentSuperClass != null && !currentSuperClass.getName().equals("java.lang.Object")) {
                try {
                    currentSuperClass = currentSuperClass.getSuperclass();
                    hierarchy++;
                } catch (NotFoundException e) {
                    break;
                }
            }
            if (hierarchy < maxHierarchy) { // only add it if the inheritance hierarchy is less than configuration
                try {
                    currentCtClass.setSuperclass(randomCtClass);
                } catch (CannotCompileException e) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * randomly add x interfaces to the current class and implement the methods
     */
    protected boolean addImplementsIfNeeded(ArrayList<Class> interfaces) {
        if (interfaces.size() > 0 && randomizer.nextBoolean()) {
            int numImplements = configurations.behaviors.min + randomizer.nextInt(configurations.behaviors.max - configurations.behaviors.min + 1);
            numImplements = Math.min(numImplements, interfaces.size());
            int maxFails = 10;
            for (int i = 0; i < numImplements; i++) {
                if (!addImplements(currentCtClass, interfaces) && maxFails > 0) {
                    i--; // failed to add implements so try again
                    maxFails--;
                }
            }

            try {
                CtClass currentInterfaces[] = currentCtClass.getInterfaces();
                for (int i = 0 ; i < currentInterfaces.length; i++) {
                    implementAbstractMethods(currentInterfaces[i].getMethods());
                }
            } catch (NotFoundException e) {
                return false;
            }
        }
        return true;
    }

    /*
     * Try to add an interface to current class, return true if successful
     */
    private boolean addImplements(CtClass currentCtClass, ArrayList<Class> interfaces) {
        if (interfaces.size() > 0) {
            int randomInterface = randomizer.nextInt(interfaces.size());
            CtClass randomCtInterface = interfaces.get(randomInterface).currentCtClass;
            try {
                CtClass currentInterfaces[] = currentCtClass.getInterfaces();
                CtClass newInterfaces[] = new CtClass[currentInterfaces.length + 1];
                for (int i = 0; i < currentInterfaces.length; i++) {
                    if (randomCtInterface.equals(currentInterfaces[i]) || randomCtInterface.subclassOf(currentInterfaces[i]))
                        return false;
                    else if (randomCtInterface.subtypeOf(currentInterfaces[i]))
                        currentInterfaces[i] = randomCtInterface;
                    newInterfaces[i] = currentInterfaces[i];
                }
                newInterfaces[newInterfaces.length - 1] = randomCtInterface;
                currentCtClass.setInterfaces(newInterfaces);
            } catch (NotFoundException e) {
                CtClass newInterfaces[] = new CtClass[]{randomCtInterface};
                currentCtClass.setInterfaces(newInterfaces);
            }
            return true;
        }
        return false;
    }

    /*
     * Create an implementation for an abstract method
     */
    protected void implementAbstractMethods(CtMethod[] methods) {
        int maxFails = 10;
        for (int i = 0; i < methods.length; i++) {
            int modifiers = methods[i].getModifiers();
            if (Modifier.isAbstract(modifiers)) {
                CtMethod oldMethod = methods[i];
                try {
                    currentCtClass.getDeclaredMethod(oldMethod.getName(), oldMethod.getParameterTypes());
                    continue; // if it reaches this point then method already exist
                } catch (NotFoundException e) {
                    // failed to get parameter types so try without any parameters
                    try {
                        currentCtClass.getDeclaredMethod(oldMethod.getName());
                        continue; // if it reaches this point then method already exist
                    } catch (NotFoundException e1) {
                        // failed to get method so implement it
                    }
                }
                try {
                    // create a return statement at the end of body if needed
                    String lastStatement = Variable.generateLiteral(oldMethod.getReturnType());
                    if (lastStatement == null)
                        lastStatement = "";
                    else
                        lastStatement = "\treturn " + lastStatement + ";\n";
                    int totalMethods = currentCtClass.getDeclaredMethods().length;
                    if (totalMethods == 0)
                        totalMethods = 5;
                    StatementFactory.setCtClass(currentCtClass);
                    CtClass parameters[] = oldMethod.getParameterTypes();
                    for (int j = 0; j < parameters.length; j++) { // add parameters to list of variables
                        TrackScope.addToScope(parameters[j], "var" + (j + 1));
                    }
                    int currentLines = 0;
                    int maxLines = Math.min(configurations.minLOCPerClass / totalMethods, 114);
                    StringBuilder body = new StringBuilder("{\n");
                    while (currentLines < maxLines) { // generate body of the method
                        Statement statement = StatementFactory.callStatement(maxLines - currentLines);
                        body.append("\t");
                        body.append(statement.getStatement().replace("\n", "\n\t"));
                        body.append("\n");
                        currentLines += statement.getCurrentLine();
                    }
                    body.append(lastStatement);
                    body.append("}\n");
                    String finalBody = body.toString();
                    for (int j = 0; j < parameters.length; j++) { // replace var1, var2, var3, etc to $1, $2, $3 for Javassist
                        finalBody = finalBody.replace("var" + (j + 1), "$" + (j + 1));
                    }
                    try {
                        CtMethod newMethod = CtNewMethod.make(oldMethod.getModifiers(), oldMethod.getReturnType(), oldMethod.getName(), oldMethod.getParameterTypes(), oldMethod.getExceptionTypes(), finalBody, currentCtClass);
                        newMethod.setModifiers(newMethod.getModifiers() & ~Modifier.ABSTRACT);
                        currentCtClass.addMethod(newMethod);
                        methodBodies.put(newMethod.getName(), body.toString());
                    } catch (CannotCompileException e) {
                        // failed to create method because generated body has some error
                        System.err.println("===========================================");
                        e.printStackTrace();
                        System.err.println("===========================================");
                        System.err.println(body.toString());
                        System.err.println("===========================================");
                    }
                } catch (/*CannotCompileException |*/ NotFoundException e) {
                    // failed to create new method so try again
                    if (maxFails > 0) {
                        i--;
                        maxFails--;
                    }
                }
            }
        }
    }
}
