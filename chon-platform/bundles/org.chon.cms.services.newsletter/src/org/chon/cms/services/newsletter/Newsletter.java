package org.chon.cms.services.newsletter;

import java.util.List;
import java.util.Map;

public interface Newsletter {
	public static final int STATUS_DEFAULT = 0;
	public static final int STATUS_SENDING = 1;
	public static final int STATUS_ERROR = -1;
	public static final int STATUS_DONE = 2;
	/**
	 * Register new email to system, register to recieve this newsletter
	 * 
	 * @param email
	 * @param group
	 * @param additionalInfo
	 * @throws NewsletterException
	 */
	public void subscribe(String email, Map<String, String> additionalInfo) throws NewsletterException;

	/**
	 * Unsubscribe, delete mail from this newsletter
	 * you need subscription code to be able to unsubscribe
	 * 
	 * @param email
	 */
	public void unsubscribe(String email, String code) throws NewsletterException;
	
	/**
	 * Check if mail is already in the system
	 * 
	 * @param email
	 * @return
	 */
	public boolean isSubscribed(String email) throws NewsletterException;
	
	/**
	 * get subscribers (list of emails)
	 *  start and limit can be null
	 *  for huge email list use pagination
	 *  
	 * @return
	 */
	public List<String> getSubscribers(Integer start, Integer limmit);
	
	/**
	 * get total number of subscribers
	 * 
	 * @return
	 */
	public long getTotalSubscribers();
	
	/**
	 * Get subscriber additional info map: name, surname ...
	 * 
	 * @param email
	 * @return
	 * @throws NewsletterException - email not exists in the system
	 */
	public Map<String, String> getSubscriberInfo(String email) throws NewsletterException;
	
	/**
	 * Async method for sending newsletter, returns process id, see getStatus
	 * 
	 * @param velocity template
	 * 
	 * @param additional params when rendering template
	 * 	all params will be added to template, subscriber param is used internally and will be overwritten
	 * 	$subscriber.email is always present
	 * 	into $subscriber will be attached all other additinalInfo from subscriber
	 * 
	 * so, params can use any keys except subscriber
	 * 
	 * @return process id
	 * @throws NewsletterException
	 */
	public void send(String subject, String template, Map<String, Object> params) throws NewsletterException;
	
	/**
	 * Send only to one, usfull for testing...
	 * 
	 * @param subject
	 * @param template
	 * @param params
	 * @throws NewsletterException
	 */
	public void send(String email, String subject, String template, Map<String, Object> params) throws NewsletterException;

	/**
	 * Get status for running newsletters values: total - total numbers of mails
	 * to send percent - percent finished sent - number sent
	 *  
	 * 	status - DEFAULT, RUNNING, ERROR, FINISHED
	 * 
	 * @param pid
	 * @return
	 */
	public int getStatus();
	
	/**
	 * get newsletter info, ... 
	 * - state (same as getStatus)
	 * - numberOfSubsribers
	 * - name
	 * 
	 * When status == RUNNING
	 * 	- percent (mails sent, float from 0 to 100)
	 *  - numMailsSent (int)
	 *  - failedEmails (list of strings)
	 * When status == ERROR
	 *  ??? error why
	 *  
	 * @return
	 */
	public Map<String, Object> getInfo();

}
