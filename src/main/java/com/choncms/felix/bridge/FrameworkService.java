package com.choncms.felix.bridge;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.osgi.framework.Constants;

import com.choncms.config.ConfigReader;


public final class FrameworkService
{
    private final ServletContext context;
    private Felix felix;

    public FrameworkService(ServletContext context)
    {
        this.context = context;
    }

    public void start()
    {
        try {
            doStart();
        } catch (Exception e) {
            log("Failed to start framework", e);
        }
    }

    public void stop()
    {
        try {
            doStop();
        } catch (Exception e) {
            log("Error stopping framework", e);
        }
    }

    private void doStart()
        throws Exception
    {
        Felix tmp = new Felix(createConfig());
        printInfo();
        tmp.start();
        this.felix = tmp;
        log("OSGi framework started", null);
    }

    private void printInfo() {
		System.out.println("--------------------------------------------------------------------------------");
		System.out.println("---------------   Starting Chon Application   ----------------------------------");
		System.out.println(" - Using work-dir ........ " + System.getProperty("app.work.dir"));
		System.out.println(" - Plugins dir ........... " + System.getProperty("chon.plugins.dir"));
		System.out.println(" - Reposotiry dir ........ " + System.getProperty("repo.dir"));
		System.out.println(" - Site URL .............. " + System.getProperty("siteUrl"));
		System.out.println("--------------------------------------------------------------------------------");
	}

	private void doStop()
        throws Exception
    {
        if (this.felix != null) {
            this.felix.stop();
        }

        log("OSGi framework stopped", null);
    }

    private Map<String, Object> createConfig()
        throws Exception
    {
        Properties props = new Properties();
        
        props.load(this.context.getResourceAsStream("/WEB-INF/framework.properties"));

        HashMap<String, Object> map = new HashMap<String, Object>();
        for (Object key : props.keySet()) {
            map.put(key.toString(), props.get(key));
        }
        
        map.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, Arrays.asList(new ProvisionActivator(this.context)));
        
        /**
         * TODO: check intergrity for configurations
         * make sure we have valid plugins dir, repository dir and resources...
         * On initial application run (if application plugins are packaged inside war) 
         * make sure we can create chon-work-dir and unpack plugins.
         * Repository and config should also be autocreated 
         */
        // read system.properties file
        ConfigReader.readSystemProperties(this.context);
        
        // set felix-cache to temp dir if felx-cache dir not found
        String felixCacheDirPath = System.getProperty("felix-cache", System.getProperty("java.io.tmpdir") + "/felix-cache");
        File felixCacheDir = new File(felixCacheDirPath);
        felixCacheDir.mkdirs();
        felixCacheDirPath = felixCacheDir.getAbsolutePath();
        
        log("Using felix-cache dir: " + felixCacheDirPath, null);
		map.put(Constants.FRAMEWORK_STORAGE, felixCacheDirPath);
		
        return map;
    }

    private void log(String message, Throwable cause)
    {
        this.context.log(message, cause);
    }
}
