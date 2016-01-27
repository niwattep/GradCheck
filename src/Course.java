import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Course {
    final String id;
    final String name;
    final int credit;
    final String letterGrade;

    private static HashMap<String, Double> gradeTable = new HashMap<String, Double>();
    private static String[] passingGrades = { "A", "B+", "B", "C+", "C", "D+", "D", "S" };
    private static String[] attemptableGrades = { "A", "B+", "B", "C+", "C", "D+", "D", "F" };
    private static String[] grantableGrades = { "A", "B+", "B", "C+", "C", "D+", "D", "F", "S", "U" };

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
        this.credit = numUnit;
        this.letterGrade = letterGrade;
    }

    public String toString() {
        String retVal = String.format("   %7s, %20s, %2d, %s", id, name, credit, letterGrade);
        /*
        return "Course [id=" + id + ", name=" + name + ", numUnit=" + numUnit + ", letterGrade="
                + letterGrade + "]";
                */
        return retVal;
    }

    public static double getGradePointOf(String letterGrade) {
        Double gradePoint = gradeTable.get(letterGrade);
        if (gradePoint != null) {
            return gradePoint.doubleValue();
        } else {
            return 0.0;
        }
    }

    public static boolean isPassingGrade(String letterGrade) {
        return Arrays.asList(passingGrades).contains(letterGrade);
    }
    
    public static boolean isAttemptableGrade(String letterGrade) {
        return Arrays.asList(attemptableGrades).contains(letterGrade);
    }

    public static boolean isGrantedGrade(String letterGrade) {
        return Arrays.asList(grantableGrades).contains(letterGrade);
    }

    public static double getGPAX(ArrayList<Course> courses) {
        double GPX = 0.0;
        int CAX = 0;
        for (Course course : courses) {
            if (Course.isAttemptableGrade(course.letterGrade)) {
                CAX += course.credit;
                double gradePoint = getGradePointOf(course.letterGrade);
                GPX += gradePoint * course.credit;
            }
        }
        return GPX / CAX;
    }
    
    public static int getCAX(ArrayList<Course> courses) {
        int CAX = 0;
        for (Course course : courses) {
            if (Course.isAttemptableGrade(course.letterGrade)) {
                CAX += course.credit;
            }
        }
        return CAX;
    }
    
    public static int getCGX(ArrayList<Course> courses) {
        int CAX = 0;
        for (Course course : courses) {
            if (Course.isGrantedGrade(course.letterGrade)) {
                CAX += course.credit;
            }
        }
        return CAX;
    }
}