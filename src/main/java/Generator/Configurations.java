package Generator;

import java.io.*;
import java.util.Random;

public class Configurations {
    /*
     * Range Class used for keeping track of min and max values for various configurations
     */
    public class Range {
        public final int min;
        public final int max;
        public final int current;

        public Range(int min, int max, int current) {
            this.min = min;
            this.max = max;
            this.current = current;
        }
    }

    public enum GenerationType {
        SOURCECODE,
        BYTECODE
    }

    // filepath needs to be set using setFilePath method before getting configuration
    private static String filePath = null;
    private static Configurations configs = null;

    // TODO: Add more variables to save configurations
    public final String outputDirectory;
    public final Range interfaces;
    public final Range interfaceHierarchy;
    public final Range interfaceMethods;
    public final Range interfaceConstants;
    public final Range abstractClasses;
    public final Range abstractMethods;
    public final Range classes;
    public final Range concreteMethods;
    public final Range behaviors;
    public final Range fields;
    public final Range inheritanceHierarchy;
    public final Range methodParameters;
    public final GenerationType generationType;
    public final int maxTotalLinesOfSourceCode;
    public final int minLOCPerClass;
    public final int maxArraySize; // value of 0 or -1 means arrays are disabled

    private Range rangeParse(Random r,String line) throws IOException{
        int[] parameters = new int[4];
        String[] ranges = line.split(",");
        for(int i = 1; i < 4; i++){
            if(ranges[i].length()>2)
                parameters[i] = r.nextInt(ranges[i].charAt(4)) + ranges[i].charAt(6);
            else parameters[i] = Integer.parseInt(ranges[i]);
        }
        return new Range(parameters[1],parameters[2],parameters[3]);
    }
    private Configurations() throws IOException {
        BufferedReader br = null;
        if (filePath != null) {
            File config = new File(filePath);
            String filePathFlag = "0";
            try {
                br = new BufferedReader(new FileReader(config));
                filePathFlag = br.readLine();
            } catch (FileNotFoundException e) {
                System.out.println("File '" + filePath + "' not found");
                filePath = null;
            }
        }
        Random randomizer = new Random();
        if (filePath == null) {
            // Set default values
            outputDirectory = "GeneratedClasses";
            interfaces = new Range(2, 5, randomizer.nextInt(4)+ 2);
            interfaceHierarchy = new Range(1, 4, -1);
            interfaceMethods = new Range(1, 3, -1);
            interfaceConstants = new Range(0, 2, -1);
            abstractClasses = new Range(3, 7, randomizer.nextInt(5) + 3);
            abstractMethods = new Range(1, 3, -1);
            classes = new Range(5, 10, randomizer.nextInt(6) + 5);
            concreteMethods = new Range(1, 5, -1);
            behaviors = new Range(0, 2, -1);
            fields = new Range(1, 5, -1);
            inheritanceHierarchy = new Range(1, 4, -1);
            methodParameters = new Range(0, 3, -1);
            generationType = GenerationType.SOURCECODE;
            maxTotalLinesOfSourceCode = 10000;
            minLOCPerClass = maxTotalLinesOfSourceCode/classes.current;
            maxArraySize = 10;
        } else {
            while (!br.readLine().equals("1")){}
            // TODO: Initialize configurations by parsing filePath
            outputDirectory = br.readLine().substring(16);
            interfaces = rangeParse(randomizer,br.readLine());
            interfaceHierarchy = rangeParse(randomizer,br.readLine());
            interfaceMethods = rangeParse(randomizer,br.readLine());
            interfaceConstants = rangeParse(randomizer,br.readLine());
            abstractClasses = rangeParse(randomizer,br.readLine());
            abstractMethods = rangeParse(randomizer,br.readLine());
            classes = rangeParse(randomizer,br.readLine());
            concreteMethods = rangeParse(randomizer,br.readLine());
            behaviors = rangeParse(randomizer,br.readLine());
            fields = rangeParse(randomizer,br.readLine());
            inheritanceHierarchy = rangeParse(randomizer,br.readLine());
            methodParameters = rangeParse(randomizer,br.readLine());
            generationType = GenerationType.SOURCECODE;
            maxTotalLinesOfSourceCode = Integer.parseInt(br.readLine().substring(26));
            minLOCPerClass = maxTotalLinesOfSourceCode/classes.current;
            maxArraySize = Integer.parseInt(br.readLine());
        }
    }

    /*
     * create a singleton
     *
     * filepath needs to be set before calling this method
     */
    public static Configurations getConfigurations() {
        if (configs == null)
            try {
                configs = new Configurations();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return configs;
    }

    public static String getFilePath() {
        return filePath;
    }

    /*
     * Set filepath so that configurations can be setup from that file
     */
    public static void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }
}
