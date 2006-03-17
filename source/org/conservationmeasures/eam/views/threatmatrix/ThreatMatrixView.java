/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.ViewChangeListener;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.interview.ThreatMatrixToolBar;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiScrollPane;


public class ThreatMatrixView extends UmbrellaView implements ViewChangeListener
{
	public ThreatMatrixView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new ThreatMatrixToolBar(getMainWindow().getActions()));
		setLayout(new BorderLayout());
		getProject().addViewChangeListener(this);
	}

	public String cardName()
	{
		return getViewName();
	}

	static public String getViewName()
	{
		return "ThreatMatrix";
	}
	
	public void switchToView(String viewName) throws Exception
	{
		boolean isSwitchingToThisView = viewName.equals(getViewName());
		if(!isSwitchingToThisView)
			return;
		
		removeAll();

		model = new ThreatMatrixTableModel(getProject());

		grid = new ThreatGridPanel(this, model);
		ThreatRatingWizardPanel wizard = new ThreatRatingWizardPanel(this);
		details = new ThreatRatingBundlePanel(getProject(), new OkListener(), new CancelListener());

		Container bottomHalf = new JPanel(new BorderLayout());
		bottomHalf.add(new UiScrollPane(grid), BorderLayout.CENTER);
		bottomHalf.add(new UiScrollPane(details), BorderLayout.AFTER_LINE_ENDS);
		
		JSplitPane bigSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		bigSplitter.setTopComponent(wizard);
		bigSplitter.setBottomComponent(bottomHalf);
		add(bigSplitter);
		
		selectBundle(wizard.getSelectedBundle());
	}
	
	public ThreatMatrixTableModel getModel()
	{
		return model;
	}
	
	public void selectBundle(ThreatRatingBundle bundle) throws Exception
	{
		grid.selectBundle(bundle);
		details.setBundle(bundle);
		invalidate();
		validate();
	}
	
	abstract class ButtonListener implements ActionListener
	{
		abstract void takeAction(ThreatRatingBundle originalBundle, ThreatRatingBundle workingBundle) throws Exception;

		public void actionPerformed(ActionEvent event)
		{
			try
			{
				ThreatRatingBundle workingBundle = details.getBundle();
				ThreatRatingBundle originalBundle = model.getBundle(workingBundle.getThreatId(), workingBundle.getTargetId());
				takeAction(originalBundle, workingBundle);
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}
	}
	
	class OkListener extends ButtonListener
	{
		void takeAction(ThreatRatingBundle originalBundle, ThreatRatingBundle workingBundle) throws Exception
		{
			Project project = model.getProject();
			originalBundle.pullDataFrom(workingBundle);
			project.getThreatRatingFramework().saveBundle(originalBundle);
			grid.refreshCell(originalBundle);
		}
	}
	
	class CancelListener extends ButtonListener
	{
		void takeAction(ThreatRatingBundle originalBundle, ThreatRatingBundle workingBundle) throws Exception
		{
			details.setBundle(originalBundle);
		}
		
	}
	
	ThreatMatrixTableModel model;
	ThreatGridPanel grid;
	ThreatRatingBundlePanel details;
}

