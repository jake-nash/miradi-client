/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.noproject;

import java.io.File;

import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiOptionPane;
import org.martus.util.DirectoryLock;
import org.martus.util.DirectoryUtils;

public class CopyProject
{
	static public void doIt(MainWindow mainWindow, File projectToCopy) throws Exception 
	{
		
		if(!ProjectServer.isExistingProject(projectToCopy))
		{
			EAM.notifyDialog(EAM.text("Project does not exist: ") + projectToCopy.getName());
			return;
		}
		
		DirectoryLock directoryLock = new DirectoryLock();

		if (!directoryLock.getDirectoryLock(projectToCopy))
		{
			EAM.notifyDialog(EAM.text("Unable to copy this project because it is in use by another copy of this application:\n") +  projectToCopy.getName());
			return;
		}

		try
		{
			String newName = UiOptionPane.showInputDialog("Enter New Project Name");
			if (newName == null)
				return;
			
			Project.validateNewProject(newName);

			String[] body = {EAM.text("Are you sure you want to copy project: "), 
					projectToCopy.getName(),
			};
			String[] buttons = {EAM.text("Copy"), EAM.text("Cancel"), };
			if(!EAM.confirmDialog(EAM.text("Copy Project"), body, buttons))
				return;
		
			directoryLock.close();
			File newFile = new File(projectToCopy.getParentFile(),newName);
			DirectoryUtils.copyDirectoryTree(projectToCopy, newFile);
		}
		catch (Exception e)
		{
			EAM.notifyDialog("Copy Failed:" +e.getMessage());
		}
		finally
		{
			directoryLock.close();
		}
	}


}