/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ObjectiveIds;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandSetNodeObjectives extends Command 
{
	public CommandSetNodeObjectives(BaseId idToUpdate, ObjectiveIds objectivesToUse)
	{
		id = idToUpdate;
		objectives = objectivesToUse;
		previousObjectives = null;
	}

	public CommandSetNodeObjectives(DataInputStream dataIn) throws IOException
	{
		id = new BaseId(dataIn.readInt());
		objectives = new ObjectiveIds(dataIn);
		previousObjectives = new ObjectiveIds(dataIn);
	
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId().asInt());
		objectives.writeDataTo(dataOut);
		previousObjectives.writeDataTo(dataOut);
	}

	public String getCommandName() 
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousObjectives = doSetObjectives(target, getCurrentObjectives(), getPreviousObjectives()); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetObjectives(target, getPreviousObjectives(), getCurrentObjectives());
	}
	
	private ObjectiveIds doSetObjectives(Project target, ObjectiveIds desiredObjectives, ObjectiveIds expectedObjectives) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			ObjectiveIds oldObjectiveIds = node.getObjectives();
			if(expectedObjectives != null && !oldObjectiveIds.equals(expectedObjectives))
				throw new Exception("CommandSetObjective expected " + expectedObjectives + " but was " + oldObjectiveIds);
			target.setObjectives(getId(), desiredObjectives);
			return oldObjectiveIds;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public String toString()
	{
		return getCommandName() + ": " + id + ", " + objectives + ", " + previousObjectives;
	}

	public ObjectiveIds getCurrentObjectives()
	{
		return objectives;
	}
	
	public ObjectiveIds getPreviousObjectives()
	{
		return previousObjectives;
	}

	BaseId getId()
	{
		return id;
	}
	
	public static final String COMMAND_NAME = "SetObjectives";

	BaseId id;
	ObjectiveIds objectives;
	ObjectiveIds previousObjectives;
}
