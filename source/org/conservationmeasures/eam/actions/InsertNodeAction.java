/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Point;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDiagramMove;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;
import org.conservationmeasures.eam.main.Project;

public abstract class InsertNodeAction extends MainWindowAction
{
	public InsertNodeAction(MainWindow mainWindowToUse, String label, Point location)
	{
		super(mainWindowToUse, label);
		createAt = location;
	}

	abstract public String getInitialText();

	protected void doInsert(Command insertCommand)
	{
		Project project = getMainWindow().getProject();
		Node insertedNode = (Node)project.executeCommand(insertCommand);
		int id = project.getDiagramModel().getNodeId(insertedNode);
	
		Command moveCommand = new CommandDiagramMove(createAt.x, createAt.y, new int[] {id});
		project.executeCommand(moveCommand);
	
		Command setTextCommand = new CommandSetNodeText(id, getInitialText());
		project.executeCommand(setTextCommand);
	}

	Point createAt;
}
