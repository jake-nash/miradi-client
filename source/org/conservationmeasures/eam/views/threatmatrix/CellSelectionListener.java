/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.views.diagram.InsertFactorLinkDoer;


class CellSelectionListener implements ListSelectionListener
{
	public CellSelectionListener(ThreatMatrixTable threatTableInUse, ThreatGridPanel threatGirdPanelInUse) {
		threatTable = threatTableInUse;
		threatGirdPanel = threatGirdPanelInUse;
	}

	public void valueChanged(ListSelectionEvent e)
	{
		int row = threatTable.getSelectedRow();
		if (row < 0)
			return;
		
		int column = threatTable.convertColumnIndexToModel(threatTable.getSelectedColumn());
		if(column < 0)
			return;

		try
		{
			ThreatRatingBundle bundle = getBundleIfAny(row, column);
			threatGirdPanel.getThreatMatrixView().selectBundle(bundle);
		}
		catch(Exception ex)
		{
			// TODO: must add errDialog call....need to see how to call when on the swing event thread
			EAM.logException(ex);
		}

		unselectToForceFutureNotifications(row, threatTable.getSelectedColumn());
	}

	private ThreatRatingBundle getBundleIfAny(int row, int column) throws Exception
	{
		ThreatMatrixTableModel model = (ThreatMatrixTableModel) threatTable.getModel();
		if(model.isSumaryColumn(row, column) || model.isSumaryRow(row, column))
			return null;
		
		if(!isLinked(row, column))
			offerToCreateLink(row, column);
		
		return model.getBundle(row, column);
	}

	private void offerToCreateLink(int row, int column) throws CommandFailedException
	{
		ThreatMatrixTableModel model = (ThreatMatrixTableModel) threatTable.getModel();
		String[] body = new String[] {
				EAM.text("Do you want to create a link between these this Threat and Target?"),
				(model).getThreatName(row),
				(model).getTargetName(column),
				};
		String[] buttons = new String[] {EAM.text("Create Link"), EAM.text("Cancel")};
		if(!EAM.confirmDialog(EAM.text("Create a link?"), body, buttons))
			return;
		
		
		Project project = model.getProject();
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(
				project, 
				model.getThreatId(row), 
				model.getTargetId(column));
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

	private boolean isLinked(int row, int column)
	{
		ThreatMatrixTableModel model = (ThreatMatrixTableModel) threatTable.getModel();
		return model.getProject().isLinked(model.getThreatId(row), model.getTargetId(column));
	}
	
	private void unselectToForceFutureNotifications(int row, int column)
	{
		threatTable.changeSelection(row, column, true,false);
	}
	
	ThreatMatrixTable threatTable;
	ThreatGridPanel threatGirdPanel;
}

