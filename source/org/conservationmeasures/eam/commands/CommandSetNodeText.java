/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.Project;
import org.jgraph.graph.GraphConstants;

public class CommandSetNodeText extends Command
{
	public CommandSetNodeText(int idToUpdate, String newText)
	{
		id = idToUpdate;
		text = newText;
	}

	public CommandSetNodeText(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		text = dataIn.readUTF();
	}
	
	public static String getCommandName()
	{
		return "SetNodeText";
	}
	
	public Object execute(Project target)
	{
		DiagramModel model = target.getDiagramModel();
		Node node = model.getNodeById(getId());
		Map map = node.getMap();
		GraphConstants.setValue(map, getText());
		model.updateNode(node);
		return null;
	}

	public void writeTo(OutputStream out) throws IOException
	{
		DataOutputStream dataOut = new DataOutputStream(out);
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(getId());
		dataOut.writeUTF(getText());
	}

	int getId()
	{
		return id;
	}
	
	String getText()
	{
		return text;
	}

	int id;
	String text;
}
