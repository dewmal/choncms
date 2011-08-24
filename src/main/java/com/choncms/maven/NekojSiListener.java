package com.choncms.maven;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;

//@Component(role = AbstractMavenLifecycleParticipant.class, hint = "NekojSiListener")
public class NekojSiListener extends AbstractMavenLifecycleParticipant {

	@Override
	public void afterProjectsRead(MavenSession session)
			throws MavenExecutionException {
		System.out.println("NekojSiListener.afterProjectsRead()");
	}
}
