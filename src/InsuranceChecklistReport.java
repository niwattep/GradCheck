import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InsuranceChecklistReport extends GradChecklistReport {
	private static String[] socialGenEdCourseIDs = { "2900111" };
    private static String[] languageGenEdCourseIDs = { "5500111", "5500112" };
    private static String[] departmentalRequirementCourseIDs = { "2301101", "5500215", "5500216",
            "5500313", "5500314" };
    private static String[] coreCourseIDs = { "2301102", "2301203", "2301333", "2603172",
            "2603213", "2603244", "2603270", "2603312", "2603313", "2603317", "2603318", "2603417",
            "2603490" };
    private static String[] coreMajorCourseIDs = { "2601115", "2601116", "2602313", "2603243", "2603335",
    		"2603346", "2603347", "2603348", "2603349", "2603376", "2603383", "2603441", "2603492",
    		"2603494", "2604361", "2605311", "3401233" };
    private static String[] electiveMajorCourseIDs = { "2301204", "2301366", "2603341", "2603350",
    		"2603430", "2603431", "2603432", "2603436", "2603370", "2603470", "2603471", "2603478",
    		"2603482", "2602323", "2602333", "2602395", "2602416", "2602432", "2604271", "2604332",
    		"2604333", "2604463", "2605319", "2605414", "2605418", "2900112", "3401250" };
    
    private int generalCredits = 0;
    private int coreCredits = 0;
    private int majorCredits = 0;
    private int electiveMajorCredits = 0;
    private int freeElectiveCredits = 0;
    
    private boolean haveNonpassingCourse = false;
    
    private List<Course> electiveMajorCourses = new ArrayList<Course>();
    private List<Course> freeElectiveCourses = new ArrayList<Course>();
    
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
    
    /**
     * Match one elective major course that is in electiveMajorCourseIDs
     * @param writer
     * @param courseIDs
     * @param unmatchedCourses
     * @return course credits. 0 if no course matched
     */
    private int matchOneElectiveMajorCourse(PrintWriter writer, String[] courseIDs,
            List<Course> unmatchedCourses) {
        List<Course> theCourses = unmatchedCourses.stream()
                .filter(c -> Arrays.asList(courseIDs).contains(c.id)).collect(Collectors.toList());

        for (Course theCourse : theCourses) {
            if (Course.isAttemptableGrade(theCourse.letterGrade)) {
            	electiveMajorCourses.add(theCourse);
                unmatchedCourses.remove(theCourse);
                return theCourse.credit;
            }
        }
        return 0;
    }
    
    /**
     * Match other course that can be elective major for insurance
     * @param writer
     * @param unmatchedCourses
     * @return course credits. 0 if no course matched
     */
    private int matchOneElectiveMajorCourse(PrintWriter writer, List<Course> unmatchedCourses) {
        List<Course> theCourses = unmatchedCourses.stream().filter(c -> isElectiveMojor(c))
                .collect(Collectors.toList());

        for (Course theCourse : theCourses) {
            if (Course.isAttemptableGrade(theCourse.letterGrade)) {
            	electiveMajorCourses.add(theCourse);
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
    
    private boolean isElectiveMojor(Course course) {
    	return course.id.startsWith("26") && (course.id.charAt(4) != '0' && 
        		course.id.charAt(4) != '1' && course.id.charAt(4) != '2');
    }
    
    /**
     * Match free elctive
     * @param writer
     * @param unmatchedCourses
     * @return course credits. 0 if no course matched
     */
    private int matchOneFreeElectiveCourse(PrintWriter writer, List<Course> unmatchedCourses) {
        List<Course> theCourses = unmatchedCourses.stream().filter(c -> isFreeElective(c))
                .collect(Collectors.toList());

        for (Course theCourse : theCourses) {
            if (Course.isAttemptableGrade(theCourse.letterGrade)) {
            	freeElectiveCourses.add(theCourse);
                unmatchedCourses.remove(theCourse);
                return theCourse.credit;
            }
        }
        return 0;
    }
    

    /**
     * If free elective is not fulfilled and elective major course credits is more than 15
     * move some elective major courses to free elective course
     */
    private void moveElectiveMajorToFreeElective() {
    	do {
    		Course movingCourse = electiveMajorCourses.remove(0);
            electiveMajorCourses.remove(movingCourse);
            electiveMajorCredits -= movingCourse.credit;
            freeElectiveCourses.add(movingCourse);
            freeElectiveCredits += movingCourse.credit;
            if (freeElectiveCredits == 6) break;
    	} while (electiveMajorCredits > 15);
    }

    /**
     * Find the first course in unmatchedCourses that has an attemptable grade
     * (e.g., not S) and remove it from unmatchedCourses.
     * 
     * Use for free elective only
     * 
     * Assumption: Every course in unmatchedCourses has a passing grade.
     * 
     * @param writer
     * @param unmatchedCourses
     * @return course credits. 0 if no course matched
     */
    private int findOneFreeElectiveCourse(PrintWriter writer, List<Course> unmatchedCourses) {
        for (Course theCourse : unmatchedCourses) {
            if (Course.isAttemptableGrade(theCourse.letterGrade)) {
            	freeElectiveCourses.add(theCourse);
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
        
        for (Course nonPass : nonPassingCourses) {
        	if (nonPass.letterGrade.equals("F") || nonPass.letterGrade.equals("U")) {
        		haveNonpassingCourse = true;
        		break;
        	}
        }

        /* Sort, so that we can get the best extra electives to become W */
        unmatchedCourses.sort((Course c1, Course c2) -> (int) (Course
                .getGradePointOf(c1.letterGrade) * 2 - Course.getGradePointOf(c2.letterGrade) * 2));

        int credits;
        
        writer.println("Group: General Education");
        writer.println("1. Social Science");
        credits = matchAndPrintOneCourse(writer, socialGenEdCourseIDs, unmatchedCourses);
        generalCredits += credits;
        if (credits == 0) {
            printMissing(writer, socialGenEdCourseIDs[0]);
        }
        writer.println("2. Humanity");
        credits = matchAndPrintOneCourse(writer, humanityGenEdCourseIDs, unmatchedCourses);
        generalCredits += credits;
        if (credits == 0) {
            printMissing(writer, "");
        }
        writer.println("3. Science and Math");
        credits = matchAndPrintOneCourse(writer, scienceMathGenEdCourseIDs, unmatchedCourses);
        generalCredits += credits;
        if (credits == 0) {
            printMissing(writer, "");
        }
        writer.println("4. Interdisciplinary");
        credits = matchAndPrintOneCourse(writer, interdisciplinaryGenEdCourseIDs, unmatchedCourses);
        generalCredits += credits;
        if (credits == 0) {
            printMissing(writer, "");
        }
        writer.println("5. Foreign languages");
        for (String languageGenEdCourseID : languageGenEdCourseIDs) {
            String[] oneCourse = { languageGenEdCourseID };
            credits = matchAndPrintOneCourse(writer, oneCourse, unmatchedCourses);
            generalCredits += credits;
            if (credits == 0) {
                printMissing(writer, languageGenEdCourseID);
            }
        }
        writer.println();

        writer.println("Group: Departmental Requirements");
        for (String departmentalRequirementCourseID : departmentalRequirementCourseIDs) {
            String[] oneCourse = { departmentalRequirementCourseID };
            credits = matchAndPrintOneCourse(writer, oneCourse, unmatchedCourses);
            generalCredits += credits;
            if (credits == 0) {
                printMissing(writer, departmentalRequirementCourseID);
            }
        }
        writer.println();

        writer.println("Group: Core Courses");
        for (String coreCourseID : coreCourseIDs) {
            String[] oneCourse = { coreCourseID };
            credits = matchAndPrintOneCourse(writer, oneCourse, unmatchedCourses);
            coreCredits += credits;
            if (credits == 0) {
                printMissing(writer, coreCourseID);
            }
        }
        writer.println();

        writer.println("Group: Core Major Courses");
        for (String coreMajorCourseID : coreMajorCourseIDs) {
            String[] oneCourse = { coreMajorCourseID };
            credits = matchAndPrintOneCourse(writer, oneCourse, unmatchedCourses);
            majorCredits += credits;
            if (credits == 0) {
                printMissing(writer, coreMajorCourseID);
            }
        }
        writer.println();

        do {
            credits = matchOneElectiveMajorCourse(writer, electiveMajorCourseIDs, unmatchedCourses);
            electiveMajorCredits += credits;
        } while (credits != 0);
        do {
        	credits = matchOneElectiveMajorCourse(writer, unmatchedCourses);
        	electiveMajorCredits += credits;
        } while (credits != 0);
        writer.println();
        
        do {
            credits = matchOneFreeElectiveCourse(writer, unmatchedCourses);
            freeElectiveCredits += credits;
        } while (credits != 0 && freeElectiveCredits < 6);
        if (freeElectiveCredits < 6) {
            do {
                credits = findOneFreeElectiveCourse(writer, unmatchedCourses);
                freeElectiveCredits += credits;
            } while (credits != 0 && freeElectiveCredits < 6); 
        }
        if (freeElectiveCredits < 6 && electiveMajorCredits > 15) {
        	moveElectiveMajorToFreeElective();
        }
        writer.println("Group: Elective Major Courses (>= 15 credits)");
        for (Course theCourse : electiveMajorCourses) {
        	writer.println(theCourse);
        }
        if (electiveMajorCredits < 15) {
        	printMissing(writer, ": NEED " + (15 - electiveMajorCredits) + " MORE CREDITS");
    	}
        writer.println();
        
        writer.println("Group: Free Electives (6 credits)");
        for (Course theCourse : freeElectiveCourses) {
        	writer.println(theCourse);
        }
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
        
        int totalCreditAttemped = Course.getCAX(student.getCoursesTaken());
        int totalCreditGranted = Course.getCGX(student.getCoursesTaken());
        double gpax = Course.getGPAX(student.getCoursesTaken());
        writer.println("Total Credit Attempted = " + totalCreditAttemped);
        writer.println("Total Credit Granted = " + totalCreditGranted);
        writer.println("GPAX = " + (Math.round(gpax * 100.0) / 100.0));
        writer.println();
        
        if (canGraduate() == true) {
        	writer.println("***** CAN GRADUATE! *****");
        	if (haveNonpassingCourse == false) {
        		if (gpax >= 3.60) writer.println("***** FIRST-CLASS HONORS *****");
        		else if (gpax >= 3.25) writer.println("***** SECOND-CLASS HONORS *****");
        	}
        } else {
        	writer.println("***** CANNOT GRADUATE! *****");
        }
        writer.println();
    }
    
    /**
     * Check if this student can graduate or not
     * 
     * @return true if this student can graduate
     */
    private boolean canGraduate() {
    	if (generalCredits == 32 && coreCredits == 42 && majorCredits == 48 &&
    			electiveMajorCredits >= 15 && freeElectiveCredits == 6) {
    		return true;
    	}
    	return false;
    }
}
