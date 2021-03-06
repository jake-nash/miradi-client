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

package org.miradi.views.diagram.doers;

import org.miradi.diagram.arranger.MeglerArranger;
import org.miradi.dialogs.base.ProgressDialog;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.UserCanceledException;
import org.miradi.main.EAM;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;
import org.miradi.utils.MiradiBackgroundWorkerThread;
import org.miradi.utils.ProgressInterface;
import org.miradi.views.ViewDoer;

public class ArrangeConceptualModelDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!isInDiagram())
			return false;
		
		return ConceptualModelDiagram.is(getCurrentDiagramObject().getType());
	}

	@Override
	protected void doIt() throws Exception
	{
		if(!isAvailable())
			return;
		
		ProgressDialog progressDialog = new ProgressDialog(getMainWindow(), EAM.text("Arranging Diagram"));
		
		Worker worker = new Worker(getProject(), getCurrentDiagramObject(), progressDialog);
		getDiagramView().getCurrentDiagramComponent().setVisible(false);
		try
		{
			progressDialog.doWorkInBackgroundWhileShowingProgress(worker);
		}
		catch (UserCanceledException e)
		{
			EAM.notifyDialog(EAM.text("Arrange operation was canceled!"));
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getDiagramView().getCurrentDiagramComponent().setVisible(true);
		}
	}
	
	static class Worker extends MiradiBackgroundWorkerThread
	{
		public Worker(Project projectToUse, DiagramObject diagramToArrange, ProgressInterface progressToNotify)
		{
			super(progressToNotify);

			project = projectToUse;
			diagram = diagramToArrange;
		}
		
		@Override
		protected void doRealWork() throws Exception
		{
			getProject().executeBeginTransaction();
			try
			{
				MeglerArranger meglerArranger = new MeglerArranger(diagram, getProgressIndicator());
				if(!meglerArranger.arrange())
				{
					EAM.notifyDialog("The auto-arrange process was stopped before it completed, \n" +
							"potentially leaving factors in undesirable locations.  \n" +
							"If this is the case, use Edit/Undo to restore the diagram to its original state.");
				}
			}
			catch(UserCanceledException e)
			{
				throw e;
			}
			catch(Exception e)
			{
				throw new CommandFailedException(e);
			}
			finally
			{
				getProject().executeEndTransaction();
			}
		}
		
		public Project getProject()
		{
			return project;
		}
		
		private Project project;
		private DiagramObject diagram;
	}

	private DiagramObject getCurrentDiagramObject()
	{
		DiagramObject currentDiagramObject = getDiagramView().getCurrentDiagramObject();
		return currentDiagramObject;
	}
}
