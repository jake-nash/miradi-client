/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.objects.BaseObject;
import org.miradi.objects.ResourcePlan;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.ResourcePlanSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;


public class ResourcePlanExporter extends AbstractPlanningObjectExporter
{
	public ResourcePlanExporter(Xmpz2XmlWriter writerToUse)
	{
		super(writerToUse, ResourcePlanSchema.getObjectType());
	}
	
	@Override
	protected void writeFields(BaseObject baseObject, final BaseObjectSchema baseObjectSchema) throws Exception
	{
		super.writeFields(baseObject, baseObjectSchema);

		ResourcePlan resourcePlan = (ResourcePlan) baseObject;
		String idElementName = RESOURCE_ID_ELEMENT_NAME + ID;

		getWriter().writeRefIfValid(baseObjectSchema.getObjectName(), idElementName, resourcePlan.getResourceRef());
	}
	
	@Override
	protected boolean doesFieldRequireSpecialHandling(String tag)
	{
		if (tag.equals(ResourcePlan.TAG_RESOURCE_ID))
			return true;

		return super.doesFieldRequireSpecialHandling(tag);
	}

	@Override
	protected String getDateUnitElementName()
	{
		return WORK_UNITS_DATE_UNIT;
	}

	@Override
	protected String getDayElementName()
	{
		return WORK_UNITS_DAY;
	}

	@Override
	protected String getMonthElementName()
	{
		return WORK_UNITS_MONTH;
	}

	@Override
	protected String getQuarterElementName()
	{
		return WORK_UNITS_QUARTER;
	}

	@Override
	protected String getYearElementName()
	{
		return WORK_UNITS_YEAR;
	}

	@Override
	protected String getFullProjectTimespanElementName()
	{
		return WORK_UNITS_FULL_PROJECT_TIMESPAN;
	}

	@Override
	protected String getQuantityElementName()
	{
		return WORK_UNITS;
	}

	@Override
	protected String getDateUnitsElementName()
	{
		return DATE_UNIT_WORK_UNITS;
	}

	@Override
	protected String getPoolName()
	{
		return RESOURCE_PLAN;
	}

}
