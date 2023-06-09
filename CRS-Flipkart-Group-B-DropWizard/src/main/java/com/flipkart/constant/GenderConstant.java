package com.flipkart.constant;
/**
 * @author Group-B
 *
 */
/**
 * Enum to represent gender
 */
public enum GenderConstant {
	MALE(1),FEMALE(2),OTHER(3);
	@SuppressWarnings("unused")
	private final int gender;
	
	/**
	 * Parameterized Constructor
	 * @param gender
	 */
	private GenderConstant(int gender)
	{
		this.gender=gender;
	}
	
	/**
	 * Method to return gender type in String
	 * @return Gender name in String
	 */
	@Override
	public String toString()
	{
		final String name=name();
		return name; 
	}
	
	/**
	 * Method to get Gender object depending upon user input
	 * @param val
	 * @return Gender object
	 */
	public static GenderConstant getName(int val)
	{
		GenderConstant gender=GenderConstant.OTHER;
		switch(val)
		{
		case 1:
			gender=GenderConstant.MALE;
			break;
		case 2:
			gender=GenderConstant.FEMALE;
			break;
			
		}
		return gender;
	}
	
	/**
	 * Method to convert String to Gender object
	 * @param val
	 * @return Gender object
	 */
	public static GenderConstant stringToGender(String val)
	{
		GenderConstant gender=GenderConstant.OTHER;
		if(val.equalsIgnoreCase("male"))
			gender=GenderConstant.MALE;
		else if(val.equalsIgnoreCase("female"))
			gender=GenderConstant.FEMALE;
		else if(val.equalsIgnoreCase("other"))
			gender=GenderConstant.OTHER;
		
		return gender;
	}

	/**
	 * Method to return corresponding integer value of enum
	 * @param gender Gender of user
	 * @return integer corresponding to gender
	 */
	public static int genderToInt (GenderConstant gender)
	{
		switch (gender)
		{
			case MALE:	return 1;
			case FEMALE: return 2;
			default: return 3;
		}
	}
}
