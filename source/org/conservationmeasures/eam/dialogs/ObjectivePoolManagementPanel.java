/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ObjectiveId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class ObjectivePoolManagementPanel extends ObjectPoolManagementPanel
{
	public ObjectivePoolManagementPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(new ObjectivePoolTablePanel(projectToUse), 
				new ObjectivePropertiesPanel(projectToUse, actions, new ObjectiveId(BaseId.INVALID.asInt())));
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Tab|Objectives");
	}
	
	public Icon getIcon()
	{
		return new ObjectiveIcon();
	}
}
