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
package org.miradi.wizard.diagram;

import org.miradi.actions.jump.ActionJumpDiagramWizardHumanWelfareTargetsStep;
import org.miradi.main.menu.ProcessSteps;
import org.miradi.wizard.DiagramWizardStep;
import org.miradi.wizard.WizardPanel;

public class DiagramWizardHumanWelfareTargetsStep extends DiagramWizardStep
{
	public DiagramWizardHumanWelfareTargetsStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);
	}
	public String getProcessStepTitle()
	{
		return ProcessSteps.PROCESS_STEP_1B;
	}

	public Class getAssociatedActionClass()
	{
		return ActionJumpDiagramWizardHumanWelfareTargetsStep.class;
	}
	
	@Override
	public String getSubHeading()
	{
		return null;
	}
}