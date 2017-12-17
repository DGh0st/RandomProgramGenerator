package Generator;

import java.util.HashMap;
import java.util.Random;

/**
 * Generating a new random name when requested
 */
public class NameGenerator {
    // In order to have a unique name for each time a name is requested we use a hashmap to track NameList
    private static HashMap<String, Character> NameList = new HashMap<String, Character>();

    public static String Generate (){
        String name = pickName();
        //go through the while loop until a unique name is created each time
        while(NameList.containsKey(name)){
            name = pickName();
        }
        NameList.put(name, 'a');
        return name;
    }

    /**
     * picks a name of between 1-16 in length
     * @return a randomly generated name
     */
    private static String pickName(){
        Random r = new Random();
        int length = r.nextInt(16) + 1;
        StringBuilder builder = new StringBuilder();
        builder.append(generateChar());
        builder.append("_");
        for (int i =1; i < length; i++){
            if(r.nextBoolean())
                builder.append(generateChar());
            else
                builder.append(generateNumber());
        }
        return builder.toString();
    }

    /**
     * @return a randomly generated character upper or lower case
     */
    private static Character generateChar (){
        Random r = new Random();
        if(r.nextBoolean())
            return (char) (r.nextInt(26)+ 'a');
        else
            return (char) (r.nextInt(26) + 'A');
    }

    /**
     * @return a randomly generated integer between 0-9
     */
    private static int generateNumber (){
        return new Random().nextInt(10);
    }

}
