import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

//Citations:
//https://www.javatpoint.com/java-hashmap - All these links Helped me to use Hashmap along with some help from ChatGpt to intergrate it into my code
//https://www.programiz.com/java-programming/hashmap  
//https://stackoverflow.com/questions/34601003/i-have-a-hashmap-how-can-i-integrate-it-with-gui-i-want-to-type-in-the-integer 
//

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

        // Use a BorderLayout for the main container
        setLayout(new BorderLayout());

        // Create two panels for teacher and student information
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

        // Create a bottom panel for the "Next" button
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

        // Add the button panel to the bottom
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

    private String getRatingDescription(String category, int value) {
        if (category.equals("Leadership")) {
            if (value < 4) {
                if (studentInfo.get("Gender").equals("She/Her")) {
                    return "She is unfortunately, not a natural leader. " +
                            "She finds it hard to take initiative when collaborating with and also with communication";
                } else {
                    return "She is unfortunately, not a natural leader. " +
                            "She finds it hard to take initiative when collaborating with and also with communication";
                }
            } else if (value < 7) {
                return "She does occasionally show flashes of a leader, but Her lack of willingess to communicate during class hinders Her potential.";
            } else if (value < 9) {
                return "She is a leader and regularly takes the initiatve when the oppurtunity is presented to her during times of collaboration.";
            } else {
                if (studentInfo.get("Gender").equals("She/Her")) {
                    return "She is a natural leader. She always takes the initative when working with others while taking the time to make sure everyone on her team is on the same pace.";
                } else {
                    return "She is a natural leader. She always takes the initative when working with others while taking the time to make sure everyone on her team is on the same pace.";
                }
            }
        }

        if (category.equals("Participation")) {
            if (value < 4) {
                if (studentInfo.get("Gender").equals("She/Her")) {
                    return "Unfortunately, she rarely participates during class discussions or asks questions. " +
                            "This often leads to not performing well on tests and quizzes, which may hinder her in any college courses she takes.";
                } else {
                    return "Unfortunately, he rarely participates during class discussions or asks questions. " +
                            "This often leads to not performing well on tests and quizzes, which may hinder him in any college courses he takes.";
                }
            } else if (value < 7) {
                return "She occasionally asks for help on concepts she may not understand, but her participation during class discussions usually stays at a minimum.";
            } else if (value < 9) {
                return "She regularly asks questions about past or new concepts. Although sometimes hesitant to give her personal input during class discussions, she is never afraid to reach out for support.";
            } else {
                if (studentInfo.get("Gender").equals("She/Her")) {
                    return "She is unafraid to ask questions on any concepts that may concern her. She regularly raises her hand to quench her urge to understand even the minor details. Her willingness to reach out for support will help her be successful in any college course she takes.";
                } else {
                    return "He is unafraid to ask questions on any concepts that may concern him. He regularly raises his hand to quench his urge to understand even the minor details. His willingness to reach out for support will help him be successful in any college course he takes.";
                }
            }
        } else if (category.equals("Work Ethic")) {
            if (value < 4) {
                if (studentInfo.get("Gender").equals("She/Her")) {
                    return "In addition, she provides minimal effort in my class. Her homework is turned in on time only about half the time, and usually with apparent minimal effort made."
                            +
                            " She does not take advantage of the class time I provide for students to work on homework, and she usually scores low on quizzes and tests.";
                } else {
                    return "In addition, he provides minimal effort in my class. His homework is turned in on time only about half the time, and usually with apparent minimal effort made."
                            +
                            " He does not take advantage of the class time I provide for students to work on homework, and he usually scores low on quizzes and tests.";
                }
            } else if (value < 7) {
                return "She provides an acceptable amount of effort in my class. For the most part, she tries to turn in most of her work on time and sometimes utilizes the provided class time to study or do her homework. However, her performance on quizzes and tests aren't always within the class average, but with the proper effort, I believe she could be very successful.";
            } else if (value < 9) {
                return "She has a great work ethic when it comes to my class. She rarely submits any assignments to me late and takes great care with completing her homework with great effort. For the most part, her scores on tests reflect the time and effort she puts into the class.";
            } else {
                if (studentInfo.get("Gender").equals("She/Her")) {
                    return "She is an exceptional student that consistently displays a stellar work ethic inside and outside the classroom. She is very proactive on her homework assignments by completing as much as possible over the weekend. Students look to her to be their role model of superb work habits and respectful behavior inside and outside the classroom.";
                } else {
                    return "He is an exceptional student that consistently displays a stellar work ethic inside and outside the classroom. He is very proactive on his homework assignments by completing as much as possible over the weekend. Students look to him to be their role model of superb work habits and respectful behavior inside and outside the classroom.";
                }
            }
        }
        if (category.equals("Personality")) {
            if (value < 4) {
                return "She hasn't made much effort to get to know the other students in class, so I haven't seen her personality shine through yet. I would hope that one day she would open up so I will have the opportunity to get to know her real personality.";
            } else if (value < 7) {
                if (studentInfo.get("Gender").equals("She/Her")) {
                    return "She shows politeness when greeting me at the beginning of class, but sometimes when I engage in a conversation with her, she appears distracted and inattentive to our conversations.";
                } else {
                    return "He shows politeness when greeting me at the beginning of class, but sometimes when I engage in a conversation with him, he appears distracted and inattentive to our conversations.";
                }
            } else if (value < 9) {
                return "She keeps the class atmosphere amusing with her silliness and humor. Although it is a little too distracting to the class once in a while, her presence in the class is always appreciated.";
            } else {
                if (studentInfo.get("Gender").equals("She/Her")) {
                    return "She is an extremely silly person that always successfully makes me laugh. She is a great student and friend to be around that emanates positivity wherever she goes.";
                } else {
                    return "He is an extremely silly person that always successfully makes me laugh. He is a great student and friend to be around that emanates positivity wherever he goes.";
                }
            }
        } else if (category.equals("Attitude")) {
            if (value < 4) {
                if (studentInfo.get("Gender").equals("She/Her")) {
                    return "Although she is a bright individual in some settings, I believe it is important to address her attitude and behavior in class. She will often intercede during my discussions with disrespectful comments and shows a disinterest in the subject.";
                } else {
                    return "Although he is a bright individual in some settings, I believe it is important to address his attitude and behavior in class. He will often intercede during my discussions with disrespectful comments and shows a disinterest in the subject.";
                }
            } else if (value < 7) {
                return "She commonly brings an okay attitude to my class. She generally brings a positive attitude to my classroom and seldom disrupts myself or other students.";
            } else if (value < 9) {
                return "She has displayed a very positive attitude in my classroom that I would like to commend. She is very courteous when interacting with me and her classmates, and she never hesitates to ask further questions on certain concepts.";
            } else {
                if (studentInfo.get("Gender").equals("She/Her")) {
                    return "She is an exceptional student that consistently displays a stellar attitude inside and outside the classroom. She is very proactive on her homework assignments by completing as much as possible over the weekend.";
                } else {
                    return "He is an exceptional student that consistently displays a stellar attitude inside and outside the classroom. He is very proactive on his homework assignments by completing as much as possible over the weekend.";
                }
            }
        } else if (category.equals("Listening")) {
            if (value < 4) {
                if (studentInfo.get("Gender").equals("She/Her")) {
                    return "She faces significant challenges when paying attention to the material being taught in class. She does demonstrate the capability to understand the concepts taught in class, but her limited listening skills have been a roadblock to her overall engagement.";
                } else {
                    return "He faces significant challenges when paying attention to the material being taught in class. He does demonstrate the capability to understand the concepts taught in class, but his limited listening skills have been a roadblock to his overall engagement.";
                }
            } else if (value < 7) {
                return "She has an adequate level of listening skills to understand the concepts being taught in class. Although she doesn't particularly excel in this area, it is acceptable enough for her to decently contribute to class discussions and follow my directions.";
            } else if (value < 9) {
                return "She has a commendable level of listening skills that serve her well in class. She is able to absorb the content at a good rate that allows her to complete her homework and assignments efficiently.";
            } else {
                return "She has a great talent for listening which has treated her well in her learning environment. In her relationships, she has the rare ability to make her peers feel heard and understood, and this rare gift also supports her understanding when she is actively listening to my lessons.";
            }
        } else if (category.equals("Collaboration")) {
            if (value < 4) {
                return "Although she has a lot of potential, her collaboration is an area she can vastly improve on. She often struggles to effectively contribute to group efforts and discussions and can often even hinder the group. I believe she has the potential to grow in this area, but I regret to inform that I haven't seen too much progress on this though.";
            } else if (value < 7) {
                return "She demonstrates satisfactory collaboration capabilities, allowing her to somewhat contribute to group projects. Although she shows a willingness to work with others, getting along with others more effectively is certainly an area she can grow in.";
            } else if (value < 9) {
                return "She possesses the remarkable ability to actively make positive contributions in any team or group. She has a natural talent to bring out the best in her classmates, which contributes to her group's ability to be productive.";
            } else {
                return "She never fails to impress me with her willingness to offer assistance to her classmates. She is very adaptable, solution-oriented, and has an open persona that makes her a wonderful partner to collaborate with. This unique skill will certainly serve her well in her academic and professional settings.";
            }
        } else if (category.equals("Dependability")) {
            if (value < 4) {
                return "Although this is not the brightest assessment of her dependability as a student, I believe this area is important to address. Her lack of reliability in meeting deadlines and completing assignments is not tolerable. She would greatly benefit from guidance in achieving these goals, but as of this time, she has shown that she is incapable of being dependable.";
            } else if (value < 7) {
                return "For the most part, she is usually pretty dependable when it comes to assisting peers and positively contributing to group projects. Occasionally she falls short at times with her commitments, but overall she will strive to be a dependable asset to any group she works with.";
            } else if (value < 9) {
                return "She effectively manages to meet her commitments and contributes reliably to her group's efforts. Her consistency in being dependable is commendable and greatly benefits the team's success.";
            } else {
                return "She has an outstanding track record of dependability. Her dedication to meeting her commitments and consistently contributing to group efforts is truly remarkable. She serves as a role model for her peers in this regard.";
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
