package com.choncms.webpage.forms.workflow;

public class WorkflowResultError implements WorkflowResult {
	public static WorkflowResultError ERROR = new WorkflowResultError();
	
	private Throwable ex;
	
	public WorkflowResultError() {
	}
	
	public WorkflowResultError(Throwable ex) {
		this.ex = ex;
	}

	@Override
	public boolean hasError() {
		return true;
	}

	@Override
	public Throwable getError() {
		return ex;
	}
}
