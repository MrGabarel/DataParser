import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {
    public static String readFileAsString(String filepath) {
        StringBuilder output = new StringBuilder();

        try (Scanner scanner = new Scanner(new File(filepath))) {

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                output.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output.toString();
    }
    public static ArrayList<ElectionResult> parse2016PresidentialResults(String data){
        String[] lines = data.split("\n");
        ArrayList<ElectionResult> output = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            String[] dataPieces;
            if (lines[i].indexOf("\"") == -1) {
                dataPieces = lines[i].split(",");
            } else {
                int loc1 = lines[i].indexOf("\"");
                int loc2 = lines[i].indexOf("\"", loc1 + 1);
                dataPieces = new String[11];
                int previous = 0;
                int current = 0;
                for (int j = 0; j < 10; j++) {
                    current = lines[i].indexOf(",", current + 1);
                    if (!(current > loc1 && current < loc2)) {
                        dataPieces[j] = lines[i].substring(previous + 1, current);
                        previous = current;
                    } else {
                        while (current > loc1 && current < loc2){
                            current = lines[i].indexOf(",", current + 1);
                        }
                        current--;
                        j--;
                    }
                }
                dataPieces[10] = lines[i].substring(previous + 1);
                dataPieces[6] = dataPieces[6].replace("\"", "");
                dataPieces[6] = dataPieces[6].replace(",", "");
            }
            dataPieces[7] = dataPieces[7].substring(0, dataPieces[7].length() - 1);
            System.out.println(i);
            ElectionResult electionResult = new ElectionResult(Double.valueOf(dataPieces[1]), Double.valueOf(dataPieces[2]), Double.valueOf(dataPieces[3]), Double.valueOf(dataPieces[4]), Double.valueOf(dataPieces[5]), Double.valueOf(dataPieces[6]), Double.valueOf(dataPieces[7]), dataPieces[8], dataPieces[9], Integer.valueOf(dataPieces[10]));
            output.add(electionResult);
        }
        return output;
    }
}
