import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.Timer;

//Connection Class
class DatabaseConnection {
    int id;
    String name;
    String email;
    String password;
    int score;
    String mainQ;
    String query;
    String sid;
    String marks;
    String questionQuery;
    Statement s;
    ResultSet resultSet;

    public void connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/";
            String username = "root";
            String Password = "";
            java.sql.Connection conn = DriverManager.getConnection(url, username, Password);
            System.out.println("Connection Successful");
            s = conn.createStatement();
            mainQ = "use OnlineExam";
            s.executeUpdate(mainQ);
            query = "Select * from Users where id = " + sid + "";

            resultSet = s.executeQuery(query);
            while (resultSet.next()) {
                name = resultSet.getString("name");
                password = resultSet.getString("password");
                score = resultSet.getInt("score");
                id = resultSet.getInt("id");
                email = resultSet.getString("email");
                marks = resultSet.getString("score");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Server Problem", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}


//Questions
class Questions {
    public String questionQuery;
    ArrayList<String> qN = new ArrayList<String>();
    ArrayList<String> Q = new ArrayList<String>();
    ArrayList<String> op1 = new ArrayList<String>();
    ArrayList<String> op2 = new ArrayList<String>();
    ArrayList<String> op3 = new ArrayList<String>();
    ArrayList<String> op4 = new ArrayList<String>();
    ArrayList<String> ans = new ArrayList<String>();
    ArrayList<String> qT = new ArrayList<String>();
    int i = 0;
    int maxQn;
    int minQn;
    int length;

    Questions() {
        ResultSet questionSet;

        try {
            DatabaseConnection c = new DatabaseConnection();
            c.mainQ = "USE Questions;";
            c.connect();
            questionQuery = "select * from Questions";
            questionSet = c.s.executeQuery(questionQuery);
            while (questionSet.next()) {
                qN.add(i, questionSet.getString("QN"));
                Q.add(i, questionSet.getString("Question"));
                op1.add(i, questionSet.getString("op1"));
                op2.add(i, questionSet.getString("op2"));
                op3.add(i, questionSet.getString("op3"));
                op4.add(i, questionSet.getString("op4"));
                ans.add(i, questionSet.getString("CA"));
                qT.add(i, questionSet.getString("QT"));

                i++;
            }
            c.s.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Server Error"
                    , "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class OnlineExam extends JFrame implements ActionListener {
    int a;
    String pw;
    private Container container;
    private JLabel utitle;
    private JTextField userId;
    private JTextField mail;
    private JPasswordField passw;
    private JLabel ptitle;
    private JButton sbtn;
    private JLabel question;
    private JButton next;
    private JButton submit;
    private JLabel opt;
    private JButton skip;
    private JButton previous;
    private JButton editProfile;
    private JButton startExam;
    private JButton logOut;

    private JButton myProfile;
    Timer t;
    private JLabel timerLabel;
    //JSeparator hzl;
    private JLabel categories;
    private JPanel questionnums;
    private int currentQuestionIndex = 0;
    private Questions q = new Questions();
    ;
    JScrollPane questionScrollPane;
    private DatabaseConnection dbConnection;
    JPanel panel;
    JPanel panel2;
    private JButton cat1;
    private JButton cat2;
    private JButton cat3;
    private JButton cat4;
    private JRadioButton option1;
    private JRadioButton option2;
    private JRadioButton option3;
    private JRadioButton option4;
    private JPanel opotionpnl;
    private JButton logOutD;
    ButtonGroup btngrp;
    private String ans;
    private String[] userAns = new String[100];
    public int marks = 0;
    private int length = 0;
    JLabel nameLabel;
    JLabel idLabel;
    JLabel marksLabel;
    JLabel score;
    JLabel email;
    JButton changePw;
    JButton Exit;
    JButton savePw;
    JButton saveDtls;
    JButton changeDtls;
    JLabel marked;
    JLabel reviewLabel;
    ArrayList<String> review = new ArrayList<String>();
    private DatabaseConnection c = new DatabaseConnection();
    private Timer timer;

    //Constructor
    public OnlineExam() throws SQLException {

        c.connect();

        setTitle("Login Form");
        setSize(500, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.getHSBColor(120, 100, 100));
        container = getContentPane();
        container.setLayout(null);

        utitle = new JLabel("UserId");
        utitle.setFont(new Font("Arial", Font.BOLD, 16));
        utitle.setSize(100, 20);
        utitle.setLocation(90, 30);
        container.add(utitle);


        userId = new JTextField();
        userId.setSize(170, 20);
        userId.setLocation(190, 30);
        userId.setFont(new Font("roboto", Font.BOLD, 14));
        container.add(userId);

        passw = new JPasswordField();
        passw.setSize(170, 20);
        passw.setLocation(190, 70);
        passw.setFont(new Font("roboto", Font.BOLD, 14));
        container.add(passw);

        mail = new JTextField();
        mail.setSize(170, 20);
        mail.setLocation(190, 70);
        mail.setFont(new Font("roboto", Font.BOLD, 14));
        mail.setVisible(false);
        container.add(mail);

        ptitle = new JLabel("Password");
        ptitle.setFont(new Font("Arial", Font.BOLD, 16));
        ptitle.setSize(100, 20);
        ptitle.setLocation(90, 70);
        container.add(ptitle);

        sbtn = new JButton("Login");
        sbtn.setFont(new Font("Arial", Font.BOLD, 16));
        sbtn.setSize(100, 30);
        sbtn.setLocation(200, 110);
        sbtn.addActionListener(this);
        container.add(sbtn);

        editProfile = new JButton("My Profile");
        editProfile.setFont(new Font("Arial", Font.BOLD, 16));
        editProfile.setSize(120, 30);
        editProfile.setLocation(180, 60);
        editProfile.addActionListener(this);
        editProfile.setVisible(false);
        container.add(editProfile);

        startExam = new JButton("Start Exam");
        startExam.setFont(new Font("Arial", Font.BOLD, 16));
        startExam.setSize(120, 30);
        startExam.setLocation(30, 60);
        startExam.addActionListener(this);
        startExam.setVisible(false);
        container.add(startExam);

        logOut = new JButton("Logout");
        logOut.setFont(new Font("Arial", Font.BOLD, 16));
        logOut.setSize(120, 30);
        logOut.setLocation(330, 60);
        logOut.addActionListener(this);
        logOut.setVisible(false);
        container.add(logOut);

        logOutD = new JButton("Quit");
        logOutD.setFont(new Font("Arial", Font.BOLD, 16));
        logOutD.setSize(100, 30);
        logOutD.setLocation(1400, 10);
        logOutD.addActionListener(this);
        logOutD.setVisible(false);
        container.add(logOutD);
//timer
        timerLabel = new JLabel();
        timerLabel.setSize(100, 20);
        timerLabel.setLocation(1330, 15);
        timerLabel.setVisible(false);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.green);

//categories
        panel = new JPanel();
        panel2 = new JPanel();
        panel.setVisible(false);
        panel2.setVisible(false);
        container.add(timerLabel);


        panel.setLayout(new GridLayout());
        panel2.setLayout(new GridLayout());
        panel.setLocation(20, 225);
        panel.setSize(300, 120);

        panel2.setLocation(20, 375);
        panel2.setSize(300, 120);
        container.add(panel);
        container.add(panel2);


        categories = new JLabel("CATEGORIES", SwingConstants.CENTER);
        categories.setFont(new Font("Arial", Font.BOLD, 17));
        categories.setLocation(70, 150);
        categories.setSize(200, 50);
        categories.setAlignmentX(JLabel.CENTER);
        container.add(categories);
        categories.setVisible(false);

        cat1 = new JButton("Technical");
        cat1.addActionListener(this);
        cat1.setFont(new Font("arial", Font.BOLD, 16));
        cat1.setBackground(new Color(0, 250, 107));
        cat1.getModel().setSelected(true);
        cat1.setForeground(Color.BLACK);
        panel.add(cat1);
        cat2 = new JButton("Aptitude");
        cat2.addActionListener(this);
        cat2.setFont(new Font("arial", Font.BOLD, 16));
        panel.add(cat2);
        cat3 = new JButton("Verbal Reasoning");
        cat3.addActionListener(this);
        cat3.setFont(new Font("arial", Font.BOLD, 16));
        panel2.add(cat3);
        cat4 = new JButton("Reasoning");
        cat4.addActionListener(this);
        cat4.setFont(new Font("arial", Font.BOLD, 16));
        panel2.add(cat4);

        question = new JLabel(q.qN.get(0) + ". " + q.Q.get(0));
        question.setSize(800, 300);
        question.setLocation(530, 10);
        question.setFont(new Font("arial", Font.BOLD, 20));
        container.add(question);
        questionScrollPane = new JScrollPane();

        opt = new JLabel("Options: ");
        opt.setLocation(535, 180);
        opt.setSize(100, 100);
        container.add(opt);

        opotionpnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btngrp = new ButtonGroup();
        EmptyBorder broder = new EmptyBorder(0, 0, 40, 0);

        option1 = new JRadioButton("A. " + q.op1.get(0));
        option1.setSize(500, 100);
        option1.setBackground(Color.white);
        option1.setOpaque(false);
        option1.setFont(new Font("arial", Font.BOLD, 18));
        option1.setBorder(broder);
        option1.setActionCommand("a");
        btngrp.add(option1);

        option2 = new JRadioButton("B. " + q.op2.get(0));
        option2.setSize(500, 100);
        option2.setBackground(Color.white);
        option2.setBorder(broder);
        option2.setOpaque(false);
        option2.setActionCommand("b");
        option2.setFont(new Font("arial", Font.BOLD, 18));
        btngrp.add(option2);

        option3 = new JRadioButton("C. " + q.op3.get(0));
        option3.setSize(500, 100);
        option3.setBackground(Color.white);
        option3.setOpaque(false);
        option3.setBorder(broder);
        option3.setActionCommand("c");
        option3.setFont(new Font("arial", Font.BOLD, 18));
        btngrp.add(option3);

        option4 = new JRadioButton("D. " + q.op4.get(0));
        option4.setSize(500, 100);
        option4.setBackground(Color.white);
        option4.setOpaque(false);
        option4.setBorder(broder);
        option4.setActionCommand("d");
        option4.setFont(new Font("arial", Font.BOLD, 18));

        btngrp.add(option4);
        opotionpnl.add(option1);
        opotionpnl.add(option2);
        opotionpnl.add(option3);
        opotionpnl.add(option4);
        opotionpnl.setSize(400, 400);
        opotionpnl.setLocation(605, 220);
        opotionpnl.setLayout(new BoxLayout(opotionpnl, BoxLayout.Y_AXIS));
        opotionpnl.setOpaque(false);
        container.add(opotionpnl);

        previous = new JButton("Previous");
        previous.setSize(150, 30);
        previous.setLocation(530, 500);
        previous.setFont(new Font("arial", Font.BOLD, 16));
        previous.addActionListener(this);
        container.add(previous);

        skip = new JButton("Mark for Review");
        skip.setSize(180, 30);
        skip.setLocation(710, 500);
        skip.setFont(new Font("arial", Font.BOLD, 16));
        skip.addActionListener(this);
        container.add(skip);

        next = new JButton("Save & Next");
        next.setSize(150, 30);
        next.setLocation(920, 500);
        next.setFont(new Font("arial", Font.BOLD, 16));
        next.addActionListener(this);
        container.add(next);
        next.setVisible(false);

        submit = new JButton("Submit");
        submit.setSize(150, 30);
        submit.setLocation(920, 500);
        submit.setFont(new Font("arial", Font.BOLD, 16));
        submit.addActionListener(this);
        container.add(submit);
        submit.setVisible(false);

        nameLabel = new JLabel();
        nameLabel.setSize(300, 20);
        nameLabel.setLocation(30, 20);
        nameLabel.setFont(new Font("arial", Font.BOLD, 16));
        nameLabel.setVisible(false);
        container.add(nameLabel);

        idLabel = new JLabel();
        idLabel.setSize(300, 20);
        idLabel.setLocation(350, 20);
        idLabel.setFont(new Font("arial", Font.BOLD, 16));
        idLabel.setVisible(false);
        container.add(idLabel);

        marksLabel = new JLabel();
        marksLabel.setSize(300, 20);
        marksLabel.setLocation(350, 50);
        marksLabel.setFont(new Font("arial", Font.BOLD, 16));
        marksLabel.setVisible(false);
        container.add(marksLabel);

        email = new JLabel();
        email.setSize(300, 30);
        email.setLocation(30, 50);
        email.setFont(new Font("arial", Font.BOLD, 16));
        email.setVisible(false);
        container.add(email);

        changeDtls = new JButton("Edit Details");
        changeDtls.setSize(130, 30);
        changeDtls.setLocation(30, 115);
        changeDtls.setFont(new Font("arial", Font.BOLD, 10));
        changeDtls.setVisible(false);
        changeDtls.addActionListener(this);
        container.add(changeDtls);

        changePw = new JButton("Change Password");
        changePw.setSize(130, 30);
        changePw.setLocation(180, 115);
        changePw.setFont(new Font("arial", Font.BOLD, 10));
        changePw.setVisible(false);
        changePw.addActionListener(this);
        container.add(changePw);

        Exit = new JButton("Back");
        Exit.setSize(120, 30);
        Exit.setLocation(330, 115);
        Exit.setFont(new Font("arial", Font.BOLD, 10));
        Exit.setVisible(false);
        Exit.addActionListener(this);
        container.add(Exit);

        savePw = new JButton("Update");
        savePw.setSize(200, 30);
        savePw.setLocation(30, 115);
        savePw.setFont(new Font("arial", Font.BOLD, 10));
        savePw.setVisible(false);
        savePw.addActionListener(this);
        container.add(savePw);

        saveDtls = new JButton("Update");
        saveDtls.setSize(200, 30);
        saveDtls.setLocation(30, 115);
        saveDtls.setFont(new Font("arial", Font.BOLD, 10));
        saveDtls.setVisible(false);
        saveDtls.addActionListener(this);
        container.add(saveDtls);

        marked = new JLabel();
        marked.setVisible(false);
        marked.setLocation(1400, 150);
        marked.setSize(25, 800);
        marked.setFont(new Font("arial", Font.BOLD, 16));
        container.add(marked);

        reviewLabel = new JLabel("Marked Questions");
        reviewLabel.setVisible(false);
        reviewLabel.setLocation(1350, 250);
        reviewLabel.setSize(130, 30);
        reviewLabel.setFont(new Font("arial", Font.BOLD, 13));
        container.add(reviewLabel);

        setVisible(true);
    }

    // Operations
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == sbtn) {
            char[] pwa = passw.getPassword();
            pw = new String(pwa);

            if (userId.getText().equals("") && pw.equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Please enter your UserId & Password", "Blank UserId & Password", JOptionPane.WARNING_MESSAGE);
            } else if (userId.getText().equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Please enter your UserId", "Blank UserId", JOptionPane.WARNING_MESSAGE);
            } else if (pw.equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Please enter your Password", "Blank Password", JOptionPane.WARNING_MESSAGE);
            } else {

                c.sid = userId.getText();
                c.connect();
                int sTId = 0;
                try {
                    sTId = Integer.parseInt(userId.getText());
                } catch (Exception et) {
                    JOptionPane.showMessageDialog(container, "Invalid Input Format", "Error", JOptionPane.ERROR_MESSAGE);

                }
                // Checking
                if (c.id == sTId && pw.equals(c.password)) {
                    setTitle("Welcome " + c.name + "");
                    startExam.setVisible(true);
                    editProfile.setVisible(true);
                    logOut.setVisible(true);
                    logOut.setLocation(330, 60);
                    logOut.setSize(120, 30);

                    sbtn.setVisible(false);
                    utitle.setVisible(false);
                    userId.setVisible(false);
                    ptitle.setVisible(false);
                    passw.setVisible(false);
                    question.setVisible(true);
                    previous.setVisible(true);
                    skip.setVisible(true);
                    opotionpnl.setVisible(true);
                    opt.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(null,
                            "Please enter correct UserId and Password.",
                            "Wrong UserId or Password", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        // profile update
        if (e.getSource() == editProfile) {
            startExam.setVisible(false);
            editProfile.setVisible(false);
            logOut.setVisible(false);
            nameLabel.setVisible(true);
            nameLabel.setText("Name: " + c.name);
            email.setVisible(true);
            email.setText("Email: " + c.email);
            changePw.setVisible(true);
            idLabel.setVisible(true);
            idLabel.setText("Id: " + c.sid);
            marksLabel.setVisible(true);
            marksLabel.setText("Marks: " + c.marks);
            Exit.setVisible(true);
            changeDtls.setVisible(true);
            changeDtls.setLocation(30, 115);
            changePw.setLocation(180, 115);
            Exit.setLocation(330, 115);

        } else if (e.getSource() == logOut) {
            utitle.setVisible(true);
            userId.setVisible(true);
            ptitle.setVisible(true);
            passw.setVisible(true);
            sbtn.setVisible(true);
            startExam.setVisible(false);
            editProfile.setVisible(false);
            userId.setText("");
            passw.setText("");

            timerLabel.setVisible(false);
            categories.setVisible(false);
            panel.setVisible(false);
            panel2.setVisible(false);
            next.setVisible(false);
            submit.setVisible(false);
            setTitle("Login Form");
            setSize(500, 200);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);
            setLocationRelativeTo(null);
            getContentPane().setBackground(Color.getHSBColor(120, 100, 100));
            container.setLayout(null);
            question.setVisible(false);
            previous.setVisible(false);
            skip.setVisible(false);
            opt.setVisible(false);
            logOut.setVisible(false);
            opotionpnl.setVisible(false);
        } else if (e.getSource() == logOutD) {
            int o = JOptionPane.showConfirmDialog(container, "Sure to quit?", "Quit", JOptionPane.YES_NO_OPTION);
            if (o == 0) {
                System.exit(0);
            }

        } else if (e.getSource() == startExam) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            getContentPane().setBackground(Color.WHITE);
            utitle.setVisible(false);
            userId.setVisible(false);
            ptitle.setVisible(false);
            passw.setVisible(false);
            sbtn.setVisible(false);
            startExam.setVisible(false);
            editProfile.setVisible(false);

            logOut.setVisible(false);
            logOutD.setVisible(true);

            reviewLabel.setVisible(true);
            timerLabel.setVisible(true);
            categories.setVisible(true);
            panel.setVisible(true);
            panel2.setVisible(true);
            next.setVisible(true);
            submit.setVisible(false);

            // Start the timer when login is successful
            startTimer();
        } else if (e.getSource() == Exit) {
            startExam.setVisible(true);
            editProfile.setVisible(true);
            logOut.setVisible(true);
            utitle.setVisible(false);
            userId.setVisible(false);
            ptitle.setVisible(false);
            passw.setVisible(false);
            idLabel.setVisible(false);
            marksLabel.setVisible(false);
            nameLabel.setVisible(false);
            changePw.setVisible(false);
            email.setVisible(false);
            savePw.setVisible(true);
            Exit.setVisible(false);
            savePw.setVisible(false);
            changeDtls.setVisible(false);
            Exit.setLocation(330, 115);
            Exit.setSize(120, 30);
            Exit.setFont(new Font("arial", Font.BOLD, 10));
            mail.setVisible(false);
            saveDtls.setVisible(false);
            logOutD.setVisible(false);
            utitle.setSize(190, 20);


        } else if (e.getSource() == changePw) {
            utitle.setVisible(true);
            utitle.setText("New Password");
            utitle.setSize(200, 20);
            utitle.setLocation(90, 30);
            userId.setVisible(true);
            userId.setLocation(240, 30);
            userId.setText("");
            ptitle.setVisible(true);
            ptitle.setText("Confirm Password");
            ptitle.setLocation(90, 70);
            passw.setVisible(true);
            passw.setLocation(240, 70);
            ptitle.setSize(200, 20);
            passw.setText("");
            Exit.setLocation(250, 115);
            changeDtls.setVisible(false);
            idLabel.setVisible(false);
            marksLabel.setVisible(false);
            nameLabel.setVisible(false);
            changePw.setVisible(false);
            email.setVisible(false);
            savePw.setVisible(true);
            saveDtls.setVisible(false);
            saveDtls.setLocation(30, 115);
            Exit.setLocation(250, 115);
            Exit.setSize(200, 30);
            Exit.setFont(new Font("arial", Font.BOLD, 16));
            savePw.setFont(new Font("arial", Font.BOLD, 16));
            changeDtls.setVisible(false);
            email.setVisible(false);

        } else if (e.getSource() == changeDtls) {
            utitle.setVisible(true);
            utitle.setText("Name");
            utitle.setSize(200, 20);
            utitle.setLocation(90, 30);
            userId.setVisible(true);
            userId.setLocation(240, 30);
            userId.setText(c.name);
            ptitle.setVisible(true);
            ptitle.setText("Email");
            ptitle.setLocation(90, 70);
            mail.setVisible(true);
            mail.setLocation(240, 70);
            ptitle.setSize(200, 20);
            mail.setText(c.email);
            Exit.setLocation(250, 115);
            changeDtls.setVisible(false);
            idLabel.setVisible(false);
            marksLabel.setVisible(false);
            nameLabel.setVisible(false);
            changePw.setVisible(false);
            email.setVisible(false);
            savePw.setVisible(false);
            saveDtls.setVisible(true);
            savePw.setLocation(30, 115);
            Exit.setLocation(250, 115);
            Exit.setSize(200, 30);
            Exit.setFont(new Font("arial", Font.BOLD, 16));
            saveDtls.setFont(new Font("arial", Font.BOLD, 16));

        } else if (e.getSource() == savePw) {
            //setting new password
            String nP = userId.getText();
            String nCP = passw.getText();
            if (nP.equals(nCP)) {
                if (nP.equals("")) {
                    JOptionPane.showMessageDialog(container, "Please Enter Your new Password", "Blank", JOptionPane.WARNING_MESSAGE);
                } else {
                    String q = "UPDATE `onlineexam`.`Users` SET `password` = '" + nCP + "' WHERE (`id` = '" + c.id + "'); ";
                    try {
                        c.s.executeUpdate(q);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(container, "Please Check the entered Password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    c.connect();
                    JOptionPane.showMessageDialog(container, "Password Updated!\nPlease Login to Continue.", "Done", JOptionPane.INFORMATION_MESSAGE);
                    changeDtls.setVisible(false);
                    email.setVisible(false);
                    utitle.setVisible(true);
                    utitle.setText("User Id");
                    userId.setVisible(true);
                    ptitle.setVisible(true);
                    ptitle.setText("Password");
                    passw.setVisible(true);
                    sbtn.setVisible(true);
                    startExam.setVisible(false);
                    editProfile.setVisible(false);
                    logOut.setVisible(false);
                    userId.setText("");
                    passw.setText("");
                    savePw.setVisible(false);
                    Exit.setVisible(false);
                    utitle.setSize(100, 20);
                    utitle.setLocation(90, 30);
                    ptitle.setSize(100, 20);
                    ptitle.setLocation(90, 70);

                }


            } else {
                JOptionPane.showMessageDialog(container, "Confirm Password Does not Match", "Not Matched", JOptionPane.WARNING_MESSAGE);
            }

        } else if (e.getSource() == saveDtls) {
            String nN = userId.getText();
            String nE = mail.getText();
            if (nN.equals(c.name) && nE.equals(c.email)) {
                JOptionPane.showMessageDialog(container, "Nothing to Update", "No Edit", JOptionPane.WARNING_MESSAGE);
            } else {
                if (nN.equals("") || (nE.equals(""))) {
                    JOptionPane.showMessageDialog(container, "Blank not Supported", "Blank", JOptionPane.WARNING_MESSAGE);
                } else {
                    String q = "UPDATE `onlineexam`.`Users` SET `name` = '" + nN + "', `email`='" + nE + "' WHERE (`id` = '" + c.id + "'); ";
                    try {
                        c.s.executeUpdate(q);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(container, "Please Check the entered Password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    c.connect();
                    JOptionPane.showMessageDialog(container, "Details Updated!\nPlease Login to Continue.", "Done", JOptionPane.INFORMATION_MESSAGE);


                    utitle.setVisible(true);
                    utitle.setText("User Id");
                    userId.setVisible(true);
                    ptitle.setVisible(true);
                    ptitle.setText("Password");
                    passw.setVisible(true);
                    sbtn.setVisible(true);
                    startExam.setVisible(false);
                    editProfile.setVisible(false);
                    logOut.setVisible(false);
                    userId.setText("");
                    passw.setText("");
                    savePw.setVisible(false);
                    saveDtls.setVisible(false);
                    changeDtls.setVisible(false);
                    mail.setVisible(false);
                    Exit.setVisible(false);
                    utitle.setSize(100, 20);
                    utitle.setLocation(90, 30);
                    ptitle.setSize(100, 20);
                    ptitle.setLocation(90, 70);
                    userId.setLocation(190, 30);
                    passw.setLocation(190, 70);
                }
            }
        }


        //category selection
        if (e.getSource() == cat1) {
            currentQuestionIndex = 0;
            setQuestion(currentQuestionIndex);
        } else if (cat2 == e.getSource()) {
            currentQuestionIndex = 3;
            setQuestion(currentQuestionIndex);
        } else if (cat3 == e.getSource()) {
            currentQuestionIndex = 6;
            setQuestion(currentQuestionIndex);
        } else if (cat4 == e.getSource()) {
            currentQuestionIndex = 9;
            setQuestion(currentQuestionIndex);
        }


        //getting question on next
        if (e.getSource() == next) {
            skip.setEnabled(true);
            //Getting answers
            if (btngrp.getSelection() != null) {

                //getting user answer in a array
                userAns[currentQuestionIndex] = (btngrp.getSelection().getActionCommand());
            } else {
                userAns[currentQuestionIndex] = "n";
            }

            if (currentQuestionIndex < q.qN.size() - 1) {
                currentQuestionIndex = currentQuestionIndex + 1;
                setQuestion(currentQuestionIndex);
            }
        }

        if (e.getSource() == previous) {
            skip.setEnabled(true);
            if (currentQuestionIndex >= 1) {
                currentQuestionIndex = currentQuestionIndex - 1;
                setQuestion(currentQuestionIndex);
            }

        }
        if (e.getSource() == submit) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }

            if (btngrp.getSelection() != null) {
                userAns[currentQuestionIndex] = (btngrp.getSelection().getActionCommand());
            } else {
                userAns[currentQuestionIndex] = "n";
            }

            //correct ans and marks
            for (int i = 0; i < q.qN.size(); i++) {
                if (q.ans.get(i).equals(userAns[i])) {
                    marks++;
                }
            }
            question.setVisible(true);
            question.setText("You Got " + marks + " out of " + q.qN.size());
            question.setSize(800, 300);
            question.setLocation(600, 200);
            question.setFont(new Font("arial", Font.BOLD, 40));
            question.setForeground(Color.BLUE);
            container.add(question);
            marked.setVisible(false);
            questionScrollPane.setVisible(false);
            opt.setVisible(false);
            opotionpnl.setVisible(false);
            submit.setVisible(false);
            skip.setVisible(false);
            panel.setVisible(false);
            panel2.setVisible(false);
            previous.setVisible(false);
            categories.setVisible(false);
            submit.setVisible(false);
            next.setVisible(false);
            timerLabel.setVisible(false);
            reviewLabel.setVisible(false);

            String q = "UPDATE `onlineexam`.`Users` SET `score` = '" + marks + "' WHERE (`id` = '" + c.id + "');";
            try {
                c.s.executeUpdate(q);
            } catch (SQLException ea) {
                throw new RuntimeException(ea);
            }
        }

        //saving the answer
        if (userAns[currentQuestionIndex] != null) {
            switch (userAns[currentQuestionIndex]) {
                case "a" -> option1.setSelected(true);
                case "b" -> option2.setSelected(true);
                case "c" -> option3.setSelected(true);
                case "d" -> option4.setSelected(true);
            }
        }

        if (e.getSource() == skip) {
            skip.setEnabled(false);
            int f = 0;
            review.add(f, String.valueOf(currentQuestionIndex + 1));
            marked.setVisible(true);
            f++;
        }
        for (int a = 0; a < review.size(); a++) {

            if (currentQuestionIndex == Integer.parseInt(review.get(a)) - 1) {
                skip.setEnabled(false);
            }
        }
        StringBuilder lT = new StringBuilder();
        for (String line : review) {
            lT.append(line).append("<br>");
        }
        marked.setText("<html>" + review.toString() + "</html>");

        //keeping the previous button on 1st question
        if (currentQuestionIndex == 0) {
            previous.setEnabled(false);
        } else {
            previous.setEnabled(true);
        }
    }


    // Timer logic
    private void startTimer() {
//        int examDurationInMinutes = 1; // Change this to the desired duration
        int examDurationInSeconds =10 ;
        timer = new Timer();


        timer.scheduleAtFixedRate(new TimerTask() {
            int remainingTime = examDurationInSeconds;

            @Override
            public void run() {
                if (remainingTime > 0) {
                    int minutes = remainingTime / 60;
                    int seconds = remainingTime % 60;
                    timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
                    remainingTime--;
                    if (remainingTime < 20) {
                        timerLabel.setForeground(Color.red);
                    }
                } else {

                    timer.cancel();
                    timerLabel.setText("Time's up! Exam ended.");
                    timerLabel.setLocation(670, 200);
                    timerLabel.setSize(400, 20);
                    getContentPane().setBackground(Color.getHSBColor(30, 74.2f, 100));
                    questionScrollPane.setBackground(Color.getHSBColor(30, 74.2f, 100));

                    //Auto Save at time's up

                    if (btngrp.getSelection() != null) {
                        userAns[currentQuestionIndex] = (btngrp.getSelection().getActionCommand());
                    } else {
                        userAns[currentQuestionIndex] = "n";
                    }

                    for (int i = 0; i < q.qN.size(); i++) {
                        if (q.ans.get(i).equals(userAns[i])) {
                            marks++;
                        }
                    }
                    question.setText("You Got " + marks + " out of " + q.qN.size());
                    question.setSize(800, 300);
                    question.setLocation(600, 200);
                    question.setFont(new Font("arial", Font.BOLD, 40));
                    question.setForeground(Color.WHITE);
                    container.add(question);
                    questionScrollPane.setVisible(false);
                    opt.setVisible(false);
                    opotionpnl.setVisible(false);
                    submit.setVisible(false);
                    skip.setVisible(false);
                    panel.setVisible(false);
                    panel2.setVisible(false);
                    previous.setVisible(false);
                    categories.setVisible(false);
                    submit.setVisible(false);
                    next.setVisible(false);
                    marked.setVisible(false);
                    reviewLabel.setVisible(false);
                    String q = "UPDATE `onlineexam`.`Users` SET `score` = '" + marks + "' WHERE (`id` = '" + c.id + "');";
                    try {
                        c.s.executeUpdate(q);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, 0, 1000); // The second parameter (0) is the delay before the timer starts, and the third parameter (1000) is the interval between timer ticks in milliseconds.


    }

    void setQuestion(int nextq) {
        //setting the scroll pane when needed
        length = q.Q.get(currentQuestionIndex).length();
        if (length > 70) {
            questionScrollPane.setVisible(true);
            questionScrollPane.getViewport().add(question);
            questionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            questionScrollPane.setSize(800, 100);
            questionScrollPane.setLocation(530, 90);
            questionScrollPane.getViewport().setBackground(Color.white);
            questionScrollPane.setBorder(null);
            container.add(questionScrollPane);
        } else {
            question.setSize(800, 300);
            question.setLocation(530, 10);
            question.setFont(new Font("arial", Font.BOLD, 20));
            container.add(question);
            questionScrollPane.setVisible(false);
        }

        //selected category
        switch (q.qT.get(currentQuestionIndex)) {
            case "T" -> {
                cat1.setBackground(new Color(0, 250, 107));
                cat1.setForeground(Color.BLACK);
                cat1.setBorderPainted(true);
            }
            case "A" -> {
                cat2.setBackground(new Color(0, 250, 107));
                cat2.setForeground(Color.BLACK);
            }
            case "V" -> {
                cat3.setBackground(new Color(0, 250, 107));
                cat3.setForeground(Color.BLACK);
            }
            case "R" -> {
                cat4.setBackground(new Color(0, 250, 107));
                cat4.setForeground(Color.BLACK);
            }
        }

        // Display Question and Options

        if (nextq >= 0 && nextq < q.qN.size()) {

            question.setText(q.qN.get(nextq) + ". " + q.Q.get(nextq));
            option1.setText("A. " + q.op1.get(nextq));
            option2.setText("B. " + q.op2.get(nextq));
            option3.setText("C. " + q.op3.get(nextq));
            option4.setText("D. " + q.op4.get(nextq));
            btngrp.clearSelection();
        }

        if (currentQuestionIndex == q.qN.size() - 1) {
            next.setVisible(false);
            submit.setVisible(true);
        } else {
            submit.setVisible(false);
            next.setVisible(true);
        }
    }
}

class Exam {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                OnlineExam a = new OnlineExam();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Server Error"
                        , "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
