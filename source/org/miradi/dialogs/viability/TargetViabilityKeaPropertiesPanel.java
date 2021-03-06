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
package org.miradi.dialogs.viability;

import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.icons.KeyEcologicalAttributeIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.project.Project;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.schemas.KeyEcologicalAttributeSchema;

public class TargetViabilityKeaPropertiesPanel extends ObjectDataInputPanelWithSections
{
	public TargetViabilityKeaPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, ObjectType.TARGET);
		createSingleSection(EAM.text("Summary"));

		ObjectDataInputField shortLabelField = createShortStringField(KeyEcologicalAttributeSchema.getObjectType(), KeyEcologicalAttribute.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(KeyEcologicalAttributeSchema.getObjectType(), KeyEcologicalAttribute.TAG_LABEL);		
		addFieldsOnOneLine(EAM.text("Key Ecological Attribute (KEA)"), new KeyEcologicalAttributeIcon(), new ObjectDataInputField[]{shortLabelField, labelField});
		
		addField(createMultilineField(KeyEcologicalAttributeSchema.getObjectType(), KeyEcologicalAttribute.TAG_DETAILS));
		addField(createChoiceField(KeyEcologicalAttributeSchema.getObjectType(), KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, new KeyEcologicalAttributeTypeQuestion()));
		addTaxonomyFields(KeyEcologicalAttributeSchema.getObjectType());
		addField(createMultilineField(KeyEcologicalAttributeSchema.getObjectType(), KeyEcologicalAttribute.TAG_DESCRIPTION));
		
		updateFieldsFromProject();
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Key Ecological Attribute Properties");
	}
}
