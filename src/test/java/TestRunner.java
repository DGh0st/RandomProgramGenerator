import Generator.ConfigurationsTest;
import Generator.Expression.ArithmeticExpressionTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


public class TestRunner {
    public static void main(String args[]) {
        Class testClasses[] = {
                ConfigurationsTest.class,
                ArithmeticExpressionTest.class
                // TODO: Add more test classes to run
        };

        Result result = JUnitCore.runClasses(testClasses);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
            System.out.println(failure.getTrace());
        }

        System.out.println("");
        if (result.wasSuccessful())
            System.out.print("Passed All Tests!");
        else
            System.out.print("Failed: " + result.getFailureCount());
        System.out.println(" (" + result.getRunTime() + " ms)\n");
    }
}
