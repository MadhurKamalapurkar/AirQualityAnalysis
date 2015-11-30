/**
 * 
 */
package com.cmpe274.model;

/**
 * @author madhur
 *
 */
public class ResponseData {
	String Latitude;
	String Longitude;
	String UTC;
	String Parameter;
	String Unit;
	String Value;
	String AQI;
	String Category;
	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return Latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return Longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	/**
	 * @return the uTC
	 */
	public String getUTC() {
		return UTC;
	}
	/**
	 * @param uTC the uTC to set
	 */
	public void setUTC(String uTC) {
		UTC = uTC;
	}
	/**
	 * @return the parameter
	 */
	public String getParameter() {
		return Parameter;
	}
	/**
	 * @param parameter the parameter to set
	 */
	public void setParameter(String parameter) {
		Parameter = parameter;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return Unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		Unit = unit;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return Value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		Value = value;
	}
	/**
	 * @return the aQI
	 */
	public String getAQI() {
		return AQI;
	}
	/**
	 * @param aQI the aQI to set
	 */
	public void setAQI(String aQI) {
		AQI = aQI;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return Category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		Category = category;
	}
}
