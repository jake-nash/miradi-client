package org.conservationmeasures.eam.views.umbrella;
import java.awt.Dimension;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactor;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetFactorSize;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.NodeCommandHelper;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestUndoRedo extends EAMTestCase 
{

	public TestUndoRedo(String name) 
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		super.setUp();
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}
	
	public void testBasics() throws Exception
	{
		String target1Text = "Target 1 Text";
		project.executeCommand(new CommandBeginTransaction());
		FactorId insertedId = insertFactor(project).getModelNodeId();
		project.executeCommand(NodeCommandHelper.createSetLabelCommand(insertedId, target1Text));
		project.executeCommand(new CommandEndTransaction());
		assertEquals("Should have 1 node now.", 1, project.getDiagramModel().getNodeCount());
		
		project.getDiagramModel().getNodeById(insertedId);
		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals("Should have 0 nodes now.", 0, project.getDiagramModel().getNodeCount());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();

		Vector inserted = project.getDiagramModel().getAllNodes();
		
		assertEquals("Should have 1 node again after redo.", 1, project.getDiagramModel().getNodeCount());
		assertEquals("wrong number of nodes after redo?", 1, inserted.size());
		DiagramFactor node = (DiagramFactor)inserted.get(0);
		assertTrue(project.getDiagramModel().isNodeInProject(node));
		assertEquals("Incorrect label?", target1Text, node.getLabel());
		
		undo.doIt();
		assertEquals("Should have 0 nodes again.", 0, project.getDiagramModel().getNodeCount());
	}

	public void testUndoRedoNodeSize() throws Exception
	{
		DiagramFactorId insertedId = insertFactor(project).getInsertedId();
		DiagramFactor node = project.getDiagramModel().getNodeById(insertedId);
		Dimension originalSize = node.getSize();

		Dimension newSize1 = new Dimension(5,10);
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetFactorSize(insertedId, newSize1, originalSize));
		project.executeCommand(new CommandEndTransaction());

		assertEquals(newSize1, node.getSize());

		Dimension newSize2 = new Dimension(20,30);
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetFactorSize(insertedId, newSize2, newSize1));
		project.executeCommand(new CommandEndTransaction());
		assertEquals(newSize2, node.getSize());

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals(newSize1, node.getSize());

		undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals(originalSize, node.getSize());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertEquals(newSize1, node.getSize());

		redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertEquals(newSize2, node.getSize());
	}
	
	private CommandDiagramAddFactor insertFactor(Project p) throws Exception 
	{
		CreateFactorParameter extraInfo = new CreateFactorParameter(Factor.TYPE_CAUSE);
		CommandCreateObject createModelNodeCommand = new CommandCreateObject(ObjectType.MODEL_NODE, extraInfo);
		p.executeCommand(createModelNodeCommand);
		FactorId modelNodeId = (FactorId)createModelNodeCommand.getCreatedId();
		CommandDiagramAddFactor addToDiagramCommand = new CommandDiagramAddFactor(new DiagramFactorId(BaseId.INVALID.asInt()), modelNodeId);
		p.executeCommand(addToDiagramCommand);
		return addToDiagramCommand;
	}

	ProjectForTesting project;
}
