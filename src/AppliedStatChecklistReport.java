import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AppliedStatChecklistReport extends GradChecklistReport {
    private static String[] socialGenEdCourseIDs = { "2900111" };
    private static String[] languageGenEdCourseIDs = { "5500111", "5500112" };
    private static String[] departmentalRequirementCourseIDs = { "2301101", "5500215", "5500216",
            "5500313", "5500314" };
    private static String[] coreCourseIDs = { "2301102", "2301203", "2301333", "2603172",
            "2603213", "2603244", "2603270", "2603312", "2603313", "2603317", "2603318", "2603417",
            "2603490" };
    private static String[] coreMajorCourseIDs = { "2601115", "2601255", "2602313", "2603336", "2603413",
    		"2603430", "2603431", "2603432", "2603433", "2603434", "2603435", "2603436", "2603499",
    		"2605311", "2900112", "3401234" };
    private static String[] electiveMajorCourseIDs = { "2603370", "2603375", "2603376", "2603383",
    		"2603437", "2601207", "2602323", "2602326", "2602344", "2602395", "2602428", "2602432",
    		"2604271", "2945310", "2945401" };
    
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
     * @return number of credits for the course that matches
     */
    private int matchAndPrintOneCourse(PrintWriter writer, String[] courseIDs,
            List<Course> unmatchedCourses) {
        List<Course> theCourses = unmatchedCourses.stream()
                .filter(c -> Arrays.asList(courseIDs).contains(c.id)).collect(Collectors.toList());

        for (Course theCourse : theCourses) {
            if (Course.isAttemptableGrade(theCourse.letterGrade)) {
                writer.println(theCourse);
                unmatchedCourses.remove(theCourse);
                return theCourse.credit;
            }
        }
        return 0;
    }

    private boolean isFreeElective(Course course) {
        return !course.id.startsWith("26") || course.id.charAt(4) == '0'
                || course.id.charAt(4) == '1' || course.id.charAt(4) == '2';
    }

    private int matchAndPrintOneFreeElectiveCourse(PrintWriter writer, List<Course> unmatchedCourses) {
        List<Course> theCourses = unmatchedCourses.stream().filter(c -> isFreeElective(c))
                .collect(Collectors.toList());

        for (Course theCourse : theCourses) {
            if (Course.isAttemptableGrade(theCourse.letterGrade)) {
                writer.println(theCourse);
                unmatchedCourses.remove(theCourse);
                return theCourse.credit;
            }
        }
        return 0;
    }

    /**
     * Find the first course in unmatchedCourses that has an attemptable grade
     * (e.g., not S) and remove it from unmatchedCourses.
     * 
     * Assumption: Every course in unmatchedCourses has a passing grade.
     * 
     * @param writer
     * @param unmatchedCourses
     * @return
     */
    private int findAndPrintOneCourse(PrintWriter writer, List<Course> unmatchedCourses) {
        for (Course theCourse : unmatchedCourses) {
            if (Course.isAttemptableGrade(theCourse.letterGrade)) {
                writer.println(theCourse);
                unmatchedCourses.remove(theCourse);
                return theCourse.credit;
            }
        }
        return 0;
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

        List<Course> nonPassingCourses = unmatchedCourses.stream()
                .filter(c -> !Course.isPassingGrade(c.letterGrade)).collect(Collectors.toList());

        unmatchedCourses.removeAll(nonPassingCourses);

        /* Sort, so that we can get the best extra electives to become W */
        unmatchedCourses.sort((Course c1, Course c2) -> (int) (Course
                .getGradePointOf(c1.letterGrade) * 2 - Course.getGradePointOf(c2.letterGrade) * 2));

        writer.println("Group: General Education");
        writer.println("1. Social Science");
        if (matchAndPrintOneCourse(writer, socialGenEdCourseIDs, unmatchedCourses) == 0) {
            printMissing(writer, socialGenEdCourseIDs[0]);
        }
        writer.println("2. Humanity");
        if (matchAndPrintOneCourse(writer, humanityGenEdCourseIDs, unmatchedCourses) == 0) {
            printMissing(writer, "");
        }
        writer.println("3. Science and Math");
        if (matchAndPrintOneCourse(writer, scienceMathGenEdCourseIDs, unmatchedCourses) == 0) {
            printMissing(writer, "");
        }
        writer.println("4. Interdisciplinary");
        if (matchAndPrintOneCourse(writer, interdisciplinaryGenEdCourseIDs, unmatchedCourses) == 0) {
            printMissing(writer, "");
        }
        writer.println("5. Foreign languages");
        for (String languageGenEdCourseID : languageGenEdCourseIDs) {
            String[] oneCourse = { languageGenEdCourseID };
            if (matchAndPrintOneCourse(writer, oneCourse, unmatchedCourses) == 0) {
                printMissing(writer, languageGenEdCourseID);
            }
        }
        writer.println();

        writer.println("Group: Departmental Requirements");
        for (String departmentalRequirementCourseID : departmentalRequirementCourseIDs) {
            String[] oneCourse = { departmentalRequirementCourseID };
            if (matchAndPrintOneCourse(writer, oneCourse, unmatchedCourses) == 0) {
                printMissing(writer, departmentalRequirementCourseID);
            }
        }
        writer.println();

        writer.println("Group: Core Courses");
        for (String coreCourseID : coreCourseIDs) {
            String[] oneCourse = { coreCourseID };
            if (matchAndPrintOneCourse(writer, oneCourse, unmatchedCourses) == 0) {
                printMissing(writer, coreCourseID);
            }
        }
        writer.println();

        writer.println("Group: Core Major Courses");
        for (String coreMajorCourseID : coreMajorCourseIDs) {
            String[] oneCourse = { coreMajorCourseID };
            if (matchAndPrintOneCourse(writer, oneCourse, unmatchedCourses) == 0) {
                printMissing(writer, coreMajorCourseID);
            }
        }
        writer.println();

        int electiveMajorCredits = 0;
        writer.println("Group: Elective Major Courses (>= 15 credits)");
        int matchedCredit;
        do {
            matchedCredit = matchAndPrintOneCourse(writer, electiveMajorCourseIDs, unmatchedCourses);
            electiveMajorCredits += matchedCredit;
        } while (matchedCredit != 0);
        if (electiveMajorCredits < 15) {
            printMissing(writer, ": NEED " + (15 - electiveMajorCredits) + " MORE CREDITS");
        }
        writer.println();

        int freeElectiveCredits = 0;
        writer.println("Group: Free Electives (6 credits)");
        /* First, try to match courses that has to be free electives */
        do {
            matchedCredit = matchAndPrintOneFreeElectiveCourse(writer, unmatchedCourses);
            freeElectiveCredits += matchedCredit;
        } while (matchedCredit != 0 && freeElectiveCredits < 6);
        
        /* If free electives hasn't been fulfilled yet, try to match any remaining courses */
        if (freeElectiveCredits < 6) {
            do {
                matchedCredit = findAndPrintOneCourse(writer, unmatchedCourses);
                freeElectiveCredits += matchedCredit;
            } while (matchedCredit != 0 && freeElectiveCredits < 6); 
        }
        /* Check whether the free electives has been fulfilled */
        if (freeElectiveCredits < 6) {
            printMissing(writer, ": NEED " + (6 - freeElectiveCredits) + " MORE CREDITS");
        }
        writer.println();

        /* Re-grade extra free electives to 'W' */
        for (Course unmatchedCourse : unmatchedCourses) {
            if (isFreeElective(unmatchedCourse)) {
                unmatchedCourse.letterGrade = "W (previously " + unmatchedCourse.letterGrade + ")";
            }
        }

        writer.println("Group: Extra Courses");
        for (Course unmatchedCourse : unmatchedCourses) {
            writer.println(unmatchedCourse);
        }
        writer.println();

        writer.println("Group: Left-overs");
        for (Course nonPassingCourse : nonPassingCourses) {
            writer.println(nonPassingCourse);
        }
        writer.println();
    }
}