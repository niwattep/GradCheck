import java.io.PrintWriter;

public abstract class GradChecklistReport {
    public void printReport (PrintWriter writer, Student student) {
        writer.println(student);
        for (Course course : student.getCoursesTaken()) {
            writer.println(course);
        }
        writer.println("GPAX = " + Course.getGPAX(student.getCoursesTaken()));
    }
}
