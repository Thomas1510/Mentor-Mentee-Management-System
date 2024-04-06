import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MentorMenteeSystem extends JFrame {

  private JTextField nameField, numberField, cgpaField, regNumberField;

  private static String message;

  public MentorMenteeSystem() {

    // Create UI components
    JLabel nameLabel = new JLabel("Mentee Name:");
    JLabel numberLabel = new JLabel("Number:");
    JLabel cgpaLabel = new JLabel("CGPA:");
    JLabel regNumberLabel = new JLabel("Register Number:");

    nameField = new JTextField(20);
    numberField = new JTextField(20);
    cgpaField = new JTextField(20);
    regNumberField = new JTextField(20);

    JButton submitButton = new JButton("Submit");
    JButton logoutButton = new JButton("Logout");
    JButton qaButton = new JButton("Message");
    JButton scheduleButton = new JButton("Schedule Meeting");

    JPanel panel = new JPanel(new GridLayout(8, 2));
    panel.add(nameLabel);
    panel.add(nameField);
    panel.add(numberLabel);
    panel.add(numberField);
    panel.add(cgpaLabel);
    panel.add(cgpaField);
    panel.add(regNumberLabel);
    panel.add(regNumberField);
    panel.add(submitButton);
    panel.add(logoutButton);
    panel.add(qaButton);
    panel.add(scheduleButton);

    submitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        submitDetails();
      }
    });

    logoutButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        logout();
      }
    });

    qaButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        message = JOptionPane.showInputDialog(MentorMenteeSystem.this, "Enter your Message:");
        if (message != null && !message.isEmpty()) {
          saveMessage(message);
        }
      }
    });

    scheduleButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        scheduleMeeting();
      }
    });

    // Set frame properties
    setTitle("Mentor Page");
    setSize(400, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    add(panel, BorderLayout.CENTER);

  }

  private void submitDetails() {
    String name = nameField.getText();
    String number = numberField.getText();
    String cgpa = cgpaField.getText();
    String regNumber = regNumberField.getText();

    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter("mentee_details.txt", true));
      writer.write("Mentee Details:\n");
      writer.write("Name: " + name + "\n");
      writer.write("Number: " + number + "\n");
      writer.write("CGPA: " + cgpa + "\n");
      writer.write("Register Number: " + regNumber + "\n");
      writer.write("\n");
      writer.close();

      JOptionPane.showMessageDialog(this, "Mentee details submitted successfully!");

    } catch (IOException ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error occurred while submitting mentee details.");
    }
  }

  
    

  private void logout() {
    int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
    if (choice == JOptionPane.YES_OPTION) {
      dispose();
      showLoginPage();
    }
  }

  private void scheduleMeeting() {
    String meeting = JOptionPane.showInputDialog(this, "Enter meeting details:");
    if (meeting != null && !meeting.isEmpty()) {
      try {
        BufferedWriter writer = new BufferedWriter(new FileWriter("meetings.txt", true));
        writer.write("Meeting: " + meeting + "\n");
        writer.close();

        JOptionPane.showMessageDialog(this, "Meeting scheduled successfully!");
      } catch (IOException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error occurred while scheduling the meeting.");
      }
    }
  }

  private void saveMessage(String message) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter("messages.txt", true));
      writer.write("Message: " + message + "\n");
      writer.close();

      JOptionPane.showMessageDialog(this, "Message sent successfully!");
    } catch (IOException ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error occurred while sending the message.");
    }
  }

  private static void showLoginPage() {
    JFrame loginFrame = new JFrame("Login");
    JPanel panel = new JPanel();
    JLabel label = new JLabel("Select User Type:");
    JButton mentorButton = new JButton("Mentor");
    JButton menteeButton = new JButton("Mentee");

    mentorButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        loginFrame.dispose();
        new MentorMenteeSystem().setVisible(true);
      }
    });

    menteeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        loginFrame.dispose();
        showMenteePage();
      }
    });

    panel.add(label);
    panel.add(mentorButton);
    panel.add(menteeButton);
    loginFrame.add(panel);
    loginFrame.setSize(300, 100);
    loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    loginFrame.setLocationRelativeTo(null);
    loginFrame.setVisible(true);
  }

  private static void showMenteePage() {
    JFrame menteeFrame = new JFrame("Mentee");
    JTextArea menteeArea = new JTextArea(10, 30);
    JButton logoutButton = new JButton("Logout");
    JButton viewMeetingsButton = new JButton("View Meetings");

    JScrollPane scrollPane = new JScrollPane(menteeArea);
    JPanel panel = new JPanel();
    panel.add(scrollPane);
    panel.add(logoutButton);
    panel.add(viewMeetingsButton);

    logoutButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        menteeFrame.dispose();
        showLoginPage();
      }
    });

    viewMeetingsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        showMeetings();
      }
    });

    try {
      String messages = new String(Files.readAllBytes(Paths.get("messages.txt")));
      menteeArea.setText(messages);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    menteeFrame.add(panel);
    menteeFrame.setSize(400, 400);
    menteeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    menteeFrame.setLocationRelativeTo(null);
    menteeFrame.setVisible(true);
  }

  private static void showMeetings() {
    JFrame meetingsFrame = new JFrame("Meetings");
    JTextArea meetingsArea = new JTextArea(10, 30);
    JButton closeButton = new JButton("Close");

    meetingsArea.setEditable(false);

    JScrollPane scrollPane = new JScrollPane(meetingsArea);
    JPanel panel = new JPanel();
    panel.add(scrollPane);
    panel.add(closeButton);

    closeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        meetingsFrame.dispose();
      }
    });

    try {
      String meetings = new String(Files.readAllBytes(Paths.get("meetings.txt")));
      meetingsArea.setText(meetings);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    meetingsFrame.add(panel);
    meetingsFrame.setSize(400, 400);
    meetingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    meetingsFrame.setLocationRelativeTo(null);
    meetingsFrame.setVisible(true);
  }

  public static void main(String[] args) {
    showLoginPage();
  }

}
