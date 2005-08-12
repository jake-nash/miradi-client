/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.BorderLayout;
import java.awt.Point;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.Doer;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiScrollPane;

public class DiagramView extends UmbrellaView
{
	public DiagramView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		diagram = new DiagramComponent(getProject(), getActions());
		getProject().setSelectionModel(diagram.getSelectionModel());

		setLayout(new BorderLayout());
		add(new UiScrollPane(diagram), BorderLayout.CENTER);
	}
	
	public DiagramComponent getDiagramComponent()
	{
		return diagram;
	}

	public String cardName()
	{
		return "Diagram";
	}

	public Doer getInsertGoalDoer(Point invocationPoint)
	{
		return new InsertGoal(getProject(), invocationPoint);
	}
	
	public Doer getInsertThreatDoer(Point invocationPoint)
	{
		return new InsertThreat(getProject(), invocationPoint);
	}
	
	public Doer getInsertInterventionDoer(Point invocationPoint)
	{
		return new InsertIntervention(getProject(), invocationPoint);
	}
	
	public Doer getInsertConnectionDoer()
	{
		return new InsertConnection(getProject());
	}
	
	public Doer getPasteDoer(Point invocationPoint)
	{
		return new Paste(getProject(), invocationPoint);
	}
	
	DiagramComponent diagram;
}
