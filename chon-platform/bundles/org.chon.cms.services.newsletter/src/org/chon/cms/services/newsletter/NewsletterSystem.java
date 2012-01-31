package org.chon.cms.services.newsletter;

public interface NewsletterSystem {
	public static final String NEWSLETTER_PUBLIC_CONTAINER = "newsletter.public.container";
	public static final String NEWSLETTER_PUBLIC_NODE = "newsletter.public";
	
	public static final String NEWSLETTER_CONTAINER = "newsletter.container";
	public static final String NEWSLETTER_SUBSCRIBER = "newsletter.subscriber";
	
	/**
	 * Get newsletter from a system
	 * 	if newsletter does not exists, system will create initial empty
	 * 
	 * @param name
	 * @return
	 */
	public Newsletter getNewsletter(String name, boolean createIfNotExists);
}
