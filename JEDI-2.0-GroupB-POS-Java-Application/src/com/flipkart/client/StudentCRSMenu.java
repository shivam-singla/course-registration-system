/**
 * 
 */
package com.flipkart.client;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.flipkart.bean.Course;
import com.flipkart.bean.Notification;
import com.flipkart.bean.RegisteredCourse;
import com.flipkart.constant.ModeOfPaymentConstant;
import com.flipkart.exception.StudentNotFoundException;
import com.flipkart.service.NotificationOperation;
import com.flipkart.service.RegistrationInterface;
import com.flipkart.service.RegistrationOperation;
import com.flipkart.service.StudentInterface;
import com.flipkart.service.StudentOperation;

/**
 * @author Group-B
 *
 */
public class StudentCRSMenu {

	String studentID;
	StudentInterface studentInterface = StudentOperation.getInstance();
	RegistrationInterface registrationInterface = RegistrationOperation.getInstance();

	/**
	 * Constructor
	 * @param studentID-> ID of student whose menu is being displayed
	 */
	public StudentCRSMenu(String studentID) {
		this.studentID = studentID;
	}

	/**
	 * Display menu for student
	 */
	public void displayMenu() {
		// Display the options available for the Student
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		while (true) {
		
			
			System.out.println("\n+------------------------------------------------------------------------+");
			System.out.println("|                             Student Menu                               |");
			System.out.println("+------------------------------------------------------------------------+");
			System.out.println("|   1    | View Course Catalogue                                         |");
			System.out.println("|   2    | View Grades                                                   |");
			System.out.println("|   3    | Semester Registration                                         |");
			System.out.println("|   4    | Add Course                                                    |");
			System.out.println("|   5    | Drop Course                                                   |");
			System.out.println("|   6    | View registered courses                                       |");
			System.out.println("|   7    | Make Payment                                                  |");
			System.out.println("|   8    | Show Notifications                                            |");
			System.out.println("|   9    | Logout                                                        |");
			System.out.println("+--------+---------------------------------------------------------------+");
			System.out.println("\nEnter Option: ");

			
			int input = sc.nextInt();
			sc.nextLine();

			switch (input) {
			case 1:
				viewCourseCatalogue();
				break;

			case 2:
				viewGrades();
				break;

			case 3:
				registerCourses();
				break;

			case 4:
				addCourse();
				break;

			case 5:
				dropCourse();
				break;

			case 6:
				viewRegisteredCourses();
				break;

			case 7:
				payFee();
				break;
			case 8:
				showNotifications();
			    break;
			case 9:
				System.out.println("==================== Logging Out ====================");
				return;

			default:
				System.err.println("No such operation exists, valid choices 1, 2, 3, 4, 5 ,6 ,7 ,8");
			}
		}
	}
	

	/**
	 * View all available courses
	 */
	private void viewCourseCatalogue() {
		try {
			List<Course> courses = studentInterface.viewCourseCatalogue();
			System.out.printf("%10s%20s%20s%20s%20s\n","Course ID","Course Name","Instructor ID","Instructor Name","Filled Seats");
			for (Course course : courses) {
				System.out.format("%10d%20s%20s%20s%20d\n", course.getCourseId(), course.getCourseName(), course.getInstructorId(),course.getInstructorName(),course.getFilledSeats());
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());		
		}
	}

	
	/**
	 * Show notifications
	 */
	private void showNotifications ()
	{
		System.out.println("Notifications for student with id: " + studentID);
		List<Notification> notifications = NotificationOperation.getInstance().getNotifications(studentID);
		notifications.forEach(notification -> {
			System.out.println("\nNotification id: " + notification.getNotificationId());
			System.out.println("Student id: " + notification.getStudentId());
			System.out.println("Notification type: " + notification.getType());
			System.out.println("Notification message: " + notification.getMessage()+"\n");
		});
	}
	
