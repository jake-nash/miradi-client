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
package org.miradi.views.library;

import javax.swing.JLabel;

import org.miradi.main.MainWindow;
import org.miradi.main.MiradiToolBar;
import org.miradi.project.Project;
import org.miradi.utils.MiradiResourceImageIcon;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.TabbedView;
import org.miradi.wizard.WizardPanel;

public class LibraryView extends TabbedView
{
	public LibraryView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	@Override
	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.LIBRARY_VIEW_NAME;
	}

	@Override
	public MiradiToolBar createToolBar()
	{
		return new LibraryToolBar(getActions());
	}

	@Override
	public void createTabs() throws Exception
	{
		String[] demoMaps = 
		{
			"Bay",
		};
		
		for(int i = 0; i < demoMaps.length; ++i)
		{
			String mapName = demoMaps[i] + ".jpg";
			JLabel image = new JLabel(new MiradiResourceImageIcon("images/" + mapName));
			image.setName(demoMaps[i]);
			addTab(image.getName(), new MiradiScrollPane(image));
		}
		
	}

	@Override
	public void deleteTabs() throws Exception
	{
		// lightweight tabs...nothing to dispose yet
		super.deleteTabs();
	}

	public WizardPanel createWizardPanel() throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}


}
