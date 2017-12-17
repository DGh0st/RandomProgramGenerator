package Generator.Class;

import Generator.Configurations;
import Generator.NameGenerator;
import javassist.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class ClassFactory {
    private Configurations configurations = null;
    private ArrayList<Class> interfaces = new ArrayList<>();
    private ArrayList<Class> abstractClasses = new ArrayList<>();
    private ArrayList<Class> classes = new ArrayList<>();

    /*
     * Create a new class factory for generating a bunch of classes with the code
     */
    public ClassFactory(String configurationFilePath) {
        Configurations.setFilePath(configurationFilePath);
        configurations = Configurations.getConfigurations(); // initialize configurations
        Class.setConfigurations(configurations);
    }

    /*
     * generate interfaces, abstract classes, and concrete classes in that order
     */
    public void generate() {
        generateInterfaces();
        generateAbstractClasses();
        generateConcreteClasses();
    }

    /*
     * Write generated classes to output directory
     */
    public void writeToDirectory() {
        writeToDirectory(configurations.outputDirectory);
    }

    /*
     * Write source code or byte code to output directory based on configurations
     */
    public void writeToDirectory(String outputDirectory) {
        ArrayList<Class> generatedClasses = new ArrayList<>();
        generatedClasses.addAll(interfaces);
        generatedClasses.addAll(abstractClasses);
        generatedClasses.addAll(classes);

        for (Class c : generatedClasses) {
            try {
                if (configurations.generationType.equals(Configurations.GenerationType.BYTECODE)) {
                    c.getCtClass().writeFile(outputDirectory);
                } else {
                    File directory = new File(outputDirectory);
                    boolean success = directory.mkdir();
                    if (success || directory.exists()) {
                        String filePath = outputDirectory + "/" + c.getCtClass().getSimpleName() + ".java";
                        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                        writer.write(c.toString());
                        writer.close();
                    } else {
                        System.out.println("Output directory does not exist");
                    }
                }
            } catch (CannotCompileException | IOException e) {
                // Failed to compile file (some syntax error) or write to file so skip it
            }
        }
    }

    /*
     * generate x number of interfaces and initialize them with code
     */
    private void generateInterfaces() {
        int numInterfaces = configurations.interfaces.current;
        int maxFails = 10;
        for (int i = 0; i < numInterfaces; i++) {
            try {
                Interface newInterface = new Interface("i_" + NameGenerator.Generate(), interfaces);
                CtClass interfaceCtClass = newInterface.getCtClass();
                interfaceCtClass.toClass(); // check if interface can be compiled
                interfaces.add(newInterface);
            } catch (CannotCompileException e) {
                // failed to compile interface so generate another one
                if (maxFails > 0) {
                    i--;
                    maxFails--;
                }
            }
        }
    }

    /*
     * generate x number of abstract classes and initialize them with code
     */
    private void generateAbstractClasses() {
        int numAbstracts = configurations.abstractClasses.current;
        int maxFails = 10;
        for (int i = 0; i < numAbstracts; i++) {
            try {
                AbstractClass newAbstract = new AbstractClass("a_" + NameGenerator.Generate(), abstractClasses, interfaces);
                CtClass abstractCtClass = newAbstract.getCtClass();
                abstractCtClass.toClass(); // check if class can be compiled
                abstractClasses.add(newAbstract);
            } catch (CannotCompileException e) {
                // failed to compile abstract class so generate another one
                if (maxFails > 0) {
                    i--;
                    maxFails--;
                }
            }
        }
    }

    /*
     * generate x number of concrete classess and initialize them with code
     */
    private void generateConcreteClasses() {
        int numClasses = configurations.classes.current;
        int maxFails = 10;
        for (int i = 0; i < numClasses; i++) {
            try {
                ArrayList<Class> generatedClasses;
                Random randomizer = new Random();
                if (classes.size() > 0) {
                    if (randomizer.nextBoolean()) {
                        generatedClasses = classes;
                    }
                    else {
                        generatedClasses = abstractClasses;
                    }
                } else {
                    generatedClasses = abstractClasses;
                }
                ConcreteClass newConcrete = new ConcreteClass("c_" + NameGenerator.Generate(), generatedClasses, interfaces);
                CtClass concreteCtClass = newConcrete.getCtClass();
                concreteCtClass.toClass(); // check if class can be compiled
                classes.add(newConcrete);
            } catch (CannotCompileException e) {
                // failed to compile class so generate another one
                if (maxFails > 0) {
                    i--;
                    maxFails--;
                }
            }
        }
    }
}