	/**
	 * View grades for the semester
	 */
	private void viewGrades() {
		try {
			List<RegisteredCourse> courses = studentInterface.viewGrades(studentID);
			System.out.printf("%10s%20s%20s\n","Course ID","Course Name","Grade");
			for(RegisteredCourse course:courses) {
				if(course.getGrade()!=null) {
					System.out.format("%10d%20s%20s\n", course.getCourseId(), course.getCourseName(), course.getGrade().toString());
				}
				else {
					System.out.format("%10d%20s%20s\n", course.getCourseId(), course.getCourseName(), "Not Graded");
				}
			}
		}
		catch (StudentNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Register courses for the semester
	 */
	private void registerCourses() {
		try {
			if(registrationInterface.isRegistrationDone(studentID)) {
				System.err.println("Already Registered");
				return;
			}
			viewCourseCatalogue();
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			HashMap<Integer,Boolean> courseIDs = new HashMap<>();
			System.out.println("Enter Course IDs for prefererred Course #1 : ");
			courseIDs.put(sc.nextInt(), true);
			System.out.println("Enter Course IDs for prefererred Course #2 : ");
			courseIDs.put(sc.nextInt(), true);
			System.out.println("Enter Course IDs for prefererred Course #3 : ");
			courseIDs.put(sc.nextInt(), true);
			System.out.println("Enter Course IDs for prefererred Course #4 : ");
			courseIDs.put(sc.nextInt(), true);
			System.out.println("Enter Course IDs for alternate Course #1 : ");
			courseIDs.put(sc.nextInt(), false);
			System.out.println("Enter Course IDs for alternate Course #2 : ");
			courseIDs.put(sc.nextInt(), false);
			
			registrationInterface.registerCourses(studentID, courseIDs);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Add a course
	 */
	private void addCourse() {
		viewCourseCatalogue();
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter CourseID : ");
		int courseID = sc.nextInt();
		sc.nextLine();
		try {
			if(registrationInterface.addCourse(studentID, courseID)) {
				System.out.println("Course Added.");
			}
			else {
				System.err.println("Please Register first.");
			}
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Drop a course
	 */
	private void dropCourse() {
		System.out.println("Registered Courses :");
		viewRegisteredCourses();
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter CourseID : ");
		int courseID = sc.nextInt();
		sc.nextLine();
		try {
			registrationInterface.dropCourse(studentID, courseID);
			System.out.println("Course Dropped.");
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * View registered courses
	 */
	private void viewRegisteredCourses() {
		try {
			List<RegisteredCourse> courses = registrationInterface.viewRegisteredCourses(studentID);
			System.out.printf("%10s%20s%20s\n","Course ID","Course Name","Instructor");
			for(RegisteredCourse course:courses) {
				System.out.format("%10d%20s%20s\n", course.getCourseId(), course.getCourseName(), course.getInstructor());
			}
		}
		catch (StudentNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Pay semester Fees
	 */
	private void payFee() {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		try{
			if(!registrationInterface.isRegistrationDone(studentID)) {
				System.err.println("Registration not yet complete.");
			}
			else {
				int fee = registrationInterface.calculateFee(studentID);
				if(fee==0) {
					System.out.println("Your payment is already completed.");
					return;
				}
				System.out.println("Pending amount is : "+fee+", Do you want to pay? (Y/N) : ");
				String inp = sc.next();
				if(inp.equals("Y") || inp.equals("y")){
					int amount = fee;
					System.out.println("==================== Payment Gateway ====================");
					System.out.println("1. Credit Card");
					System.out.println("2. Net Banking");
					System.out.println("3. Debit Card");
					System.out.println("4. Scholarship");
					System.out.println("5. Cash");
					System.out.println("6. Cheque");
					System.out.println("7. Cancel");
					
					System.out.println("Select Mode of Payment : ");
					int modeChoice = sc.nextInt();
					switch(modeChoice)
					{
					case 1:
						payViaCreditCard(studentID, amount);
						break;
					case 2:
						payViaNetBanking(studentID, amount);
						break;
					case 3:
						payViaDebitCard(studentID, amount);
						break;
					case 4:
						payViaScholarship(studentID, amount);
						break;
					case 5:
						payViaCash(studentID, amount);
						break;
					case 6:
						payViaCheque(studentID, amount);
						break;
					case 7:
						return;
					default:
						System.err.println("No such mode exists, Enter valid choices.");						
					}
				}
				
			}
		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}
	}

	private void payViaCheque(String studentID2, int amount) {
		// TODO Auto-generated method stub
		try {
			registrationInterface.payFee(studentID, ModeOfPaymentConstant.CHEQUE, amount, "Not Applicable");
			System.out.println("Your Payment has been Accepted.");	
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Pay by DD(Demand Draft)
	 * @param studentID2-> Student of of student paying
	 * @param amount -> Amount they are paying
	 */
	private void payViaCash(String studentID2, int amount) {
		
		try {
			registrationInterface.payFee(studentID, ModeOfPaymentConstant.CASH, amount, "Not Applicable");
			System.out.println("Your Payment has been Accepted.");	
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Pay by scholarship
	 * @param studentID2-> Student of of student paying
	 * @param amount -> Amount they are paying
	 */
	private void payViaScholarship(String studentID2, int amount) {
			
		try {
			registrationInterface.payFee(studentID, ModeOfPaymentConstant.SCHOLORSHIP, amount, "Not Applicable");
			System.out.println("Congratulions, Your Scholorship has been approved by the Admin.");
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
	}

	private void payViaDebitCard(String studentID, int amount) {
		try {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			sc.useDelimiter("\r\n");
			System.out.println("==================== Debit Card Details ====================");

			System.out.println("Card Number:");
			String cardNo = sc.next();
			while(!cardNo.matches("\\d{16}")) {
				System.err.println("Invalid Debit Card details entered. Enter again : ");
				cardNo = sc.next();
			}
			
			System.out.println("Name:");
			sc.next();
			
			System.out.println("Expiry Date(dd-mm-yyyy):");
			String date = sc.next();
			while(!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
				System.err.println("Invalid date format entered. Enter Again : ");
				date = sc.next();
			}
			
			System.out.println("CVV:");
			String cvv = sc.next();
			while(!cvv.matches("\\d{3}")) {
				System.err.println("Invalid CVV entered. Enter again : ");
				cvv = sc.next();
			}
			
			registrationInterface.payFee(studentID, ModeOfPaymentConstant.DEBIT_CARD, amount, cardNo);
			System.out.println("Your Payment is complete.");
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Pay via net banking
	 * @param studentID2-> Student of of student paying
	 * @param amount -> Amount they are paying
	 */
	private void payViaNetBanking(String studentID2, int amount) {
		try {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			System.out.println("==================== Net Banking Details ====================");
			System.out.println("Username:");
			String username=sc.next();
			System.out.println("Password:");
			sc.next();
			registrationInterface.payFee(studentID, ModeOfPaymentConstant.NET_BANKING, amount, username);
			System.out.println("Your Payment is complete.");
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Pay via credit card
	 * @param studentID2-> Student of of student paying
	 * @param amount -> Amount they are paying
	 */
	private void payViaCreditCard(String studentID2, int amount) {
		try {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			sc.useDelimiter("\r\n");
			System.out.println("==================== Credit Card Details ====================");

			System.out.println("Card Number:");
			String cardNo = sc.nextLine();
			while(!(cardNo.matches("\\d{16}"))) {
				System.err.println("Invalid Credit Card details entered. Enter again : ");
				cardNo = sc.nextLine();
			}
			
			System.out.println("Name:");
			sc.nextLine();
			
			System.out.println("Expiry Date(dd-mm-yyyy):");
			String date = sc.nextLine();
			while(!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
				System.err.println("Invalid date format entered. Enter Again : ");
				date = sc.nextLine();
			}
			
			System.out.println("CVV:");
			String cvv = sc.nextLine();
			while(!cvv.matches("\\d{3}")) {
				System.err.println("Invalid CVV entered. Enter again : ");
				cvv = sc.nextLine();
			}
			
			registrationInterface.payFee(studentID, ModeOfPaymentConstant.CREDIT_CARD, amount, cardNo);
			System.out.println("Your Payment is complete.");
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
}
