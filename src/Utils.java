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

    public static String[] removeQuotationMarks(String[] lines){
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            while (line.indexOf("\"") != -1){
                int index1 = line.indexOf("\"");
                int index2 = line.indexOf("\"", index1 + 1);
                String oldWord = line.substring(index1, index2 + 1);
                String word = oldWord;
                String changedWord = word.replace("\"", "");
                changedWord = changedWord.replace(",", "");
                changedWord = changedWord.replace(" ", "");
                line = line.replace(oldWord, changedWord);
            }
        }
        return lines;
    }
    public static String[] removeConsecutiveCommas(String[] lines){
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            while (line.indexOf(",,") != -1){
                line.replace(",,", ",");
            }
        }
        return lines;
    }

    public static ArrayList<ElectionResult> parse2016PresidentialResults(String data) {
        String[] lines = data.split("\n");
        ArrayList<ElectionResult> output = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            String[] dataPieces;
            if (!lines[i].contains("\"")) {
                dataPieces = lines[i].split(",");
            } else {
                dataPieces = splitProblematicStrings(lines[i]);
            }
            dataPieces[7] = dataPieces[7].substring(0, dataPieces[7].length() - 1);
            ElectionResult electionResult = new ElectionResult(Double.valueOf(dataPieces[1]), Double.valueOf(dataPieces[2]), Double.valueOf(dataPieces[3]), Double.valueOf(dataPieces[4]), Double.valueOf(dataPieces[5]), Double.valueOf(dataPieces[6]), Double.valueOf(dataPieces[7]), dataPieces[8], dataPieces[9], Integer.valueOf(dataPieces[10]));
            output.add(electionResult);
        }
        return output;
    }

    private static String[] splitProblematicStrings(String line){
        int loc1 = line.indexOf("\"");
        int loc2 = line.indexOf("\"", loc1 + 1);
        String[] dataPieces = new String[11];
        int previous = 0;
        int current = 0;
        for (int j = 0; j < 10; j++) {
            current = line.indexOf(",", current + 1);
            if (!(current > loc1 && current < loc2)) {
                dataPieces[j] = line.substring(previous + 1, current);
                previous = current;
            } else {
                while (current > loc1 && current < loc2) {
                    current = line.indexOf(",", current + 1);
                }
                current--;
                j--;
            }
        }
        dataPieces[10] = line.substring(previous + 1);
        dataPieces[6] = dataPieces[6].replace("\"", "");
        dataPieces[6] = dataPieces[6].replace(",", "");
        return dataPieces;
    }

    public static String[][] parseGeneral(String data, int startingLine){
        String[] lines = data.split("\n");
        String[][] output = new String[lines.length][52];
        removeQuotationMarks(lines);
        for (int i = startingLine; i < lines.length; i++) {
            String line = lines[i];
            String[] dataPieces = line.split(",");
            output[i] = dataPieces;
        }
        return output;
    }

    public static  DataManager parseAllData(String educationData, String electionData, String employmentData){
        String[][] electionParsed = parseGeneral(electionData, 1);
        String[][] educationParsed = parseGeneral(educationData, 5);
        String[][] employmentParsed = parseGeneral(employmentData, 7);
        DataManager output = new DataManager(new ArrayList<State>());
        ArrayList<State> states = output.getStates();
        for (int i = 0; i < educationParsed.length; i++) {
            String[] strings = employmentParsed[i];
            System.out.println(i);
            if (!states.contains(new State(strings[1], null))) states.add(new State(strings[1], null));
        }
        addElectionParsed(electionParsed, output);
        addEducationParsed(electionParsed, output);
        addEmploymentParsed(electionParsed, output);
        return output;
    }

    private static void addElectionParsed(String[][] electionParsed, DataManager output) {
        ArrayList<State> states = output.getStates();
        for (int i = 0; i < electionParsed.length; i++) {
            String[] dataPieces = electionParsed[i];
            Election2016 input = new Election2016(Double.valueOf(dataPieces[1]), Double.valueOf(dataPieces[2]), Double.valueOf(dataPieces[3]));
            states.get(states.indexOf(dataPieces[8])).getCounties().add(new County(dataPieces[9], Integer.valueOf(dataPieces[10]), input, null, null));
        }
    }

    private static void addEducationParsed(String[][] electionParsed, DataManager dataManager) {
        ArrayList<State> states = dataManager.getStates();
        for (int i = 0; i < electionParsed.length; i++) {
            String[] dataPieces = electionParsed[i];
            String state = dataPieces[1];
            Education2016 input = new Education2016(Double.valueOf(dataPieces[7]), Double.valueOf(dataPieces[8]), Double.valueOf(dataPieces[9]), Double.valueOf(dataPieces[10]));
            State correctState = new State(null, null);
            for (int j = 0; j < states.size(); j++) {
                if (state.equals(states.get(i).getName())) correctState = states.get(i);
            }
            County correctCounty = new County(null, 0, null, null, null);
            for (int j = 0; j < correctState.getCounties().size(); j++) {
                if (correctState.getCounties().get(i).getName().equals(dataPieces[2])) correctCounty = correctState.getCounties().get(i);
            }
            correctCounty.setEduc2016(input);
        }
    }

    private static void addEmploymentParsed(String[][] employmentParsed, DataManager dataManager) {
        ArrayList<State> states = dataManager.getStates();
        for (int i = 0; i < employmentParsed.length; i++) {
            String[] dataPieces = employmentParsed[i];
            String state = dataPieces[1];
            Employment2016 input = new Employment2016(Integer.valueOf(dataPieces[42]), Integer.valueOf(dataPieces[43]), Integer.valueOf(dataPieces[44]), Double.valueOf(dataPieces[45]));
            State correctState = new State(null, null);
            for (int j = 0; j < states.size(); j++) {
                if (state.equals(states.get(i).getName())) correctState = states.get(i);
            }
            County correctCounty = new County(null, 0, null, null, null);
            for (int j = 0; j < correctState.getCounties().size(); j++) {
                if (correctState.getCounties().get(i).getName().equals(dataPieces[2])) correctCounty = correctState.getCounties().get(i);
            }
            correctCounty.setEmploy2016(input);
        }
    }
}
