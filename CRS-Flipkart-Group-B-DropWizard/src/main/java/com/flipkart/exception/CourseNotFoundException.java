package com.flipkart.exception;

/**
 * @author Group-B
 *
 */
public class CourseNotFoundException extends Exception{
    private int courseCode;

    /**
     * constructor for the exception class
     * @param courseCode --> this is the ID of the course which is cannot be found
     */
    public CourseNotFoundException(int courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * getter method for @param courseCode
     * @return
     */
    public int getCourseCode() {
        return courseCode;
    }

    /**
     * Overrides the error message shown for exception class
     * @return
     */
    @Override
    public String getMessage() {
        return "Course : " + courseCode + " not found in the course catalogue";
    }
}
