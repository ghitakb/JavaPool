import java.util.Scanner;

public class Program {

    // Reads and validates the week header (e.g., "Week 1")
    public static void validateWeekHeader(String inputLine, int expectedWeek) {
        if (!inputLine.equals("Week " + expectedWeek)) {
            System.err.println("IllegalArgument");
            System.exit(-1);
        }
    }

    // Extracts the minimum value from a string of 5 test scores
    public static int findMinValue(String line) {
        Scanner lineScanner = new Scanner(line);
        int min = 10;
        int count = 0;

        while (lineScanner.hasNextInt()) {
            int num = lineScanner.nextInt();
            if (num < 1 || num > 9) {
                continue; // skip invalid scores silently
            }
            if (num < min) {
                min = num;
            }
            count++;
        }

        lineScanner.close();

        if (count != 5) {
            System.err.println("IllegalArgument");
            System.exit(-1);
        }

        return min;
    }

    // Prints the final chart output line by line
    public static void printProgressChart(String minValues) {
        Scanner minScanner = new Scanner(minValues);
        int weekNumber = 1;

        while (minScanner.hasNextInt()) {
            int min = minScanner.nextInt();
            System.out.print("Week " + weekNumber + " ");
            for (int i = 0; i < min; i++) {
                System.out.print("=");
            }
            System.out.println(">");
            weekNumber++;
        }

        minScanner.close();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StringBuilder minResults = new StringBuilder();
        int expectedWeek = 1;

        while (expectedWeek <= 18) {
            String headerLine = input.nextLine();

            if (headerLine.equals("42")) {
                break;
            }

            validateWeekHeader(headerLine, expectedWeek);

            String scoresLine = input.nextLine();
            int min = findMinValue(scoresLine);
            minResults.append(min).append(" ");
            expectedWeek++;
        }

        printProgressChart(minResults.toString());
        input.close();
    }
}
