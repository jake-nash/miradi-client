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

package org.miradi.schemas;

import org.miradi.objecthelpers.ObjectType;


public class UnspecifiedBaseObjectSchema extends BaseObjectSchema
{
	public UnspecifiedBaseObjectSchema(final String objectNameToUse)
	{
		this(ObjectType.FAKE, objectNameToUse);
	}

	public UnspecifiedBaseObjectSchema(final int objectTypeToUse, final String objectNameToUse)
	{
		super();
		
		objectType = objectTypeToUse;
		objectName = objectNameToUse;
	}
	
	@Override
	public int getType()
	{
		return objectType;
	}

	@Override
	public String getObjectName()
	{
		return objectName;
	}

	private int objectType;
	private String objectName;
}
