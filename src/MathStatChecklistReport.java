import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MathStatChecklistReport extends GradChecklistReport {
    private static String[] socialGenEdCourseIDs = { "2900111" };
    private static String[] languageGenEdCourseIDs = { "5500111", "5500112" };
    private static String[] departmentalRequirementCourseIDs = { "2301101", "5500215", "5500216",
            "5500313", "5500314" };
    private static String[] coreCourseIDs = { "2301102", "2301203", "2301333", "2603172",
            "2603213", "2603244", "2603270", "2603312", "2603313", "2603317", "2603318", "2603417",
            "2603490" };

    /**
     * Match the first course in courseIDs with a course in unmatchedCourses
     * that has an attemptable grade (e.g., not S) and remove it from
     * unmatchedCourses.
     * 
     * Assumption: Every course in unmatchedCourses has a passing grade.
     * 
     * @param writer
     * @param courseIDs
     * @param unmatchedCourses
     * @return true if there is a match, false otherwise
     */
    private boolean matchAndPrintOneCourse(PrintWriter writer, String[] courseIDs,
            List<Course> unmatchedCourses) {
        List<Course> theCourses = unmatchedCourses.stream()
                .filter(c -> Arrays.asList(courseIDs).contains(c.id)).collect(Collectors.toList());

        for (Course theCourse : theCourses) {
            if (Course.isAttemptableGrade(theCourse.letterGrade)) {
                writer.println(theCourse);
                unmatchedCourses.remove(theCourse);
                return true;
            }
        }
        return false;
    }

    private void printMissing(PrintWriter writer, String detail) {
        writer.println("   ************** MISSING " + detail + " **************");
    }

    /**
     * Go over the student's attempted courses and print out the course info
     * with respect to the student's curriculum.
     * 
     * TODO: rewrite and explain side-effect on W
     */
    public void printCurriculumCourses(PrintWriter writer, Student student) {
        List<Course> unmatchedCourses = new ArrayList<Course>(student.getCoursesTaken()); // shallow
                                                                                          // copy
        // TODO: filter only passing grades

        writer.println("Group: General Education");
        writer.println("1. Social Science");
        if (!matchAndPrintOneCourse(writer, socialGenEdCourseIDs, unmatchedCourses)) {
            printMissing(writer, socialGenEdCourseIDs[0]);
        }
        writer.println("2. Humanity");
        if (!matchAndPrintOneCourse(writer, humanityGenEdCourseIDs, unmatchedCourses)) {
            printMissing(writer, "");
        }
        writer.println("3. Science and Math");
        if (!matchAndPrintOneCourse(writer, scienceMathGenEdCourseIDs, unmatchedCourses)) {
            printMissing(writer, "");
        }
        writer.println("4. Interdisciplinary");
        if (!matchAndPrintOneCourse(writer, interdisciplinaryGenEdCourseIDs, unmatchedCourses)) {
            printMissing(writer, "");
        }
        writer.println("5. Foreign languages");
        for (String languageGenEdCourseID : languageGenEdCourseIDs) {
            String[] oneCourse = { languageGenEdCourseID };
            if (!matchAndPrintOneCourse(writer, oneCourse, unmatchedCourses)) {
                printMissing(writer, languageGenEdCourseID);
            }
        }
        writer.println();

        writer.println("Group: Departmental Requirements");
        for (String departmentalRequirementCourseID : departmentalRequirementCourseIDs) {
            String[] oneCourse = { departmentalRequirementCourseID };
            if (!matchAndPrintOneCourse(writer, oneCourse, unmatchedCourses)) {
                printMissing(writer, departmentalRequirementCourseID);
            }
        }
        writer.println();
        
        writer.println("Group: Core Courses");
        for (String coreCourseID : coreCourseIDs) {
            String[] oneCourse = { coreCourseID };
            if (!matchAndPrintOneCourse(writer, oneCourse, unmatchedCourses)) {
                printMissing(writer, coreCourseID);
            }
        }
        writer.println();

        writer.println("Group: Extraneous Courses");
        for (Course unmatchedCourse : unmatchedCourses) {
            writer.println(unmatchedCourse);
        }
    }
}
