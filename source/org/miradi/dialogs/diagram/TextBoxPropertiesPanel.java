/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
package org.miradi.dialogs.diagram;

import javax.swing.JComponent;

import org.miradi.dialogfields.RadioButtonsField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.TextBox;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;
import org.miradi.questions.ZPositionQuestion;

public class TextBoxPropertiesPanel extends ObjectDataInputPanel
{
	public TextBoxPropertiesPanel(Project projectToUse, DiagramFactor diagramFactor)
	{
		super(projectToUse, diagramFactor.getWrappedORef());

		setObjectRefs(new ORef[] {diagramFactor.getWrappedORef(), diagramFactor.getRef()});

		addField(createExpandableField(TextBox.TAG_LABEL));
		addField(createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_BACKGROUND_COLOR, new DiagramFactorBackgroundQuestion()));
		addField(createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FONT_SIZE, new DiagramFactorFontSizeQuestion()));
		addField(createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FOREGROUND_COLOR, new DiagramFactorFontColorQuestion()));
		addField(createChoiceField(DiagramFactor.getObjectType(), DiagramFactor.TAG_FONT_STYLE, new DiagramFactorFontStyleQuestion()));
		
		ChoiceQuestion zPositionQuestion = getProject().getQuestion(ZPositionQuestion.class);
		RadioButtonsField zPositionField = createRadioButtonsField(TextBox.getObjectType(), TextBox.TAG_Z_POSITION_CODE, zPositionQuestion);
		JComponent behindRadioButton = zPositionField.getComponent(zPositionQuestion.findIndexByCode(ZPositionQuestion.BEHIND_CODE));
		JComponent frontRadioButton = zPositionField.getComponent(zPositionQuestion.findIndexByCode(ZPositionQuestion.FRONT_CODE));
		addRadioButtonFieldWithCustomLabel(zPositionField, new JComponent[]{behindRadioButton, frontRadioButton, });

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Text Box Properties");
	}
}
