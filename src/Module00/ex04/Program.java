import java.util.Scanner;


public class Program {

    // Count frequency of each character
    public static int[] countFrequencies(String input){
        int[] freq = new int[65536];
        for (int i = 0; i < input.length(); i++)
            freq[input.charAt(i)]++;
        return freq;
    }

    // Find top 10 most frequent characters
    public static void findTop10(int[] freq, char[] topChars, int[] topFreqs){
        for (int i = 0; i < 65536; i++) {
            if (freq[i] == 0)
                continue;

            for (int j = 0; j < 10; j++)
            {
                if (freq[i] > topFreqs[j] || (freq[i] == topFreqs[j] && i < topChars[j]))
                 {
                    // Shift elements to the right
                    for (int k = 9; k > j; k--) {
                        topFreqs[k] = topFreqs[k - 1];
                        topChars[k] = topChars[k - 1];
                    }
                    topFreqs[j] = freq[i];
                    topChars[j] = (char) i;
                    break;
                }
            }
        }
    }

    public static void printHistogram(char[] topChars, int[] topCounts){
        int maxCount = topCounts[0];
        if (maxCount == 0) {
            System.out.println();
            return;
        }

        // the height of each column and max number length
        int[] heights = new int[10];
        int maxNumLength = 0;
        for (int col = 0; col < 10; col++) {
            if (topCounts[col] > 0) {
                heights[col] = (topCounts[col] * 10) / maxCount;
                maxNumLength = Math.max(maxNumLength, String.valueOf(topCounts[col]).length());
            }
        }

        // Print the histogram with numbers above the bars
        for (int row = 10; row >= -1; row--) {
            for (int col = 0; col < 10; col++) {
                if (topCounts[col] == 0) continue;

                if (row == -1) {
                    // Bottom line - print character with consistent spacing
                    System.out.print(String.format("%-" + (maxNumLength + 1) + "s", topChars[col] + " "));
                }
                else if (row == heights[col]) {
                    // Print the number with consistent spacing
                    System.out.print(String.format("%-" + (maxNumLength + 1) + "d", topCounts[col]));
                }
                else if (row < heights[col]) {
                    // Print the bar with consistent spacing
                    System.out.print(String.format("%-" + (maxNumLength + 1) + "s", "# "));
                }
                else {
                    // Empty space with consistent spacing
                    System.out.print(String.format("%-" + (maxNumLength + 1) + "s", " "));
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
       Scanner in = new Scanner(System.in);
       String input = in.nextLine();
       in.close();

        int[] freq = countFrequencies(input);
        char[] topChars = new char[10];
        int[] topFreqs = new int[10];

        findTop10(freq, topChars, topFreqs);
        printHistogram(topChars, topFreqs);
    }
}

// AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAASSSSSSSSSSSSSSSSSSSSSSSSDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDWEWWKFKKDKKDSKAKLSLDKSKALLLLLLLLLLRTRTETWTWWWWWWWWWWOOOOOOO42
