package org.chon.jcr.client.service.model;

import java.text.SimpleDateFormat;

public class AttributeType {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	/**
	 * BOOLEAN, true or false
	 */
	public static final AttributeType BOOLEAN = new AttributeType("BOOLEAN");
	
	/**
	 * NUMBER type
	 */
	public static final AttributeType NUMBER = new AttributeType("INT") ;
	
	/**
	 * String type
	 */
	public static final AttributeType TEXT = new AttributeType("TEXT");
	
	/**
	 * DATE type
	 * attr from this type String value is: "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
	 * $see AttributeType.DATE_FORMAT
	 */
	public static final AttributeType DATE = new AttributeType("DATE");
	
	
	private String typeName;
	private AttributeType(String typeName) {
		this.typeName = typeName;
	}
	
	@Override
	public String toString() {
		return typeName;
	}

	public static AttributeType fromString(String t) {
		if(t == null) {
			return null;
		}
		t = t.toUpperCase();
		if(t.startsWith("T")) return AttributeType.TEXT;
		if(t.startsWith("D")) return AttributeType.DATE;
		if(t.startsWith("B")) return AttributeType.BOOLEAN;
		if(t.startsWith("N")) return AttributeType.NUMBER;
		return null;
	}
}
