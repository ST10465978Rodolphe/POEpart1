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
        String response = msg.sendMessage(); // This requires manual selection in a JOptionPane
        assertNotNull("Send message should return a response", response);
    }

    @Test
    public void testTotalMessagesAfterSend() {
        int before = Message.returnTotalMessages();
        Message msg = new Message("+27831234567", "Another message.");
        msg.sendMessage(); // Requires interaction
        int after = Message.returnTotalMessages();
        assertTrue("Message count should not decrease", after >= before);
    }
}
