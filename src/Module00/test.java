import java.util.Scanner;
import java.util.Arrays;


public class test {

    /* ==== CONSTANTS ======================================================= */
    private static final int MAX_STUDENTS = 10;
    private static final int MAX_CLASSES  = 10;
    private static final int SEPT_DAYS    = 30;     // September 2020
    private static final String[] WEEK_DAYS = {"TU", "WE", "TH", "FR", "SA", "SU", "MO"}; // 1 Sep 2020 was Tuesday


    /* ==== MAIN ============================================================ */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        /* ---------- 1. students ---------- */
        String[] students = new String[MAX_STUDENTS];
        int studentCount = readStudents(sc, students);

        /* ---------- 2. weekly classes ---- */
        int[] classHours  = new int[MAX_CLASSES];   // 1‑6  -> 1pm …6pm
        String[] classDays = new String[MAX_CLASSES]; // MO … SU
        int classCount = readClasses(sc, classHours, classDays);

        /* ---------- 3. attendance -------- */
        int[][] attendance = new int[MAX_STUDENTS][SEPT_DAYS + 1];
        readAttendance(sc, students, studentCount, attendance);
        for (int i = 0; i < attendance.length; i++) { // Iterate through rows
            for (int j = 0; j < attendance[i].length; j++) { // Iterate through columns of the current row
                System.out.print(attendance[i][j] + " "); // Print element and a space
            }
            System.out.println(); // Move to the next line after printing a row
        }

        /* ---------- 4. build timetable --- */
        int[] tHours  = new int[31];     // actual lesson list (≤30)
        String[] tDays = new String[31];
        int[] tDates  = new int[31];
        int tCount = buildTimetable(classHours, classDays, classCount,
                tHours, tDays, tDates);
        sortTimetable(tHours, tDays, tDates, tCount);

        /* ---------- 5. print ------------- */
        printHeader(tHours, tDays, tDates, tCount);
        printAttendanceTable(students, studentCount,
                attendance, tDates, tCount);
    }

    /* ==== STEP‑1 : students ============================================== */
    private static int readStudents(Scanner sc, String[] students) {
        int n = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if(line.length() > 10) {
                System.out.println("length check, Maximum length of a student’s name is 10  ");
                continue;
            }
            if (line.equals(".")) break;
            students[n++] = line;               // correctness guaranteed
        }
        return n;
    }

    /* ==== STEP‑2 : weekly class slots ==================================== */
    private static int readClasses(Scanner sc, int[] hours, String[] days) {
        int n = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals(".")) break;
            String[] p = line.split(" ");
            hours[n] = Integer.parseInt(p[0]);  // 1 … 6
            days[n]  = p[1];                    // MO … SU
            n++;
        }
        return n;
    }

    /* ==== STEP‑3 : attendance records ==================================== */
    private static void readAttendance(Scanner sc, String[] students, int studentCount, int[][] attendance) {

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals(".")) break;

            String[] p = line.split(" ");
            String name   = p[0];
            int hour      = Integer.parseInt(p[1]);   // not needed but provided
            int dayOfMonth= Integer.parseInt(p[2]);   // 1‑30
            String status = p[3];                     // HERE / NOT_HERE

            int sIdx = findStudent(students, studentCount, name);
            if (sIdx != -1 && dayOfMonth >= 1 && dayOfMonth <= SEPT_DAYS) {
                System.out.println("sIdx = " + sIdx);

                attendance[sIdx][dayOfMonth] = status.equals("HERE") ? 1 : -1;
            }
        }
    }

    private static int findStudent(String[] students, int n, String name) {
        for (int i = 0; i < n; i++) {
            if (students[i].equals(name)) return i;
        }
        return -1;
    }

    /* ==== STEP‑4 : expand weekly slots into actual dates ================= */
    private static int buildTimetable(int[] cHours, String[] cDays, int cCount,
                                      int[] tHours, String[] tDays, int[] tDates) {

        /* weekday for each date in September */
        String[] dayName = new String[SEPT_DAYS + 1];  // 1‑based
        for (int d = 1; d <= SEPT_DAYS; d++) {
            dayName[d] = WEEK_DAYS[(d - 1) % 7];
        }

        int t = 0;
        for (int c = 0; c < cCount; c++) {
            for (int d = 1; d <= SEPT_DAYS; d++) {
                if (dayName[d].equals(cDays[c])) {
                    tHours[t]  = cHours[c];
                    tDays[t]   = cDays[c];
                    tDates[t]  = d;
                    t++;
                }
            }
        }
        System.out.println("cHours = " + Arrays.toString(cHours) +
                "\ncDays = " + Arrays.toString(cDays) +
                "\ncCount = " + (cCount) +
                "\ntHours = " + Arrays.toString(tHours) +
                "\ntDays = " + Arrays.toString(tDays) +
                "\ntDates = " + Arrays.toString(tDates) );
        System.out.println("t = " + t);
        return t;   // number of actual lessons
    }

    /* small selection‑sort: by date, then by hour ------------------------- */
    private static void sortTimetable(int[] h, String[] d, int[] date, int n) {
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (date[j] < date[min] ||
                        (date[j] == date[min] && h[j] < h[min])) {
                    min = j;
                }
            }
            swap(h, i, min);
            swap(d, i, min);
            swap(date, i, min);
        }
    }

    private static void swap(int[] a, int i, int j) {
        int tmp = a[i]; a[i] = a[j]; a[j] = tmp;
    }
    private static void swap(String[] a, int i, int j) {
        String tmp = a[i]; a[i] = a[j]; a[j] = tmp;
    }

    /* ==== STEP‑5A : header line ========================================= */
    private static void printHeader(int[] h, String[] day, int[] date, int n) {
        System.out.printf("%-12s", ""); // leave space for name column
        for (int i = 0; i < n; i++) {
            System.out.printf("| %-11s", String.format("%d:00 %s %2d", h[i], day[i], date[i]));
        }
        System.out.println("|");
    }


    /* ==== STEP‑5B : attendance table ==================================== */
    private static void printAttendanceTable(String[] students, int sCount,
                                             int[][] att,
                                             int[] tDates, int tCount) {
        for (int s = 0; s < sCount; s++) {
            System.out.printf("%-12s", students[s]);  // left-align student name (12 chars)
            for (int i = 0; i < tCount; i++) {
                int v = att[s][tDates[i]];
                String content = (v == 1) ? "1" : (v == -1) ? "-1" : "";
                System.out.printf("| %-11s", content); // | content with padding
            }
            System.out.println("|"); // final closing pipe
        }
    }

}

