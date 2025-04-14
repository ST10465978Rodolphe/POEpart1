/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.chatapp;

/**
 *
 * @author RC_Student_lab
 */
import java.util.regex.*;

public class Login {

    String username;
    String password;
    String cellNumber;
    String firstName;
    String lastName;

    public Login(String username, String password, String cellNumber, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.cellNumber = cellNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }

    // This regular expression was created with assistance of ChatGPT, an AI language model by openAI (2025).
    // Reference: OpenAI. (2025). ChatGPT (April 2025 version) [Large language model]. https://chat.openai.com/
    public boolean checkPasswordComplexity() {
        return password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$");
    }

    // This regular expression was created with assistance of ChatGPT, an AI language model by openAI (2025).
    // Reference: OpenAI. (2025). ChatGPT (April 2025 version) [Large language model]. https://chat.openai.com/
    public boolean checkCellPhoneNumber() {
        return cellNumber.matches("^\\+27\\d{9}$");
    }

    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        return "User successfully registered!";
    }

    public boolean loginUser(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }

    public String returnLoginStatus(boolean loggedIn) {
        if (loggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
