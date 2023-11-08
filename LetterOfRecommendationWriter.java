import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

//Citations:
//https://www.javatpoint.com/java-hashmap - All these links Helped me to use Hashmap along with some help from ChatGpt to intergrate it into my code
//https://www.programiz.com/java-programming/hashmap  
//https://stackoverflow.com/questions/34601003/i-have-a-hashmap-how-can-i-integrate-it-with-gui-i-want-to-type-in-the-integer 
// Java Ternary - https://www.geeksforgeeks.org/java-ternary-operator-with-examples/ and https://stackoverflow.com/questions/12643973/java-calling-a-method-using-a-ternary-operator 

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
        setSize(600, 300); // Wider window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel teacherPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JPanel studentPanel = new JPanel(new GridLayout(5, 2, 10, 10));

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

        teacherPanel.add(teacherLabel);
        teacherPanel.add(new JLabel(""));
        teacherPanel.add(new JLabel("Full Name:"));
        teacherPanel.add(teacherFullName);
        teacherPanel.add(new JLabel("School:"));
        teacherPanel.add(teacherSchool);
        teacherPanel.add(new JLabel("Subject You Teach:"));
        teacherPanel.add(teacherSubject);
        teacherPanel.add(new JLabel("Email Address:"));
        teacherPanel.add(teacherEmail);

        studentPanel.add(studentLabel);
        studentPanel.add(new JLabel(""));
        studentPanel.add(new JLabel("Student Name:"));
        studentPanel.add(studentName);
        studentPanel.add(new JLabel("Gender:"));
        studentPanel.add(studentGender);
        studentPanel.add(new JLabel("Unweighted GPA:"));
        studentPanel.add(studentGPA);
        studentPanel.add(new JLabel("Number of AP Classes:"));
        studentPanel.add(studentAPClasses);

        JPanel buttonPanel = new JPanel();
        JButton nextButton = new JButton("Next");
        buttonPanel.add(nextButton);

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

        add(teacherPanel, BorderLayout.WEST);
        add(studentPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
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

        for (int i = 0; i < 9; i++) {
            add(labels[i]);
            add(sliders[i]);
        }

        add(new JLabel(""));
        add(ratingLabel);

        JButton nextButton = new JButton("Next");
        add(nextButton);

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Map<String, Integer> ratings = new HashMap<>();
                String[] ratingDescriptions = new String[9];

                for (int i = 0; i < 9; i++) {
                    int value = sliders[i].getValue();
                    String category = labels[i].getText();
                    String description = getRatingDescription(category, value);
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

    // Java Ternary -
    // https://www.geeksforgeeks.org/java-ternary-operator-with-examples/ and
    // https://stackoverflow.com/questions/12643973/java-calling-a-method-using-a-ternary-operator
    private String getRatingDescription(String category, int value) {
        String studentGender = studentInfo.get("Gender");
        String pronounSubject = "they";
        String pronounPossessive = "their";
        String pronounObject = "them";
        String genderPronoun = studentGender.equals("She/Her") ? "she" : "he";
        String genderPronounCap = studentGender.equals("She/Her") ? "She" : "He";

        if (studentGender.equals("He/Him")) {
            pronounSubject = "he";
            pronounPossessive = "his";
            pronounObject = "him";
        } else if (studentGender.equals("She/Her")) {
            pronounSubject = "she";
            pronounPossessive = "her";
            pronounObject = "her";
        }

        if (category.equals("Leadership")) {
            if (value < 4) {
                return pronounSubject + " is unfortunately, not a natural leader. " +
                        pronounSubject
                        + " finds it hard to take initiative when collaborating with others and also with communication.";
            } else if (value < 7) {
                return "While " + pronounSubject + " does occasionally show flashes of leadership, " +
                        pronounPossessive + " lack of willingness to communicate during class hinders " +
                        pronounPossessive + " potential.";
            } else if (value < 9) {
                return pronounSubject
                        + " is a leader and regularly takes the initiative when the opportunity is presented during times of collaboration.";
            } else {
                return pronounSubject
                        + " always takes the initiative when working with others while taking the time to make sure everyone on "
                        +
                        pronounPossessive + " team is on the same page.";
            }
        }

        if (category.equals("Participation")) {
            if (value < 4) {
                return "Unfortunately, " + genderPronoun
                        + " rarely participates during class discussions or asks questions. " +
                        "This often leads to poor performance on tests and quizzes, which may hinder " + genderPronoun
                        + " in college courses.";
            } else if (value < 7) {
                return genderPronounCap
                        + " occasionally asks for help on unfamiliar concepts, but participation in class discussions remains minimal.";
            } else if (value < 9) {
                return genderPronounCap
                        + " regularly asks questions about past or new concepts and is not afraid to seek support.";
            } else {
                return genderPronounCap
                        + " is unafraid to ask questions and actively engages in class, which will help "
                        + genderPronoun + " succeed in college courses.";
            }
        } else if (category.equals("Work Ethic")) {
            if (value < 4) {
                return "In addition, " + genderPronoun
                        + " provides minimal effort in the class, often turning in homework late with minimal effort and scoring low on quizzes and tests.";
            } else if (value < 7) {
                return genderPronounCap + " provides an acceptable amount of effort, but with more effort, "
                        + genderPronoun + " could be very successful.";
            } else if (value < 9) {
                return genderPronounCap + " has a great work ethic, submitting assignments on time with great effort.";
            } else {
                return genderPronounCap
                        + " is an exceptional student with a stellar work ethic inside and outside the classroom.";
            }
        } else if (category.equals("Personality")) {
            if (value < 4) {
                return "The student hasn't made much effort to get to know classmates, so their personality hasn't shined through yet.";
            } else if (value < 7) {
                return genderPronounCap
                        + " shows politeness when greeting but can sometimes appear distracted during conversations.";
            } else if (value < 9) {
                return "The student keeps the class atmosphere amusing with silliness and humor.";
            } else {
                return genderPronounCap
                        + " is an extremely silly person who always successfully makes others laugh and emanates positivity.";
            }
        } else if (category.equals("Attitude")) {
            if (value < 4) {
                return "Although the student is bright, it is important to address their attitude and behavior in class, as they often intercede with disrespectful comments.";
            } else if (value < 7) {
                return genderPronounCap
                        + " commonly brings an okay attitude to class and generally brings a positive attitude.";
            } else if (value < 9) {
                return genderPronounCap
                        + " has displayed a very positive attitude in class, being courteous and eager to ask questions.";
            } else {
                return genderPronounCap
                        + " is an exceptional student who consistently displays a stellar attitude inside and outside the classroom.";
            }
        } else if (category.equals("Listening Skills")) {
            if (value < 4) {
                return genderPronounCap
                        + " faces challenges when paying attention to the material being taught in class but demonstrates the capability to understand the concepts.";
            } else if (value < 7) {
                return genderPronounCap + " has an adequate level of listening skills to understand class content.";
            } else if (value < 9) {
                return genderPronounCap + " has commendable listening skills, absorbing content efficiently.";
            } else {
                return genderPronounCap
                        + " has a great talent for listening and makes peers feel heard and understood.";
            }
        } else if (category.equals("Collaboration")) {
            if (value < 4) {
                return "Although the student has potential, their collaboration is an area they can vastly improve on, often struggling to contribute effectively to group efforts.";
            } else if (value < 7) {
                return genderPronounCap
                        + " demonstrates satisfactory collaboration capabilities, allowing them to contribute to group projects.";
            } else if (value < 9) {
                return genderPronounCap
                        + " possesses the remarkable ability to make positive contributions in any team or group.";
            } else {
                return genderPronounCap
                        + " never fails to impress with willingness to offer assistance and is a wonderful partner to collaborate with.";
            }
        } else if (category.equals("Dependability")) {
            if (value < 4) {
                return "The student's lack of reliability in meeting deadlines and completing assignments is not tolerable and needs improvement.";
            } else if (value < 7) {
                return genderPronounCap
                        + " is usually pretty dependable when assisting peers and contributing to group projects.";
            } else if (value < 9) {
                return genderPronounCap
                        + " effectively manages to meet commitments and contributes reliably to group efforts.";
            } else {
                return genderPronounCap
                        + " has an outstanding track record of dependability, serving as a role model for peers.";
            }
        }

        if (category.equals("Listening Skills")) {
            if (value < 4) {
                return genderPronounCap
                        + " faces challenges when paying attention to the material being taught in class but demonstrates the capability to understand the concepts.";
            } else if (value < 7) {
                return genderPronounCap
                        + " has an adequate level of listening skills to understand the concepts being taught in class.";
            } else if (value < 9) {
                return genderPronounCap + " has commendable listening skills that serve well in class.";
            } else {
                return genderPronounCap
                        + " has a great talent for listening and makes peers feel heard and understood.";
            }
        } else if (category.equals("Adept")) {
            if (value < 4) {
                return "Although " + genderPronoun
                        + " has potential, there's room for improvement in the adeptness of certain skills or subjects.";
            } else if (value < 7) {
                return genderPronounCap
                        + " demonstrates satisfactory skills in the area, with potential for further improvement.";
            } else if (value < 9) {
                return genderPronounCap + " excels in the subject and consistently displays adeptness.";
            } else {
                return genderPronounCap + " is exceptionally adept in the subject and stands out as a role model.";
            }
        } else if (category.equals("Creativity")) {
            if (value < 4) {
                return genderPronounCap
                        + " needs to work on expressing creativity and originality in assignments and projects.";
            } else if (value < 7) {
                return genderPronounCap + " shows some creativity in assignments but has room to enhance originality.";
            } else if (value < 9) {
                return genderPronounCap
                        + " consistently demonstrates creativity and originality in assignments and projects.";
            } else {
                return genderPronounCap + " excels in creativity, consistently providing original and innovative work.";
            }
        }
        // If the category is not recognized, return an appropriate default description
        return "No description available for this category.";
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

        public LetterOutput(Map<String, String> teacherInfo, Map<String, String> studentInfo,
                Map<String, Integer> ratings,
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
}