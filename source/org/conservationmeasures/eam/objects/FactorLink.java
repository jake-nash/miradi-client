/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class FactorLink extends EAMBaseObject
{
	public FactorLink(FactorLinkId id, FactorId fromNodeId, FactorId toNodeId)
	{
		super(id);
		setFromId(fromNodeId);
		setToId(toNodeId);
		stressLabel = new StringData();
	}

	public FactorLink(int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(new FactorLinkId(idAsInt), jsonObject);
		fromId = new FactorId(jsonObject.getInt(TAG_FROM_ID));
		toId = new FactorId(jsonObject.getInt(TAG_TO_ID));
		stressLabel = new StringData(jsonObject.optString(TAG_STRESS_LABEL));
	}
	
	public void setFromId(FactorId fromNodeId)
	{
		fromId = fromNodeId;
	}
	
	public void setToId(FactorId toNodeId)
	{
		toId = toNodeId;
	}

	public int getType()
	{
		return ObjectType.MODEL_LINKAGE;
	}
	
	public FactorId getFromNodeId()
	{
		return fromId;
	}
	
	public FactorId getToNodeId()
	{
		return toId;
	}
	
	public String getStressLabel()
	{
		return stressLabel.get();
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateFactorLinkParameter(getFromNodeId(), getToNodeId());
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(TAG_STRESS_LABEL.equals(fieldTag))
			stressLabel.set(dataValue);
		else
			super.setData(fieldTag, dataValue);
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_STRESS_LABEL.equals(fieldTag))
			return getStressLabel();
		
		return super.getData(fieldTag);
	}
	
	public FactorId getNodeId(int direction)
	{
		if(direction == FROM)
			return getFromNodeId();
		if(direction == TO)
			return getToNodeId();
		throw new RuntimeException("Link: Unknown direction " + direction);
	}
	
	public FactorId getOppositeNodeId(int direction)
	{
		if(direction == FROM)
			return getNodeId(TO);
		if(direction == TO)
			return getNodeId(FROM);
		throw new RuntimeException("Link: Unknown direction " + direction);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();
		json.put(TAG_FROM_ID, fromId.asInt());
		json.put(TAG_TO_ID, toId.asInt());
		json.put(TAG_STRESS_LABEL, getStressLabel());
		return json;
	}
	
	private static String TAG_FROM_ID = "FromId";
	private static String TAG_TO_ID = "ToId";
	public static String TAG_STRESS_LABEL = "StressLabel";
	
	public static final int FROM = 1;
	public static final int TO = 2;
	
	private FactorId fromId;
	private FactorId toId;
	private StringData stressLabel;
}
