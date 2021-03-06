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
package org.miradi.objecthelpers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.miradi.ids.IdList;
import org.miradi.objects.BaseObject;

public class ORefSet extends HashSet<ORef>
{
	public ORefSet()
	{
		super();
	}

	public ORefSet(BaseObject baseObject)
	{
		this(baseObject.getRef());
	}
	
	public ORefSet(ORef ref)
	{
		this();
		add(ref);
	}
	
	public ORefSet(IdList idList)
	{
		this(new ORefList(idList));
	}

	public ORefSet(ORefList refList)
	{
		this();
		addAll(Arrays.asList(refList.toArray()));
	}
	
	public ORefSet(BaseObject[] baseObjects)
	{
		this(new ORefList(baseObjects));
	}

	public ORefSet(Set<ORef> other)
	{
		super(other);
	}

	public ORefSet(Vector<? extends BaseObject> baseObjects)
	{
		this(new ORefList(baseObjects));
	}

	public void addAllRefs(ORefList refs)
	{
		addAll(Arrays.asList(refs.toArray()));
	}

	public static ORefSet subtract(ORefSet newReferrals, ORefSet oldReferrals)
	{
		ORefSet result = new ORefSet();
		for(ORef ref : newReferrals)
		{
			if(!oldReferrals.contains(ref))
				result.add(ref);
		}

		return result;
	}

	public boolean containsAny(ORefSet other)
	{
		for(ORef ref : other)
		{
			if(contains(ref))
				return true;
		}
		
		return false;
	}

	public ORefList toRefList()
	{
		return new ORefList(toArray(new ORef[0]));
	}
	
	public IdList toIdList(int objectType)
	{
		return toRefList().convertToIdList(objectType);
	}
	
	public ORefSet getFilteredBy(int typeToFilterOn)
	{
		ORefSet newList = new ORefSet();
		for(ORef ref : this)
		{
			if (ref.getObjectType() == typeToFilterOn)
				newList.add(ref);	
		}
		
		return newList;
	}
	
	public void addRef(BaseObject baseObject)
	{
		add(baseObject.getRef());
	}
	
	public boolean hasData()
	{
		return !isEmpty();
	}
	
	public static ORefSet getNonOverlappingRefs(ORefSet list1, ORefSet list2)
	{
		ORefSet nonOverlappingRefs = new ORefSet();
		nonOverlappingRefs.addAll(getNonOverlapping(list1, list2));
		nonOverlappingRefs.addAll(getNonOverlapping(list2, list1));
		
		return nonOverlappingRefs;
	}
	
	private static ORefSet getNonOverlapping(ORefSet smallerList, ORefSet biggerList)
	{
		ORefSet nonOverlapping = new ORefSet();
		for (ORef ref : biggerList)
		{
			if (!smallerList.contains(ref))
				nonOverlapping.add(ref);
		}
		
		return nonOverlapping;
	}

	public static ORefSet getInvalidRefs(ORefSet oRefSet)
	{
		ORefSet invalidRefs = new ORefSet();
		for(ORef ref : oRefSet)
		{
			if (ref.isInvalid())
				invalidRefs.add(ref);
		}

		return invalidRefs;
	}
}
