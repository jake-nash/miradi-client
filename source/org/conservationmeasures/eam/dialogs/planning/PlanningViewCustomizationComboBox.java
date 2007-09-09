/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.PlanningViewCustomizationQuestion;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;

public class PlanningViewCustomizationComboBox extends PlanningViewComboBox
{
	public PlanningViewCustomizationComboBox(Project projectToUse)
	{
		super(projectToUse, new PlanningViewCustomizationQuestion(projectToUse).getChoices());
	}
	
	public CodeList getColumnList() throws Exception
	{
		return getList(PlanningViewConfiguration.TAG_COL_CONFIGURATION);
	}

	public CodeList getRowList() throws Exception
	{
		return getList(PlanningViewConfiguration.TAG_ROW_CONFIGURATION);
	}
	
	private CodeList getList(String tag) throws Exception
	{
		if (isInvalidConfiguration())
			return new CodeList();
		
		return new CodeList(findConfiguration().getData(tag)); 
	}
	
	private PlanningViewConfiguration findConfiguration() throws Exception
	{
		return (PlanningViewConfiguration) getProject().findObject(getCurrentConfigurationRef());
	}
	
	private ORef getCurrentConfigurationRef() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		return viewData.getORef(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
	}
	
	private boolean isInvalidConfiguration() throws Exception
	{
		if (getCurrentConfigurationRef().isInvalid())
			return true;
		
		return false;
	}
	
	public String getPropertyName()
	{
		return PlanningView.CUSTOMIZABLE_COMBO;
	}
	
	public void setSelectionFromProjectSetting()
	{
		
	}
	
	public String getChoiceTag()
	{
		return ViewData.TAG_PLANNING_CUSTOM_PLAN_REF;
	}
}
