import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

class ATM {
    static int Id = 0;
    static String Bal = "";
    static int newbal = 0;
    static String mqueryu = "";
    static String mqueryt = "";
    static String mquery = "";
    static int response;
    static String uname = "";
    static String AN = "";
    static int PIN=0;
    static ResultSet resultSet1;

    //Connection with mysql
    static int Operations() {
        String[] options2 = new String[]{"Transaction History", "Withdraw", "Deposit", "Transfer", "Quit"};
        response = JOptionPane.showOptionDialog(null,
                "Name: " + uname + "                                      Account No: " + AN + "\nAvailable Balance: " + Bal + "\n \n Select Any Option to Proceed: " +
                        "\n", "Welcome " + uname + " 😊😊",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options2, options2[0]);
        return response;
    }

    public static String[] details(int ab, String query) {
        String[] details = new String[8];

        try {
            String url = "jdbc:mysql://localhost:3306/"; // table details
            String username = "root"; // MySQL credentials
            String password = "";
            Connection con = DriverManager.getConnection(url, username, password);
//            System.out.println("Connection Established successfully");
            Statement statement = con.createStatement();
            String MainQ = "USE atm";
            statement.executeUpdate(MainQ);

//getting the details using the ID


            String bal = "", APIN = "", ID = "", uStsS = "", Trans = "", Date = "";

            int updateSts = 0;
            int i = 0;

            if (query.substring(0, 1).equals("U") || query.substring(0, 1).equals("I")) {
                updateSts = statement.executeUpdate(query);
                uStsS = String.valueOf(updateSts);
            } else if (query.lastIndexOf("Thist") > 0) {
                int t = 0;
                ResultSet resultSet1 = statement.executeQuery(query);
                while (resultSet1.next()) {
                    t = t + 1;
                }

                String[] hists = new String[t];
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String tran = resultSet.getString("transaction");
                    String date = resultSet.getString("time");
                    hists[i] = "" + (i + 1) + ". " + tran + " on " + date + "\n";
                    i++;
                }

//         display message dialog
                JOptionPane.showMessageDialog(null,
                        new JList(hists),
                        "Last Transactions",
                        JOptionPane.INFORMATION_MESSAGE);

            } else {
                ResultSet result = statement.executeQuery(query);
                if (result.next()) {
                    ResultSet resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        uname = resultSet.getString("uname");
                        bal = resultSet.getString("bal");
                        APIN = resultSet.getString("pin");
                        AN = resultSet.getString("acc");
                        ID = resultSet.getString("id");
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Please Enter Valid Id", "Wrong Information", JOptionPane.ERROR_MESSAGE);
                    input();
                }
            }
            details = new String[]{APIN, AN, uname, bal, ID, uStsS, Trans, Date};
            statement.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Server Error", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return details;
    }

    //Input method
    public static void input() {

        Object[] options = {"Ok", "Cancel"};
        JTextField xField = new JTextField(8);
        JPasswordField yField = new JPasswordField(4);
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Enter Your Id: "));
        myPanel.add(xField);
        myPanel.add(Box.createHorizontalStrut(15));
        myPanel.add(new JLabel("Enter Your PIN: "));
        myPanel.add(yField);
        xField.requestFocusInWindow();
        int result = JOptionPane.showOptionDialog(null, myPanel,
                "Please Enter Your Id and Password", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, null);


        if (result == 1 || result == -1) {
            System.exit(0);
        }

        //Input check

        if (Objects.equals(xField.getText(), "") || Objects.equals(yField.getText(), "")) {
            Object[] options1 = {"Retry", "Exit"};


            int n = JOptionPane.showOptionDialog(null,
                    "Please Enter your Id & PIN to Continue",
                    "Blank Information",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options1, options1[0]);

            if (n == 0) {
                input();
            } else {
                System.exit(0);
            }

        } else {
            try{
            PIN = Integer.parseInt(yField.getText());
            Id = Integer.parseInt(xField.getText());}
            catch (Exception e){
                JOptionPane.showMessageDialog(myPanel,"Please Enter Numbers Only","Error",JOptionPane.WARNING_MESSAGE);
            }
            mquery = "select * from users where id=" + Id + ";";

            String[] rDetails = details(Id, mquery);

            int rPIN = Integer.parseInt(rDetails[0]);
            int rID = Integer.parseInt(rDetails[4]);
            String name = rDetails[2];
            String AN = rDetails[1];
            Bal = rDetails[3];

            //pin and Id validation

            if (rPIN == PIN && Id == rID) {
                System.out.println("Logged in Successfully");

                response = Operations();

            } else {
                Object[] options3 = {"Retry", "Exit"};

                int n = JOptionPane.showOptionDialog(null,
                        "Wrong Id or PIN to Continue",
                        "Wrong Id or PIN",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null, options3, options3[0]);
                if (n == 0) {
                    input();
                } else {
                    System.exit(0);
                }
            }
        }
    }

