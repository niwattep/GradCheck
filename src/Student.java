import java.util.ArrayList;

public class Student {
    final String name;
    final int majorCode;
    final String studentId;

    /**
     * Courses taken, in chronological order. Students may take the course
     * multiple times.
     */
    private ArrayList<Course> coursesTaken = new ArrayList<Course>();

    public Student(String name, int majorCode, String studentId) {
        super();
        this.name = name;
        this.majorCode = majorCode;
        this.studentId = studentId;
    }

    public String toString() {
        return "Student [name=" + name + ", majorCode=" + majorCode
                + ", studentId=" + studentId + "]";
    }
}
