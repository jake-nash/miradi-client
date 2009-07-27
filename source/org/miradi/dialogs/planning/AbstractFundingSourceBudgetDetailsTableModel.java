/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogs.planning;

import java.awt.Color;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.planning.propertiesPanel.AssignmentDateUnitsTableModel;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FundingSource;
import org.miradi.project.Project;
import org.miradi.utils.OptionalDouble;

abstract public class AbstractFundingSourceBudgetDetailsTableModel extends AssignmentDateUnitsTableModel
{
	public AbstractFundingSourceBudgetDetailsTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse, String treeModelIdentifierAsTagToUse) throws Exception
	{
		super(projectToUse, providerToUse, treeModelIdentifierAsTagToUse);
	}
	
	@Override
	protected OptionalDouble getOptionalDoubleData(BaseObject baseObject, DateUnit dateUnit) throws Exception
	{
		TimePeriodCosts timePeriodCosts = getProjectTotalTimePeriodCostFor(dateUnit);
		if (FundingSource.is(baseObject))
		{
			ORefSet fundingSoruceRefsToRetain = new ORefSet(baseObject);
			timePeriodCosts.filterFundingSourcesExpenses(fundingSoruceRefsToRetain);
			timePeriodCosts.filterFundingSourcesWorkUnits(fundingSoruceRefsToRetain);
		}
		
		return calculateValue(timePeriodCosts);
	}
		
	@Override
	public boolean isCurrencyColumn(int column)
	{
		return true;
	}
	
	@Override
	public Color getCellBackgroundColor(int column)
	{
		DateUnit dateUnit = getDateUnit(column);
		return AppPreferences.getBudgetDetailsBackgroundColor(dateUnit);
	}
	
	@Override
	protected boolean isEditableModel()
	{
		return false;
	}

	@Override
	protected boolean isAssignmentForModel(Assignment assignment)
	{
		return false;
	}

	@Override
	protected String getAssignmentsTag()
	{
		 throw new RuntimeException(getErrorMessage());
	}
	
	@Override
	protected int getAssignmentType()
	{
		throw new RuntimeException(getErrorMessage());
	}
	
	@Override
	protected CommandSetObjectData createAppendAssignmentCommand(BaseObject baseObjectForRowColumn, ORef assignmentRef)	throws Exception
	{
		throw new RuntimeException(getErrorMessage());
	}

	private String getErrorMessage()
	{
		return EAM.text("This model is not for a single assignment and has no assignment information");
	}
}
