package com.saran.testUtils;

import java.util.List;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.Result;
import com.codepine.api.testrail.model.ResultField;
import com.codepine.api.testrail.model.Run;

public class TestRailListener extends Utility{
	
	public TestRail testRail;
	public Run run;
	private int projectId;
	
	
	public TestRailListener(int projectId) {
		this.projectId=projectId;
		
	}
	
	public void intialize() {
		testRail = TestRail.builder(testRailProperties.getProperty("testRail.url"),
				testRailProperties.getProperty("testRail.username"),
				testRailProperties.getProperty("testRail.password")).build();
	}
	
	public void addTestRun() {

		int suiteid= Integer.parseInt(testRailProperties.getProperty("testRail.suiteId"));
		String testRunName=testRailProperties.getProperty("testRail.testRunName");
		
		run=testRail.runs().add(projectId, new Run().setSuiteId(suiteid).setName(testRunName)).execute();
		
		testRailProperties.setProperty("testRail.runId", String.valueOf(run.getId()));
		
	}
	
	public void addTestResult(int testRunID,int testCaseID,int statusID) {
		List<ResultField> customResultFields= testRail.resultFields().list().execute();
		testRail.results().addForCase(testRunID, testCaseID, new Result().setStatusId(statusID), customResultFields).execute();
		
	}
	
	public void closeTestRun() {
		testRail.runs().close(run.getId()).execute();
	}
	
	

}
