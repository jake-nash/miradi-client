/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.HeadlessException;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.conservationmeasures.eam.actions.ActionAbout;
import org.conservationmeasures.eam.actions.ActionClose;
import org.conservationmeasures.eam.actions.ActionComingAttractions;
import org.conservationmeasures.eam.actions.ActionConfigureLayers;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionExit;
import org.conservationmeasures.eam.actions.ActionExportZippedProjectFile;
import org.conservationmeasures.eam.actions.ActionHelpButtonExamples;
import org.conservationmeasures.eam.actions.ActionHelptButtonMoreInfo;
import org.conservationmeasures.eam.actions.ActionHelptButtonWorkshop;
import org.conservationmeasures.eam.actions.ActionImportTncCapWorkbook;
import org.conservationmeasures.eam.actions.ActionImportZippedProjectFile;
import org.conservationmeasures.eam.actions.ActionInsertContributingFactor;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertDraftStrategy;
import org.conservationmeasures.eam.actions.ActionInsertFactorLink;
import org.conservationmeasures.eam.actions.ActionInsertStrategy;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPasteWithoutLinks;
import org.conservationmeasures.eam.actions.ActionPreferences;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionProjectSaveAs;
import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionSaveImage;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionSelectChain;
import org.conservationmeasures.eam.actions.ActionShowFullModelMode;
import org.conservationmeasures.eam.actions.ActionShowSelectedChainMode;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.actions.jump.ActionJumpActivitiesAndActionPlan;
import org.conservationmeasures.eam.actions.jump.ActionJumpAdaptAndMonitorPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeData;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeInterventions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeProjectCapacity;
import org.conservationmeasures.eam.actions.jump.ActionJumpAnalyzeResourcesFeasibilityAndRisk;
import org.conservationmeasures.eam.actions.jump.ActionJumpArticulateCoreAssumptions;
import org.conservationmeasures.eam.actions.jump.ActionJumpAssessStakeholders;
import org.conservationmeasures.eam.actions.jump.ActionJumpCloseTheLoop;
import org.conservationmeasures.eam.actions.jump.ActionJumpCommunicateResults;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreate;
import org.conservationmeasures.eam.actions.jump.ActionJumpCreateModel;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineAudiences;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineIndicators;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineScope;
import org.conservationmeasures.eam.actions.jump.ActionJumpDefineTasks;
import org.conservationmeasures.eam.actions.jump.ActionJumpDescribeTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDesignateLeader;
import org.conservationmeasures.eam.actions.jump.ActionJumpDetermineNeeds;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopActivities;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopBudgets;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopCharter;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopObjectives;
import org.conservationmeasures.eam.actions.jump.ActionJumpDevelopTargetGoals;
import org.conservationmeasures.eam.actions.jump.ActionJumpDocument;
import org.conservationmeasures.eam.actions.jump.ActionJumpEstablishVision;
import org.conservationmeasures.eam.actions.jump.ActionJumpGroundTruthRevise;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyContributingFactors;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpIdentifyTargets;
import org.conservationmeasures.eam.actions.jump.ActionJumpImplementPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpPlanDataStorage;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDirectThreats;
import org.conservationmeasures.eam.actions.jump.ActionJumpRankDraftStrategies;
import org.conservationmeasures.eam.actions.jump.ActionJumpRefinePlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpResultsChains;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectAppropriateMethods;
import org.conservationmeasures.eam.actions.jump.ActionJumpSelectTeam;
import org.conservationmeasures.eam.actions.jump.ActionJumpShare;
import org.conservationmeasures.eam.actions.jump.ActionJumpShorttermPlans;
import org.conservationmeasures.eam.actions.jump.ActionJumpTeamRoles;
import org.conservationmeasures.eam.utils.MenuItemWithoutLocation;
import org.conservationmeasures.eam.views.umbrella.HelpButtonData;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class MainMenuBar extends JMenuBar
{
	public MainMenuBar(Actions actions) throws HeadlessException
	{
		add(createFileMenu(actions));
		add(createEditMenu(actions));
		add(createInsertMenu(actions));
		add(createViewMenu(actions));
		add(createProcessMenu(actions));
		add(createHelpMenu(actions));
	}

	private JMenu createFileMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|File"));
		menu.setMnemonic(KeyEvent.VK_F);
		
		addMenuItem(actions, menu, ActionNewProject.class, KeyEvent.VK_N);
		addMenuItem(actions, menu, ActionProjectSaveAs.class, KeyEvent.VK_A);
		addMenuItem(actions, menu, ActionClose.class, KeyEvent.VK_C);
		menu.addSeparator();
		addMenuItem(actions, menu, ActionPrint.class, KeyEvent.VK_P);
		menu.addSeparator();
		addMenuItem(actions, menu, ActionSaveImage.class, KeyEvent.VK_J);
		menu.add(createExportSetupMenu());
		menu.add(createExportMenu(actions));
		menu.add(createImportMenu(actions));
		menu.addSeparator();
		addMenuItem(actions, menu, ActionExit.class, KeyEvent.VK_E);
		return menu;
	}


	private JMenu createExportSetupMenu()
	{
		JMenu menu = new JMenu("Configure Export");
		menu.setMnemonic(KeyEvent.VK_C);
		
		menu.add("Entire Project");
		menu.add("Specific Views");
		menu.add("Restrict Data");
		menu.add("Encryption Level");
		return menu;
	}

	private JMenu createExportMenu(Actions actions)
	{
		JMenu menu = new JMenu("Export");
		menu.setMnemonic(KeyEvent.VK_E);
		
		addMenuItem(actions, menu, ActionExportZippedProjectFile.class, KeyEvent.VK_E);
		menu.add("CMP Learning Center");
		menu.add(createExportOrgDatabaseSubmenu());
		menu.add(createExportDonorReportsSubmenu());
		menu.add(createExportOtherSubmenu());
		return menu;
	}
	
	private JMenu createExportOrgDatabaseSubmenu()
	{
		JMenu menu = new JMenu("Organization Database");
		menu.setMnemonic(KeyEvent.VK_O);
		
		menu.add("WWF Track");
		menu.add("TNC Project Database");
		menu.add("WCS Accounting System");
		return menu;
	}
	
	private JMenu createExportDonorReportsSubmenu()
	{
		JMenu menu = new JMenu("Donor Report");
		menu.setMnemonic(KeyEvent.VK_D);
		
		menu.add("GEF Format");
		menu.add("Moore Format");
		menu.add("CGBD Common Format");
		return menu;
	}
	
	private JMenu createExportOtherSubmenu()
	{
		JMenu menu = new JMenu("Other Format");
		menu.setMnemonic(KeyEvent.VK_O);
		
		menu.add("Diagram to MS Visio");
		menu.add("Workplan to MS Project");
		menu.add("Budget to MS Excel");
		menu.add("Map to ARC");
		menu.add("Generic XML");
		return menu;
	}
	
	private JMenu createImportMenu(Actions actions)
	{
		JMenu menu = new JMenu("Import");
		menu.setMnemonic(KeyEvent.VK_I);
		
		addMenuItem(actions, menu, ActionImportZippedProjectFile.class, KeyEvent.VK_P);
		addMenuItem(actions, menu, ActionImportTncCapWorkbook.class, KeyEvent.VK_C);
		
		menu.add(createImportProjectSubmenu());
		menu.add(createImportMapSubmenu());
		menu.add(createImportDiagramSubmenu());
		menu.add(createImportWorkplanSubmenu());
		return menu;
	}
	
	private JMenu createImportProjectSubmenu()
	{
		JMenu menu = new JMenu("Conservation Project");
		menu.setMnemonic(KeyEvent.VK_C);
		
		menu.add("TNC CAP Workbook");
		return menu;
	}
	
	private JMenu createImportMapSubmenu()
	{
		JMenu menu = new JMenu("Maps");
		menu.setMnemonic(KeyEvent.VK_M);
		
		menu.add("ARC Shape Files");
		return menu;
	}
	
	private JMenu createImportDiagramSubmenu()
	{
		JMenu menu = new JMenu("Diagrams");
		menu.setMnemonic(KeyEvent.VK_D);
		
		menu.add("MS Visio");
		return menu;
	}
	
	private JMenu createImportWorkplanSubmenu()
	{
		JMenu menu = new JMenu("Workplans");
		menu.setMnemonic(KeyEvent.VK_W);
		
		menu.add("MS Project");
		return menu;
	}
	
	private JMenu createEditMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Edit"));
		menu.setMnemonic(KeyEvent.VK_E);
		
		addMenuItem(actions, menu, ActionUndo.class, KeyEvent.VK_U);
		addMenuItem(actions, menu, ActionRedo.class, KeyEvent.VK_R);
		menu.addSeparator();
		
		addMenuItem(actions, menu, ActionCut.class, KeyEvent.VK_T);
		addMenuItem(actions, menu, ActionCopy.class, KeyEvent.VK_C);
		addMenuItem(actions, menu, ActionPaste.class, KeyEvent.VK_P);
		addMenuItem(actions, menu, ActionPasteWithoutLinks.class, -1);
		menu.addSeparator();
		
		addMenuItem(actions, menu, ActionDelete.class, KeyEvent.VK_D);
		addMenuItem(actions, menu, ActionSelectAll.class, KeyEvent.VK_A);
		addMenuItem(actions, menu, ActionSelectChain.class, KeyEvent.VK_C);
		menu.addSeparator();
		addMenuItem(actions, menu, ActionPreferences.class, KeyEvent.VK_I);
		return menu;
	}
	
	private JMenu createInsertMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Insert"));
		menu.setMnemonic(KeyEvent.VK_I);
		
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertDraftStrategy.class),KeyEvent.VK_D));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertStrategy.class),KeyEvent.VK_S));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertContributingFactor.class),KeyEvent.VK_C));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertDirectThreat.class),KeyEvent.VK_D));
		menu.add(createJMenuItemCenterLocation(actions.get(ActionInsertTarget.class),KeyEvent.VK_T));
		menu.addSeparator();
		
		addMenuItem(actions, menu, ActionInsertFactorLink.class, KeyEvent.VK_I);
		return menu;
	}

	
	private JMenu createViewMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|View"));
		menu.setMnemonic(KeyEvent.VK_V);
		
		Action[] viewSwitchActions = ViewSwitcher.getViewSwitchActions(actions);
		for(int i = 0; i < viewSwitchActions.length; ++i)
			menu.add(viewSwitchActions[i]);
		menu.addSeparator();
		addMenuItem(actions, menu, ActionZoomIn.class, KeyEvent.VK_I);
		addMenuItem(actions, menu, ActionZoomOut.class, KeyEvent.VK_O);
		menu.addSeparator();
		addMenuItem(actions, menu, ActionConfigureLayers.class, KeyEvent.VK_C);
		addMenuItem(actions, menu, ActionShowSelectedChainMode.class, KeyEvent.VK_O);
		addMenuItem(actions, menu, ActionShowFullModelMode.class, KeyEvent.VK_F);
		
		return menu;
	}
	
	private JMenu createMenu1(Actions actions)
	{
		JMenu menu1 = new JMenu("1. Conceptualize Your Project");
		menu1.setMnemonic(KeyEvent.VK_C);
		
		JMenu menu1a = new JMenu("1A. Define initial project team");
		menu1a.setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(actions, menu1a, ActionJumpSelectTeam.class, KeyEvent.VK_S);
		addMenuItem(actions, menu1a, ActionJumpDesignateLeader.class, KeyEvent.VK_D);
		addMenuItem(actions, menu1a, ActionJumpDevelopCharter.class, KeyEvent.VK_D);
	
		JMenu menu1b = new JMenu("1B. Define clear and common purpose");
		menu1b.setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(actions, menu1b, ActionJumpDefineScope.class, KeyEvent.VK_D);
		addMenuItem(actions, menu1b, ActionJumpEstablishVision.class, KeyEvent.VK_E);
		addMenuItem(actions, menu1b, ActionJumpIdentifyTargets.class, KeyEvent.VK_I);
		addMenuItem(actions, menu1b, ActionJumpDescribeTargets.class, KeyEvent.VK_D);
		
		JMenu menu1c = new JMenu("1C. Understand project context");
		menu1c.setMnemonic(KeyEvent.VK_U);
		
		addMenuItem(actions, menu1c, ActionJumpIdentifyDirectThreats.class, KeyEvent.VK_I);
		addMenuItem(actions, menu1c, ActionJumpRankDirectThreats.class, KeyEvent.VK_R);
		addMenuItem(actions, menu1c, ActionJumpIdentifyContributingFactors.class, KeyEvent.VK_I);
		addMenuItem(actions, menu1c, ActionJumpAssessStakeholders.class, KeyEvent.VK_A);
		addMenuItem(actions, menu1c, ActionJumpAnalyzeProjectCapacity.class, KeyEvent.VK_A);
		
		JMenu menu1d = new JMenu("1D. Model project situation");
		menu1d.setMnemonic(KeyEvent.VK_M);
		
		addMenuItem(actions, menu1d, ActionJumpArticulateCoreAssumptions.class, KeyEvent.VK_A);
		addMenuItem(actions, menu1d, ActionJumpCreateModel.class, KeyEvent.VK_C);
		addMenuItem(actions, menu1d, ActionJumpGroundTruthRevise.class, KeyEvent.VK_G);
		
		menu1.add(menu1a);
		menu1.add(menu1b);
		menu1.add(menu1c);
		menu1.add(menu1d);
		return menu1;
	}
	
	private JMenu createMenu2(Actions actions)
	{
		JMenu menu2 = new JMenu("2. Plan Your Actions");
		menu2.setMnemonic(KeyEvent.VK_P);
		
		JMenu menu2a = new JMenu("2A. Develop clear goal and objectives");
		menu2a.setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(actions, menu2a, ActionJumpDevelopTargetGoals.class, KeyEvent.VK_D);
		addMenuItem(actions, menu2a, ActionJumpIdentifyStrategies.class, KeyEvent.VK_I);
		addMenuItem(actions, menu2a, ActionJumpDevelopObjectives.class, KeyEvent.VK_D);;
		
		JMenu menu2b = new JMenu("2B. Strategically select activities");
		menu2b.setMnemonic(KeyEvent.VK_S);
		
		addMenuItem(actions, menu2b, ActionJumpRankDraftStrategies.class, KeyEvent.VK_R);
		addMenuItem(actions, menu2b, ActionJumpResultsChains.class, KeyEvent.VK_R);;
		addMenuItem(actions, menu2b, ActionJumpActivitiesAndActionPlan.class, KeyEvent.VK_A);;
		addMenuItem(actions, menu2b, ActionJumpAnalyzeResourcesFeasibilityAndRisk.class, KeyEvent.VK_A);;

		menu2.add(menu2a);
		menu2.add(menu2b);
		return menu2;
	}
	
	private JMenu createMenu3(Actions actions)
	{
		JMenu menu3 = new JMenu("3. Plan Your Monitoring");
		menu3.setMnemonic(KeyEvent.VK_P);
		
		JMenu menu3a = new JMenu("3A. Focus your monitoring");
		menu3a.setMnemonic(KeyEvent.VK_F);
		
		addMenuItem(actions, menu3a, ActionJumpDetermineNeeds.class, KeyEvent.VK_D);
		addMenuItem(actions, menu3a, ActionJumpDefineAudiences.class, KeyEvent.VK_D);
		
		JMenu menu3b = new JMenu("3B. Develop a formal monitoring plan");
		menu3b.setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(actions, menu3b, ActionJumpDefineIndicators.class, KeyEvent.VK_D);
		addMenuItem(actions, menu3b, ActionJumpSelectAppropriateMethods.class, KeyEvent.VK_S);
		addMenuItem(actions, menu3b, ActionJumpPlanDataStorage.class, KeyEvent.VK_P);

		menu3.add(menu3a);
		menu3.add(menu3b);
		return menu3;
	}
	
	private JMenu createMenu4(Actions actions)
	{
		JMenu menu4 = new JMenu("4. Implement Actions and Monitoring");
		menu4.setMnemonic(KeyEvent.VK_I);
		
		addMenuItem(actions, menu4, ActionJumpImplementPlans.class, KeyEvent.VK_I);
		addMenuItem(actions, menu4, ActionJumpRefinePlans.class, KeyEvent.VK_R);

		JMenu menu4a = new JMenu("4A. Develop detailed short-term work plans");
		menu4a.setMnemonic(KeyEvent.VK_D);
		
		addMenuItem(actions, menu4a, ActionJumpShorttermPlans.class, KeyEvent.VK_S);
		addMenuItem(actions, menu4a, ActionJumpDevelopActivities.class, KeyEvent.VK_D);
		addMenuItem(actions, menu4a, ActionJumpDefineTasks.class, KeyEvent.VK_D);
		addMenuItem(actions, menu4a, ActionJumpDevelopBudgets.class, KeyEvent.VK_D);
		addMenuItem(actions, menu4a, ActionJumpTeamRoles.class, KeyEvent.VK_T);

		menu4.add(menu4a);
		return menu4;
	}
	
	private JMenu createMenu5(Actions actions)
	{
		JMenu menu5 = new JMenu("5. Analyze");
		menu5.setMnemonic(KeyEvent.VK_A);
		
		addMenuItem(actions, menu5, ActionJumpAnalyzeData.class, KeyEvent.VK_A);
		addMenuItem(actions, menu5, ActionJumpAnalyzeInterventions.class, KeyEvent.VK_A);
		addMenuItem(actions, menu5, ActionJumpCommunicateResults.class, KeyEvent.VK_C);
		
		return menu5;
	}
	
	private JMenu createMenu6(Actions actions)
	{
		JMenu menu6 = new JMenu("6. Use/Adapt");
		menu6.setMnemonic(KeyEvent.VK_U);
		addMenuItem(actions, menu6, ActionJumpAdaptAndMonitorPlans.class, KeyEvent.VK_A);
		return menu6;
	}
	
	private JMenu createMenu7(Actions actions)
	{
		JMenu menu7 = new JMenu("7. Capture and Share Learning");
		menu7.setMnemonic(KeyEvent.VK_C);
		
		addMenuItem(actions, menu7, ActionJumpDocument.class, KeyEvent.VK_D);
		addMenuItem(actions, menu7, ActionJumpShare.class, KeyEvent.VK_S);
		addMenuItem(actions, menu7, ActionJumpCreate.class, KeyEvent.VK_C);
		
		return menu7;
	}

	private JMenuItem createMenu8(Actions actions)
	{
		return new JMenuItem(actions.get(ActionJumpCloseTheLoop.class));
	}
	
	private JMenu createProcessMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Step-by-Step"));
		menu.setMnemonic(KeyEvent.VK_S);
		
		menu.add(createMenu1(actions));
		menu.add(createMenu2(actions));
		menu.add(createMenu3(actions));
		menu.add(createMenu4(actions));
		menu.add(createMenu5(actions));
		menu.add(createMenu6(actions));
		menu.add(createMenu7(actions));
		menu.add(createMenu8(actions));
		return menu;
	}

	private JMenu createHelpMenu(Actions actions)
	{
		JMenu menu = new JMenu(EAM.text("MenuBar|Help"));
		menu.setMnemonic(KeyEvent.VK_H);
		
		JMenuItem item1  = addMenuItem(actions, menu, ActionComingAttractions.class, KeyEvent.VK_C);
		item1.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(UmbrellaView.class, HelpButtonData.COMING_ATTACTIONS, HelpButtonData.COMING_ATTRACTIONS_HTML));

		JMenuItem item2  = addMenuItem(actions, menu, ActionHelptButtonMoreInfo.class, KeyEvent.VK_M);
		item2.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.MORE_INFO, HelpButtonData.MORE_INFO_HTML));
		
		JMenuItem item3  = addMenuItem(actions, menu, ActionHelpButtonExamples.class, KeyEvent.VK_E);
		item3.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.EXAMPLES, HelpButtonData.EXAMPLES_HTML));
		
		JMenuItem item4  = addMenuItem(actions, menu, ActionHelptButtonWorkshop.class, KeyEvent.VK_W);
		item4.putClientProperty(HelpButtonData.class, 
				new HelpButtonData(HelpButtonData.WORKSHOP, HelpButtonData.WORKSHOP_HTML));
		
		addMenuItem(actions, menu, ActionAbout.class, KeyEvent.VK_A);
		return menu;
	}
	
	
	private JMenuItem addMenuItem(Actions actions, JMenu menu, Class class1, int mnemonic)
	{
		EAMenuItem menuItemNewProject = new EAMenuItem(actions.get(class1), mnemonic);
		menu.add(menuItemNewProject);
		return menuItemNewProject; 
	}
	
	
	private JMenuItem createJMenuItemCenterLocation(EAMAction action, int mnemonic)
	{
		JMenuItem centeredLocationAction = new MenuItemWithoutLocation(action);
		centeredLocationAction.setMnemonic(mnemonic);
		return centeredLocationAction;
	}
	
}
