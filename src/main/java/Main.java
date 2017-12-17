import Generator.Class.ClassFactory;

public class Main {
    public static void main(String args[]) {
        ClassFactory generator;
        if (args.length > 0)
            generator = new ClassFactory(args[0]);
        else
            generator = new ClassFactory(null);
        generator.generate();
        generator.writeToDirectory();
    }
}
