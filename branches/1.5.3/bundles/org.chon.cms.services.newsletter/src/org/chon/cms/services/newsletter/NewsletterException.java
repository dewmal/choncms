package org.chon.cms.services.newsletter;

public class NewsletterException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public NewsletterException() {
		super();
	}


	public NewsletterException(String message, Throwable cause) {
		super(message, cause);
	}


	public NewsletterException(Throwable cause) {
		super(cause);
	}


	public NewsletterException(String msg) {
		super(msg);
	}
}
