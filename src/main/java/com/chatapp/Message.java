/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chatapp;

import javax.swing.JOptionPane;
import java.util.*;
import java.util.regex.Pattern;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Message {

    private static int messageCount = 0;
    public static List<Message> sentMessages = new ArrayList<>();
    public static List<Message> disregardedMessages = new ArrayList<>();
    public static List<Message> storedMessages = new ArrayList<>();

    public static List<String> messageHashes = new ArrayList<>();
    public static List<String> messageIDs = new ArrayList<>();

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

        messageHashes.add(this.messageHash);
        messageIDs.add(this.messageID);
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
                disregardedMessages.add(this);
                return "Message discarded.";
            case 2:
                this.status = "Stored";
                storedMessages.add(this);
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

    public static void loadStoredMessagesFromJson() {
        try (Scanner scanner = new Scanner(new FileReader("stored_messages.json"))) {
            JSONParser parser = new JSONParser();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                JSONObject json = (JSONObject) parser.parse(line);
                String recipient = (String) json.get("Recipient");
                String content = (String) json.get("Message");

                Message m = new Message(recipient, content);
                m.status = "Stored";
                storedMessages.add(m);
            }
        } catch (IOException | ParseException e) {
            JOptionPane.showMessageDialog(null, "Failed to load stored messages.");
        }
    }
// This JSON loading logic was developed with the assistance of ChatGPT, an AI model by OpenAI (2025).
// Reference: OpenAI. (2025). ChatGPT (May 2025 version) [Large language model]. https://chat.openai.com/
    
    public static List<String> getSentContents() {
        return sentMessages.stream()
                .map(Message::getMessageContent)
                .collect(Collectors.toList());
    }

    public static String getLongestSentMessage() {
        return sentMessages.stream()
                .max(Comparator.comparingInt(m -> m.getMessageContent().length()))
                .map(Message::getMessageContent)
                .orElse("No messages sent.");
    }

    public static Message findById(String id) {
        return sentMessages.stream()
                .filter(m -> m.getMessageID().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static List<Message> findByRecipient(String recipient) {
        List<Message> results = new ArrayList<>();
        for (Message m : sentMessages) {
            if (m.getRecipient().equals(recipient)) {
                results.add(m);
            }
        }
        for (Message m : storedMessages) {
            if (m.getRecipient().equals(recipient)) {
                results.add(m);
            }
        }
        return results;
    }

    public static boolean deleteByHash(String hash) {
        return sentMessages.removeIf(m -> m.getMessageHash().equals(hash));
    }

    public static String getReport() {
        StringBuilder sb = new StringBuilder("=== Sent Messages Report ===\n");
        for (Message m : sentMessages) {
            sb.append("Hash: ").append(m.getMessageHash()).append("\n")
              .append("Recipient: ").append(m.getRecipient()).append("\n")
              .append("Message: ").append(m.getMessageContent()).append("\n\n");
        }
        return sb.length() > 0 ? sb.toString() : "No messages to report.";
    }

    // Getters
    public String getMessageContent() {
        return messageContent;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }
}
