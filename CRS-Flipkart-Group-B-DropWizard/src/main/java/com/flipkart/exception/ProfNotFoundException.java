package com.flipkart.exception;

/**
 * @author Group-B
 *
 */

public class ProfNotFoundException extends Exception {
    private String ProfID;

    /**
     * Constructor for ProfFoundExceptionClass
     * @param ProfID --> This is the ID of the prof being removed
     */
    public ProfNotFoundException (String ProfID) {
        this.ProfID = ProfID;
    }

    /**
     * getter method for ProfID
     * @return
     */
    public String getProfID() {
        return ProfID;
    }

    /**
     * overriden getMessage from Exception Class
     * @return
     */
    @Override
    public String getMessage() {
        return "Prof : " + ProfID + " not found in the database";
    }
}
