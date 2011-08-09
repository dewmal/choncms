package org.chon.web.mpac;
public interface Module extends ActionHandler {
	public String getName();
	public int getOrder();
	public String getLayout();
	
}
