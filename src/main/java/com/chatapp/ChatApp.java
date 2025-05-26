package com.chatapp;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author RC_Student_lab
 */
import javax.swing.JOptionPane;

public class ChatApp {

    public static void main(String[] args) {
        // Step 1: Registration
        String firstName = JOptionPane.showInputDialog("Enter your first name:");
        String lastName = JOptionPane.showInputDialog("Enter your last name:");
        String username = JOptionPane.showInputDialog("Enter a username (max 5 characters and must include '_'):");
        String password = JOptionPane.showInputDialog("Enter a password (8+ chars, 1 cap, 1 num, 1 special):");
        String cellNumber = JOptionPane.showInputDialog("Enter your cell phone number (e.g., +27831234567):");

        Login user = new Login(username, password, cellNumber, firstName, lastName);
        JOptionPane.showMessageDialog(null, user.registerUser());

        // Step 2: Login
        String loginUser = JOptionPane.showInputDialog("Login\nEnter your username:");
        String loginPass = JOptionPane.showInputDialog("Enter your password:");

        boolean isLoggedIn = user.loginUser(loginUser, loginPass);
        JOptionPane.showMessageDialog(null, user.returnLoginStatus(isLoggedIn));

        // Step 3: Menu if logged in
        if (isLoggedIn) {
            JOptionPane.showMessageDialog(null, "Welcome to QuickChat!");

            int totalMessagesToSend = Integer.parseInt(JOptionPane.showInputDialog("How many messages do you want to send?"));
            int messagesEntered = 0;

            while (true) {
                String menu = """
                        Please select an option:
                        1 - Send Message
                        2 - Show recently sent messages
                        3 - Quit""";
                String choice = JOptionPane.showInputDialog(menu);

                if (choice.equals("1")) {
                    if (messagesEntered >= totalMessagesToSend) {
                        JOptionPane.showMessageDialog(null, "You have reached the message limit.");
                        continue;
                    }

                    String recipient = JOptionPane.showInputDialog("Enter recipient's phone number (+27 format):");
                    String content = JOptionPane.showInputDialog("Enter your message (max 250 characters):");

                    if (content.length() > 250) {
                        JOptionPane.showMessageDialog(null, "Message exceeds 250 characters by " + (content.length() - 250) + ". Please reduce size.");
                        continue;
                    }

                    Message message = new Message(recipient, content);

                    if (!message.checkRecipientCell()) {
                        JOptionPane.showMessageDialog(null, "Invalid recipient phone number format.");
                        continue;
                    }

                    String result = message.sendMessage();
                    JOptionPane.showMessageDialog(null, result);

                    if (result.contains("sent")) {
                        JOptionPane.showMessageDialog(null, """
                                Message ID: %s
                                Hash: %s
                                To: %s
                                Message: %s
                                """.formatted(
                                message.checkMessageID() ? message.createMessageHash() : "Invalid ID",
                                message.createMessageHash(),
                                recipient, content));
                        messagesEntered++;
                    }

                } else if (choice.equals("2")) {
                    JOptionPane.showMessageDialog(null, Message.printMessages());
                } else if (choice.equals("3")) {
                    JOptionPane.showMessageDialog(null, "You sent " + Message.returnTotalMessages() + " messages. Goodbye!");
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid option.");
                }
            }
        }
    }
}