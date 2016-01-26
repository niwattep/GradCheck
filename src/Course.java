import java.util.Arrays;
import java.util.HashMap;

public class Course {
    final String id;
    final String name;
    final int numUnit;
    final String letterGrade;

    private static HashMap<String, Double> gradeTable = new HashMap<String, Double>();
    private static String[] attemptableGrades = { "A", "B+", "B", "C+", "C",
            "D+", "D", "F", "S", "U" };
    private static String[] grantableGrades = { "A", "B+", "B", "C+", "C",
            "D+", "D", "F" };

    static {
        /* setup the static map */
        gradeTable.put("A", 4.0);
        gradeTable.put("B+", 3.5);
        gradeTable.put("B", 3.0);
        gradeTable.put("C+", 2.5);
        gradeTable.put("C", 2.0);
        gradeTable.put("D+", 1.5);
        gradeTable.put("D", 1.0);
        gradeTable.put("F", 0.0); // don't need this but we keep it for
                                  // completeness
    }

    public Course(String id, String name, int numUnit, String letterGrade) {
        super();
        this.id = id;
        this.name = name;
        this.numUnit = numUnit;
        this.letterGrade = letterGrade;
    }
    
    public String toString() {
        return "Course [id=" + id + ", name=" + name + ", numUnit=" + numUnit + ", letterGrade="
                + letterGrade + "]";
    }

    public static double getGradePointOf(String letterGrade) {
        Double gradePoint = gradeTable.get(letterGrade);
        if (gradePoint != null) {
            return gradePoint.doubleValue();
        } else {
            return 0.0;
        }
    }

    public static boolean isAttemptableGrade(String letterGrade) {
        return Arrays.asList(attemptableGrades).contains(letterGrade);
    }

    public static boolean isGrantedGrade(String letterGrade) {
        return Arrays.asList(grantableGrades).contains(letterGrade);
    }

    public static void main(String[] args) {
        System.out.println(isAttemptableGrade(new String("U")));
        System.out.println(isAttemptableGrade("W"));
        System.out.println(isAttemptableGrade("X"));
    }
}