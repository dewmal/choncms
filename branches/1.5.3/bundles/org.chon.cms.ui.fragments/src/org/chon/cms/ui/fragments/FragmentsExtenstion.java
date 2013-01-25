package org.chon.cms.ui.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import org.chon.cms.core.Extension;
import org.chon.cms.model.content.IContentNode;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONObject;
import org.json.XML;

public class FragmentsExtenstion implements Extension {
	public static final String FRAGMENT_PREFIX = "fragment.";
	
	public class Fragment {
		private String name;
		private String value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	private IContentNode node;
	private String prefix;
	private Application app;
	private JSONObject config;

	public FragmentsExtenstion(Application app, String prefix, IContentNode node, JSONObject config) {
		this.prefix = prefix;
		this.node = node;
		this.app = app;
		this.config = config;
	}
	
	
	private List<Fragment> getAllFragments() throws RepositoryException {
		List<Fragment> rv = new ArrayList<Fragment>();
		PropertyIterator pi = node.getNode().getProperties();
		while (pi.hasNext()) {
			Property p = pi.nextProperty();
			String propertyName = p.getName();
			if (propertyName.startsWith(FRAGMENT_PREFIX)) {
				Fragment f = new Fragment();
				f.setName(propertyName.substring(FRAGMENT_PREFIX.length()));
				f.setValue(p.getString());
				rv.add(f);
			}
		}
		return rv;
	}

	@Override
	public Map<String, Action> getAdminActons() {
		Map<String, Action> actions = new HashMap<String, Action>();
		actions.put(prefix + ".list", new Action() {

			@Override
			public String run(Application app, Request req, Response resp) {
				Map<String, Object> params = new HashMap<String, Object>();
				try {
					List<Fragment> fragments = getAllFragments();
					params.put("fragments", fragments);
					JSONObject fragmentsMapJSON = new JSONObject();
					for(Fragment f : fragments) {
						f.setValue(XML.escape(f.getValue()));
						fragmentsMapJSON.put(f.getName(), "1");
					}
					params.put("fragmentsMapJSON", fragmentsMapJSON.toString());
					params.put("fragmentPrefix", FRAGMENT_PREFIX);
					params.put("nodeId", node.getId());
					String af = req.get("activeFragment");
					String activeFragment = "false";  
					if(af != null) {
						for(int i=0; i<fragments.size(); i++) {
							if(af.equals(fragments.get(i).getName())) {
								activeFragment = String.valueOf(i);
								break;
							}
						}
					}
					params.put("activeFragment", activeFragment);
					params.put("prefix", prefix);
					return resp.formatTemplate(prefix + "/list.html", params);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "Oops, an error occured, " + e.getMessage();
				}
			}
		});
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		Map<String, Action> ajaxActions = new HashMap<String, Action>();
		ajaxActions.put(prefix + ".removeFragment", new Action() {
			
			@Override
			public String run(Application app, Request req, Response resp) {
				String name = req.get("name");
				try {
					Property prop = node.getNode().getProperty(FRAGMENT_PREFIX + name);
					prop.remove();
					node.getNode().getSession().save();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "Error: " + e.getMessage();
				}
				return "OK";
			}
		});
		ajaxActions.put(prefix + ".saveFragment", new Action() {
			
			@Override
			public String run(Application app, Request req, Response resp) {
				String name = req.get("name");
				try {
					Property prop = node.getNode().getProperty(FRAGMENT_PREFIX + name);
					prop.setValue(req.get("value"));
					prop.getSession().save();
				} catch (RepositoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "Error: " + e.getMessage();
				}
				return "OK";
			}
		});
		return ajaxActions;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode contentNode) {
		IContentNode fragmentsNode = this.node;
		return new FragmentsFO(app, req, resp, contentNode, fragmentsNode, config);
	}

}
