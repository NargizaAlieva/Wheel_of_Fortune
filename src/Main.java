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
        int playerIndex = 0;

        while (isGameOver(playersMatrix, wordSpase)) {
            if (playerIndex >= playersMatrix.length)
                playerIndex = 0;

            printUpdatedDescription(playersMatrix, playerInput, wrongChar, correctChar, randomIndex, wordSpase);

            System.out.println("\u001B[33m" + playersMatrix[playerIndex][0] + "\u001B[0m your turn!");
            System.out.print("Enter Letter (or word): ");
            playerInput = scanner.nextLine();
            while (isLetterRepeated (playerInput, wrongChar,  correctChar)) {
                printUpdatedDescription(playersMatrix, playerInput, wrongChar, correctChar, randomIndex, wordSpase);

                System.out.print("\u001B[33m" + playersMatrix[playerIndex][0] + "\u001B[0m, this letter was already chosen.\nPlease enter another letter (or word): ");
                playerInput = scanner.nextLine();
            }

            if (isItLetter(playerInput)) {
                updateWordSpace(playerInput, randomIndex, wordSpase);

                if (isLetterCorrect(playerInput, randomIndex)) {
                    addPlayersPoints(playersMatrix, playerIndex, randomIndex, playerInput);

                    while (isGameOver(playersMatrix, wordSpase) && isLetterCorrect(playerInput, randomIndex)) {
                        printUpdatedDescription(playersMatrix, playerInput, wrongChar, correctChar, randomIndex, wordSpase);

                        System.out.println("\u001B[33m" + playersMatrix[playerIndex][0] + "\u001B[0m your guessed the letter! You have extra chance!");
                        System.out.print("Enter Letter (or word): ");
                        playerInput = scanner.nextLine();

                        while (isLetterRepeated (playerInput, wrongChar,  correctChar)) {
                            printUpdatedDescription(playersMatrix, playerInput, wrongChar, correctChar, randomIndex, wordSpase);

                            System.out.print("\u001B[33m" + playersMatrix[playerIndex][0] + "\u001B[0m, this letter was already chosen.\nPlease enter another letter (or word): ");
                            playerInput = scanner.nextLine();
                        }

                        if (isItLetter(playerInput)) {
                            updateWordSpace(playerInput, randomIndex, wordSpase);
                            addPlayersPoints(playersMatrix, playerIndex, randomIndex, playerInput);
                        } else {
                            if (!isWordCorrect(playerInput, randomIndex))
                                playersMatrix = excludePlayer(playersMatrix, playerIndex);
                            else {
                                System.out.println("Congratulations " + playersMatrix[playerIndex][0] + " you win! You guessed the word!");
                                System.exit(0);
                            }
                        }
                    }
                }
            } else {
                if (!isWordCorrect(playerInput, randomIndex)) {
                    if (playersMatrix.length == 2) {
                        if (playerIndex + 1 >= playersMatrix.length) {
                            playerIndex = 0;
                            System.out.println("Congratulations " + playersMatrix[playerIndex][0] + " you are the last player so you win!");
                        } else {
                            System.out.println("Congratulations " + playersMatrix[++playerIndex][0] + " you are the last player so you win!");
                        }
                        System.exit(0);
                    }
                    playersMatrix = excludePlayer(playersMatrix, playerIndex);
                } else {
                    System.out.println("Congratulations " + playersMatrix[playerIndex][0] + " you win! You guessed the word!");
                    System.exit(0);
                }
            }
            playerIndex++;
        }

        printFinalMessage (playersMatrix, wordSpase, randomIndex, playerIndex);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static boolean isThereMoreWinners(String[][] playersMatrix) {
        int winnersNumber = 0;
        for (String[] plNum : playersMatrix) {
            if (identifyHighestScore(playersMatrix) == Integer.parseInt(plNum[1])) {
                winnersNumber++;
            }
        }
        return winnersNumber > 1;
    }

    public static void printFinalMessage (String[][] playersMatrix, char[] wordSpase, int randomIndex, int playerNumber) {
        clearScreen();
        printPlayersScore (playersMatrix);
        String winnersName = "";
        if (isThereMoreWinners(playersMatrix)) {
            System.out.println("Friendship wins!");
            System.exit(0);
        }else if (isHalfWordGuessed(playersMatrix)) {
            winnersName = playersMatrix[startFinalRound(playersMatrix, playerNumber, randomIndex)][0];

        } else if (isWordFullyGuessed(wordSpase)) {
            for (String[] matrix : playersMatrix)
                if (Integer.parseInt(matrix[1]) == identifyHighestScore(playersMatrix))
                    winnersName = matrix[0];
        }
        System.out.println("Congratulations " + winnersName + " you win! You got highest score!");
    }

    public static int startFinalRound(String[][] playersMatrix, int playerIndex, int randomIndex) {
        Scanner scanner = new Scanner(System.in);

        int playerWithHighestScore = playerIndex - 1;
        if (playerIndex - 1 < 0)
            playerWithHighestScore = 0;

        int index = playerIndex;
        for (int i = 1; i < playersMatrix.length; i++) {
            if (index >= playersMatrix.length)
                index = 0;
            System.out.println(playersMatrix[index][0] + ", please enter word!");
            String playerInput = scanner.nextLine();
            clearScreen();
            printPlayersScore(playersMatrix);
            if (isWordCorrect(playerInput, randomIndex))
                return index;
            index++;
        }
        return playerWithHighestScore;
    }

    public static boolean isGameOver (String[][] playersMatrix, char[] wordSpase) {
        return !isWordFullyGuessed(wordSpase) && !isHalfWordGuessed(playersMatrix);
    }

    public static boolean isHalfWordGuessed (String[][] playersMatrix) {
        return identifyHighestScore(playersMatrix) > 600;
    }

    public static boolean isWordFullyGuessed(char[] wordSpase) {
        for (char c : wordSpase)
            if (c == '_')
                return false;
        return true;
    }

    public static String[][] excludePlayer(String[][] playersMatrix, int playerIndex) {
        String[][] updatedPlayersMatrix = new String[playersMatrix.length - 1][2];
        int index = 0;
        for(int t = 0; t < playersMatrix.length; t++) {
            if (t == playerIndex)
                t++;
            if (t >= playersMatrix.length)
                t = 0;
            System.arraycopy(playersMatrix[t], 0, updatedPlayersMatrix[index], 0, 2);
            index++;
        }
        return updatedPlayersMatrix;
    }


    public static int identifyHighestScore(String[][] playersMatrix) {
        int highestScore = 0;
        for (String[] plNum : playersMatrix) {
            if (highestScore < Integer.parseInt(plNum[1])) {
                highestScore = Integer.parseInt(plNum[1]);
            }
        }
        return highestScore;
    }

    public static void addPlayersPoints(String[][] playersMatrix, int playerIndex, int randomIndex, String playerInput) {
        int letterCount = 0;
        for (int l = 0; l < convertWordToArray(randomIndex).length; l++) {
            if (convertWordToArray(randomIndex)[l] == convertInputToArray(playerInput)[0])
                letterCount++;
        }
        playersMatrix[playerIndex][1] = String.valueOf(Integer.parseInt(playersMatrix[playerIndex][1]) + letterCount * countPointsForOneLetter(randomIndex));
    }

    public static int countPointsForOneLetter(int randomIndex) {
        return (int) Math.round(1000.0 / convertWordToArray(randomIndex).length);
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