//a
//.
//2 MO
//.
//a 2 28 HERE
/*
* import java.util.Scanner;
import java.util.Arrays;


public class Program {


//    Classes can be held on any day of week between 1 pm and 6 pm
//    total classes per week cannot exceed 10.
//     Maximum length of a student’s name is 10 (no spaces).


    private static final int MAX_STUDENTS = 10;
    private static final int MAX_CLASSES  = 10;
    private static final int SEPT_DAYS    = 30;     // September 2020
    private static final String[] WEEK_DAYS = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"}; // 1 Sep 2020 was Tuesday


    private static int readStudents(Scanner sc, String[] students) {
        int n = 0;
        while (sc.hasNextLine())
        {
            String line = sc.nextLine().trim();
            if(line.length() > 10) {
                System.err.println("length check, Maximum length of a student’s name is 10");
                continue;
            }
            if (line.equals("."))
                break;
            students[n++] = line;               // correctness guaranteed
//            System.out.println("n = " + n);
            if (n > MAX_STUDENTS)
            {
                System.err.println("Number of Students check, Maximum Number of a students is 10");
                continue;
            }


        }
        return n;
    }

    private static int readClasses(Scanner sc, int[] hours, String[] days) {
        int n = 0;

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("."))
                break;

            String[] p = line.split(" ");
            if (p.length != 2) {
                System.out.println("Invalid input format. Expected: hour & day");
                continue;
            }

            //  Validate hour
            try {
                int hour = Integer.parseInt(p[0]);
                if (hour < 1 || hour > 6) {
                    System.out.println("Classes should be between 1 pm and 6 pm.");
                    continue;
                }
                hours[n] = hour;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format: " + p[0]);
                continue;
            }

            // ----- Validate day -----
            String dayInput = p[1];
            if (dayInput == null || dayInput.isEmpty()) {
                System.out.println("Missing day of the week!");
                continue;
            }

            int i;
            for (i = 0; i < WEEK_DAYS.length; i++) {
                if (WEEK_DAYS[i].equals(dayInput)) {
                    days[n] = dayInput;
                    n++;
                    break;
                }
            }

            // If no match found
            if (i == WEEK_DAYS.length) {
                System.out.println("Invalid day of the week!");
            }

            // Stop if max 10 classes reached
            if (n >= 10) {
                System.out.println("Maximum number of classes (10) reached.");
                break;
            }
        }

        return n;
    }

    private static int findStudent(String[] students, int n, String name) {
        for (int i = 0; i < n; i++) {
            if (students[i].equals(name))
                return i;
        }
        return -1;
    }


    private static void readAttendance(Scanner sc, String[] students, int studentCount, int[][] attendance) {

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("."))
                break;

            String[] p = line.split(" ");
            if (p.length != 4) {
                System.out.println("Invalid input format. Expected: Name, Hour, Day of month & status of attendance");
                continue;
            }
            try {
                String name     = p[0];
                int sIdx        = findStudent(students, studentCount, name);
                if (sIdx == -1) {
                    System.err.println("students name doesn't belong to the list.");
                    continue;
                }
                int hour        = Integer.parseInt(p[1]);
                if (hour < 1 || hour > 6) {
                    System.err.println("Classes should be between 1 pm and 6 pm.");
                    continue;
                }
                int dayOfMonth  = Integer.parseInt(p[2]);   // 1‑30
                if (dayOfMonth < 1 || dayOfMonth > 31) {
                    System.err.println("Day of Month should be between 1 and 31.");
                    continue;
                }
//                System.out.println("sIdx = " + sIdx);
                String status   = p[3]; // HERE / NOT_HERE
                int value = status.equals("HERE") ? 1 : status.equals("NOT_HERE") ? -1 : 0;
                if (value == 0) {
                    System.err.println("❌ Invalid status (use HERE or NOT_HERE)");
                    continue;
                }
                attendance[sIdx][dayOfMonth] = value;
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid number format");
                continue;
            }

        }
    }

    private static int buildTimetable(int[] cHours, String[] cDays, int cCount, int[] tHours, String[] tDays, int[] tDates) {

        // weekday for each date in September
String[] dayName = new String[SEPT_DAYS + 1];  // 1‑based
        for (int d = 1; d <= SEPT_DAYS; d++) {
dayName[d] = WEEK_DAYS[(d) % 7];
//            System.out.println("dayName[d] = " + dayName[d]);
        }

int t = 0;
        for (int c = 0; c < cCount; c++) {
        for (int d = 1; d <= SEPT_DAYS; d++) {
        if (dayName[d].equals(cDays[c])) {
tHours[t]  = cHours[c];
tDays[t]   = cDays[c];
tDates[t]  = d;
t++;
        }
        }
        }
//        System.out.println("cHours = " + Arrays.toString(cHours) +
//                "\ncDays = " + Arrays.toString(cDays) +
//                "\ncCount = " + (cCount) +
//                "\ntHours = " + Arrays.toString(tHours) +
//                "\ntDays = " + Arrays.toString(tDays) +
//                "\ntDates = " + Arrays.toString(tDates));
//        System.out.println("t = " + t);
        return t;   // number of actual lessons
    }


private static void swap(int[] a, int i, int j) {
    int tmp = a[i];
    a[i] = a[j];
    a[j] = tmp;
}
private static void swap(String[] a, int i, int j) {
    String tmp = a[i];
    a[i] = a[j];
    a[j] = tmp;
}

private static void sortTimetable(int[] h, String[] d, int[] date, int n) {
    for (int i = 0; i < n - 1; i++) {
        int min = i;
        for (int j = i + 1; j < n; j++) {
            if (date[j] < date[min] ||
                    (date[j] == date[min] && h[j] < h[min])) {
                min = j;
            }
        }
        swap(h, i, min);
        swap(d, i, min);
        swap(date, i, min);
    }
}

// ==== STEP‑5A : header line =========================================
private static void printHeader(int[] h, String[] day, int[] date, int n) {
    System.out.printf("%-12s", ""); // leave space for name column
    for (int i = 0; i < n; i++) {
        System.out.printf("| %-11s", String.format("%d:00 %s %2d", h[i], day[i], date[i]));
    }
    System.out.println("|");
}


//==== STEP‑5B : attendance table ====================================
private static void printAttendanceTable(String[] students, int sCount,
                                         int[][] att,
                                         int[] tDates, int tCount) {
    for (int s = 0; s < sCount; s++) {
        System.out.printf("%-12s", students[s]);  // left-align student name (12 chars)
        for (int i = 0; i < tCount; i++) {
            int v = att[s][tDates[i]];
            String content = (v == 1) ? "1" : (v == -1) ? "-1" : "";
            System.out.printf("| %-11s", content); // | content with padding
        }
        System.out.println("|"); // final closing pipe
    }
}

public static void main(String[] args) {

    Scanner sc = new Scanner(System.in);

    //---------- 1. students ----------
    String[] students = new String[MAX_STUDENTS];
    int studentCount = readStudents(sc, students);

    // ---------- 2. weekly classes ----
    int[] classHours  = new int[MAX_CLASSES];   // 1‑6  -> 1pm …6pm
    String[] classDays = new String[MAX_CLASSES]; // MO … SU
    int classCount = readClasses(sc, classHours, classDays);

    // ---------- 3. attendance --------
    int[][] attendance = new int[MAX_STUDENTS][SEPT_DAYS + 1];
    readAttendance(sc, students, studentCount, attendance);
//        System.out.println("students = " + Arrays.toString(students));
//        for (int i = 0; i < attendance.length; i++) { // Iterate through rows
//            for (int j = 0; j < attendance[i].length; j++) { // Iterate through columns of the current row
//                System.out.print(attendance[i][j] + " "); // Print element and a space
//            }
//            System.out.println(); // Move to the next line after printing a row
//        }

    //--------- 4. build timetable ---
    int[] tHours  = new int[31];     // actual lesson list (≤30)
    String[] tDays = new String[31];
    int[] tDates  = new int[31];
    int tCount = buildTimetable(classHours, classDays, classCount,
            tHours, tDays, tDates);
    sortTimetable(tHours, tDays, tDates, tCount);

    //---------- 5. print -------------
    printHeader(tHours, tDays, tDates, tCount);
    printAttendanceTable(students, studentCount,
            attendance, tDates, tCount);
}

}*/

//
//jhon
//mike
//.
//2 MO
//4 WE
//.
//mike 2 28 NOT_HERE
//jhon 4 9 HERE

// explicative error message
// the try catch use probably should be more in the code or not use it at all
// the checks since they are checked twice, probably do it in a function or clean the code more
// simplify the code and make it human coded
// the