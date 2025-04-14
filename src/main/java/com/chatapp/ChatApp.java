package com.chatapp;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author RC_Student_lab
 */
import java.util.Scanner;

public class ChatApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Register new user:");
        System.out.print("Enter first name: ");
        String firstName = input.nextLine();
        System.out.print("Enter last name: ");
        String lastName = input.nextLine();
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter password: ");
        String password = input.nextLine();
        System.out.print("Enter cell number (e.g., +27838968976): ");
        String cell = input.nextLine();

        Login user = new Login(username, password, cell, firstName, lastName);
        System.out.println(user.registerUser());

        System.out.println("\nLogin:");
        System.out.print("Enter username: ");
        String loginUsername = input.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = input.nextLine();

        boolean success = user.loginUser(loginUsername, loginPassword);
        System.out.println(user.returnLoginStatus(success));
    }
}
