package com.choncms.maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

public abstract class AbstractCreatorMojo extends AbstractMojo {
    protected static final String RES_BASE_URL = "http://resources.megx.net/";
    protected String resourceLocation = RES_BASE_URL + "default";

    /**
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    protected abstract String getProjectType();

    protected Map<String, Object> getTemplateVariables() {
        Map<String, Object> templateVarsMap = new HashMap<String, Object>();
        templateVarsMap.put("project-groupId", "com.choncms");
        templateVarsMap.put("project-package", "com.choncms");
        templateVarsMap.put("project-name", "Chon");
        templateVarsMap.put("project-version", "0.0.1-SNAPSHOT");

        templateVarsMap.put("project-parent-groupId", "com.choncms");
        templateVarsMap.put("project-parent-artifactId", "bundles");
        templateVarsMap.put("project-parent-version", "0.0.1-SNAPSHOT");
        templateVarsMap.put("project-parent-pom-relativePath", "../pom.xml");
        return templateVarsMap;
    }

    /**
     * @parameter expression="${basedir}" default-value="${user.dir}"
     */
    protected String basedir;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            Map<String, Object> tplVars = getTemplateVariables();
            String projectStructrueXml = getProjectType() + ".structure.xml";
            Resource project = ProjectStructure.read(projectStructrueXml,
                    tplVars, resourceLocation);
            File base = new File(basedir);
            base.mkdirs();
            project.create(base);
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating project: ", e);
        }
    }

    protected String getValue(String description, String defVal)
            throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Value for '" + description
                + "', default (leave blank): '" + defVal + "': ");
        String line = br.readLine();
        if (line == null || line.trim().length() == 0) {
            return defVal;
        }
        return line;
    }

}
