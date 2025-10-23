import java.util.Scanner;

public class Program {

//    check if name is repeated
    private static final int MAX_STUDENTS = 10;
    private static final int MAX_CLASSES = 10;
    private static final int SEPT_DAYS = 30; // September 2020
    private static final String[] WEEK_DAYS = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};

    // Utility messages
    private static void error(String msg)
    {
        System.err.println("Error: " + msg);
    }

//    private static void info(String msg) {
//        System.out.println("Info:  " + msg);
//    }

    // Validation helpers
    private static boolean isValidHour(int hour)
    {
        if (hour < 1 || hour > 6) {
            error("Class hour must be between 1pm and 6pm.");
            return (false);
        }
        return (true);
    }

    private static boolean isValidDay(String day)
    {
        for (String d : WEEK_DAYS)
        {
            if (d.equals(day))
                return (true);
        }
        error("Invalid day of the week. Use one of: MO TU WE TH FR SA SU");
        return (false);
    }

    private static boolean isValidDayOfMonth(int day)
    {
        if (day < 1 || day > SEPT_DAYS)
        {
            error("Day of month must be between 1 and " + SEPT_DAYS + ".");
            return (false);
        }
        return (true);
    }

    // Input reading PART 1
    private static int readStudents(Scanner sc, String[] students)
    {
        int count = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("."))
                break;
            if (line.length() > 10)
            {
                error("Student name too long (max 10 characters). Skipping: " + line);
                continue;
            }
            if (findStudent(students, count, line) != -1) {
                error("Duplicate student name: '" + line + "'. Skipping: " + line);
                continue;
            }

            if (count >= MAX_STUDENTS)
            {
                error("Maximum number of students (" + MAX_STUDENTS + ") reached.");
                break;
            }
            students[count++] = line;
        }
        return (count);
    }

    // Input reading PART 2
    private static int readClasses(Scanner sc, int[] hours, String[] days)
    {
        int count = 0;
        while (sc.hasNextLine())
        {
            String line = sc.nextLine().trim();
            if (line.equals(".")) break;

            String[] parts = line.split("\\s+");
            if (parts.length != 2)
            {
                error("Invalid class format. Expected: 'hour & day'");
                continue;
            }

            int hour;
            try
            {
                hour = Integer.parseInt(parts[0]);
            }
            catch (NumberFormatException e)
            {
                error("Invalid hour: " + parts[0]);
                continue;
            }

            String day = parts[1];
            if (!isValidHour(hour) || !isValidDay(day))
                continue;

            if (count >= MAX_CLASSES)
            {
                error("Maximum number of classes (" + MAX_CLASSES + ") reached.");
                break;
            }

            hours[count] = hour;
            days[count] = day;
            count++;
        }
        return (count);
    }

    // Input reading PART 3
    private static int findStudent(String[] students, int count, String name)
    {
        for (int i = 0; i < count; i++)
        {
            if (students[i].equals(name))
                return (i);
        }
        return (-1);
    }

    private static void readAttendance(Scanner sc, String[] students, int studentCount, int[][] attendance)
    {
        while (sc.hasNextLine())
        {
            String line = sc.nextLine().trim();
            if (line.equals("."))
                break;

            String[] parts = line.split("\\s+");
            if (parts.length != 4) {
                error("Invalid attendance format. Expected: 'name, hour, day & HERE/NOT_HERE'");
                continue;
            }

            String name = parts[0];
            int studentIdx = findStudent(students, studentCount, name);
            if (studentIdx == -1) {
                error("Unknown student: " + name);
                continue;
            }

            int hour, day;
            try {
                hour = Integer.parseInt(parts[1]);
                day = Integer.parseInt(parts[2]);
            }
            catch (NumberFormatException e) {
                error("Invalid numeric value in attendance entry: " + line);
                continue;
            }

            if (!isValidHour(hour) || !isValidDayOfMonth(day))
                continue;

            String status = parts[3];
            int value = status.equals("HERE") ? 1 : status.equals("NOT_HERE") ? -1 : 0;
            if (value == 0) {
                error("Invalid status. Use HERE or NOT_HERE.");
                continue;
            }

            attendance[studentIdx][day] = value;
        }
    }

    // Timetable building and sorting
    private static int buildTimetable(int[] cHours, String[] cDays, int cCount, int[] tHours, String[] tDays, int[] tDates)
    {
        String[] dayName = new String[SEPT_DAYS + 1];
        for (int d = 1; d <= SEPT_DAYS; d++)
        {
            dayName[d] = WEEK_DAYS[(d) % 7]; // 1 Sep 2020 = Tuesday
        }

        int count = 0;
        for (int c = 0; c < cCount; c++) {
            for (int d = 1; d <= SEPT_DAYS; d++)
            {
                if (dayName[d].equals(cDays[c]))
                {
                    tHours[count] = cHours[c];
                    tDays[count] = cDays[c];
                    tDates[count] = d;
                    count++;
                }
            }
        }
        return (count);
    }

    private static void swap(int[] arr, int i, int j)
    {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static void swap(String[] arr, int i, int j)
    {
        String tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static void sortTimetable(int[] hours, String[] days, int[] dates, int n)
    {
        for (int i = 0; i < n - 1; i++)
        {
            int min = i;
            for (int j = i + 1; j < n; j++)
            {
                if (dates[j] < dates[min] || (dates[j] == dates[min] && hours[j] < hours[min]))
                    min = j;
            }
            swap(hours, i, min);
            swap(days, i, min);
            swap(dates, i, min);
        }
    }

    // Output
    private static void printHeader(int[] hours, String[] days, int[] dates, int count)
    {
        System.out.printf("%-12s", "");
        for (int i = 0; i < count; i++)
            System.out.printf("| %-11s", String.format("%d:00 %s %2d", hours[i], days[i], dates[i]));
        System.out.println("|");
    }

    private static void printAttendanceTable(String[] students, int sCount, int[][] att, int[] tDates, int tCount)
    {
        for (int s = 0; s < sCount; s++)
        {
            System.out.printf("%-12s", students[s]);
            for (int i = 0; i < tCount; i++)
            {
                int v = att[s][tDates[i]];
                String mark = (v == 1) ? "1" : (v == -1) ? "-1" : "";
                System.out.printf("| %-11s", mark);
            }
            System.out.println("|");
        }
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        String[] students = new String[MAX_STUDENTS];
        int studentCount = readStudents(sc, students);

        int[] classHours = new int[MAX_CLASSES];
        String[] classDays = new String[MAX_CLASSES];
        int classCount = readClasses(sc, classHours, classDays);

        int[][] attendance = new int[MAX_STUDENTS][SEPT_DAYS + 1];
        readAttendance(sc, students, studentCount, attendance);

        int[] tHours = new int[31];
        String[] tDays = new String[31];
        int[] tDates = new int[31];

        int tCount = buildTimetable(classHours, classDays, classCount, tHours, tDays, tDates);
        sortTimetable(tHours, tDays, tDates, tCount);

        printHeader(tHours, tDays, tDates, tCount);
        printAttendanceTable(students, studentCount, attendance, tDates, tCount);
    }
}
