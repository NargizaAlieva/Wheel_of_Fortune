import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Character> wrongChar = new ArrayList<Character>();
        ArrayList<Character> correctChar = new ArrayList<Character>();
        int randomIndex = chooseRandomIndex();
        char[] wordSpase = new char[convertWordToArray(randomIndex).length];
        Arrays.fill(wordSpase, '_');

        String[][] playersMatrix = createPlayersMatrix();
        String playerInput = " ";
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static String[][] createPlayersMatrix() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Enter players number: ");
        int playersNumber = scanner.nextInt();

        String[][] playersNameScore = new String[playersNumber][playersNumber];

        for (int i = 0; i < playersNumber; i++) {
            playersNameScore[i][0] = "0";
            playersNameScore[i][1] = "0";
        }
        for (int j = 0; j < playersNumber; j++) {
            if(j > 0)
                System.out.print("Enter your name: ");
            int randomIndex = random.nextInt(playersNumber);
            while (!playersNameScore[randomIndex][0].equals("0")) {
                randomIndex = random.nextInt(playersNumber);
            }
            playersNameScore[randomIndex][0] = scanner.nextLine();
        }

        for (int j = 0; j < playersNumber; j++) {
            if (playersNameScore[j][0].isEmpty()) {
                System.out.print("Enter your name: ");
                playersNameScore[j][0] = scanner.nextLine();
            }
        }

        return playersNameScore;

    }
    public static boolean isLetterRepeated (String playerInput, ArrayList<Character> wrongChars, ArrayList<Character> correctChars) {
        if (isItLetter(playerInput)) {
            for (Character character : correctChars) {
                if (convertInputToArray(playerInput)[0] == character) {
                    return true;
                }
            }
            for (Character character : wrongChars) {
                if (convertInputToArray(playerInput)[0] == character) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isWordCorrect(String playerInput, int randomIndex) {
        return createDatabase()[randomIndex][0].equals(playerInput);
    }

    public static boolean isLetterCorrect(String playerInput, int randomIndex) {
        for (int j = 0; j < convertWordToArray(randomIndex).length; j++) {
            if (convertWordToArray(randomIndex)[j] == convertInputToArray(playerInput)[0])
                return true;
        }
        return false;
    }

    public static boolean isItLetter(String playerInput) {
        char[] inputtedWord = convertInputToArray(playerInput);
        return inputtedWord.length == 1;
    }

    public static char[] convertInputToArray(String playerInput) {
        return playerInput.toLowerCase().toCharArray();
    }

    public static void printUpdatedDescription(String[][] playersMatrix, String playerInput, ArrayList<Character> wrongChars, ArrayList<Character> correctChars, int randomIndex, char[] wordSpase) {
        clearScreen();
        printPlayersScore (playersMatrix);
        updateAlphabet(playerInput, randomIndex, wrongChars, correctChars);

        System.out.print("\n\u001B[36m" + createDatabase()[randomIndex][1] + "\n\u001B[0m");

        if (isItLetter(playerInput)) {
            for (int i = 0; i < wordSpase.length; i++) {
                if (isLetterCorrect(playerInput, randomIndex)) {
                    if (convertWordToArray(randomIndex)[i] == convertInputToArray(playerInput)[0])
                        wordSpase[i] = convertWordToArray(randomIndex)[i];
                }
            }
        }

        for (char letter : wordSpase) {
            if (letter != '_')
                System.out.print("\u0332" + letter + " ");
            else
                System.out.print(letter + " ");
        }
        System.out.println("\n");
    }

    public static void printPlayersScore (String[][] playersMatrix) {
        char[] longestName = convertInputToArray(playersMatrix[0][0]);
        for (int n = 1; n < playersMatrix.length; n++) {
            if (longestName.length < convertInputToArray(playersMatrix[n][0]).length)
                longestName = convertInputToArray(playersMatrix[n][0]);
        }

        for (int m = 0; m < longestName.length + 12; m++)
            System.out.print("\u001B[36m" + "_" + "\u001B[0m");
        for (String[] matrix : playersMatrix) {
            System.out.println();
            System.out.print("\u001B[36m| \u001B[0m" + matrix[0] + "\u001B[33m ---> \u001B[34m" + matrix[1]);
        }
        System.out.println();
        for (int m = 0; m < longestName.length + 12; m++) {
            System.out.print("\u001B[36m" + "_" + "\u001B[0m");
        }
        System.out.println("\n");
    }

    public static void updateWordSpace(String playerInput, int randomIndex, char[] wordSpace) {
        for (int i = 0; i < wordSpace.length; i++) {
            if (isLetterCorrect(playerInput, randomIndex)) {
                if (convertWordToArray(randomIndex)[i] == convertInputToArray(playerInput)[0])
                    wordSpace[i] = convertWordToArray(randomIndex)[i];
            }
        }
    }

    public static void updateAlphabet(String playerInput, int randomIndex, ArrayList<Character> wrongChars, ArrayList<Character> correctChars) {
        char inputtedChar = convertInputToArray(playerInput)[0];
        if (!isLetterRepeated(playerInput, wrongChars, correctChars))
            if (isItLetter(playerInput)) {
                if (isLetterCorrect(playerInput, randomIndex))
                    correctChars.add(inputtedChar);
                else
                    wrongChars.add(inputtedChar);
            }

        for (int i = 0; i < createAlphabet().length; i++) {
            boolean isLetterPrinted = false;
            for (Character character : correctChars) {
                if (createAlphabet()[i] == character) {
                    isLetterPrinted = true;
                    System.out.print("\u001B[32m" + character + " \u001B[0m");
                }
            }

            for (Character character : wrongChars) {
                if (createAlphabet()[i] == character) {
                    isLetterPrinted = true;
                    System.out.print("\u001B[31m" + character + " \u001B[0m");
                }
            }
            if (!isLetterPrinted)
                System.out.print(createAlphabet()[i] + " ");
        }
        System.out.println();

    }

    public static char[] createAlphabet() {
        char[] alphabet = new char[26];
        char letter = 'a';
        for (int i = 0; i < alphabet.length; i++) {
            alphabet[i] = letter;
            letter++;
        }
        return alphabet;
    }

    public static char[] convertWordToArray(int randomIndex) {
        return createDatabase()[randomIndex][0].toCharArray();
    }

    public static int chooseRandomIndex() {
        Random random = new Random();
        return random.nextInt(createDatabase().length);
    }

    public static String[][] createDatabase() {
        String[][] wordDatabase = new String[14][14];

        wordDatabase[0][0] = "obedience";
        wordDatabase[0][1] = "Dumplings have long been prepared in the form of ears. What do such dumplings symbolize?";

        wordDatabase[1][0] = "teenager";
        wordDatabase[1][1] = "In Australia, in parking lots near some shopping malls, classical music is played at night and in the evenings to scare someone away. Who?";

        wordDatabase[2][0] = "disappointment";
        wordDatabase[2][1] = "Sarah Churchill, the Duchess of 18th century England, said: \"You are young if you are still capable of...\"";

        wordDatabase[3][0] = "confidence";
        wordDatabase[3][1] = "English writer Kipling said, \"A woman's intuition is far more accurate than a man's...\"";

        wordDatabase[4][0] = "infertility";
        wordDatabase[4][1] = "Harvard University researchers found that eating ice cream in large quantities reduced the risk of this.";

        wordDatabase[5][0] = "loneliness";
        wordDatabase[5][1] = "Jewelers often say that diamonds need this.";

        wordDatabase[6][0] = "talkativeness";
        wordDatabase[6][1] = "What unusual reason exists in China for the dissolution of a marriage?";

        wordDatabase[7][0] = "octopus";
        wordDatabase[7][1] = "An animal that rivals humans, owls, and cats in its eyesight.";

        wordDatabase[8][0] = "scorpion";
        wordDatabase[8][1] = "Which creature can hold its breath for 6 days?";

        wordDatabase[9][0] = "eggplant";
        wordDatabase[9][1] = "The name of this plant comes from the Greek for \"generating purity\"";

        wordDatabase[10][0] = "crocodile";
        wordDatabase[10][1] = "What animal can digest a steel nail?";

        wordDatabase[11][0] = "platypus";
        wordDatabase[11][1] = "What animal gives milk and lays eggs at the same time?";

        wordDatabase[12][0] = "bolivia";
        wordDatabase[12][1] = "Which country in the world has two capitals?";

        wordDatabase[13][0] = "sewerage";
        wordDatabase[13][1] = "The first such museum appeared in Paris before 1975. Excursions on it were conducted by boat. Now tourists view its exhibits from special grids and ramps. Which museum are we talking about?";

        return wordDatabase;
    }
}