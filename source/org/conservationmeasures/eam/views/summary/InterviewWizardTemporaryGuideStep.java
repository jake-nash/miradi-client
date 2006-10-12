/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class InterviewWizardTemporaryGuideStep extends InterviewWizardStep
{
	public InterviewWizardTemporaryGuideStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	
	public String getResourceFileName()
	{
		return HTML_FILENAME;
	}
	
	String HTML_FILENAME = "TemporaryGuideStep.html";
}
