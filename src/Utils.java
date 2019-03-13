import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {
    private static int educStartingLine = 5;
    private static int educEndingLine = 3209;
    private static int electionStartingLine = 1;
    private static int electionEndingLine = 3141;
    private static int employStartingLine = 7;
    private static int employEndingLine = 3204;

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

    public static String[][] parseGeneral(String data, int startingLine, int endingLine){
        String[] lines = data.split("\n");
        String[][] output = new String[lines.length][52];
        removeQuotationMarks(lines);
        for (int i = startingLine; i < endingLine; i++) {
            String line = lines[i];
            String[] dataPieces = line.split(",");
            output[i] = dataPieces;
        }
        return output;
    }

    public static  DataManager parseAllData(String electionData, String educationData, String employmentData){
        String[][] electionParsed = parseGeneral(electionData, electionStartingLine, electionEndingLine);
        String[][] educationParsed = parseGeneral(educationData, educStartingLine, educEndingLine);
        String[][] employmentParsed = parseGeneral(employmentData, employStartingLine, employEndingLine );
        DataManager output = new DataManager(new ArrayList<State>());
        ArrayList<State> states = output.getStates();
        for (int i = educStartingLine; i < educEndingLine; i++) {
            String[] strings = educationParsed[i];
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
        for (int i = electionStartingLine; i < electionEndingLine; i++) {
            String[] dataPieces = electionParsed[i];
            Election2016 input = new Election2016(Double.valueOf(dataPieces[1]), Double.valueOf(dataPieces[2]), Double.valueOf(dataPieces[3]));
            states.get(states.indexOf(dataPieces[8])).getCounties().add(new County(dataPieces[9], Integer.valueOf(dataPieces[10]), input, null, null));
        }
    }

    private static void addEducationParsed(String[][] electionParsed, DataManager dataManager) {
        for (int i = educStartingLine; i < educEndingLine; i++) {
            String[] dataPieces = electionParsed[i];
            String stateName = dataPieces[1];
            String county = dataPieces[2];
            Education2016 input = new Education2016(Double.valueOf(dataPieces[7]), Double.valueOf(dataPieces[8]), Double.valueOf(dataPieces[9]), Double.valueOf(dataPieces[10]));
            State state = findStateByName(stateName, dataManager);
            County correctCounty = findCountyByName(county, state);
            if (correctCounty.equals(null)) correctCounty = new County(county, Integer.valueOf(dataPieces[0]), null, null, null);
            correctCounty.setEduc2016(input);
        }
    }

    private static void addEmploymentParsed(String[][] employmentParsed, DataManager dataManager) {
        for (int i = employStartingLine; i < employEndingLine; i++) {
            String[] dataPieces = employmentParsed[i];
            String stateName = dataPieces[1];
            String county = dataPieces[2];
            Employment2016 input = new Employment2016(Integer.valueOf(dataPieces[42]), Integer.valueOf(dataPieces[43]), Integer.valueOf(dataPieces[44]), Double.valueOf(dataPieces[45]));
            State state = findStateByName(stateName, dataManager);
            County correctCounty = findCountyByName(county, state);
            if (correctCounty.equals(null)) correctCounty = new County(county, Integer.valueOf(dataPieces[0]), null, null, null);
            correctCounty.setEmploy2016(input);
        }
    }
    private static State findStateByName (String stateName, DataManager dataManager){
        for (int i = 0; i < dataManager.getStates().size(); i++) if (dataManager.getStates().get(i).getName().equals(stateName)) return dataManager.getStates().get(i);
        return null;
    }
    private static County findCountyByName(String countyName, State state){
        for (int i = 0; i < state.getCounties().size(); i++) if (state.getCounties().get(i).getName().equals(countyName)) return state.getCounties().get(i);
        return null;
    }
}
