/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.AudienceSchema;
import org.miradi.utils.EnhancedJsonObject;

public class Audience extends BaseObject
{
	public Audience(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse, new AudienceSchema());
	}
		
	public Audience(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json, new AudienceSchema());
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
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
		return objectType == AudienceSchema.getObjectType();
	}
	
	public static Audience find(ObjectManager objectManager, ORef ref)
	{
		return (Audience) objectManager.findObject(ref);
	}
	
	public static Audience find(Project project, ORef ref)
	{
		return find(project.getObjectManager(), ref);
	}
	
	public static final String TAG_PEOPLE_COUNT = "PeopleCount";
	public static final String TAG_SUMMARY = "Summary";
}
