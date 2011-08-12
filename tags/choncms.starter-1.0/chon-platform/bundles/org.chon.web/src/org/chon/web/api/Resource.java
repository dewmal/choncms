package org.chon.web.api;

public interface Resource {
	public static final int ActionResource = 1;
	public static final int StaticResource = 2;
	public static final int ImageResource  = 3;
	public static final int PluginResource = 4;
	
	public void process(ServerInfo si);
}
