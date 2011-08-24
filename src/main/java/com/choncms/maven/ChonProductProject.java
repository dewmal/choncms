package com.choncms.maven;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.eclipse.tycho.core.ArtifactDependencyVisitor;
import org.eclipse.tycho.core.ArtifactDependencyWalker;
import org.eclipse.tycho.core.TargetEnvironment;
import org.eclipse.tycho.core.TychoProject;
import org.eclipse.tycho.core.osgitools.AbstractArtifactDependencyWalker;
import org.eclipse.tycho.core.osgitools.DefaultReactorProject;
import org.eclipse.tycho.core.osgitools.EclipseApplicationProject;
import org.eclipse.tycho.model.ProductConfiguration;

@Component(role = TychoProject.class, hint = "chon-product")
public class ChonProductProject extends EclipseApplicationProject {
	protected ArtifactDependencyWalker newDependencyWalker(MavenProject project, TargetEnvironment environment) {
        final ProductConfiguration product = loadProduct(DefaultReactorProject.adapt(project));
        return new AbstractArtifactDependencyWalker(getTargetPlatform(project, environment), getEnvironments(project,
                environment)) {
            public void walk(ArtifactDependencyVisitor visitor) {
            	System.out
						.println("ChonProductProject.newDependencyWalker(...).new AbstractArtifactDependencyWalker() {...}.walk()");
            }
            
        };
    }
}
