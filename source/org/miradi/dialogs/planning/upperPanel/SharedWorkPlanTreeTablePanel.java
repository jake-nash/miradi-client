/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.planning.upperPanel;

import org.miradi.actions.*;
import org.miradi.dialogs.planning.SharedWorkPlanRowColumnProvider;
import org.miradi.dialogs.planning.propertiesPanel.AbstractFixedHeightDirectlyAboveTreeTablePanel;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNodeAlwaysExpanded;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.questions.SharedWorkPlanVisibleRowsQuestion;

import javax.swing.*;

public class SharedWorkPlanTreeTablePanel extends AbstractWorkPlanTreeTablePanel
{
	protected SharedWorkPlanTreeTablePanel(MainWindow mainWindowToUse,
										   PlanningTreeTable treeToUse,
										   PlanningTreeTableModel modelToUse,
										   Class[] buttonActions,
										   PlanningTreeRowColumnProvider rowColumnProvider,
										   AbstractFixedHeightDirectlyAboveTreeTablePanel treeTableHeaderPanel) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonActions, rowColumnProvider, treeTableHeaderPanel);
	}

	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse) throws Exception
	{
		SharedWorkPlanRowColumnProvider rowColumnProvider = new SharedWorkPlanRowColumnProvider(mainWindowToUse.getProject());
		PlanningTreeRootNodeAlwaysExpanded rootNode = new PlanningTreeRootNodeAlwaysExpanded(mainWindowToUse.getProject());
		SharedWorkPlanTreeTableModel model = new SharedWorkPlanTreeTableModel(mainWindowToUse.getProject(), rootNode, rowColumnProvider);
		SharedWorkPlanningTreeTableWithVisibleRootNode treeTable = new SharedWorkPlanningTreeTableWithVisibleRootNode(mainWindowToUse, model);
		AbstractFixedHeightDirectlyAboveTreeTablePanel treeTableHeaderPanel = new AbstractFixedHeightDirectlyAboveTreeTablePanel();

		return new SharedWorkPlanTreeTablePanel(mainWindowToUse, treeTable, model, getButtonActions(), rowColumnProvider, treeTableHeaderPanel);
	}

	@Override
	protected String getTextForCustomizeTableFilter(String workPlanBudgetMode)
	{
		return SharedWorkPlanVisibleRowsQuestion.getTextForChoice(workPlanBudgetMode);
	}

	@Override
	protected Icon getIconForCustomizeTableFilter(String workPlanBudgetMode)
	{
		return SharedWorkPlanVisibleRowsQuestion.getIconForChoice(workPlanBudgetMode);
	}

	@Override
	public void updateStatusBar()
	{
		super.updateStatusBar();

		SharedWorkPlanTreeTableModel treeTableModel = (SharedWorkPlanTreeTableModel) getTreeTableModel();
		if (treeTableModel != null)
		{
			if (treeTableModel.treeHasSubTasks())
			{
				String currentStatus = getMainWindow().getMainStatusBar().getWarningStatus();
				String newStatus = currentStatus + SharedWorkPlanTreeTableModel.HAS_HIDDEN_SUB_TASKS_DOUBLE_ASTERISK + EAM.text("Sub-tasks are not shown; use the legacy Work Plan tab to view sub-tasks");
				getMainWindow().setStatusBarWarningMessage(newStatus);
			}
		}
	}

	private static Class[] getButtonActions()
	{
		return new Class[] {
				ActionExpandAllRows.class,
				ActionTreeNodeUp.class,
				ActionSharedWorkPlanningCreationMenu.class,
				ActionSharedWorkPlanBudgetCustomizeTableEditor.class,

				ActionCollapseAllRows.class,
				ActionTreeNodeDown.class,
				ActionDeletePlanningViewTreeNode.class,
				ActionFilterWorkPlanByProjectResource.class,
		};
	}
}