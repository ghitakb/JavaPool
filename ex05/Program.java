import java.util.Scanner;

public class Program {

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


