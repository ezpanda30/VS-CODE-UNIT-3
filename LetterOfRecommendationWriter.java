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
                new JLabel("Personality"),
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
        // Leadership
        if (category.equals("Leadership")) {
            if (value < 4) {
                return "Although " + pronounSubject + "  tries to take any leadership position very seriously, " +
                        pronounPossessive
                        + " leadership skills are not at a high enough level to effectively lead" + pronounPossessive
                        + " group to success. ";
            } else if (value < 7) {
                return "Although being a leader isn't a natural quality " + pronounSubject + " possesses, "
                        + pronounSubject + " continues to show some progress in this area with " + pronounObject
                        + " determination and dedication. ";
            } else if (value < 9) {
                return genderPronounCap
                        + " possesses great leadership potential through collaboration during group activities. With even more practice, I have no doubt "
                        + pronounSubject + " will become a marveolous leader.";

            } else {
                return genderPronounCap +
                        " possesses the rare and natural talent to effortlessly lead a group with " + pronounPossessive
                        + " unwavering confidence and friendly persona. ";
            }
        }

        // Participation

        if (category.equals("Participation")) {
            if (value < 4) {
                return "Unfortunately, " + genderPronoun
                        + " rarely participates during class discussions or asks questions " + pronounSubject
                        + " may have concerning the homework. " +
                        "This often leads " + pronounObject
                        + " to not perform well on tests and quizzes which may hinder " + pronounObject
                        + " in any college courses " + pronounSubject + " takes. ";
            } else if (value < 7) {
                return genderPronounCap
                        + " occasionally asks me for help on concepts " + pronounSubject + " may not understand, but "
                        + pronounPossessive + " participation during class discussions usually stay to a minimum. ";
            } else if (value < 9) {
                return genderPronounCap
                        + " regularly asks me questions about about past or new concepts that we have learned. Although sometimes hesitant to give"
                        + pronounPossessive + " personal " +
                        "input during class discussions," + pronounObject
                        + " is usually not afraid to reach out to for further explanation. ";
            } else {
                return genderPronounCap +
                        " is unafraid to ask questions on any concepts that may concern " + pronounObject + ". "
                        + genderPronounCap + " regularly raises " + pronounPossessive + " hand to quench "
                        + pronounPossessive
                        + " urge to " +
                        "understand even the minor details. ";

            }
        }

        // Work Ethic

        else if (category.equals("Work Ethic")) {
            if (value < 4) {
                return "In addition, " + genderPronoun
                        + "does not take advantage of the class time I provide for students to work on homework. ";
            } else if (value < 7) {
                return genderPronounCap + "  provides an acceptable amount of effort in my class. For the most part, "
                        + genderPronoun + " tries to turn in most of " + pronounPossessive
                        + " work on time, and sometimes utilizes the " + "provided class time to study or do "
                        + pronounPossessive + " homework. ";
            } else if (value < 9) {
                return genderPronounCap + " has a great work ethic when it comes to my class. For the most part, "
                        + pronounPossessive + " scores on tests reflect the time and effort " + pronounSubject
                        + " puts into the class. ";
            } else {
                return genderPronounCap
                        + " has an excellent work ethic in my class. " + genderPronounCap
                        + " has never submitted an assignment late, and it's clear how " + pronounSubject
                        + " carefully reviews " + pronounPossessive + " work multiple times to ensure that" +
                        pronounSubject + " understands the concepts. ";
            }
        }

        // Personality

        else if (category.equals("Personality")) {
            if (value < 4) {
                return genderPronounCap
                        + " hasn't made much effort to get to know the other students in class, so I haven't seen "
                        + pronounPossessive + " personality shine through yet. " + "I would hope that one day "
                        + genderPronoun + " would open up to me and " + pronounPossessive + " other classmates. ";
            } else if (value < 7) {
                return genderPronounCap +
                        " shows " + pronounPossessive
                        + " politeness when greeting me at the beginning of class, but sometimes when I engage in a conversation with "
                        + pronounObject + ", " + pronounSubject + " appears " +
                        "distracted and inattentive to our conversations. ";
            } else if (value < 9) {
                return pronounSubject + " keeps the class atmosphere amusing with " + pronounPossessive
                        + " silliness and humor. ";
            } else {
                return genderPronounCap
                        + " is a great student and friend to be around that emanates positivity wherever "
                        + pronounSubject + " goes. ";
            }
        }

        // Attitude

        else if (category.equals("Attitude")) {
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
        }

        // Listening Skills
        else if (category.equals("Listening Skills")) {
            if (value < 4) {
                return genderPronounCap
                        + " will often intercede during my discussions with disrespectful comments and shows a disinterest in the subject. ";
            } else if (value < 7) {
                return genderPronounCap
                        + " generally brings an okay attitude to my class. I believe it is important to note that despite "
                        + pronounPossessive + " lack of effort, it is only sometimes reflected on " + pronounPossessive
                        + " test scores. ";
            } else if (value < 9) {
                return genderPronounCap
                        + " has displayed a very positive attitude in my classroom that I would like to commend. "
                        + genderPronounCap + " is very courteous when interacting with me and " + pronounPossessive
                        + " classmates, and " + pronounSubject
                        + " never hesitates to ask further questions on certain concepts. ";
            } else {
                return genderPronounCap
                        + " is very proactive on " + pronounPossessive
                        + " homework assignments by completing as much as possible over the weekend. Students look to "
                        + pronounObject + " to be their role model for superb work habits. ";

            }
        }

        // Listening

        if (category.equals("Listening Skills")) {
            if (value < 4) {
                return genderPronounCap
                        + " faces significant challenges when paying attention to the material being taught in class. "
                        + genderPronounCap
                        + " is routinely found disengaged from the information in my lectures which will not serve "
                        + pronounObject + " well in college and beyond. ";
            } else if (value < 7) {
                return genderPronounCap
                        + " has an adequate level of listening skills to understand the concepts being taught in class. Although "
                        + pronounSubject + " doesn't particularly excel in this area, it is acceptable enough for "
                        + pronounObject + " to decently contribute to class discussions. ";
            } else if (value < 9) {
                return genderPronounCap + " has a commendable level of listening skills that serves " + pronounObject
                        + " well in class. " + genderPronounCap
                        + " is able to absorb the content at a good rate that allows " + pronounObject + " to complete "
                        + pronounPossessive + " homework and assignments efficiently. ";
            } else {
                return genderPronounCap
                        + " has a great talent for listening which has treated " + pronounObject
                        + " well in the learning environment. In " + pronounObject + "relationships, " + pronounSubject
                        + " has the rare ability to make her peers feel heard and understood. ";
            }
        }

        // Collaboration

        else if (category.equals("Collaboration")) {
            if (value < 4) {
                return "Although " + pronounSubject + " has a lot of potential, " + pronounPossessive
                        + " collaboration is an area " + pronounSubject + " can vastly improve on. " + genderPronounCap
                        + " often struggles to effectively contribute to group efforts and discussions and can often even hinder the group. ";
            } else if (value < 7) {
                return genderPronounCap
                        + " demonstrates satisfactory collaboration capabilities, allowing " + pronounObject
                        + " to somewhat contribute to group projects. Although " + pronounSubject
                        + " shows a willingness to work with others, getting along with others more effectively is certainly an area "
                        + pronounSubject + " can grow in. ";
            } else if (value < 9) {
                return genderPronounCap
                        + " has a natural talent to bring out the best in " + pronounPossessive
                        + " classmates, which contributes to " + pronounPossessive
                        + " group's ability to be productive. ";
            } else {
                return genderPronounCap
                        + " is very adaptable, solution-oriented, and has an open persona that makes " + pronounObject
                        + " a wonderful partner to collaborate with. This unique skill will certainly serve "
                        + pronounObject + " well in " + pronounPossessive + " academic and professional settings. ";
            }
        }

        // Dependability

        else if (category.equals("Dependability")) {
            if (value < 4) {
                return "I understand that " + pronounSubject
                        + " is a very busy individual with many extracurriculars, but " + pronounPossessive
                        + " lack of reliability in meeting deadlines and completing assignments is not tolerable. ";
            } else if (value < 7) {
                return "For the most part " + genderPronoun
                        + " is usually pretty dependable when it comes to assisting peers and positively contributing to group projects. However, "
                        + pronounSubject + " may fall short due to " + pronounPossessive + " numerous commitments. ";
            } else if (value < 9) {
                return genderPronounCap
                        + " demonstrates a strong sense of responsibility and dependability. " + genderPronounCap
                        + " is usually very punctual and ready to learn once " + pronounSubject
                        + " enters the classroom. ";
            } else {
                return "When " + genderPronoun
                        + " receives an assignment, " + pronounSubject + " makes it " + pronounPossessive
                        + " utmost duty to complete it with great care and attention to detail. ";
            }
        }

        // Adept
        else if (category.equals("Adept")) {
            if (value < 4) {
                return "It is difficult for " + pronounObject + " to keep up in class because " + pronounSubject
                        + " is typically a much slower learner than " + pronounPossessive + " other classmates. ";
            } else if (value < 7) {
                return genderPronounCap
                        + " is naturally a very slow learner, but " + pronounSubject
                        + " puts in the effort to try and boost " + pronounPossessive + " learning speed. ";
            } else if (value < 9) {
                return genderPronounCap + " is fairly quick at retaining information taught in class, and "
                        + pronounSubject + " can usually explain back to me in " + pronounPossessive + " own words. ";
            } else {
                return genderPronounCap
                        + " ability to retain information fully and efficiently is extremely impressive, allowing "
                        + pronounObject + " to prepare for exams with minimal studying. ";
            }
        }

        // Creativity
        else if (category.equals("Creativity")) {
            if (value < 4) {
                return genderPronounCap
                        + " lacks the creativity to find a solution to homework questions that are in an altered format from the way I've taught. ";
            } else if (value < 7) {
                return "Although" + genderPronoun + " creativity is not one of " + pronounPossessive
                        + " stronger points, " + pronounSubject
                        + " works hard to think outside of the box to solve unusual problems. ";
            } else {
                return genderPronounCap
                        + " uses " + pronounPossessive
                        + " exceptional creativity to think outside the box and utilize unique methods to solve problems. ";
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

            letter.append("Student Information:\n");
            letter.append("Name: ").append(studentInfo.get("Student Name")).append("\n");
            letter.append("Gender: ").append(studentInfo.get("Gender")).append("\n");
            letter.append("Unweighted GPA: ").append(studentInfo.get("Unweighted GPA")).append("\n");
            letter.append("Number of AP Classes: ").append(studentInfo.get("Number of AP Classes")).append("\n\n");

            letter.append("To Whom It May Concern,\n\n");
            letter.append("I would like to recommend my student ").append(studentInfo.get("Student Name"))
                    .append(" for the 2024-2025 school year. ");
            letter.append("I have had the privilege of being ")
                    .append(studentInfo.get("Student Name"));
            letter.append(" in my class, and it has been a truly remarkable experience.\n\n");
            letter.append("Teacher Information:\n");
            for (Map.Entry<String, String> entry : teacherInfo.entrySet()) {
                letter.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            letter.append("\n");

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