import java.util.ArrayList;

/***
 * Main class for data parsers
 * @author: Guy Harel
 */
public class Main {
    public static void main(String[] args) {
        // Test of Utils

        String data = Utils.readFileAsString("data/2016_Presidential_Results.csv");
        String data1 = Utils.readFileAsString("data/Education.csv");
        String data2 = Utils.readFileAsString("data/Unemployment.csv");
        DataManager dataManager = Utils.parseAllData(data, data1, data2);
        System.out.println();
    }
}