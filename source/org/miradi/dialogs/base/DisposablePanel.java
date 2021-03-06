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
package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.Vector;

import javax.swing.JPanel;

import org.martus.swing.Utilities;
import org.miradi.actions.ObjectsAction;
import org.miradi.main.MainWindow;
import org.miradi.utils.ObjectsActionButton;
import org.miradi.views.umbrella.ObjectPicker;

public class DisposablePanel extends JPanel
{
	public DisposablePanel()
	{
		this(new BorderLayout());
	}
	
	public DisposablePanel(LayoutManager2 layoutToUse)
	{
		super(layoutToUse);
		objectsActionButtonsToDispose = new Vector<ObjectsActionButton>();
	}
	
	public ObjectsActionButton createObjectsActionButton(ObjectsAction action, ObjectPicker picker)
	{
		ObjectsActionButton objectsActionButton = new ObjectsActionButton(action, picker);
		objectsActionButtonsToDispose.add(objectsActionButton);
		return objectsActionButton;
	}
	
	public void dispose()
	{
		for(int i = 0; i < objectsActionButtonsToDispose.size(); ++i)
		{
			objectsActionButtonsToDispose.get(i).dispose();
		}
		
		objectsActionButtonsToDispose.removeAllElements();
	}
	
	public void becomeActive()
	{
	}

	public void becomeInactive()
	{
	}

	public Class getJumpActionClass()
	{
		return null;
	}
	
	public void showModalDialogWithScrollBar(MainWindow mainWindow, String dialogTitle, Dimension preferredSize)
	{
		ModalDialogWithClose dialog = new ModalDialogWithClose(mainWindow, dialogTitle);
		dialog.setScrollableMainPanel(this);
		showDialog(dialog, preferredSize);
	}
	
	public void showModalDialogWithoutScrollBar(MainWindow mainWindow, String dialogTitle, Dimension preferredSize)
	{
		ModalDialogWithClose dialog = new ModalDialogWithClose(mainWindow, dialogTitle);
		dialog.setMainPanel(this);
		showDialog(dialog, preferredSize);
	}

	private void showDialog(DialogWithDisposablePanel dialog, Dimension preferredSize)
	{
		
		if (preferredSize != null)
			dialog.setPreferredSize(preferredSize);
		
		Utilities.centerDlg(dialog);
		dialog.becomeActive();
		dialog.setVisible(true);
	}
	
	protected void disposePanel(DisposablePanel disposablePanel)
	{
		if (disposablePanel != null)
		{
			disposablePanel.dispose();
		}
	}
	
	private Vector<ObjectsActionButton> objectsActionButtonsToDispose;
}
