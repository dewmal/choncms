package org.chon.cms.core.model.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import org.apache.commons.io.IOUtils;
import org.chon.cms.model.ContentModel;
import org.chon.cms.model.content.IContentNode;
import org.chon.cms.model.content.base.BaseWWWContentNode;

public class FileContentNode extends BaseWWWContentNode {

	public FileContentNode(ContentModel model, Node node, IContentNode typeDesc) {
		super(model, node, typeDesc);
	}

	public Node getJCRContentNode() {
		try {
			return getNode().getNode("jcr:content");
		} catch (PathNotFoundException e) {
			e.printStackTrace();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getMimeType() {
		try {
			return getJCRContentNode().getProperty("jcr:mimeType").getString();
		} catch (ValueFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PathNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public InputStream getStream() throws ValueFormatException, RepositoryException {
		Property data = getJCRContentNode().getProperty("jcr:data");
		InputStream is = data.getBinary().getStream();
		return is;
	}

	public String getData() throws ValueFormatException, RepositoryException, IOException {
		InputStream is = getStream();
		StringWriter sw = new StringWriter();
		IOUtils.copy(is, sw, "UTF-8");
		IOUtils.closeQuietly(is);
		return sw.toString();
	}
}
