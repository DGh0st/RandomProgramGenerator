package Generator.Statement;

/**
 * Statement class is inherited by all types of statements in this package
 *
 * By default any Statement is empty and has a current line to track current line in any scope
 */
public class Statement {

    private String Statement = "";
    protected int currentLine = 0;

    public Statement(){}

    public String getStatement() {
        return Statement;
    }

    public void setStatement(String statement) {
        Statement = statement;
    }

    public int getCurrentLine() {
        return currentLine;
    }
}
