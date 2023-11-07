import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class LetterOfRecommendationWriter {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InfoPage infoPage = new InfoPage();
            infoPage.setVisible(true);
        });
    }
}

class InfoPage extends JFrame {
    private JTextField teacherFullName, teacherSchool, teacherSubject, teacherEmail, teacherDate;
    private JTextField studentName;
    private JComboBox<String> studentGender;
    private JSpinner studentGPA, studentAPClasses;

    public InfoPage() {
        setTitle("Letter of Recommendation - Info Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 2));

        // Initialize components
        JLabel teacherLabel = new JLabel("Teacher Information:");
        teacherFullName = new JTextField(20);
        teacherSchool = new JTextField(20);
        teacherSubject = new JTextField(20);
        teacherEmail = new JTextField(20);
        teacherDate = new JTextField(20);

        JLabel studentLabel = new JLabel("Student Information:");
        studentName = new JTextField(20);
        studentGender = new JComboBox<>(new String[] { "He/Him", "She/Her", "They/Them" });
        studentGPA = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 4.0, 0.1));
        studentAPClasses = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));

        // Add components to the frame
        add(teacherLabel);
        add(new JLabel("")); // Empty label for spacing
        add(new JLabel("Full Name:"));
        add(teacherFullName);
        add(new JLabel("School:"));
        add(teacherSchool);
        add(new JLabel("Subject You Teach:"));
        add(teacherSubject);
        add(new JLabel("Email Address:"));
        add(teacherEmail);
        add(new JLabel("Date:"));
        add(teacherDate);

        add(studentLabel);
        add(new JLabel("")); // Empty label for spacing
        add(new JLabel("Student Name:"));
        add(studentName);
        add(new JLabel("Gender:"));
        add(studentGender);
        add(new JLabel("Unweighted GPA:"));
        add(studentGPA);
        add(new JLabel("Number of AP Classes:"));
        add(studentAPClasses);

        JButton nextButton = new JButton("Next");
        add(nextButton);

        // Add action listener to the Next button
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Retrieve input data and move to the next frame
                Map<String, String> teacherInfo = new HashMap<>();
                teacherInfo.put("Full Name", teacherFullName.getText());
                teacherInfo.put("School", teacherSchool.getText());
                teacherInfo.put("Subject", teacherSubject.getText());
                teacherInfo.put("Email Address", teacherEmail.getText());
                teacherInfo.put("Date", teacherDate.getText());

                Map<String, String> studentInfo = new HashMap<>();
                studentInfo.put("Student Name", studentName.getText());
                studentInfo.put("Gender", (String) studentGender.getSelectedItem());
                studentInfo.put("Unweighted GPA", studentGPA.getValue().toString());
                studentInfo.put("Number of AP Classes", studentAPClasses.getValue().toString());

                StudentRating studentRating = new StudentRating(teacherInfo, studentInfo);
                studentRating.setVisible(true);
                InfoPage.this.dispose();
            }
        });
    }
}

class StudentRating extends JFrame {
    private Map<String, String> teacherInfo;
    private Map<String, String> studentInfo;

    public StudentRating(Map<String, String> teacherInfo, Map<String, String> studentInfo) {
        this.teacherInfo = teacherInfo;
        this.studentInfo = studentInfo;

        setTitle("Letter of Recommendation - Student Rating");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(11, 2));

        // Initialize components
        JLabel[] labels = {
                new JLabel("Leadership"), new JLabel("Participation"), new JLabel("Work Ethic"),
                new JLabel("Attitude"), new JLabel("Listening Skills"), new JLabel("Collaboration"),
                new JLabel("Dependability"), new JLabel("Adept"), new JLabel("Creativity")
        };
        JSlider[] sliders = new JSlider[9];

        for (int i = 0; i < 9; i++) {
            sliders[i] = new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
            sliders[i].setMajorTickSpacing(1);
            sliders[i].setPaintTicks(true);
            sliders[i].setPaintLabels(true);
        }

        JLabel ratingLabel = new JLabel("Rating Descriptions:");

        // Add components to the frame
        for (int i = 0; i < 9; i++) {
            add(labels[i]);
            add(sliders[i]);
        }

        add(new JLabel("")); // Empty label for spacing
        add(ratingLabel);

        JButton nextButton = new JButton("Next");
        add(nextButton);

        // Add action listener to the Next button
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Map<String, Integer> ratings = new HashMap<>();
                String[] ratingDescriptions = new String[9];

                for (int i = 0; i < 9; i++) {
                    int value = sliders[i].getValue();
                    String description = getRatingDescription(value);
                    ratings.put(labels[i].getText(), value);
                    ratingDescriptions[i] = labels[i].getText() + ": " + description;
                }

