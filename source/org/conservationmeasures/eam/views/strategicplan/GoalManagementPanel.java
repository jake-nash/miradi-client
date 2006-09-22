package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.actions.ActionCreateGoal;
import org.conservationmeasures.eam.actions.ActionModifyGoal;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectpools.GoalPool;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class GoalManagementPanel extends ObjectManagementPanel
{
	public GoalManagementPanel(UmbrellaView viewToUse)
	{
		super(viewToUse, new GoalTableModel(viewToUse.getProject()), buttonActionClasses);
	}
	
	public Goal getSelectedGoal()
	{
		int row = table.getSelectedRow();
		if(row < 0)
			return null;
		
		GoalPool pool = getProject().getGoalPool();
		BaseId id = pool.getIds()[row];
		Goal goal = pool.find(id);
		return goal;
	}

	static final Class[] buttonActionClasses = {
		ActionCreateGoal.class,
		ActionModifyGoal.class,
		};


}
