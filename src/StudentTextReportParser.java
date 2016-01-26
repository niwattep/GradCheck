import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StudentTextReportParser {
    /**
     * Read 8 lines containing a single course information and return the object
     * representing it. If the first line doesn't have the course id, don't read
     * the seven further lines and return null right away.
     * 
     * @param reader
     * @return Course information or null, if the data is not course
     *         information.
     * @throws IOException
     */
    private static Course parseCourse(BufferedReader reader) throws IOException {
        String courseID;

        String courseIDLine = reader.readLine().trim(); // 1st line
        if (courseIDLine.length() > 0) {
            courseID = courseIDLine;
        } else {
            return null; // the first line is not the course ID
        }
        reader.readLine(); // ignore
        String courseName = reader.readLine().trim(); // 3rd line
        reader.readLine(); // ignore
        String numUnitLine = reader.readLine().trim(); // 5th line
        int numUnit = Integer.valueOf(numUnitLine);
        reader.readLine(); // ignore
        String grade = reader.readLine().trim(); // 7th line

        reader.readLine(); // ignore
        return new Course(courseID, courseName, numUnit, grade);
    }

    private static ArrayList<Course> parseCourses(BufferedReader reader) throws IOException {
        ArrayList<Course> courses = new ArrayList<Course>();
        while (true) {
            Course course = parseCourse(reader);
            if (course == null) {
                /* There are two cases here. Regular semesters or summer semesters.
                 * Regular semester will have the next line = "CA",
                 * follow by 21 trailing lines of other info.
                 * Summer semester will have the next line = empty,
                 * follow by another empty line.
                 */
                if (reader.readLine().trim().equals("CA")) {
                    for (int i = 0; i < 21; i = i + 1) { // ignore 21 trailing lines
                        reader.readLine();
                    }
                } else {
                    reader.readLine(); // ignore one line
                }
                
                return courses;
            } else {
                courses.add(course);
            }
        }
    }

    /**
     * @param reader
     * @return Student ID or null if this is not a semester header.
     * @throws IOException
     */
    private static String parseSemesterHeader(BufferedReader reader) throws IOException {
        String studentId;

        String idLine = reader.readLine(); // 18th line
        try {
            studentId = idLine.substring(idLine.length() - 12); // ### ##### ##
        } catch (IndexOutOfBoundsException e) {
            return null; // this line is not a semester header
        }

        for (int i = 0; i < 10; i = i + 1) { // ignore the next 10 lines
            reader.readLine();
        }
        return studentId;
    }

    /**
     * Parse the student information from a buffered reader.
     * 
     * @param reader
     * @return student information
     * @throws IOException
     */
    public static Student parseStudentInfo(BufferedReader reader) throws IOException {
        for (int i = 0; i < 13; i = i + 1) { // ignore the first 13 lines
            reader.readLine();
        }

        String majorCodeLine = reader.readLine(); // 13th line
        String majorCodeString = majorCodeLine.substring(majorCodeLine.length() - 5);
        int majorCode = Integer.valueOf(majorCodeString);
        reader.readLine();

        String studentNameLine = reader.readLine(); // 15th line
        String studentName = studentNameLine.substring(13); // ignore prefixes
        reader.readLine();
        reader.readLine();

        String studentId = parseSemesterHeader(reader); // 1st semester header
        Student thisStudent = new Student(studentName, majorCode, studentId);

        do {
            thisStudent.addCourses(parseCourses(reader));
        } while (parseSemesterHeader(reader) != null); // read till no semester

        return thisStudent;
    }

    public static void main(String[] args) {
        String inputFileName = "testcases/MSTAT55_1.FIN.DAT";
        if (args.length > 0) {
            inputFileName = args[0];
        }
        
        try {
            Student student = parseStudentInfo(new BufferedReader(new FileReader(inputFileName)));
            System.out.println(student);
            for (Course course : student.getCoursesTaken()) {
                System.out.println(course);
            }
            System.out.println("GPAX = " + Course.getGPAX(student.getCoursesTaken()));
        } catch (FileNotFoundException e) {
            System.err.println("File not found:" + args[0]);
        } catch (IOException e) {
            System.err.println("Unhandled file format:" + args[0]);
        }
    }

}
