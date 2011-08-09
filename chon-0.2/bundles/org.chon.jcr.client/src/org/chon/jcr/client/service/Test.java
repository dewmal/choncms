package org.chon.jcr.client.service;

import java.util.List;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.chon.jcr.client.service.impl.RepoServicesImpl;
import org.chon.jcr.client.service.model.NodeAttribute;
import org.chon.jcr.client.service.model.NodeInfo;


public class Test {

	/**
	 * @param args
	 * @throws RepositoryException 
	 * @throws LoginException 
	 */
	public static void main(String[] args) throws Exception {
		RepoService service = new RepoServicesImpl();
		
		Repository repo = null; //new TransientRepository(new File("c:/temp/repo3"));
		Session session = repo.login(new SimpleCredentials("abu", "read".toCharArray()));
		//Status status = service.moveNode(session, "c87af3aa-f81f-4857-9ec2-83ac512d2215", "e75a82df-f6e9-4ec4-80f2-dc1b284b0b80");
		//System.out.println(status.getCode() + " - " + status.getDescription());
		
		//NodeInfo nodeInfo = service.getNode(session, "e75a82df-f6e9-4ec4-80f2-dc1b284b0b80", 5, true);
		NodeInfo nodeInfo = service.getNode(session, null, 100, true);
		printNodeInfo(nodeInfo, 0);
		
		//NodeAttribute a = new NodeAttribute("description", AttributeType.TEXT, "ЈОЦО Едитед 5");
		//Status status = service.renameNode(session, nodeInfo.getId(), "Јовица EDITED");
		//Status status = service.editNode(session, nodeInfo.getId(), new NodeAttribute[] {a});
		//Status status = service.deleteAttr(session, nodeInfo.getId(), "joco");
		//System.out.println();
		//System.out.println(status.getCode() + " - " + status.getDescription());
		//System.out.println();
		//nodeInfo = service.getNode(session, nodeInfo.getId(), 0, true);
		//printNodeInfo(nodeInfo, 0);
		session.logout();
	}

	private static void printNodeInfo(NodeInfo nodeInfo, int i) {
		if("jcr:system".equals(nodeInfo.getName())) {
			return;
		}
		System.out.println(space(i) + "_");
		System.out.println(space(i) + "Node["+nodeInfo.getPath()+"]: " + nodeInfo.getId());
		NodeAttribute[] attr = nodeInfo.getAttributes();
		if(attr != null) {
			for(NodeAttribute a : attr) {
				String v = a.getData().length()>30?a.getData().substring(0, 30)+"..." : a.getData();
				System.out.println(space(i) + " - A: " + a.getName() + "  = " + v);
			}
		}
		
		List<NodeInfo> childs = nodeInfo.getChilds();
		if(childs != null) {
			for(NodeInfo c : childs) {
				printNodeInfo(c, i+1);
			}
		}
		
	}

	private static String space(int s) {
		String rv="";
		for(int i=0; i<s; i++) {
			rv += "    ";
		}
		return rv;
	}

}
