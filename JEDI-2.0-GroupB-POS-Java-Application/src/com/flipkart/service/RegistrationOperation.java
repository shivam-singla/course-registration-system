/**
 * 
 */
package com.flipkart.service;
import java.util.Date;
/**
 * @author Group-B
 *
 */
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.flipkart.bean.Course;

import com.flipkart.bean.Payment;
import com.flipkart.bean.PaymentNotification;
import com.flipkart.bean.RegisteredCourse;
import com.flipkart.bean.Student;
import com.flipkart.client.CRSApplication;
import com.flipkart.constant.ModeOfPaymentConstant;
import com.flipkart.dao.ProfessorDaoOperation;
import com.flipkart.exception.CourseLimitExceededException;
import com.flipkart.exception.CourseNotFoundException;
import com.flipkart.exception.CourseSeatsFullException;
import com.flipkart.exception.DatabaseException;
import com.flipkart.exception.NotifIdNotExistsException;
import com.flipkart.exception.PaymentAlreadyDoneException;
import com.flipkart.exception.RegistrationNotCompleteException;
import com.flipkart.exception.StudentNotFoundException;
import com.flipkart.dao.RegistrationDaoInterface;
import com.flipkart.dao.RegistrationDaoOperation;

import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Locale;  

/**
 * @author shubh
 *
 */
public class RegistrationOperation implements RegistrationInterface {
	private static Logger logger = Logger.getLogger(RegistrationOperation.class);

	private static volatile RegistrationOperation instance = null;
	RegistrationDaoInterface registrationDaoInterface = RegistrationDaoOperation.getInstance();
	UserInterface userInterface = UserOperation.getInstance();

	private RegistrationOperation(){}

	public static RegistrationOperation getInstance() {
		if (instance == null) {
			synchronized (RegistrationOperation.class) {
				instance = new RegistrationOperation();
			}
		}
		return instance;
	}

	@Override
	public boolean registerCourses(String studentId, HashMap<Integer,Boolean> courseIDs) throws StudentNotFoundException{ //status=alternate, primary or registered
		if(registrationDaoInterface.isRegistrationDone(studentId)) {
			logger.info("Already Registered");
			return false;
		}
		registrationDaoInterface.registerCourses(studentId, courseIDs);
		logger.info("You have successfully registed for the current semester. Kindly make the Payment.");
		Date date_ = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");  
		String date= formatter.format(date_);
		 NotificationOperation.getInstance().sendNotification(studentId, "Registration", "You have sucessfully registered at "+date);
		return true;
	}
	
	@Override
	public boolean isRegistrationDone(String studentId) throws StudentNotFoundException{
		return registrationDaoInterface.isRegistrationDone(studentId);
	}
	
	@Override
	public boolean isPaymentDone(String studentId) throws StudentNotFoundException
	{
		return registrationDaoInterface.isPaymentDone(studentId);
	}

	@Override
	public boolean addCourse(String studentId, int courseCode)
			throws CourseNotFoundException, CourseLimitExceededException, CourseSeatsFullException, StudentNotFoundException, DatabaseException{
		if(!registrationDaoInterface.isRegistrationDone(studentId)) {
			return false;
		}
		Course course = registrationDaoInterface.getCourse(courseCode);
		if(course.getFilledSeats()>=Course.MAX_SEATS){
			throw new CourseSeatsFullException(course.getCourseId());
		}
		if(viewRegisteredCourses(studentId).size() >= Student.MAX_COURSES){
			throw new CourseLimitExceededException(Student.MAX_COURSES);
		}
		registrationDaoInterface.addCourse(studentId, courseCode);
		Date date_ = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");  
		String date= formatter.format(date_);
		 NotificationOperation.getInstance().sendNotification(studentId, "Registration", "You have sucessfully added the course at "+date);
		return true;
	}

	@Override
	public boolean dropCourse(String studentId, int courseCode) throws CourseNotFoundException, StudentNotFoundException, DatabaseException{
		if(!registrationDaoInterface.isRegistrationDone(studentId)) {
			return false;
		}
		List<RegisteredCourse> regCourses = viewRegisteredCourses(studentId);
		boolean isRegistered=false;
		for(RegisteredCourse course : regCourses){
			if(course.getCourseId()==courseCode){
				isRegistered=true;
			}
		}
		if(!isRegistered){
			throw new CourseNotFoundException(courseCode);
		}
		registrationDaoInterface.dropCourse(studentId, courseCode);
		Date date_ = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");  
		String date= formatter.format(date_);
		 NotificationOperation.getInstance().sendNotification(studentId, "Registration", "You have sucessfully dropped the course at "+date);
		return true;
	}

	@Override
	public List<RegisteredCourse> viewRegisteredCourses(String studentId) throws StudentNotFoundException{
		return registrationDaoInterface.viewRegisteredCourses(studentId);
	}
	
	@Override
	public int calculateFee(String studentId) throws StudentNotFoundException{
		return registrationDaoInterface.calculateFee(studentId);
	}

	@Override
	public void payFee(String studentId, ModeOfPaymentConstant mode, int amount, String creds) throws StudentNotFoundException, NotifIdNotExistsException{
		float feeToBePaid = calculateFee(studentId);
		String message;

		if(!registrationDaoInterface.isRegistrationDone(studentId)) {
			// should not happen in current code
			message = "Registration not Yet Complete";
//			throw new RegistrationNotCompleteException(studentId);
		}
		else if(registrationDaoInterface.isPaymentDone(studentId)) {
			// should not happen in current code
			message = "Payment Already Done";
//			throw new PaymentAlreadyDoneException(studentId);
		}else if(amount != feeToBePaid) {
			// should not happen in current code
			message =  "Amount chosen to pay and fee to be paid are different";
		}else {
			Payment payObj = new Payment(studentId, mode, amount);
			if(payObj.isStatus()) {
				registrationDaoInterface.feePaid(studentId);
				message = "Payment Successful! Payment has been via the Transaction Id: " + payObj.getPaymentId();
			}else {
				message = "Payment Failed! Try again later";
			}
			
		}
		NotificationOperation.getInstance().sendNotification(studentId, "Payment", message);
		PaymentOperation.getInstance().sendPayment(studentId, amount, mode, creds);

	}



}
