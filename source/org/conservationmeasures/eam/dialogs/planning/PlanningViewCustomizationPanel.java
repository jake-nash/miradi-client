/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.PlanningViewCustomizationQuestion;
import org.conservationmeasures.eam.questions.PlanningViewSingleLevelQuestion;
import org.conservationmeasures.eam.views.planning.PlanningView;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningViewCustomizationPanel extends JPanel implements CommandExecutedListener
{
	public PlanningViewCustomizationPanel(Project projectToUse)
	{
		super(new GridLayoutPlus(3, 2));
		project = projectToUse;
		project.addCommandExecutedListener(this);
		
		rebuildLegendPanel();
		setBorder(BorderFactory.createTitledBorder(EAM.text("Standard Views")));
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
	}
	
	protected void rebuildLegendPanel()
	{
		try
		{
			rebuildPanel();
			selectDefaults();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error Occurred While in Planning View Leged Panel"));
		}
	}
	
	protected void rebuildPanel() throws Exception
	{
		removeAll();
		radioGroup = new PlanningViewRadioGroup(project);
		
		PlanningViewStrategicRadioButton strategicRadioButton = new PlanningViewStrategicRadioButton(project);
		radioGroup.addRadioButtonToGroup(strategicRadioButton);
		addRadioButtonWithLeftComponent(strategicRadioButton, new JLabel(EAM.text("Strategic Plan")));
		
		PlanningViewMonitoringRadioButton monitoringRadioButton = new PlanningViewMonitoringRadioButton(project);
		radioGroup.addRadioButtonToGroup(monitoringRadioButton);
		addRadioButtonWithLeftComponent(monitoringRadioButton, new JLabel(EAM.text("Monitoring Plan")));
		
		PlanningViewWorkPlanRadioButton workPlanRadioButton = new PlanningViewWorkPlanRadioButton(project);
		radioGroup.addRadioButtonToGroup(workPlanRadioButton);
		addRadioButtonWithLeftComponent(workPlanRadioButton, new JLabel(EAM.text("Work Plan")));
		
		
		singleLevelCombo = new PlanningViewSingleLevelComboBox(project);
		PlanningViewSingleLevelRadioButton singleLevelRadioButton = new PlanningViewSingleLevelRadioButton(project, singleLevelCombo);
		radioGroup.addRadioButtonToGroup(singleLevelRadioButton);
		radioGroup.addComboBoxToHashMap(singleLevelCombo);
		addRadioButtonWithLeftComponent(singleLevelRadioButton, singleLevelCombo);

		customizationComboBox = new PlanningViewCustomizationComboBox(project);
		PlanningViewCustomizationRadioButton customizationRadioButton = new PlanningViewCustomizationRadioButton(project, customizationComboBox);
		radioGroup.addComboBoxToHashMap(customizationComboBox);
		radioGroup.addRadioButtonToGroup(customizationRadioButton);
		addRadioButtonWithLeftComponent(customizationRadioButton, customizationComboBox);
	}
	
	private void selectDefaults() throws Exception
	{
		radioGroup.selectAppropriateRadioButton();
		selectAppropriateSingleLevelComboBoxItem();
		selectAppropriateConfiguredComboBoxItem();
	}
	
	private void addRadioButtonWithLeftComponent(JRadioButton radioButton, Component leftComponent)
	{
		add(leftComponent);
		add(radioButton);
	}
	
	private void selectAppropriateSingleLevelComboBoxItem()
	{
		try
		{
			ViewData viewData = project.getCurrentViewData();
			String selectedRadioName = viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE);
			if (! selectedRadioName.equals(PlanningView.SINGLE_LEVEL_RADIO_CHOICE))
				return;

			String preconfiguredChoice = getCurrentSingleLevelChoice(viewData);
			setComboBoxSelection(PlanningView.SINGLE_LEVEL_COMBO, preconfiguredChoice);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private void selectAppropriateConfiguredComboBoxItem()
	{
		try
		{
			ViewData viewData = project.getCurrentViewData();
			String selectedRadioName = viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE);
			if (! selectedRadioName.equals(PlanningView.CUSTOMIZABLE_RADIO_CHOICE))
				return;
			
			ORef configurationChoiceRef = getCurrentConfigurationComboBoxChoice(viewData);
			selectConfigurationComboButton(configurationChoiceRef);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private ORef getCurrentConfigurationComboBoxChoice(ViewData viewData)
	{	
		String preconfiguredChoice = viewData.getData(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
		boolean shouldReturnDefault = preconfiguredChoice.trim().equals("");
		if (shouldReturnDefault)
			return ORef.INVALID;

		return ORef.createFromString(preconfiguredChoice);
	}
	
	private String getCurrentSingleLevelChoice(ViewData viewData)
	{	
		String singleLevelChoice = viewData.getData(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE);
		boolean shouldReturnDefault = singleLevelChoice.trim().equals("");
		if (shouldReturnDefault)
			return Goal.OBJECT_NAME;

		return singleLevelChoice;
	}
	
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		selectRadioButtonFromProjectSetting(event);
	}
	
	private void selectRadioButtonFromProjectSetting(CommandExecutedEvent event)
	{
		possiblyRebuild(event);
		if (event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_STYLE_CHOICE))
		{
			CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
			radioGroup.updateRadioSelection(setCommand.getDataValue());
			return;
		}
		
		if (event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_CUSTOM_PLAN_REF))
		{
			radioGroup.updateRadioSelection(PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
			return;
		}
		
		if (event.isSetDataCommandWithThisTypeAndTag(PlanningViewConfiguration.getObjectType(), PlanningViewConfiguration.TAG_LABEL))
		{
			CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
			ORef ref = setCommand.getObjectORef();
			selectConfigurationComboButton(ref);
			return;
		}
		
		setSingleLevelComboSelection(event, PlanningView.SINGLE_LEVEL_RADIO_CHOICE, PlanningView.SINGLE_LEVEL_COMBO);

	}

	private void setSingleLevelComboSelection(CommandExecutedEvent event, String choice, String comboPropertyName)
	{
		if (! event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), choice))
			return;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		String property = setCommand.getDataValue();
		setComboBoxSelection(comboPropertyName, property);
	}

	private void possiblyRebuild(CommandExecutedEvent event)
	{
		if (! (event.isCreateObjectCommand() || event.isDeleteObjectCommand()))
			return;
		
		rebuildLegendPanel();
		validate();
	}
	
	private void setComboBoxSelection(String comboName, String itemProperty)
	{		
		JComboBox comboBox = radioGroup.findComboBox(comboName);
		ChoiceItem currentSelection = (ChoiceItem)comboBox.getSelectedItem();
		PlanningViewSingleLevelQuestion question = new PlanningViewSingleLevelQuestion();
		ChoiceItem choiceItemToSelect = question.findChoiceByCode(itemProperty);
		if (currentSelection.equals(choiceItemToSelect))
			return;
		
		comboBox.setSelectedItem(choiceItemToSelect);
	}

	private void selectConfigurationComboButton(ORef refToSelect)
	{
		JComboBox comboBox = radioGroup.findComboBox(PlanningView.CUSTOMIZABLE_COMBO);
		if (refToSelect.isInvalid())
			return;
		
		ChoiceItem choiceItem = (ChoiceItem) comboBox.getSelectedItem();
		ORef currentRef = ORef.createFromString(choiceItem.getCode());
		if (currentRef.equals(refToSelect))
			return;
		
		ChoiceItem choiceToSelect = getConfigurationToSelect(refToSelect);
		comboBox.setSelectedItem(choiceToSelect);
	}

	private ChoiceItem getConfigurationToSelect(ORef refToSelect)
	{
		if (refToSelect.getObjectId().isInvalid())
			return PlanningViewCustomizationQuestion.createDefaultInvalidConfigurationObject(project);
		
		PlanningViewConfiguration configuration = (PlanningViewConfiguration) project.findObject(refToSelect);
		return new ChoiceItem(configuration.getRef().toString(), configuration.getLabel());
	}
			
	private Project project;
	private PlanningViewRadioGroup radioGroup;
	private PlanningViewSingleLevelComboBox singleLevelCombo;
	private PlanningViewCustomizationComboBox customizationComboBox;
}
