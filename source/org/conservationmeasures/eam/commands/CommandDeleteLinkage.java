/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandDeleteLinkage extends Command
{
	public CommandDeleteLinkage(BaseId idToDelete)
	{
		id = idToDelete;
		wasFrom = new ModelNodeId(BaseId.INVALID.asInt());
		wasTo = new ModelNodeId(BaseId.INVALID.asInt());
	}
	
	public ModelNodeId getWasFromId()
	{
		return wasFrom;
	}
	
	public ModelNodeId getWasToId()
	{
		return wasTo;
	}

	public String toString()
	{
		return getCommandName() + ":" + getId() + "," + getWasFromId() + "," + getWasToId();
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		DiagramModel model = target.getDiagramModel();
		try
		{
			DiagramLinkage linkageToDelete = model.getLinkageById(id);
			wasFrom = linkageToDelete.getFromNode().getWrappedId();
			wasTo = linkageToDelete.getToNode().getWrappedId();
			target.deleteLinkage(id);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		try
		{
			target.insertLinkageAtId(getId(), getWasFromId(), getWasToId());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public BaseId getId()
	{
		return id;
	}


	public static final String COMMAND_NAME = "DeleteLinkage";

	BaseId id;
	ModelNodeId wasFrom;
	ModelNodeId wasTo;
}
