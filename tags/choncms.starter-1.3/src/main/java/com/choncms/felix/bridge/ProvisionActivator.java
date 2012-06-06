package com.choncms.felix.bridge;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public final class ProvisionActivator
    implements BundleActivator
{
    private final ServletContext servletContext;

    public ProvisionActivator(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    public void start(BundleContext context)
        throws Exception
    {
        servletContext.setAttribute(BundleContext.class.getName(), context);

        ArrayList<Bundle> installed = new ArrayList<Bundle>();
        for (URL url : findBundles()) {
            this.servletContext.log("Installing bundle [" + url + "]");
            try {
            	Bundle bundle = context.installBundle(url.toExternalForm());
            	installed.add(bundle);
            } catch (Exception e) {
            	System.err.println(e.getMessage());
			}
        }

        for (Bundle bundle : installed) {
            bundle.start();
        }
    }

    public void stop(BundleContext context)
        throws Exception
    {
    }

    private List<URL> findBundles()
        throws Exception
    {
    	File pluginsDir = new File(System.getProperty("chon.plugins.dir"));
    	if(!pluginsDir.exists()) {
    		throw new Exception("Invalid plugins dir ");
    	}
    	
    	File [] plugins = pluginsDir.listFiles(new FilenameFilter() {
			public boolean accept(File arg0, String name) {
				return name.endsWith(".jar");
			}
		});
    	
        ArrayList<URL> list = new ArrayList<URL>();
        if(plugins != null) {
        	System.out.println("Found " + plugins.length + " plugins. Loading ... ");
	        for(File f : plugins) {
	        	list.add(f.toURI().toURL());
	        }
        }
        
        return list;
        /*
        for (Object o : this.servletContext.getResourcePaths("/WEB-INF/plugins/")) {
            String name = (String)o;
            if (name.endsWith(".jar")) {
                URL url = this.servletContext.getResource(name);
                if (url != null) {
                    list.add(url);
                }
            }
        }
        return list;
        */
    }
}
