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
import java.util.List;

public class ChatApp {

    public static void main(String[] args) {
        String firstName = JOptionPane.showInputDialog("Enter your first name:");
        String lastName = JOptionPane.showInputDialog("Enter your last name:");
        String username = JOptionPane.showInputDialog("Enter a username (max 5 characters and must include '_'):");
        String password = JOptionPane.showInputDialog("Enter a password (8+ chars, 1 cap, 1 num, 1 special):");
        String cellNumber = JOptionPane.showInputDialog("Enter your cell phone number (e.g., +27831234567):");

        Login user = new Login(username, password, cellNumber, firstName, lastName);
        JOptionPane.showMessageDialog(null, user.registerUser());

        String loginUser = JOptionPane.showInputDialog("Login\nEnter your username:");
        String loginPass = JOptionPane.showInputDialog("Enter your password:");
        boolean isLoggedIn = user.loginUser(loginUser, loginPass);

        JOptionPane.showMessageDialog(null, user.returnLoginStatus(isLoggedIn));

        if (isLoggedIn) {
            Message.loadStoredMessagesFromJson();
            JOptionPane.showMessageDialog(null, "Welcome to QuickChat!");

            int totalMessagesToSend = Integer.parseInt(JOptionPane.showInputDialog("How many messages do you want to send?"));
            int messagesEntered = 0;

            while (true) {
                String menu = """
                    Please select an option:
                    1 - Send Message
                    2 - Show sent messages report
                    3 - Show longest message
                    4 - Search by message ID
                    5 - Search by recipient
                    6 - Delete message by hash
                    7 - Quit""";

                String choice = JOptionPane.showInputDialog(menu);

                switch (choice) {
                    case "1" -> {
                        if (messagesEntered >= totalMessagesToSend) {
                            JOptionPane.showMessageDialog(null, "You have reached your message limit.");
                            continue;
                        }

                        String recipient = JOptionPane.showInputDialog("Enter recipient number (+27 format):");
                        String message = JOptionPane.showInputDialog("Enter your message (max 250 chars):");

                        if (message.length() > 250) {
                            JOptionPane.showMessageDialog(null, "Message exceeds 250 characters by " + (message.length() - 250));
                            continue;
                        }

                        Message m = new Message(recipient, message);

                        if (!m.checkRecipientCell()) {
                            JOptionPane.showMessageDialog(null, "Invalid cell number format.");
                            continue;
                        }

                        String result = m.sendMessage();
                        JOptionPane.showMessageDialog(null, result);

                        if (result.contains("sent")) {
                            JOptionPane.showMessageDialog(null, "Message ID: " + m.getMessageID()
                                    + "\nHash: " + m.getMessageHash()
                                    + "\nTo: " + m.getRecipient()
                                    + "\nMessage: " + m.getMessageContent());
                            messagesEntered++;
                        }
                    }

                    case "2" -> JOptionPane.showMessageDialog(null, Message.getReport());

                    case "3" -> JOptionPane.showMessageDialog(null, "Longest message:\n" + Message.getLongestSentMessage());

                    case "4" -> {
                        String id = JOptionPane.showInputDialog("Enter Message ID:");
                        Message found = Message.findById(id);
                        JOptionPane.showMessageDialog(null,
                                (found != null) ? "To: " + found.getRecipient() + "\n" + found.getMessageContent()
                                                : "Message not found.");
                    }

                    case "5" -> {
                        String rec = JOptionPane.showInputDialog("Enter recipient number:");
                        List<Message> messages = Message.findByRecipient(rec);
                        StringBuilder sb = new StringBuilder();
                        for (Message m : messages) {
                            sb.append(m.getMessageContent()).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, messages.isEmpty() ? "No messages found." : sb.toString());
                    }

                    case "6" -> {
                        String hash = JOptionPane.showInputDialog("Enter message hash to delete:");
                        boolean deleted = Message.deleteByHash(hash);
                        JOptionPane.showMessageDialog(null, deleted ? "Message successfully deleted." : "No message found with that hash.");
                    }

                    case "7" -> {
                        JOptionPane.showMessageDialog(null, "You sent " + Message.sentMessages.size() + " messages. Goodbye!");
                        System.exit(0);
                    }

                    default -> JOptionPane.showMessageDialog(null, "Invalid option.");
                }
            }
        }
    }
}
