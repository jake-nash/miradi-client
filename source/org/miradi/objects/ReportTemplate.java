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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.ReportTemplateSchema;

public class ReportTemplate extends BaseObject
{
	public ReportTemplate(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static ReportTemplateSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static ReportTemplateSchema createSchema(ObjectManager objectManager)
	{
		return (ReportTemplateSchema) objectManager.getSchemas().get(ObjectType.REPORT_TEMPLATE);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	@Override
	public String getShortLabel()
	{
		return getData(TAG_SHORT_LABEL);
	}
	
	public static boolean is(BaseObject object)
	{
		return is(object.getRef());
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == ReportTemplateSchema.getObjectType();
	}
	
	public static ReportTemplate find(ObjectManager objectManager, ORef reportTemplateRef)
	{
		return (ReportTemplate) objectManager.findObject(reportTemplateRef);
	}
	
	public static ReportTemplate find(Project project, ORef reportTemplateRef)
	{
		return find(project.getObjectManager(), reportTemplateRef);
	}

	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_INCLUDE_SECTION_CODES = "IncludeSectionCodes";
	public static final String TAG_COMMENTS = "Comments";
}
