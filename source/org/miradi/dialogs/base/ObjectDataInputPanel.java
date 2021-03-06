/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.base;

import org.martus.swing.UiLabel;
import org.miradi.dialogfields.ObjectDataField;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogfields.RadioButtonsField;
import org.miradi.dialogs.fieldComponents.ClickablePanelTitleLabel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.forms.FieldPanelSpec;
import org.miradi.icons.QuestionMarkIcon;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.FillerLabel;

import javax.swing.*;
import java.util.Vector;

abstract public class ObjectDataInputPanel extends AbstractObjectDataInputPanelWithCreationFromForms
{
	public ObjectDataInputPanel(Project projectToUse, int objectType)
	{
		this(projectToUse, ORef.createInvalidWithType(objectType));
	}
	
	public ObjectDataInputPanel(Project projectToUse, ORef orefToUse)
	{
		this(projectToUse, new ORef[] {orefToUse});
	}
	
	public ObjectDataInputPanel(Project projectToUse, int objectType, FieldPanelSpec formToUse) throws Exception
	{
		this(projectToUse, new ORef[]{ORef.createInvalidWithType(objectType)}, formToUse);
	}
	
	public ObjectDataInputPanel(Project projectToUse, ORef[] orefsToUse, FieldPanelSpec formToUse) throws Exception
	{
		super(projectToUse, orefsToUse, formToUse);
	}
	
	public ObjectDataInputPanel(Project projectToUse, ORef[] orefsToUse)
	{
		super(projectToUse, orefsToUse);
	}
	
	public void addBlankHorizontalLine()
	{
		addTopAlignedLabel(new UiLabel(" "));
		addFieldComponent(new UiLabel(" "));
	}
	
	public void addRadioButtonField(int objectType, String fieldTag, ChoiceQuestion question)
	{
		addRadioButtonFieldWithCustomLabel(objectType, fieldTag, question, "");
	}
	
	public ObjectDataInputField addRadioButtonFieldWithCustomLabel(int objectType, String fieldTag, ChoiceQuestion question, String customLabel)
	{
		RadioButtonsField radioButtonField = createRadioButtonsField(objectType, fieldTag, question);
		Vector<JComponent> radioButtons = createRadioButtons(question, radioButtonField);
		
		return addComponentsFieldWithCustomLabel(radioButtonField, customLabel, radioButtons.toArray(new JComponent[0]));	
	}
	
	public ObjectDataInputField addRadioButtonFieldWithCustomLabelAndLink(int objectType, String fieldTag, ChoiceQuestion question, String customLabel, String htmlFileName) throws Exception
	{
		RadioButtonsField radioButtonField = createRadioButtonsField(objectType, fieldTag, question);
		Vector<JComponent> components = createRadioButtons(question, radioButtonField);
		components.add(createHyperLinkPanel(htmlFileName));
		
		return addComponentsFieldWithCustomLabel(radioButtonField, customLabel, components.toArray(new JComponent[0]));	
	}

	private JComponent createHyperLinkPanel(String htmlFileName) throws Exception
	{
		return new ClickablePanelTitleLabel(new QuestionMarkIcon(), htmlFileName);
	}

	private Vector<JComponent> createRadioButtons(ChoiceQuestion question, RadioButtonsField radioButtonField)
	{
		Vector<JComponent> radioButtons = new Vector<JComponent>();
		CodeList allCodes = question.getAllCodes();
		for (int index = 0; index < allCodes.size(); ++index)
		{
			JComponent radioButton = radioButtonField.getComponent(question.findIndexByCode(allCodes.get(index)));
			radioButtons.add(radioButton);
		}
		return radioButtons;
	}
	
	public ObjectDataInputField addFieldWithPopUpInformation(ObjectDataInputField field, String htmlFileName) throws Exception
	{
		Vector<JComponent> components = new Vector<JComponent>();
		components.add(field.getComponent());
		components.add(new FillerLabel());
		components.add(createHyperLinkPanel(htmlFileName));
		
		return addComponentsFieldWithCustomLabel(field, "", false, components.toArray(new JComponent[0]));
	}
	
	public ObjectDataInputField addComponentsFieldWithCustomLabel(ObjectDataInputField field, String customLabel, JComponent[] components)
	{
		return addComponentsFieldWithCustomLabel(field, customLabel, true, components);
	}
	
	public ObjectDataInputField addComponentsFieldWithCustomLabel(ObjectDataInputField field, String customLabel, boolean addHorizontalStrut, JComponent[] components)
	{
		addFieldToList(field);

		Box box = Box.createHorizontalBox();
		box.setBackground(AppPreferences.getDataPanelBackgroundColor());
		if (addHorizontalStrut)
			box.add(Box.createHorizontalStrut(20));
		box.add(new PanelTitleLabel(customLabel));
		for(JComponent component : components)
		{
			box.add(component);
		}

		addLabel(field.getObjectType(), field.getTag());
		addFieldComponent(box);

		return field;
	}

	public ObjectDataInputField addFieldWithCustomLabelAndHint(ObjectDataInputField field, String hint)
	{
		addFieldToList(field);
		addLabel(field.getObjectType(), field.getTag());
		createPanelForFieldWithCustomLabelAndHint(field, hint);
		return field;
	}

