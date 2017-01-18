import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BITChecklistReport extends GradChecklistReport {
	private static String[] socialGenEdCourseIDs = { "2900111" };
    private static String[] languageGenEdCourseIDs = { "5500111", "5500112" };
    private static String[] departmentalRequirementCourseIDs = { "2301101", "5500215", "5500216",
            "5500313", "5500314" };
    private static String[] coreCourseIDs = { "2301102", "2301203", "2301333", "2603172",
            "2603213", "2603244", "2603270", "2603312", "2603313", "2603317", "2603318", "2603417",
            "2603490" };
    private static String[] coreMajorCourseIDs = { "2601115", "2601207", "2601255", "2602313", "2603274",
    		"2603383", "2603385", "2603386", "2603470", "2603471", "2603472", "2603477", "2603479", "2603488", 
    		"2603493", "2603496" };
    
    private static String[] electiveMajorCourseIDs = { "2603275", "2603276", "2603277", "2603480", "2603483", "2603484",
    		"2603485", "2603486", "2603487", "2601363", "2603476", "2603486", "2603489" };
    private static String[] programmerCourseIDs = { "2603275", "2603276", "2603277", "2603480", "2603483", "2603484" };
    private static String[] saCourseIDs = { "2603483", "2603484", "2603485", "2603486", "2603487" };
    private static String[] auditCourseIDs = { "2601363", "2603476", "2603483", "2603486", "2603489" };
    
    private int generalCredits = 0;
    private int coreCredits = 0;
    private int majorCredits = 0;
    private int programmerCredits = 0;
    private int saCredits = 0;
    private int auditCredits = 0;
    private int freeElectiveCredits = 0;
    
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
        List<Course> theCourses = unmatchedCourses
        		.stream()
                .filter(c -> Arrays.asList(courseIDs).contains(c.id))
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
     * Find completed tracks and print the result
     * 
     * @param writer
     * @param unmatchedCourses
     */
    private void checkTrack(PrintWriter writer, List<Course> unmatchedCourses) {
    	writer.println("Track Result");
    	programmerCredits = programmerComplete(unmatchedCourses);
    	saCredits = saComplete(unmatchedCourses);
    	auditCredits = auditComplete(unmatchedCourses);
    	if (programmerCredits >= 15) {
    		writer.println("Programmer: ***** COMPLETE! *****");
    	} else {
    		writer.println("Programmer: NEED " + (15 - programmerCredits) + " MORE CREDITS");
    	}
    	if (saCredits >= 15) {
    		writer.println("System Analyst: ***** COMPLETE! *****");
    	} else {
    		writer.println("System Analyst: NEED " + (15 - saCredits) + " MORE CREDITS");
    	}
    	if (auditCredits >= 15) {
    		writer.println("Computer Audit: ***** COMPLETE! *****");
    	} else {
    		writer.println("Computer Audit: NEED " + (15 - auditCredits) + " MORE CREDITS");
    	}
    	writer.println();
    }
    
    /**
     * Calculate credits of programmer
     * 
     * @param unmatchedCourses
     * @return programmerCredits
     */
    private int programmerComplete(List<Course> unmatchedCourses) {
    	List<Course> programmerCourses = unmatchedCourses
    			.stream()
    			.filter(c -> Arrays.asList(programmerCourseIDs).contains(c.id))
    			.collect(Collectors.toList());
    	int programmerCredits = 0;
    	for (Course c : programmerCourses) {
    		if (Course.isAttemptableGrade(c.letterGrade)) {
    			programmerCredits += c.credit;
    		}
    	}
    	return programmerCredits;
    }
    
    /**
     * Calculate credits of system analyst
     * 
     * @param unmatchedCourses
     * @return saCredits
     */
    private int saComplete(List<Course> unmatchedCourses) {
    	List<Course> saCourses = unmatchedCourses
    			.stream()
    			.filter(c -> Arrays.asList(saCourseIDs).contains(c.id))
    			.collect(Collectors.toList());
    	int saCredits = 0;
    	for (Course c : saCourses) {
    		if (Course.isAttemptableGrade(c.letterGrade)) {
    			saCredits += c.credit;
    		}
    	}
    	return saCredits;
    }
    
    /**
     * Calculate credits of computer audit
     * 
     * @param unmatchedCourses
     * @return auditCredits
     */
    private int auditComplete(List<Course> unmatchedCourses) {
    	List<Course> auditCourses = unmatchedCourses
    			.stream()
    			.filter(c -> Arrays.asList(auditCourseIDs).contains(c.id))
    			.collect(Collectors.toList());
    	int auditCredits = 0;
    	for (Course c : auditCourses) {
    		if (Course.isAttemptableGrade(c.letterGrade)) {
    			auditCredits += c.credit;
    		}
    	}
    	return auditCredits;
    }
     
    private boolean isFreeElective(Course course) {
        return !course.id.startsWith("26") || (course.id.charAt(4) != '0' &&
        		course.id.charAt(4) != '1' && course.id.charAt(4) != '2');
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
            //System.out.println(credits); //test
            majorCredits += credits;
            //System.out.println("major " + majorCredits); //test
            if (credits == 0) {
                printMissing(writer, coreMajorCourseID);
            }
        }
        writer.println();
        
        writer.println("Group: Elective Major Courses");
        writer.println();
        checkTrack(writer, unmatchedCourses);
        do {
            credits = matchAndPrintOneCourse(writer, electiveMajorCourseIDs, unmatchedCourses);
        } while (credits != 0);
        writer.println();
        
        writer.println("Group: Free Electives (6 credits)");
        /* First, try to match courses that has to be free electives */
        do {
            credits = matchAndPrintOneFreeElectiveCourse(writer, unmatchedCourses);
            freeElectiveCredits += credits;
        } while (credits != 0 && freeElectiveCredits < 6);
        /* If free electives hasn't been fulfilled yet, try to match any remaining courses */
        if (freeElectiveCredits < 6) {
            do {
                credits = findAndPrintOneCourse(writer, unmatchedCourses);
                freeElectiveCredits += credits;
            } while (credits != 0 && freeElectiveCredits < 6); 
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
        
        if (canGraduate() == true) {
        	writer.println("***** CAN GRADUATE! *****");
        	/*writer.println(generalCredits);
        	writer.println(coreCredits);
        	writer.println(majorCredits);
        	writer.println(electiveMajorCredits);
        	writer.println(freeElectiveCredits);*/
        } else {
        	writer.println("***** CANNOT GRADUATE! *****");
        	/*writer.println(generalCredits);
        	writer.println(coreCredits);
        	writer.println(majorCredits);
        	writer.println(electiveMajorCredits);
        	writer.println(freeElectiveCredits);*/
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
    			(programmerCredits >= 15 || saCredits >= 15 || auditCredits >= 15) && 
    			freeElectiveCredits == 6) {
    		return true;
    	}
    	return false;
    }
}
