/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.awt.Point;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactor;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactorLink;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.FactorDataHelper;
import org.conservationmeasures.eam.diagram.cells.FactorDataMap;
import org.conservationmeasures.eam.diagram.cells.FactorLinkDataMap;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.views.diagram.InsertFactorLinkDoer;

public class FactorCommandHelper
{
	public FactorCommandHelper(Project projectToUse)
	{
		project = projectToUse;
	}

	public CommandCreateObject createFactorAnd3333DiagramFactor(FactorType nodeType) throws Exception
	{
		CreateFactorParameter extraInfo = new CreateFactorParameter(nodeType);
		CommandCreateObject createModelNode = new CommandCreateObject(ObjectType.FACTOR, extraInfo);
		executeCommand(createModelNode);

		FactorId modelNodeId = new FactorId(createModelNode.getCreatedId().asInt());
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(modelNodeId);
		CommandCreateObject createDiagramFactor = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		executeCommand(createDiagramFactor);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactor.getCreatedId();
		CommandDiagramAddFactor commandInsertNode = new CommandDiagramAddFactor(diagramFactorId);
		executeCommand(commandInsertNode);
		
		Factor factor = project.findNode(modelNodeId);
		Command[] commandsToAddToView = getProject().getCurrentViewData().buildCommandsToAddNode(factor.getRef());
		for(int i = 0; i < commandsToAddToView.length; ++i)
			executeCommand(commandsToAddToView[i]);
		
		return createDiagramFactor;
	}
	public CommandCreateObject createFactorAndDiagramFactor(int objectType) throws Exception
	{
		CommandCreateObject createModelNode = new CommandCreateObject(objectType);
		executeCommand(createModelNode);

		FactorId modelNodeId = new FactorId(createModelNode.getCreatedId().asInt());
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(modelNodeId);
		CommandCreateObject createDiagramFactor = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		executeCommand(createDiagramFactor);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactor.getCreatedId();
		CommandDiagramAddFactor commandInsertNode = new CommandDiagramAddFactor(diagramFactorId);
		executeCommand(commandInsertNode);
		
		Factor factor = project.findNode(modelNodeId);
		Command[] commandsToAddToView = getProject().getCurrentViewData().buildCommandsToAddNode(factor.getRef());
		for(int i = 0; i < commandsToAddToView.length; ++i)
			executeCommand(commandsToAddToView[i]);
		
		return createDiagramFactor;
	}

	

	public void pasteFactorsAndLinksIntoProject(TransferableEamList list, Point startPoint) throws Exception
	{
		executeCommand(new CommandBeginTransaction());
		FactorDataHelper dataHelper = new FactorDataHelper(project.getAllDiagramFactorIds());
		pasteFactorsIntoProject(list, startPoint, dataHelper);
		pasteLinksIntoProject(list, dataHelper);
		executeCommand(new CommandEndTransaction());
	}
	
	public void pasteFactorsOnlyIntoProject(TransferableEamList list, Point startPoint) throws Exception
	{
		executeCommand(new CommandBeginTransaction());
		FactorDataHelper dataHelper = new FactorDataHelper(project.getAllDiagramFactorIds());
		pasteFactorsIntoProject(list, startPoint, dataHelper);
		executeCommand(new CommandEndTransaction());
	}

