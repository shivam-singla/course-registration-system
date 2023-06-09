package com.flipkart.restController;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
 
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
 
import com.flipkart.bean.Course;
import com.flipkart.bean.Student;
import com.flipkart.constant.GradeConstant;
import com.flipkart.dao.ProfessorDaoOperation;
import com.flipkart.exception.StudentNotFoundException;
import com.flipkart.service.ProfessorInterface;
import com.flipkart.service.ProfessorOperation;
 
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
import org.hibernate.validator.constraints.Email;
 
/**
 * * @author Group-B
 * Shivam Singla (Leader)
 * Cyrus
 * Tanvi
 * Ansh
 * Ramasamy
 * Divy
 * Rest API class for professor 
 */
@Path("/professor")
public class ProfessorRestAPI {
	
	ProfessorInterface professorInterface = ProfessorOperation.getInstance();
	
	
	/**
	 * View all students registered in a given course
	 */
	@GET
	@Path("/viewEnrolledStudents")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Student> viewEnrolledStudents(@NotNull
			@QueryParam("profId") String profId, 
			@NotNull 
			@QueryParam("courseId") int courseId) throws ValidationException {
		
		List<Student> students=new ArrayList<Student>();
	
		try{
			
			students = professorInterface.viewStudent(courseId, profId);
			
		}
		catch(Exception e){
			return null;
		}
		
		return students;
	}
	
	/**
	 * View all courses alloted to professor
	 */
	@GET
	@Path("/viewCourses")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> viewCourses(@NotNull @QueryParam("profId") String profId) {
		List<Course> courses=new ArrayList<Course>();
		try{
			courses = professorInterface.viewCourses(profId);
		}
		catch(Exception e){
			return null;
		}
		return courses;
		
	}
	
	/**
	 * Assign Grades to a student
	 */
	@POST
	@Path("/assignGrade")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignGrade(
			@NotNull
			@QueryParam("studentId") String studentId,
			
			@NotNull
			@QueryParam("courseId") int courseId,
			
			@NotNull
			@QueryParam("profId") String profId,
			
			@NotNull
			@QueryParam("grade") String grade) throws ValidationException  {
		
		try {
			List<Course> courses = professorInterface.viewCourses(profId);
			Boolean success=false;
			for(Course course:courses) {
				if(course.getCourseId()==courseId) {
					success=true;
					break;
				}
			}
			if(!success) {
				return Response.status(400).entity("Course does not match with professor.").build();
			}
			
			professorInterface.assignGrade(studentId, courseId, GradeConstant.fromString(grade));
		}
		catch(StudentNotFoundException e) {
			return Response.status(400).entity(e.getMessage()).build();
		}
		catch(Exception e){
			return Response.status(500).entity(e.getMessage()).build();
		}
		return Response.status(200).entity("Grade added for student with studentId: "+studentId).build();
	}
	
 
 
}