                Personalization personalization = new Personalization(teacherInfo, studentInfo, ratings,
                        ratingDescriptions);
                personalization.setVisible(true);
                StudentRating.this.dispose();
            }
        });
    }

    private String getRatingDescription(int value) {
        if (value < 4) {
            return "They provide minimal effort in my class. Homework is turned in late or with minimal effort.";
        } else if (value < 7) {
            return "They perform adequately in my class.";
        } else if (value < 9) {
            return "They consistently excel in my class.";
        } else {
            return "They are exceptional in every aspect of my class.";
        }
    }
}

class Personalization extends JFrame {
    private Map<String, String> teacherInfo;
    private Map<String, String> studentInfo;
    private Map<String, Integer> ratings;
    private String[] ratingDescriptions;

    private JTextArea personalizationTextArea;

    public Personalization(Map<String, String> teacherInfo, Map<String, String> studentInfo,
            Map<String, Integer> ratings, String[] ratingDescriptions) {
        this.teacherInfo = teacherInfo;
        this.studentInfo = studentInfo;
        this.ratings = ratings;
        this.ratingDescriptions = ratingDescriptions;

        setTitle("Letter of Recommendation - Personalization");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel personalizationLabel = new JLabel("Write a personal paragraph about the student:");

        personalizationTextArea = new JTextArea(10, 40);
        personalizationTextArea.setLineWrap(true);
        personalizationTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(personalizationTextArea);

        JButton nextButton = new JButton("Next");

        add(personalizationLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(nextButton, BorderLayout.SOUTH);

        // Add action listener to the Next button
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LetterOutput letterOutput = new LetterOutput(teacherInfo, studentInfo, ratings, ratingDescriptions,
                        personalizationTextArea.getText());
                letterOutput.setVisible(true);
                Personalization.this.dispose();
            }
        });
    }
}

class LetterOutput extends JFrame {
    private Map<String, String> teacherInfo;
    private Map<String, String> studentInfo;
    private Map<String, Integer> ratings;
    private String[] ratingDescriptions;
    private String personalizationText;

    private JTextArea letterTextArea;

    public LetterOutput(Map<String, String> teacherInfo, Map<String, String> studentInfo, Map<String, Integer> ratings,
            String[] ratingDescriptions, String personalizationText) {
        this.teacherInfo = teacherInfo;
        this.studentInfo = studentInfo;
        this.ratings = ratings;
        this.ratingDescriptions = ratingDescriptions;
        this.personalizationText = personalizationText;

        setTitle("Letter of Recommendation - Letter Output");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        letterTextArea = new JTextArea(20, 40);
        letterTextArea.setLineWrap(true);
        letterTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(letterTextArea);

        JButton exportButton = new JButton("Export");

        add(scrollPane, BorderLayout.CENTER);
        add(exportButton, BorderLayout.SOUTH);

        generateLetter();

        // Add action listener to the Export button
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement logic to export the letter
                // You can use FileWriter or other methods to save the letter to a file
                // Here, we'll just print the letter to the console for demonstration
                System.out.println(letterTextArea.getText());
            }
        });
    }

    private void generateLetter() {
        StringBuilder letter = new StringBuilder();

        letter.append("To Whom It May Concern,\n\n");
        letter.append("I am writing to highly recommend ").append(studentInfo.get("Student Name"))
                .append(" for any academic pursuit. ");
        letter.append("In my role as a teacher, I have had the privilege of having ")
                .append(studentInfo.get("Student Name"));
        letter.append(" in my class, and it has been a truly remarkable experience.\n\n");
        letter.append("Teacher Information:\n");
        for (Map.Entry<String, String> entry : teacherInfo.entrySet()) {
            letter.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        letter.append("\n");

        letter.append("Student Information:\n");
        letter.append("Name: ").append(studentInfo.get("Student Name")).append("\n");
        letter.append("Gender: ").append(studentInfo.get("Gender")).append("\n");
        letter.append("Unweighted GPA: ").append(studentInfo.get("Unweighted GPA")).append("\n");
        letter.append("Number of AP Classes: ").append(studentInfo.get("Number of AP Classes")).append("\n\n");

        letter.append("In terms of the ratings, here is what I would say about ")
                .append(studentInfo.get("Student Name")).append(":\n");
        for (int i = 0; i < ratingDescriptions.length; i++) {
            letter.append(ratingDescriptions[i]).append("\n");
        }
        letter.append("\n");

        letter.append("Furthermore, ").append(studentInfo.get("Student Name"))
                .append(" stands out in my class not only ");
        letter.append("for their academic achievements but also for their outstanding character. ");
        letter.append(personalizationText).append("\n\n");

        letter.append("In conclusion, I wholeheartedly recommend ").append(studentInfo.get("Student Name"));
        letter.append(
                " for any academic or personal endeavor. They are an exceptional individual with a bright future ahead.\n\n");
        letter.append("Sincerely,\n");
        letter.append(teacherInfo.get("Full Name"));

        letterTextArea.setText(letter.toString());
    }
}
