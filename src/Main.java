import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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