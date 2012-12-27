package com.choncms.webpage.forms.workflow;

public class WorkflowResultOK implements WorkflowResult {
	public static WorkflowResultOK SUCCESS = new WorkflowResultOK();

	@Override
	public boolean hasError() {
		return false;
	}

	@Override
	public Throwable getError() {
		return null;
	}
}
