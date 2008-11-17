/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.umbrella.doers;

import java.io.File;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.project.ProjectUnzipper;
import org.miradi.project.ProjectZipper;
import org.miradi.utils.ZIPFileFilter;
import org.miradi.views.MainWindowDoer;
import org.miradi.views.umbrella.CreateProjectDialog;

public class CopyProjectToDoer extends MainWindowDoer
{
	public boolean isAvailable()
	{
		Project project = getProject();
		return project.isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		while(true)
		{
			CreateProjectDialog dlg = new CreateProjectDialog(getMainWindow(), EAM.text("Copy Project To"));
			if(!dlg.showCreateDialog(EAM.text("Button|Copy To")))
				return;

			File chosen = dlg.getSelectedFile();

			try
			{
				saveAsProject(chosen.getName());
			}
			catch(Exception e)
			{
				EAM.logException(e);
				throw new CommandFailedException(
						"CopyProjectToDoer: Possible Write Protected:" + e);
			}

			return;
		}
	}

	private void saveAsProject(String newProjectName) throws Exception
	{
		File projectDirToCopy = getProject().getDatabase().getTopDirectory();
		File homeDir = EAM.getHomeDirectory();
		File tempZipFile = File.createTempFile("$$$" + newProjectName, ZIPFileFilter.EXTENSION);
		ProjectZipper.createProjectZipFile(tempZipFile, newProjectName, projectDirToCopy);
		ProjectUnzipper.unzipToProjectDirectory(tempZipFile, homeDir, newProjectName);
		tempZipFile.delete();
	}
}
