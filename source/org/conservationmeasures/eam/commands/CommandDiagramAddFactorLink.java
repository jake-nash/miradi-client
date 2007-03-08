/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.commands;

import java.util.HashMap;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandDiagramAddFactorLink extends Command
{
	public CommandDiagramAddFactorLink(DiagramFactorLinkId diagramFactorLinkToUse)
	{
		diagramFactorLinkIdToAdd = diagramFactorLinkToUse;
		insertedDiagramFactorLinkId = new DiagramFactorLinkId(BaseId.INVALID.asInt());
	}
	
	public DiagramFactorLinkId getDiagramFactorLinkId()
	{
		return insertedDiagramFactorLinkId;
	}

	public DiagramFactorLinkId getFactorLinkId()
	{
		return diagramFactorLinkIdToAdd;
	}

	public String toString()
	{
		return getCommandName() + ": " + insertedDiagramFactorLinkId + "," + diagramFactorLinkIdToAdd ;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			insertedDiagramFactorLinkId = target.addLinkToDiagram(diagramFactorLinkIdToAdd);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public Command getReverseCommand() throws CommandFailedException
	{
		CommandDiagramRemoveFactorLink commandDiagramRemoveFactorLink = new CommandDiagramRemoveFactorLink(insertedDiagramFactorLinkId);
		commandDiagramRemoveFactorLink.setFactorLinkId(diagramFactorLinkIdToAdd);
		return commandDiagramRemoveFactorLink;
	}

	public void setDiagramFactorLinkId(DiagramFactorLinkId insertedDiagramFactorLinkIdToUse)
	{
		insertedDiagramFactorLinkId = insertedDiagramFactorLinkIdToUse;
	}

	public HashMap getLogData()
	{
		HashMap dataPairs = new HashMap();
		dataPairs.put(FactorLinkId.class.getSimpleName(), diagramFactorLinkIdToAdd);
		return dataPairs;
	}
	
	public static final String COMMAND_NAME = "CommandDiagramAddFactorLink";

	DiagramFactorLinkId diagramFactorLinkIdToAdd;
	DiagramFactorLinkId insertedDiagramFactorLinkId;
}
