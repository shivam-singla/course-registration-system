/**
 * 
 */
package com.flipkart.client;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.flipkart.bean.Student;
import com.flipkart.constant.GenderConstant;
import com.flipkart.constant.RoleConstant;
import com.flipkart.dao.StudentDaoInterface;
import com.flipkart.dao.StudentDaoOperation;
import com.flipkart.dao.UserDaoInterface;
import com.flipkart.dao.UserDaoOperation;
import com.flipkart.exception.PasswordMismatchException;
import com.flipkart.exception.StudentNotFoundException;
import com.flipkart.exception.UserNotFoundException;
import com.flipkart.service.StudentInterface;
import com.flipkart.service.StudentOperation;
import com.flipkart.service.UserOperation;
import java.time.*;
import java.util.Date;
/**
 * @author Group-B
 *
 */

@SuppressWarnings("unused")
public class CRSApplication {

	/**
	 * Main method
	 * @param args
	 */
	private static Scanner scanner = new Scanner(System.in);
	public static void main(String[] args) {
		showMainMenu();
	}

	/**
	 * Method to display main menu
	 */
	public static void showMainMenu() {
		while (true)
		{
			    System.out.println("+---------------------------------------------+");
		        System.out.println("|  Welcome to the Course Registration System  |");
		        System.out.println("+--------|------------------------------------+");
		        System.out.println("|   1    |  Login                             |");
		        System.out.println("|   2    |  Student Registration              |");
		        System.out.println("|   3    |  Update Password                   |");
		        System.out.println("|   4    |  Exit                              |");
		        System.out.println("+--------+------------------------------------+");
		        System.out.print("\nEnter Option: ");

				String optionChosen = scanner.nextLine();
				switch(optionChosen) {		
				case "1":
					login();
					break;

				case "2":
					signup();
					break;
				case "3":
					updatepassword();
					break;
				case "4":
					exit();
					return;
				default:
					System.err.println("Invalid Option.");
				}
		}
			
	}

	private static void updatepassword() {
		try {
			UserDaoInterface UserDaoInterface = UserDaoOperation.getInstance();

			System.out.println("Enter User ID : ");
			String uid = scanner.nextLine();
			System.out.println("Enter the Old Password : ");
			String old_pass = scanner.nextLine();
			System.out.println("Enter New Password : ");
			String pass = scanner.nextLine();
			
			UserDaoInterface.updateUserPassword(uid, old_pass, pass);
			System.out.println("Your Password has been Successfully Updated.");
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		
	}

	/**
	 * Method to exit application
	 */
	private static void exit() {
		System.out.println("Exit Application");
	}

	/**
	 * Method to login into application
	 */
	private static void login() {
		System.out.println("Enter user ID : ");
		String uName = scanner.nextLine();
		System.out.println("Enter password : ");
		String password = scanner.nextLine();
		loginMain(uName, password);
	}

	/**
	 * Method for student to signup
	 */
	private static void signup() {
		try {
			StudentDaoInterface studentDaoInterface = StudentDaoOperation.getInstance();

			System.out.println("Enter Student ID : ");
			String sid = scanner.nextLine();
			System.out.println("Enter Student name : ");
			String name = scanner.nextLine();
			RoleConstant role = RoleConstant.Student;
			System.out.println("Enter Student Password : ");
			String pass = scanner.nextLine();
			System.out.println("Enter Student Gender, 1=male, 2=female, 3=other : ");
			int gende = scanner.nextInt();scanner.nextLine();
			GenderConstant gender = GenderConstant.OTHER;
			switch (gende){
				case 1:
					gender = GenderConstant.MALE;break;
				case 2:
					gender = GenderConstant.FEMALE;break;
				case 3:
					gender = GenderConstant.OTHER;break;
			}
			System.out.println("Enter Address of student : ");
			String address = scanner.nextLine();
			System.out.println("Enter Username of student : ");
			String username = scanner.nextLine();
			System.out.println("Enter Student DOB(Date of Birth) in dd/mm/yyyy format : ");
			String dob = scanner.nextLine();
			Date doB = new SimpleDateFormat("dd/MM/yyyy").parse(dob);
			System.out.println("Enter Branch of the student : ");
			String branch = scanner.nextLine();
			System.out.println("Enter Student's Batch year : ");
			int batchYear = scanner.nextInt();scanner.nextLine();

			Student student = new Student(sid,name,role,pass,gender,address,username,doB,branch,batchYear,false, false);

			studentDaoInterface.signUp(student);

			System.out.println("Sign up complete.");
			System.out.println("Waiting for approval.");

		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Method "login" will call this method for logging in
	 * @param userId -> user ID of user logging in
	 * @param password -> password of user
	 */
	private static void loginMain(String userId, String password) {
		RoleConstant role = null;
		try {
			role = UserOperation.getInstance().login(userId, password);
		} catch (Exception e){
			System.err.println(e.getMessage());
		}
		finally
		{
			if (role == RoleConstant.Student)
			{
				StudentInterface studentInterface = StudentOperation.getInstance();
				try {
					if (studentInterface.isApproved(userId)) {
						StudentCRSMenu sm = new StudentCRSMenu(userId);
						System.out.println("Student Logged in successfully with user ID: " + userId);
						Date currentDate = new Date();
						System.out.println("Last Login: "+currentDate);
						sm.displayMenu();
						
						
					} else {
						System.err.println("Login Failed.");
						System.err.println("Waiting for approval.");
					}
				} catch (StudentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			else if (role == RoleConstant.Professor)
			{
				ProfessorCRSMenu pm = new ProfessorCRSMenu(userId);
				System.out.println("Professor Logged in successfully with user ID: " + userId);
				Date currentDate = new Date();
				System.out.println("Last Login: "+currentDate);
				pm.displayMenu();
				
			}
			else if (role == RoleConstant.Admin)
			{
				AdminCRSMenu am = new AdminCRSMenu();
				System.out.println("Admin Logged in successfully with user ID: " + userId);
				Date currentDate = new Date();
				System.out.println("Last Login: "+currentDate);
				am.printAdminMenu();
	
			}
		}
	}
}
