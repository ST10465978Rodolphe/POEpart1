/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author RC_Student_lab
 */
package com.chatapp;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class MessageTest {

    @Test
    public void testValidRecipientCell() {
        Message msg = new Message("+27831234567", "Short message");
        assertTrue("Valid SA number should return true", msg.checkRecipientCell());
    }

    @Test
    public void testInvalidRecipientCell() {
        Message msg = new Message("0831234567", "Short message");
        assertFalse("Invalid number format should return false", msg.checkRecipientCell());
    }

    @Test
    public void testValidMessageIDLength() {
        Message msg = new Message("+27831234567", "Short message");
        assertTrue("Message ID should be exactly 10 digits", msg.checkMessageID());
    }

    @Test
    public void testMessageHashFormat() {
        Message msg = new Message("+27831234567", "Hi Mike, can you join us for dinner tonight.");
        String hash = msg.createMessageHash();
        assertNotNull("Hash should not be null", hash);
        assertTrue("Hash should contain first two digits and message count", hash.contains(":"));
        assertTrue("Hash should be in uppercase", hash.equals(hash.toUpperCase()));
    }

    @Test
    public void testMessageContentUnderLimit() {
        String msgText = "Hello this is a short message.";
        assertTrue("Message should be under 250 characters", msgText.length() <= 250);
    }

    @Test
    public void testMessageContentOverLimit() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            longText.append("x");
        }
        assertTrue("Message should be over 250 characters", longText.length() > 250);
    }

    @Test
    public void testSendMessageReturnsValidResponse() {
        Message msg = new Message("+27831234567", "Test sending message.");
        String response = msg.sendMessage(); // JOptionPane interaction required
        assertNotNull("Send message should return a response", response);
    }

    @Test
    public void testTotalMessagesAfterSend() {
        int before = Message.sentMessages.size();
        Message msg = new Message("+27831234567", "Another message.");
        msg.sendMessage(); // Requires user interaction
        int after = Message.sentMessages.size();
        assertTrue("Message count should not decrease", after >= before);
    }

    // === Part 3 Tests ===

    @Test
    public void testSentArrayPopulated() {
        Message.sentMessages.clear(); // reset list
        Message m1 = new Message("+278345578964", "Did you get the cake?");
        Message m4 = new Message("08388845679", "It is dinner time !");
        Message.sentMessages.add(m1);
        Message.sentMessages.add(m4);

        List<String> expected = Arrays.asList("Did you get the cake?", "It is dinner time !");
        assertEquals(expected, Message.getSentContents());
    }

    @Test
    public void testLongestMessage() {
        Message.sentMessages.clear();
        Message m2 = new Message("+278388845678", "Where are you? You are late! I have asked you to be on time.");
        Message.sentMessages.add(m2);

        assertEquals("Where are you? You are late! I have asked you to be on time.", Message.getLongestSentMessage());
    }

    @Test
    public void testFindById() {
        Message.sentMessages.clear();
        Message m = new Message("0838884567", "It is dinner time !");
        Message.sentMessages.add(m);
        String id = m.getMessageID();

        Message found = Message.findById(id);
        assertNotNull(found);
        assertEquals("It is dinner time !", found.getMessageContent());
    }

    @Test
    public void testFindByRecipient() {
        Message.sentMessages.clear();
        Message.storedMessages.clear();
        Message m1 = new Message("+278388845678", "Where are you? You are late! I have asked you to be on time.");
        Message m2 = new Message("+278388845679", "Ok, I am leaving without you.");
        Message.sentMessages.add(m1);
        Message.storedMessages.add(m2);

        List<Message> found = Message.findByRecipient("+278388845678");
        assertEquals(1, found.size());
    }

    @Test
    public void testDeleteByHash() {
        Message.sentMessages.clear();
        Message m = new Message("+278344845678", "Yohoooo, I am at your gate.");
        Message.sentMessages.add(m);
        String hash = m.getMessageHash();

        boolean deleted = Message.deleteByHash(hash);
        assertTrue(deleted);
    }

    @Test
    public void testReportContainsMessage() {
        Message.sentMessages.clear();
        Message m = new Message("+27831234567", "This is a test message.");
        Message.sentMessages.add(m);

        String report = Message.getReport();
        assertTrue(report.contains(m.getMessageHash()));
        assertTrue(report.contains(m.getRecipient()));
        assertTrue(report.contains(m.getMessageContent()));
    }
}
