import java.util.ArrayList;

/***
 * Main class for data parsers
 * @author: Guy Harel
 */
public class Main {
    public static void main(String[] args) {
        // Test of Utils

        String data = Utils.readFileAsString("data/2016_Presidential_Results.csv");
        ArrayList<ElectionResult> results = Utils.parse2016PresidentialResults(data);
        System.out.println("results = " + results.get(0));
    }
}