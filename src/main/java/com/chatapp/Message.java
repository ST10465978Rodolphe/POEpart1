/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chatapp;

/**
 *
 * @author RC_Student_lab
 */

import javax.swing.JOptionPane;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;

public class Message {

    private static int messageCount = 0;
    private static List<Message> sentMessages = new ArrayList<>();

    private final String messageID;
    private final String recipient;
    private final String messageContent;
    private final String messageHash;
    private String status;

    public Message(String recipient, String content) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageContent = content;
        this.messageHash = createMessageHash();
        this.status = "Pending";
    }

    private String generateMessageID() {
        Random rand = new Random();
        return String.valueOf(1000000000L + (long)(rand.nextDouble() * 8999999999L));
    }

    public boolean checkMessageID() {
        return this.messageID.length() == 10;
    }

    public boolean checkRecipientCell() {
        return Pattern.matches("^\\+27\\d{9}$", this.recipient);
    }

    public String createMessageHash() {
        String[] words = this.messageContent.split("\\s+");
        String first = words.length > 0 ? words[0] : "";
        String last = words.length > 1 ? words[words.length - 1] : "";
        return (messageID.substring(0, 2) + ":" + messageCount + ":" + first + last).toUpperCase();
    }

    public String sendMessage() {
        String[] options = {"Send", "Disregard", "Store"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose an action for the message:",
                "Message Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        switch (choice) {
            case 0:
                this.status = "Sent";
                sentMessages.add(this);
                messageCount++;
                return "Message successfully sent.";
            case 1:
                this.status = "Discarded";
                return "Message discarded.";
            case 2:
                this.status = "Stored";
                storeMessage();
                return "Message successfully stored.";
            default:
                return "No action taken.";
        }
    }

    public void storeMessage() {
        JSONObject obj = new JSONObject();
        obj.put("MessageID", messageID);
        obj.put("Recipient", recipient);
        obj.put("Message", messageContent);
        obj.put("Hash", messageHash);
        obj.put("Status", status);

        try (FileWriter file = new FileWriter("stored_messages.json", true)) {
            file.write(obj.toJSONString() + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error storing message.");
        }
    }

    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message ID: ").append(m.messageID).append("\n")
              .append("Message Hash: ").append(m.messageHash).append("\n")
              .append("Recipient: ").append(m.recipient).append("\n")
              .append("Message: ").append(m.messageContent).append("\n\n");
        }
        return sb.length() > 0 ? sb.toString() : "No messages sent yet.";
    }

    public static int returnTotalMessages() {
        return messageCount;
    }
}
