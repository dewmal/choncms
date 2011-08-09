package org.chon.cms.model.content;

import javax.jcr.Node;

import org.chon.cms.model.ContentModel;

/**
 * In order to be able to wrap nodes into specific classes you need to register 
 * factory that can create content node from jcr node
 * 
 *  Register factories as OSGI service
 *  
 *  	ctx.registerService(MyFactory.class.getName(), instance, properties);
 *  
 *  
 * @author Jovica
 *
 */
public interface IContentNodeFactory {
	/**
	 * If this factory can create instances contentNodeClass
	 * @param contentNodeClass
	 * @return
	 */
	public boolean canCreate(String contentNodeClass);
	
	/**
	 * Crates instance from contentNodeClass
	 * 
	 * @param contentNodeClass
	 * @return
	 */
	public IContentNode createIntance(String contentNodeClass, ContentModel contentModel, Node node, IContentNode typeDesc);
}
