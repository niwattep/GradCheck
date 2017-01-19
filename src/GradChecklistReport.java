import java.io.PrintWriter;

public abstract class GradChecklistReport {
    static String[] humanityGenEdCourseIDs = { "2207363", "2207474", "2502393", "2541152",
        "2200301", "0123105", "0123101", "2200201", "2206101", "2542003", "2201111", "2207143",
        "2502430", "2541163", "2206247", "2502292", "2502291", "2541168", "2501292", "2501295",
        "0123104", "2541159", "3501456", "2502378", "2502379", "2542002", "2207389", "2207388",
        "2541151", "2223243", "3503111", "0123601", "2206248", "2542001", "2541157", "2722272" };
    static String[] scienceMathGenEdCourseIDs = { "3742102", "3742100", "3743422",
        "2110191", "2102041", "3600207", "2303150", "3600205", "3700104", "3700108", "3705103",
        "2313213", "2305151", "3640202", "2101251", "3141105", "3200103", "2111330", "3641201",
        "3700107", "3600206", "4000210", "3640203", "3900200" };
    static String[] interdisciplinaryGenEdCourseIDs = { "2503215", "0201200", "3000257",
        "3300100", "0201104", "0201153", "0201130", "0201122", "3301151", "0201119", "0201127",
        "0201125", "0201254", "0201251", "0201281", "0201118", "0201270", "0201131", "0201220",
        "3800252", "0201126", "2750178", "0201107", "0201101", "3000396", "0201207", "2502390",
        "0201108", "0201209" };
    
    abstract public void printCurriculumCourses(PrintWriter writer, Student student);
    
    public void printReport (PrintWriter writer, Student student) {
        writer.println(student);
        writer.println();

        printCurriculumCourses(writer, student);
        
        /*writer.println("Total Credit Attempted = " + Course.getCAX(student.getCoursesTaken()));
        writer.println("Total Credit Granted = " + Course.getCGX(student.getCoursesTaken()));
        writer.println("GPAX = " + Course.getGPAX(student.getCoursesTaken()));*/

    }
}
