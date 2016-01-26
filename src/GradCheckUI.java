import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GradCheckUI implements ActionListener {
    private static GradCheckUI instance = new GradCheckUI();

    private static JFrame mainFrame;
    private static JScrollPane inputScrollPane;
    private static JTextArea inputTextArea;
    private static JButton checkGradeButton;

    public static void main(String[] args) {
        mainFrame = new JFrame("GradCheck Software for 2603483");
        mainFrame.setLayout(new FlowLayout());
        mainFrame.setPreferredSize(new Dimension(700, 500));

        inputTextArea = new JTextArea(24, 60);
        inputScrollPane = new JScrollPane(inputTextArea);

        checkGradeButton = new JButton("Check Grade");
        checkGradeButton.addActionListener(instance);

        mainFrame.getContentPane().add(inputScrollPane);
        mainFrame.getContentPane().add(checkGradeButton);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null); // center of the screen
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == checkGradeButton) {
            StringReader stringReader = new StringReader(inputTextArea.getText());
            BufferedReader reader = new BufferedReader(stringReader);
            try {
                Student student = StudentTextReportParser.parseStudentInfo(reader);
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                GradChecklistReport report = new MathStatChecklistReport();
                report.printReport(printWriter, student);

                String outputDialogName = "Checklist for " + student.name;

                JTextArea outputTextArea = new JTextArea(stringWriter.toString());
                JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
                outputScrollPane.setPreferredSize(new Dimension(700, 500));

                JOptionPane.showMessageDialog(mainFrame, outputScrollPane, outputDialogName,
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainFrame, "Read error: unhandled input format");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainFrame, "Parse error: unhandled input format");
            }
        }
    }
}
