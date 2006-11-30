/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.ORefData;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Task extends EAMBaseObject
{
	public Task(BaseId idToUse, CreateTaskParameter extraInfo) throws Exception
	{
		super(idToUse);
		clear();
		parentRef.set(extraInfo.getParentRef().toString());
	}
	
	public Task(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
		parentRef.set(json.optString(TAG_PARENT_REF));
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject jsonObject = super.toJson();
		jsonObject.put(TAG_PARENT_REF, parentRef.toString());
		
		return jsonObject;
	}

	public int getType()
	{
		return ObjectType.TASK;
	}

	public void addSubtaskId(BaseId subtaskId)
	{
		subtaskIds.add(subtaskId);
	}
	
	public int getSubtaskCount()
	{
		return subtaskIds.size();
	}
	
	public BaseId getSubtaskId(int index)
	{
		return subtaskIds.get(index);
	}
	
	public IdList getSubtaskIdList()
	{
		return subtaskIds.getIdList().createClone();
	}
	
	public int getResourceCount()
	{
		return resourceIds.size();
	}
	
	public IdList getResourceIdList()
	{
		return resourceIds.getIdList().createClone();
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateTaskParameter(parentRef.getRawRef());
	}

	public String toString()
	{
		return getLabel();
	}
	
	
	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if (fieldTag.equals(TAG_PARENT_REF))
			parentRef.set(dataValue);
		else
			super.setData(fieldTag, dataValue);
	}
	
	public String getData(String fieldTag)
	{
		if (fieldTag.equals(TAG_PARENT_REF))
			return parentRef.get();
		
		return super.getData(fieldTag);
	}


	public void clear()
	{
		super.clear();
		parentRef = new ORefData();
		subtaskIds = new IdListData();
		resourceIds = new IdListData();
		
		addField(TAG_SUBTASK_IDS, subtaskIds);
		addField(TAG_RESOURCE_IDS, resourceIds);
	}

	public final static String TAG_PARENT_REF = "ParentRef";
	public final static String TAG_SUBTASK_IDS = "SubtaskIds";
	public final static String TAG_RESOURCE_IDS = "ResourceIds";
	public final static String PSEUDO_TAG_FACTOR_LABEL = "PseudoTagFactorLabel";
	
	IdListData subtaskIds;
	IdListData resourceIds;
	ORefData parentRef;
}
