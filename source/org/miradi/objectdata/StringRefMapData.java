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
package org.miradi.objectdata;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.schemas.AbstractFieldSchema;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2XmlSchemaCreator;

public class StringRefMapData extends ObjectData
{
	public StringRefMapData(String tagToUse)
	{
		super(tagToUse);
		data = new StringRefMap();
	}
	
	@Override
	public boolean isStringRefMapData()
	{
		return true;
	}

	@Override
	public String get()
	{
		return getStringRefMap().toJsonString();
	}
	
	public StringRefMap getStringRefMap()
	{
		return data;
	}

	@Override
	public ORefList getRefList()
	{
		ORef[] allRefs = data.getValues().toArray(new ORef[0]);
		ORefList allValues = new ORefList(allRefs);
		
		return allValues;
	}

	@Override
	public void set(String newValue) throws Exception
	{
		data = new StringRefMap(newValue);
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof StringRefMapData))
			return false;

		StringRefMapData other = (StringRefMapData) rawOther;
		return other.data.equals(data);
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public void writeAsXmpz2XmlData(Xmpz2XmlWriter writer, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		writer.writeRefMapXenoData(get());
	}
	
	@Override
	public String createXmpz2SchemaElementString(Xmpz2XmlSchemaCreator creator, BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema) throws Exception
	{
		return creator.createStringRefMapSchemaElement(baseObjectSchema, fieldSchema);
	}

	private StringRefMap data;
}
