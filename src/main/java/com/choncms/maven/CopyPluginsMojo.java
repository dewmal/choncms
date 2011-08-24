package com.choncms.maven;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import java.io.IOException;
import java.util.Map;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.tycho.ArtifactDescriptor;
import org.eclipse.tycho.ReactorProject;
import org.eclipse.tycho.core.TargetPlatform;
import org.eclipse.tycho.core.TychoProject;
import org.eclipse.tycho.core.osgitools.DefaultReactorProject;
import org.eclipse.tycho.core.utils.TychoProjectUtils;
import org.eclipse.tycho.model.PluginRef;
import org.eclipse.tycho.model.ProductConfiguration;

/**
 * Goal which copies bundles to plugins output directory.
 * 
 * @goal copy-plugins
 * 
 * @phase package
 */
public class CopyPluginsMojo extends AbstractMojo {
	private static final String PLUGINS_DIR = "/plugins";
	private static final String DROPINS_DIR = "/dropins";
    
	 /**
     * @parameter
     */
	private File targetDir;
	
	/** @parameter expression="${session}" */
	protected MavenSession session;

	/** @parameter expression="${project}" */
	protected MavenProject project;

	
	/** @component */
	protected PlexusContainer plexus;

	@Requirement(role=TychoProject.class)
	Map<String, TychoProject> projectTypes;
	
	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File outputDirectory;

	public void execute() throws MojoExecutionException {
		File plugins_dir = new File(outputDirectory, PLUGINS_DIR);
		if (!plugins_dir.exists()) {
			plugins_dir.mkdirs();
		}
		getLog().info(" *** Building product. Copying plying to: " + plugins_dir.getAbsolutePath());
		
		File dropins_dir = new File(outputDirectory, DROPINS_DIR);
		if (!dropins_dir.exists()) {
			dropins_dir.mkdirs();
		}
		
			TargetPlatform tp = TychoProjectUtils.getTargetPlatform(project);
			
				
		//LinkedList<TargetPlatform> pList = getPlatformsForSessionProjects();
		ProductConfiguration product = loadProduct(DefaultReactorProject.adapt(project));
		
		for (PluginRef ref : product.getPlugins()) {
			ArtifactDescriptor artifact = tp.getArtifact(org.eclipse.tycho.ArtifactKey.TYPE_ECLIPSE_PLUGIN, ref.getId(), ref.getVersion()); 
//					getArtifact(pList,
//					org.eclipse.tycho.ArtifactKey.TYPE_ECLIPSE_PLUGIN,
//					ref.getId(), ref.getVersion());
			if (artifact == null) {
				throw new MojoExecutionException(" MISSING ARTIFACT: " + ref.getId());
			} else {
				File location = getArtifactLocation(artifact);
				copyArtifact(location, plugins_dir);
			}
		}
		if(targetDir != null) {
			targetDir.mkdirs();
			try {
				FileUtils.copyDirectoryStructure(outputDirectory, targetDir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void copyArtifact(File location, File plugins_dir)
			throws MojoExecutionException {
		try {
			FileUtils.copyFileToDirectory(location, plugins_dir);
		} catch (IOException e) {
			throw new MojoExecutionException("Error copying file ", e);
		}
	}

	private File getArtifactLocation(ArtifactDescriptor artifact) {
		File location;
		ReactorProject bundleProject = artifact.getMavenProject();
		if (bundleProject != null) {
		    location = bundleProject.getArtifact(artifact.getClassifier());
		    if (location == null) {
		        throw new IllegalStateException(bundleProject.getId()
		                + " does not provide an artifact with classifier '" + artifact.getClassifier() + "'");
		    }
		    if (location.isDirectory()) {
		        throw new RuntimeException("Bundle project " + bundleProject.getId()
		                + " artifact is a directory. The build should at least run ``package'' phase.");
		    }
		} else {
		    location = artifact.getLocation();
		}
		return location;
	}

	protected ProductConfiguration loadProduct(final ReactorProject project) {
		File file = new File(project.getBasedir(), project.getArtifactId()
				+ ".product");
		try {
			return ProductConfiguration.read(file);
		} catch (IOException e) {
			throw new RuntimeException(
					"Could not read product configuration file "
							+ file.getAbsolutePath(), e);
		}
	}
}
