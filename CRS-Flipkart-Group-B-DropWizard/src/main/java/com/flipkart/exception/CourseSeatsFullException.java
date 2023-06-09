package com.flipkart.exception;

/**
 * @author Group-B
 *
 */

public class CourseSeatsFullException extends Exception {

    private int courseID;

    /**
     * Constructor
     * @param courseID
     */
    public CourseSeatsFullException(int courseID){
        this.courseID = courseID;
    }

    /**
	 * Message returned when exception is thrown
	 */
	@Override
	public String getMessage() {
		return  "Seats are not available in : " + courseID;
	}

}
