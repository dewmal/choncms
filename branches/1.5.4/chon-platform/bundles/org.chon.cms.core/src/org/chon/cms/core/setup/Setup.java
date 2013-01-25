package org.chon.cms.core.setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.common.configuration.ConfigurationFactory;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Setup respository
 *  
 * TODO: rework this, make plugin for general setup...
 * @author Jovica
 *
 */
public class Setup {
	private static final Log log = LogFactory.getLog(Setup.class);
	
	private class NodeCreation {
		String path;
		JSONObject props;
	}
	
	private List<NodeCreation> readKSon(String [] lines) throws JSONException {
		List<NodeCreation> ncList = new ArrayList<Setup.NodeCreation>();
		String [] levels = new String [100];
		for(int i=0; i<lines.length; i++) {
			String line = lines[i];
			if(line.trim().length()==0) continue;
			int tabs = getTabs(line);
			String name = getName(line);
			levels[tabs] = name;
			String path = "";
			for(int j=0; j<tabs; j++) {
				path = path + "/" + levels[j];
			}
			NodeCreation nc = new NodeCreation();
			nc.path = path + "/" + name;
			nc.props = getProps(line);
			ncList.add(nc);
		}
		return ncList;
	}
	private JSONObject getProps(String line) throws JSONException {
		String ln = line.trim();
		int n = ln.indexOf("=");
		String ss = ln.substring(n+1);
		return new JSONObject(ss);
	}
	private String getName(String line) {
		String ln = line.trim();
		int n = ln.indexOf("=");
		String ss = ln.substring(0, n);
		return ss.trim();
	}
	private int getTabs(String line) {
		int i=0, k=0;
		while(Character.isWhitespace(line.charAt(i))) {
			if(line.charAt(i) == '\t')
				k++;
			i++;
		}
		return k;
	}
	
	public void install(Session session) throws Exception {
		
		String cfgPath = ConfigurationFactory.getConfigSonInstance().getConfigurationPath();
		File setupKson = new File(cfgPath, "setup.kson");
		InputStream is = null;
		if(setupKson.exists()) {
			log.debug("Reagind kson from " + setupKson.getAbsolutePath());
			is = new FileInputStream(setupKson);
		} else {
			String ksonCp = "/org/chon/cms/core/setup/setup.kson";
			is = Setup.class.getResourceAsStream(ksonCp);
			log.info("Creating initial setup.kson in " + setupKson.getAbsolutePath());
			IOUtils.copy(is, new FileOutputStream(setupKson));
			is = new FileInputStream(setupKson);
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		List<String> lines = new ArrayList<String>();
		
		while(true) {			
			String line = br.readLine();
			if(line==null) break;
			lines.add(line);
		}
		List<NodeCreation> ncList = readKSon(lines.toArray(new String [lines.size()]));
		for(NodeCreation nc : ncList) {
			try {
				createNode(session, nc);
			} catch (Exception e) {
				System.err.println("error crating node " + nc.path + " -> "+ e.getMessage());
			}
		}
		session.save();
	}
	
	private void createNode(Session session, NodeCreation nc)
			throws ItemExistsException, PathNotFoundException,
			VersionException, ConstraintViolationException, LockException,
			RepositoryException, JSONException {
		while (nc.path.startsWith("/"))
			nc.path = nc.path.substring(1);

		Node node = null;
		String mode = null;
		if (!session.getRootNode().hasNode(nc.path)) {
			node = session.getRootNode().addNode(nc.path);
			mode = "Create";
		} else {
			node = session.getRootNode().getNode(nc.path);
			mode = "Update";
		}
		if (nc.props != null) {
			Iterator it = nc.props.keys();
			while (it.hasNext()) {
				String k = (String) it.next();
				node.setProperty(k, nc.props.getString(k));
			}
		}
		System.out.println(mode + "ed node" + node);
	}
}
