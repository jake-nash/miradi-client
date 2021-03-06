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

import javax.swing.JOptionPane;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.schemas.TaggedObjectSetSchema;
import org.miradi.utils.XmlUtilities2;
import org.miradi.views.ObjectsDoer;

public class CreateNamedTaggedObjectSetDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		return true;
	}

	@Override
	protected void doIt() throws Exception
	{
		if (!isAvailable())
			return;
		
		String newTagName = new JOptionPane().showInputDialog(EAM.text("Enter Tag Name"));
		newTagName = XmlUtilities2.getXmlEncoded(newTagName);
		boolean dialogWasCanceled = (newTagName == null);
		if (dialogWasCanceled)
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			CommandCreateObject createCommand = new CommandCreateObject(TaggedObjectSetSchema.getObjectType());
			getProject().executeCommand(createCommand);
			
			ORef newlyCreateTagRef = createCommand.getObjectRef();
			CommandSetObjectData setTagName = new CommandSetObjectData(newlyCreateTagRef, TaggedObjectSet.TAG_LABEL, newTagName);
			getProject().executeCommand(setTagName);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
}
