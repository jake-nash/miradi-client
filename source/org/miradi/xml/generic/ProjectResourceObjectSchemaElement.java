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

package org.miradi.xml.generic;

import org.miradi.objects.ProjectResource;

public class ProjectResourceObjectSchemaElement extends AbstractBaseObjectSchemaElement
{
	public ProjectResourceObjectSchemaElement()
	{
		super(XmlConstants.PROJECT_RESOURCE);
		
		createCodeField(XmlSchemaCreator.RESOURCE_TYPE_ELEMENT_NAME, XmlSchemaCreator.VOCABULARY_RESOURCE_TYPE);
		createOptionalTextField(ProjectResource.TAG_GIVEN_NAME);
		createOptionalTextField(ProjectResource.TAG_SUR_NAME);
		createOptionalTextField(ProjectResource.TAG_INITIALS);
		createOptionalCodeListField(XmlSchemaCreator.RESOURCE_ROLE_CODES_ELEMENT_NAME);
		createOptionalTextField(ProjectResource.TAG_ORGANIZATION);
		createOptionalTextField(ProjectResource.TAG_POSITION);
		createOptionalTextField(ProjectResource.TAG_LOCATION);
		createOptionalTextField(ProjectResource.TAG_PHONE_NUMBER);
		createOptionalTextField(ProjectResource.TAG_PHONE_NUMBER_MOBILE);
		createOptionalTextField(ProjectResource.TAG_PHONE_NUMBER_HOME);
		createOptionalTextField(ProjectResource.TAG_PHONE_NUMBER_OTHER);
		createOptionalTextField(ProjectResource.TAG_EMAIL);
		createOptionalTextField(ProjectResource.TAG_ALTERNATIVE_EMAIL);
		createOptionalTextField(ProjectResource.TAG_IM_ADDRESS);
		createOptionalTextField(ProjectResource.TAG_IM_SERVICE);
		createOptionalDateField(ProjectResource.TAG_DATE_UPDATED);
		createOptionalNumericField(ProjectResource.TAG_COST_PER_UNIT);
		createOptionalTextField(ProjectResource.TAG_COMMENTS);
		createOptionalBooleanField(ProjectResource.TAG_IS_CCN_COACH);
		createOptionalTextField(ProjectResource.TAG_CUSTOM_FIELD_1);
		createOptionalTextField(ProjectResource.TAG_CUSTOM_FIELD_2);
	}
}