	public PanelTitleLabel addFieldWithCustomLabelAndDynamicHint(ObjectDataInputField field, String hint)
	{
		addFieldToList(field);
		addLabel(field.getObjectType(), field.getTag());
		return createPanelForFieldWithCustomLabelAndHint(field, hint);
	}

	private PanelTitleLabel createPanelForFieldWithCustomLabelAndHint(ObjectDataInputField field, String hint)
	{
		Box box = Box.createHorizontalBox();
		box.setBackground(AppPreferences.getDataPanelBackgroundColor());
		box.add(field.getComponent());
		box.add(Box.createHorizontalStrut(20));
		PanelTitleLabel panelTitleLabel = new PanelTitleLabel(hint);
		box.add(panelTitleLabel);
		addFieldComponent(box);
		return panelTitleLabel;
	}

	protected void addFieldsOnOneLine(String translatedString, Icon icon, ObjectDataInputField[] fields)
	{
		JPanel fieldPanel = createFieldPanel(fields);		
		fieldPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		addLabelWithIcon(translatedString, icon);
		add(fieldPanel);
	}
	
	protected void addFieldsOnOneLine(String translatedString, Icon icon, String[] fieldLabelTexts, ObjectDataInputField[] fields)
	{
		JPanel fieldPanel = createFieldPanel(fieldLabelTexts, fields);		
		fieldPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		addLabelWithIcon(translatedString, icon);
		add(fieldPanel);
	}
	
	//TODO,  come up with a better solution for this.  the reason this exists is because
	// the label visibility is changed outside of here.  
	protected void addFieldsOnOneLine(PanelTitleLabel label, Object[] labelsAndFields)
	{
		OneRowPanel fieldPanel = new OneRowPanel();
		fieldPanel.setGaps(3);
		fieldPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		for(int i = 0; i < labelsAndFields.length; i+=2)
		{
			PanelTitleLabel fieldLabel = (PanelTitleLabel) labelsAndFields[i];
			ObjectDataInputField objectDataInputField = (ObjectDataInputField) labelsAndFields[i+1];
			addFieldToList(objectDataInputField);
			fieldPanel.add(fieldLabel);
			fieldPanel.add((objectDataInputField).getComponent());
		}
		
		addTopAlignedLabel(label);
		add(fieldPanel);
	}
	
	protected void addFieldsOnOneLine(String translatedLabel, ObjectDataInputField[] fields)
	{
		JPanel fieldPanel = createFieldPanel(fields);
		addHtmlWrappedLabel(translatedLabel);
		add(fieldPanel);
	}
	
	protected void addFieldsOnOneLine(PanelTitleLabel label, ObjectDataInputField[] fields)
	{
		JPanel fieldPanel = createFieldPanel(fields);
		add(label);
		add(fieldPanel);
	}
	
	public JPanel createFieldPanel(ObjectDataInputField[] fields)
	{
		final OneRowPanel fieldPanel = new OneRowPanel();
		fieldPanel.setGaps(3);
		
		return createFieldPanel(fieldPanel, fields);
	}

	private JPanel createFieldPanel(String labelTexts[], ObjectDataField[] fields)
	{
		OneRowPanel fieldPanel = new OneRowPanel();
		fieldPanel.setGaps(3);
		fieldPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		for(int i = 0; i < fields.length; ++i)
		{
			addFieldToList(fields[i]);
			if (i < labelTexts.length)
				fieldPanel.add(new PanelTitleLabel(labelTexts[i]));
			
			fieldPanel.add(fields[i].getComponent());
			fieldPanel.add(new JLabel(" "));
		}
		
		return fieldPanel;
	}

	protected void addFieldsOnOneLineWithoutFieldLabels(String translatedLabel, ObjectDataField[] fields)
	{
		JPanel fieldPanel = createFieldPanel(new String[0], fields);
		addHtmlWrappedLabel(translatedLabel);
		add(fieldPanel);
	}
	
	protected void addFieldWithEditButton(PanelTitleLabel label, ObjectDataInputField field, PanelButton button)
	{
		addFieldToList(field);
		OneRowPanel fieldPanel = new OneRowPanel();
		fieldPanel.setGaps(3);
		fieldPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		fieldPanel.add(field.getComponent());
		fieldPanel.add(button);
		addTopAlignedLabel(label);
		add(fieldPanel);
	}

	protected void addFieldWithEditButton(String translatedLabel, ObjectDataInputField field, PanelButton button)
	{
		addFieldToList(field);
		OneRowPanel fieldPanel = new OneRowPanel();
		fieldPanel.setGaps(3);
		fieldPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		fieldPanel.add(field.getComponent());
		fieldPanel.add(button);
		addHtmlWrappedLabel(translatedLabel);
		add(fieldPanel);
	}
	
	protected boolean isOneOfOurFields(String tag)
	{
		Vector<ObjectDataField> fields = getFields();
		for(ObjectDataField field : fields)
		{
			if (tag.equals(field.getTag()))
				return true;
		}
		
		return false;
	}
}
