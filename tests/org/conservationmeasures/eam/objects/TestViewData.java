/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;

public class TestViewData extends EAMTestCase
{
	public TestViewData(String name)
	{
		super(name);
	}
	
	public void testCurrentTab() throws Exception
	{
		ViewData vd = new ViewData(new BaseId(33));
		assertEquals("0", vd.getData(ViewData.TAG_CURRENT_TAB));
		int tab = 6;
		vd.setData(ViewData.TAG_CURRENT_TAB, Integer.toString(tab));
		assertEquals(6, new Integer(vd.getData(ViewData.TAG_CURRENT_TAB)).intValue());
		
		ViewData got = new ViewData(55, vd.toJson());
		assertEquals(vd.getData(ViewData.TAG_CURRENT_TAB), got.getData(ViewData.TAG_CURRENT_TAB));
	}

	
	public void testCurrentSortData() throws Exception
	{
		
		ViewData vd1 = new ViewData(new BaseId(33));
		vd1.setData(ViewData.TAG_CURRENT_SORT_BY, "TAREGT");
		assertEquals("TAREGT", vd1.getData(ViewData.TAG_CURRENT_SORT_BY));

		ViewData vd2 = new ViewData(new BaseId(34));
		vd2.setData(ViewData.TAG_CURRENT_SORT_DIRECTION, "ASCENDING");
		assertEquals("ASCENDING", vd2.getData(ViewData.TAG_CURRENT_SORT_DIRECTION));
		
	}
	
	
	public void testMode() throws Exception
	{
		ViewData vd = new ViewData(new BaseId(33));
		String modeTag = ViewData.TAG_CURRENT_MODE;
		assertEquals("Didn't start with default mode?", "", vd.getData(modeTag));
		String sampleMode = "Brainstorm";
		vd.setData(modeTag, sampleMode);
		assertEquals("Set/get didn't work?", sampleMode, vd.getData(modeTag));
		
		ViewData got = (ViewData)EAMBaseObject.createFromJson(vd.getType(), vd.toJson());
		assertEquals("json didn't preserve mode?", vd.getData(modeTag), got.getData(modeTag));
	}
	
	public void testBrainstormNodeIds() throws Exception
	{
		ViewData vd = new ViewData(new BaseId(33));
		String idsTag = ViewData.TAG_BRAINSTORM_NODE_IDS;
		assertEquals("didn't start with empty id list?", 0, new IdList(vd.getData(idsTag)).size());
		IdList sampleIds = createSampleIdList();
		vd.setData(idsTag, sampleIds.toString());
		assertEquals("Set/get didn't work?", sampleIds, new IdList(vd.getData(idsTag)));

		ViewData got = (ViewData)EAMBaseObject.createFromJson(vd.getType(), vd.toJson());
		assertEquals("json didn't preserve ids?", vd.getData(idsTag), got.getData(idsTag));
	}

	private IdList createSampleIdList()
	{
		IdList sampleIds = new IdList();
		sampleIds.add(7);
		sampleIds.add(41);
		return sampleIds;
	}
	
	public void testBuildCommandsToInsertNode() throws Exception
	{
		ViewData vd = new ViewData(new BaseId(33));
		IdList sampleIds = createSampleIdList();
		vd.setData(ViewData.TAG_BRAINSTORM_NODE_IDS, sampleIds.toString());
		BaseId idToAdd = new BaseId(983);
		Command[] inNormalMode = vd.buildCommandsToAddNode(idToAdd);
		assertEquals("added when not in brainstorm mode?", 0, inNormalMode.length);
		
		vd.setData(ViewData.TAG_CURRENT_MODE, ViewData.MODE_STRATEGY_BRAINSTORM);
		Command[] inBrainstormMode = vd.buildCommandsToAddNode(idToAdd);
		assertEquals("didn't add when in brainstorm mode?", 1, inBrainstormMode.length);
		CommandSetObjectData cmd = (CommandSetObjectData)inBrainstormMode[0];
		IdList expected = new IdList(sampleIds);
		expected.add(idToAdd);
		assertEquals("command wrong id?", vd.getId(), cmd.getObjectId());
		assertEquals("command wrong field?", ViewData.TAG_BRAINSTORM_NODE_IDS, cmd.getFieldTag());
		assertEquals("didn't create proper command?", expected.toString(), cmd.getDataValue());
	}
	
	public void testBuildCommandsToRemoveNode() throws Exception
	{
		ViewData vd = new ViewData(new BaseId(33));
		IdList sampleIds = createSampleIdList();
		vd.setData(ViewData.TAG_BRAINSTORM_NODE_IDS, sampleIds.toString());
		FactorId idToRemove = new FactorId(sampleIds.get(0).asInt());
		Command[] inNormalMode = vd.buildCommandsToRemoveNode(idToRemove);
		assertEquals("removed when not in brainstorm mode?", 0, inNormalMode.length);
		
		vd.setData(ViewData.TAG_CURRENT_MODE, ViewData.MODE_STRATEGY_BRAINSTORM);
		Command[] inBrainstormMode = vd.buildCommandsToRemoveNode(idToRemove);
		assertEquals("didn't remove when in brainstorm mode?", 1, inBrainstormMode.length);
		CommandSetObjectData cmd = (CommandSetObjectData)inBrainstormMode[0];
		IdList expected = new IdList(sampleIds);
		expected.removeId(idToRemove);
		assertEquals("command wrong id?", vd.getId(), cmd.getObjectId());
		assertEquals("command wrong field?", ViewData.TAG_BRAINSTORM_NODE_IDS, cmd.getFieldTag());
		assertEquals("didn't create proper command?", expected.toString(), cmd.getDataValue());
	}
}
