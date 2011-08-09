package org.chon.cms.admin.explorer.ext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.chon.cms.admin.explorer.ext.actions.Action_Init;
import org.chon.cms.admin.explorer.ext.actions.Action_Main;
import org.chon.cms.admin.explorer.ext.actions.AjaxAction_grid;
import org.chon.cms.admin.explorer.ext.actions.AjaxAction_search;
import org.chon.cms.admin.explorer.ext.actions.AjaxAction_treeItem;
import org.chon.cms.core.Extension;
import org.chon.cms.model.content.IContentNode;
import org.chon.jcr.client.service.model.NodeInfo;
import org.chon.web.api.Request;
import org.chon.web.api.Response;
import org.chon.web.mpac.Action;
import org.json.JSONObject;


public class ExplorerExtension implements Extension {
	public static final String SESSION_KEY = ExplorerExtension.class.getName();
	private Map<String, Action> actions = new HashMap<String, Action>();
	private Map<String, Action> ajaxActions = new HashMap<String, Action>();
	
	private JSONObject config;
	private List<String> jsIncList;
	
	public ExplorerExtension(JSONObject config, List<String> jsIncList) {
		this.config = config;
		this.jsIncList = jsIncList;
		
		//admin actions
		actions.put("explorer.main", new Action_Main(this));
		
		//ajax actions
		ajaxActions.put("explorer.init", new Action_Init(this));
		ajaxActions.put("explorer.treeItem", new AjaxAction_treeItem(this));
		AjaxAction_grid gridAjaxAction = new AjaxAction_grid(this);
		ajaxActions.put("explorer.grid", gridAjaxAction);
		ajaxActions.put("explorer.search", new AjaxAction_search(gridAjaxAction));
		
	}

	@Override
	public Map<String, Action> getAdminActons() {
		return actions;
	}

	@Override
	public Map<String, Action> getAjaxActons() {
		return ajaxActions;
	}

	public boolean notFiltered(Request req, NodeInfo ni) {
		JSONObject cfg = (JSONObject)req.attr(SESSION_KEY);
		String filter = cfg.optString("filter");
		if(filter==null || filter.trim().length()==0) {
			filter = this.getConfig().optString("filter");
		}
		if(filter==null || filter.trim().length()==0) {
			filter = req.get("filter");
		}
		
		//no filter
		if(filter==null || filter.trim().length()==0) return true;
		String[] filterArr = filter.split(",");
		if(isIn(ni.getType(), filterArr)) {
			return true;
		}
		return false;
	}

	private boolean isIn(String type, String[] filterArr) {
		for(String a : filterArr) {
			if(a.equals(type)) return true;
		}
		return false;
	}

	@Override
	public Object getTplObject(Request req, Response resp, IContentNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String dateToJSONString( Date date ) {
        
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );
        
        TimeZone tz = TimeZone.getTimeZone( "UTC" );
        
        df.setTimeZone( tz );

        String output = df.format( date );

        int inset0 = 9;
        int inset1 = 6;
        
        String s0 = output.substring( 0, output.length() - inset0 );
        String s1 = output.substring( output.length() - inset1, output.length() );

        String result = s0 + s1;

        result = result.replaceAll( "UTC", "+00:00" );
        
        return result;
        
    }

	public JSONObject getConfig() {
		return config;
	}
	
	public List<String> getJsIncList() {
		return jsIncList;
	}
}
