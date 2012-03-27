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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.WwfProjectDataSchema;
import org.miradi.utils.EnhancedJsonObject;

public class WwfProjectData extends BaseObject
{
	public WwfProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, new WwfProjectDataSchema());
		clear();
	}
	
	public WwfProjectData(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject, new WwfProjectDataSchema());
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public static int getObjectType()
	{
		return ObjectType.WWF_PROJECT_DATA;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public static WwfProjectData find(ObjectManager objectManager, ORef wwfProjectDataRef)
	{
		return (WwfProjectData) objectManager.findObject(wwfProjectDataRef);
	}
	
	public static WwfProjectData find(Project project, ORef wwfProjectDataRef)
	{
		return find(project.getObjectManager(), wwfProjectDataRef);
	}
	
	public static final String TAG_MANAGING_OFFICES = "ManagingOffices";
	public static final String TAG_REGIONS = "Regions";
	public static final String TAG_COUNTRIES = "Countries";
	public static final String TAG_ECOREGIONS = "EcoRegions";
	
	public static final String OBJECT_NAME = "WwfProjectData";
}
