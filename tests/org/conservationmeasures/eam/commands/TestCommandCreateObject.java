/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestCommandCreateObject extends EAMTestCase
{
	public TestCommandCreateObject(String name)
	{
		super(name);
	}

	public void testRedo() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		
		try
		{
			int type = ObjectType.TASK;
			String extraInfo = "Hello";
			CommandCreateObject cmd = new CommandCreateObject(type, extraInfo);
			assertEquals("already has an id?", BaseId.INVALID, cmd.getCreatedId());
			assertEquals("didn't memorize extraInfo?", extraInfo, cmd.getExtraInfo());
			cmd.execute(project);
			BaseId createdId = cmd.getCreatedId();
			int highestId = project.getAnnotationIdAssigner().getHighestAssignedId();
			assertEquals("didn't assign an id?", highestId, createdId.asInt());
			cmd.undo(project);
			assertEquals("lost id?", highestId, cmd.getCreatedId().asInt());
			cmd.execute(project);
			assertEquals("didn't keep same id?", createdId, cmd.getCreatedId());
		}
		finally
		{
			project.close();
		}
	}
}
