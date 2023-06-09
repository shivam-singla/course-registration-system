/**
 * 
 */
package com.flipkart.client;

import java.util.List;

import java.util.Scanner;

import com.flipkart.bean.Course;
import com.flipkart.bean.Student;
import com.flipkart.constant.GradeConstant;
import com.flipkart.service.ProfessorInterface;
import com.flipkart.service.ProfessorOperation;

/**
 * @author Group-B
 *
 */

public class ProfessorCRSMenu {

//	private static Logger logger = Logger.getLogger(ProfessorMenu.class);

	String professorID;
	ProfessorInterface professorInterface = ProfessorOperation.getInstance();

	/**
	 * Constructor
	 * @param profID
	 */
	public ProfessorCRSMenu(String profID) {
		this.professorID = profID;
	}

	/**
	 * Display the menu for professor
	 */
	public void displayMenu() {
		// Display the options available for the Professor
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);

		while (true) {
			
			System.out.println("+------------------------------------------------------------------------+");
	        System.out.println("|                           Professor Menu                               |");
	        System.out.println("+------------------------------------------------------------------------+");
	        System.out.println("|   1    | View Courses                                                  |");
	        System.out.println("|   2    | View Enrolled Students                                        |");
	        System.out.println("|   3    | Assign Grade                                                  |");
	        System.out.println("|   4    | Logout                                                        |");
	        System.out.println("+--------+---------------------------------------------------------------+");
	        System.out.print("\nEnter Option: ");


			int input = sc.nextInt();
			sc.nextLine();
			switch (input) {
			case 1:
				// View courses taught by the professor
				viewCourses();
				break;
			case 2:
				// View enrolled students for the course
				viewStudents();
				break;

			case 3:
				// Add grade for a student
				assignGrades();
				break;
			case 4:
				// Logout
//				sc.close();
				System.out.println("==================== Logging Out ====================");
				return;
			default:
				System.err.println("No such operation exists, valid choices 1, 2, 3, 4");
			}
		}
	}

	/**
	 * View all courses alloted to professor
	 */
	private void viewCourses() {
		try{
			List<Course> courses = professorInterface.viewCourses(professorID);
			System.out.printf("%10s%20s%20s\n","Course ID","Course Name","Filled Seats");
			for (Course course : courses) {
				System.out.format("%10d%20s%20s\n", course.getCourseId(), course.getCourseName(), course.getFilledSeats());
			}
		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}
		
	}

	/**
	 * View all students registered in a given course
	 */
	private void viewStudents() {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		try{
			System.out.println("Enter CourseID : ");
			int courseID = sc.nextInt();
			List<Student> students = professorInterface.viewStudent(courseID, professorID);
			System.out.printf("%10s%20s%20s\n","StudentID","Student Name","Branch");
			for (Student student : students) {
				System.out.format("%10s%20s%20s\n", student.getId(),  student.getName(), student.getBranch());
			}
		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Assign Grades to a student
	 */
	private void assignGrades() {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Course ID : ");
		int courseID = sc.nextInt();
		System.out.println("Enter Student ID : ");
		String studentID = sc.next();
		System.out.println("Enter Grade : ");
		String grade = sc.next();
		try {
			professorInterface.assignGrade(studentID, courseID, GradeConstant.fromString(grade));
		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}
	}
}