    //History Method
    static void hist(String trnx) {
        Date dater = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(dater);

        mqueryt = "Insert into Thist(Id,Transaction,Time) values(" + Id + ",'" + trnx + "','" + date + "');";
        details(0, mqueryt);

    }

    //Transfer method
    static void Transfer() {

        Object[] options = {"Transfer", "Cancel"};

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter Receiver's Account Number: "));
        JTextField textField = new JTextField(10);
        panel.add(textField);
        panel.add(new JLabel("Enter Amount: "));
        JTextField textFielda = new JTextField(10);
        panel.add(textFielda);

        int resultT = JOptionPane.showOptionDialog(null, panel, "Transfer Money",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, null);

        String wd = textField.getText();
        String wd1 = textFielda.getText();
        if (resultT == 0) {
            if ((!wd.equals("")) && (!wd1.equals(""))) {
                int tAmount = Integer.parseInt(textFielda.getText());
                int tAcc = Integer.parseInt(textField.getText());
                int avlabl = Integer.parseInt(Bal);
                newbal = avlabl - tAmount;

                String[] a = {};

                mqueryu = "UPDATE users SET bal= " + newbal + " WHERE id=" + Id + ";";
                details(0, mqueryu);

                a = details(0, mquery);
                int c = Integer.parseInt(a[1]);
                String name = a[2];
                if (a[5].equals("0")) {
                    Object[] options1 = {"Retry", "Exit"};
                    int m = JOptionPane.showOptionDialog(null,
                            "Please Check the Account Number You Have Entered",
                            "Wrong Information",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null, options1, options1[0]);
                    Transfer();
                } else if (tAmount < 1) {

                    Object[] options1 = {"Retry", "Exit"};
                    int m = JOptionPane.showOptionDialog(null,
                            "Amount can't be less than Rs.1",
                            "Wrong Information",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null, options1, options1[0]);
                    Transfer();
                } else if (tAmount > avlabl) {
                    Object[] options1 = {"Retry", "Exit"};
                    int m = JOptionPane.showOptionDialog(null,
                            "Insufficient Balance",
                            "Wrong Information",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null, options1, options1[0]);
                    Transfer();
                } else if (tAcc == c) {
                    Object[] options1 = {"Retry", "Exit"};
                    int m = JOptionPane.showOptionDialog(null,
                            "Nice Try!! But Can't Send Money to Yourself",
                            "Wrong Information",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null, options1, options1[0]);
                    Transfer();
                } else {
                    String q = "select * from users where acc=" + tAcc + ";";
                    String[] b = details(0, q);
                    int rBal = Integer.parseInt(b[3]);
                    int rNewBal = rBal + tAmount;
                    String rAcUpdate = "UPDATE users SET bal= " + rNewBal + " WHERE acc=" + tAcc + ";";
                    details(0, rAcUpdate);
                    JOptionPane.showMessageDialog(null,
                            "Rs." + tAmount + " Send to " + b[2] + ", Account No.: " + tAcc + "\n \n Your Current Balance= " + newbal,
                            "Transaction Successful", JOptionPane.INFORMATION_MESSAGE);
                    int Idr = Integer.parseInt(b[4]);

                    Date dater = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = dateFormat.format(dater);
                    hist("Rs." + tAmount + " Dr. Transferred to A/C No: " + tAcc + ", " + b[2] + "");
                    response = -1;
                    mqueryt = "Insert into Thist(Id,Transaction,Time) values (" + Idr + ",'Rs. " + tAmount + " Cr. from A/C No: " + c + ", " + name + "','" + date + "');";
                    details(0, mqueryt);
                    details(0,"select * from Users where id="+Id);
                    Bal=String.valueOf(newbal);

                    response=Operations();
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Please Enter Some Values", "Blank Information", JOptionPane.WARNING_MESSAGE, null);
                Transfer();

            }

        } else if (resultT == 1) {
            response = Operations();
        } else {
            System.exit(0);
        }
    }

    //Withdraw
    public static void Withdraw() {
        Object[] options = {"Withdraw", "Cancel"};
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter Amount: "));
        JTextField textField = new JTextField(10);
        panel.add(textField);

        int resultw = JOptionPane.showOptionDialog(null, panel, "Withdraw Money",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, null);

        if (resultw == 0) {
            int totBal = Integer.parseInt(Bal);
            String wd = textField.getText();
            int wAmount = 0;

            if (!wd.equals("")) {
                wAmount = Integer.parseInt(textField.getText());
                newbal = totBal - wAmount;

                if (wAmount >= totBal) {
                    JOptionPane.showMessageDialog(null,
                            "Insufficient Balance", "Transaction Failed! ", JOptionPane.WARNING_MESSAGE, null);
                    Withdraw();
                } else if ((wAmount <= 0)) {
                    JOptionPane.showMessageDialog(null,
                            "Can't be less than 1", "Transaction Failed! ", JOptionPane.WARNING_MESSAGE, null);
                    Withdraw();
                } else {
                    mqueryu = "UPDATE users SET bal=" + newbal + " WHERE Id=" + Id + "; ";
                    details(0, mqueryu);

                    JOptionPane.showMessageDialog(null,
                            "Please Collect the Money", "Transaction Successful ", JOptionPane.INFORMATION_MESSAGE);
                    hist("Rs." + wAmount + " Dr. Withdrawn From ATM");
                    Bal=String.valueOf(totBal- wAmount);

                    response =  Operations();
                }

            } else {
                JOptionPane.showMessageDialog(null,
                        "Blank is not Allowed", "Transaction Failed! ", JOptionPane.WARNING_MESSAGE, null);
                Withdraw();
            }

        } else if (resultw == 1) {
            response = Operations();

        } else {
            System.out.println("Logout");
        }

    }

    //deposit method
    static void Deposit() {
        Object[] options = {"Deposit", "Cancel"};

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter Amount: "));
        JTextField textField = new JTextField(10);
        panel.add(textField);

        int resultD = JOptionPane.showOptionDialog(null, panel, "Deposit Money",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, null);

        String wd = textField.getText();


        if (resultD == 0) {
            if (!wd.equals("")) {
                int dAmount = Integer.parseInt(textField.getText());
                int totBal = Integer.parseInt(Bal);
                newbal = totBal + dAmount;
                if (dAmount > 0) {
                    mqueryu = "UPDATE users SET bal=" + newbal + " WHERE Id=" + Id + "; ";
                    details(0, mqueryu);
                    JOptionPane.showMessageDialog(null,
                            "Rs." + dAmount + " Added to Your Account.\nNew A/C Balance is: " + newbal,
                            "Transaction Complete ", JOptionPane.INFORMATION_MESSAGE, null);
                    hist("Rs." + dAmount + " Cr.");
                    response = -1;
                    Bal=String.valueOf(totBal + dAmount);
                    response =  Operations();
                } else {
                    JOptionPane.showMessageDialog(null, "Deposit Amount can't be less than zero.",
                            "Transaction Failed! ", JOptionPane.ERROR_MESSAGE, null);
                    Deposit();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Enter Some Amount Please.",
                        "Transaction Failed! ", JOptionPane.WARNING_MESSAGE, null);
                Deposit();
            }
        } else if (resultD == 1) {
            Operations();
        } else {
            System.exit(0);
        }

    }

    public static void main(String[] args) throws Exception {

        input();
        while (response > -1) {
            //withdraw
            if (response == 1) {
                Withdraw();
            }

            //transaction  History
            else if (response == 0) {
                details(0, "Select * from Thist where id=" + Id + " order by time desc;");
                response = -1;
                Operations();
            }

            //deposit
            else if (response == 2) {
                Deposit();
            }

            //Transfer
            else if (response == 3) {
                Transfer();
            }

            //quit
            else if (response == 4) {
                Id = 0;
                System.exit(0);
                System.out.println("Logged Out Successfully");
            }
        }
        System.out.println("Logged Out Successfully");
    }
}