	private void pasteFactorsIntoProject(TransferableEamList list, Point startPoint, FactorDataHelper dataHelper) throws Exception 
	{
		FactorDataMap[] nodes = list.getArrayOfFactorDataMaps();
		for (int i = 0; i < nodes.length; i++) 
		{
			FactorDataMap nodeData = nodes[i];
			DiagramFactorId originalDiagramNodeId = new DiagramFactorId(nodeData.getId(DiagramFactor.TAG_ID).asInt());
			
			int type = FactorType.getFactorTypeFromString(nodeData.getString(Factor.TAG_NODE_TYPE)); 
			CommandCreateObject addCommand = createFactorAndDiagramFactor(type);
			DiagramFactorId newNodeId = (DiagramFactorId) addCommand.getCreatedId();
			dataHelper.setNewId(originalDiagramNodeId, newNodeId);
			
			Point point = nodeData.getPoint(DiagramFactor.TAG_LOCATION);
			point.setLocation(point.x, point.y);
			
			dataHelper.setOriginalLocation(originalDiagramNodeId, point);
			
			EAM.logDebug("Paste Node: " + newNodeId);
		}
		
		for (int i = 0; i < nodes.length; i++) 
		{
			FactorDataMap nodeData = nodes[i];
			DiagramFactorId originalDiagramNodeId = new DiagramFactorId(nodeData.getId(DiagramFactor.TAG_ID).asInt());
			
			Point newNodeLocation = dataHelper.getNewLocation(originalDiagramNodeId, startPoint);
			
			int offsetToAvoidOverlaying = getProject().getDiagramClipboard().getPasteOffset();
			newNodeLocation.setLocation(newNodeLocation.x + offsetToAvoidOverlaying, newNodeLocation.y + offsetToAvoidOverlaying);
			newNodeLocation = getProject().getSnapped(newNodeLocation);
			
			DiagramFactorId newNodeId = dataHelper.getNewId(originalDiagramNodeId);
			FactorCell newNode = getDiagramFactorById(newNodeId);
			
			String currentSize = EnhancedJsonObject.convertFromDimension(newNode.getSize());
			String previousSize = EnhancedJsonObject.convertFromDimension(newNode.getPreviousSize());
			CommandSetObjectData setSizeCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newNode.getDiagramFactorId(), DiagramFactor.TAG_SIZE, currentSize, previousSize);
			executeCommand(setSizeCommand);
			
			DiagramFactorId newDiagramNodeId = getDiagramFactorById(newNodeId).getDiagramFactorId();
			String newMoveLocation = EnhancedJsonObject.convertFromPoint(new Point(newNodeLocation.x, newNodeLocation.y));
			CommandSetObjectData moveCommand = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, newDiagramNodeId, DiagramFactor.TAG_LOCATION, newMoveLocation);
			executeCommand(moveCommand);
			
			CommandSetObjectData setLabel = new CommandSetObjectData(ObjectType.FACTOR, newNode.getWrappedId(), Factor.TAG_LABEL, nodeData.getLabel()); 
			executeCommand(setLabel);
		}
	}

	private FactorCell getDiagramFactorById(DiagramFactorId newNodeId) throws Exception
	{
		return getDiagramModel().getFactorCellById(newNodeId);
	}
	
	private void pasteLinksIntoProject(TransferableEamList list, FactorDataHelper dataHelper) throws Exception 
	{
		FactorLinkDataMap[] links = list.getArrayOfFactorLinkDataMaps();
		for (int i = 0; i < links.length; i++) 
		{
			FactorLinkDataMap linkageData = links[i];
			
			DiagramFactorId oldFromDiagramId = linkageData.getFromId();
			DiagramFactorId newFromId = dataHelper.getNewId(oldFromDiagramId);
			DiagramFactorId newToId = dataHelper.getNewId(linkageData.getToId());
			if(newFromId.isInvalid() || newToId.isInvalid())
			{
				EAM.logWarning("Unable to Paste Link : from OriginalId:" + linkageData.getFromId() + " to OriginalId:" + linkageData.getToId()+" node deleted?");	
				continue;
			}
			
			FactorCell newFromNode = getDiagramFactorById(newFromId);
			FactorCell newToNode = getDiagramFactorById(newToId);
			CommandDiagramAddFactorLink addLinkageCommand = InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(project, newFromNode.getWrappedId(), newToNode.getWrappedId());
			EAM.logDebug("Paste Link : " + addLinkageCommand.getFactorLinkId() + " from:" + newFromId + " to:" + newToId);
		}
	}
	
	public static CommandSetObjectData createSetLabelCommand(FactorId id, String newLabel)
	{
		
		//FIXME should get node from project and get type from node instead of using FACTOR
		//ask kevin (nima)
		int type = ObjectType.FACTOR;
		String tag = Factor.TAG_LABEL;
		CommandSetObjectData cmd = new CommandSetObjectData(type, id, tag, newLabel);
		return cmd;
	}

	private Project getProject()
	{
		return project;
	}
	
	private void executeCommand(Command cmd) throws CommandFailedException
	{
		getProject().executeCommand(cmd);
	}
	
	private DiagramModel getDiagramModel()
	{
		return getProject().getDiagramModel();
	}

	Project project;
}
