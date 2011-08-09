package org.choncms.maven.plugins;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;

/**
 * Says "Hi" to the user.
 * @goal copy-plugins
 */
public class CpPluginsMojo extends AbstractMojo
{
	/** @parameter expression="${session}" */
    private MavenSession session;
    
    public void execute() throws MojoExecutionException
    {
    	getLog().info("[JOCO]: Entering goal copy-plugins .... ");
    	List<MavenProject> projects = session.getTopLevelProject().getCollectedProjects();
    	
    	File plugins = new File(session.getCurrentProject().getBuild().getDirectory(), "plugins");
    	plugins.mkdirs();
    	
    	for(MavenProject p : projects) {
    		Artifact a = p.getArtifact();
    		if("eclipse-plugin".equals(a.getType())) {    			
    			File f = p.getArtifact().getFile();
    			try {
    				getLog().info("Including artifact: ................. " + f.getName());
					FileUtils.copyFileToDirectory(f, plugins);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	
    }
}