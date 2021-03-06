/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.views.workplan;

import javax.swing.Icon;

import org.miradi.actions.ActionCreateAccountingCode;
import org.miradi.actions.ActionDeleteAccountingCode;
import org.miradi.actions.ActionImportAccountingCodes;
import org.miradi.dialogs.planning.AccountingCodeRowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.icons.AccountingCodeIcon;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public class AccountingCodeManagementConfiguration extends WorkPlanManagementPanelConfiguration
{
	public AccountingCodeManagementConfiguration(Project projectToUse)
	{
		super(projectToUse);
	}

	@Override
	public Class[] getButtonActions()
	{
		return new Class[] {
				ActionCreateAccountingCode.class, 
				ActionDeleteAccountingCode.class,
				ActionImportAccountingCodes.class,
		};
	}

	@Override
	public Icon getIcon()
	{
		return new AccountingCodeIcon();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Accounting Codes");
	}

	@Override
	public WorkPlanCategoryTreeRowColumnProvider getRowColumnProvider() throws Exception
	{
		return new AccountingCodeRowColumnProvider(getProject());
	}

	@Override
	public String getUniqueTreeTableIdentifier()
	{
		return UNIQUE_TREE_TABLE_IDENTIFIER;
	}

	private static final String UNIQUE_TREE_TABLE_IDENTIFIER = "AccountingCodeTreeTableModel";
}
