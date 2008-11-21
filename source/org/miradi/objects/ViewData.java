/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import java.text.ParseException;

import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.objectdata.CodeListData;
import org.miradi.objectdata.IntegerData;
import org.miradi.objectdata.ORefData;
import org.miradi.objectdata.ORefListData;
import org.miradi.objectdata.ObjectData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.questions.InternalQuestionWithoutValues;
import org.miradi.utils.EnhancedJsonObject;

public class ViewData extends BaseObject
{
	public ViewData(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}

	public ViewData(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}

	public Command[] buildCommandsToAddNode(ORef oRefToAdd) throws ParseException
	{
		if(getCurrentMode().equals(MODE_DEFAULT))
			return new Command[0];
		
		CommandSetObjectData cmd = CommandSetObjectData.createAppendORefCommand(this, TAG_CHAIN_MODE_FACTOR_REFS, oRefToAdd);
		return new Command[] {cmd};
	}

	public Command[] buildCommandsToRemoveNode(ORef oRefToRemove) throws ParseException
	{
		if(getCurrentMode().equals(MODE_DEFAULT))
			return new Command[0];
		
		ORefList currentORefs = new ORefList(getData(TAG_CHAIN_MODE_FACTOR_REFS));
		if(!currentORefs.contains(oRefToRemove))
			return new Command[0];
		
		CommandSetObjectData cmd = CommandSetObjectData.createRemoveORefCommand(this, TAG_CHAIN_MODE_FACTOR_REFS, oRefToRemove);
		return new Command[] {cmd};
	}
	
	@Override
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_CHAIN_MODE_FACTOR_REFS))
			return true;
		
		return super.isRefList(tag);
	}
	
	public ORef getCurrentConceptualModelRef()
	{
		return currentConceptualModelRef.getRawRef();
	}
	
	public ORef getCurrentResultsChainRef()
	{
		return currentResultsChainRef.getRawRef();
	}
	
	public void setCurrentTab(int newTab) throws Exception
	{
		currentTab.set(Integer.toString(newTab));
	}
	
	public int getCurrentTab()
	{
		return currentTab.asInt();
	}

	public String getCurrentMode()
	{
		return currentMode.get();
	}

	public int getType()
	{
		return getObjectType();
	}
	
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.VIEW_DATA;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	@Override
	protected ORefList getRefListForField(ObjectData field)
	{
		// NOTE: Expansion list is not considered a true reference to those objects
		if(field.getTag().equals(TAG_CURRENT_EXPANSION_LIST))
			return new ORefList();
		
		return super.getRefListForField(field);
	}
	
	
	void clear()
	{
		super.clear();
		currentMode = new StringData(TAG_CURRENT_MODE);
		chainModeFactorRefs = new ORefListData(TAG_CHAIN_MODE_FACTOR_REFS);
		currentTab = new IntegerData(TAG_CURRENT_TAB);
		expandedNodesList = new ORefListData(TAG_CURRENT_EXPANSION_LIST);
		currentResultsChainRef = new ORefData(TAG_CURRENT_RESULTS_CHAIN_REF);
		currentConceptualModelRef = new ORefData(TAG_CURRENT_CONCEPTUAL_MODEL_REF);
		diagramHiddenTypes = new CodeListData(TAG_DIAGRAM_HIDDEN_TYPES, getQuestion(InternalQuestionWithoutValues.class));
		planningStyleChoice = new StringData(TAG_PLANNING_STYLE_CHOICE);
		planningSingleLevelChoice = new StringData(TAG_PLANNING_SINGLE_LEVEL_CHOICE);
		planningCustomChoiceRef = new ORefData(TAG_PLANNING_CUSTOM_PLAN_REF);
		
		addField(TAG_CURRENT_CONCEPTUAL_MODEL_REF, currentConceptualModelRef);
		addField(TAG_CURRENT_RESULTS_CHAIN_REF, currentResultsChainRef);
		addField(TAG_CURRENT_MODE, currentMode);
		addField(TAG_CHAIN_MODE_FACTOR_REFS, chainModeFactorRefs);
		addField(TAG_CURRENT_TAB, currentTab);
		addField(TAG_CURRENT_EXPANSION_LIST, expandedNodesList);
		addField(TAG_DIAGRAM_HIDDEN_TYPES, diagramHiddenTypes);
		
		addField(TAG_PLANNING_STYLE_CHOICE, planningStyleChoice);
		addField(TAG_PLANNING_SINGLE_LEVEL_CHOICE, planningSingleLevelChoice);
		addField(TAG_PLANNING_CUSTOM_PLAN_REF, planningCustomChoiceRef);
	}

	public static final String TAG_CURRENT_CONCEPTUAL_MODEL_REF = "CurrentConceptualModelRef";
	public static final String TAG_CURRENT_RESULTS_CHAIN_REF = "CurrentResultsChainRef";
	public static final String TAG_CURRENT_MODE = "CurrentMode";
	public static final String TAG_CHAIN_MODE_FACTOR_REFS = "ChainModeFactorRefs";
	public static final String TAG_CURRENT_TAB = "CurrentTab";
	public static final String TAG_CURRENT_EXPANSION_LIST  = "CurrentExpansionList";
	public static final String TAG_DIAGRAM_HIDDEN_TYPES = "DiagramHiddenTypes";
	
	public static final String TAG_PLANNING_STYLE_CHOICE = "PlanningStyleChoice";
	public static final String TAG_PLANNING_SINGLE_LEVEL_CHOICE = "SingleLevelChoice";
	public static final String TAG_PLANNING_CUSTOM_PLAN_REF = "CustomPlanRef";
	
	public static final String MODE_DEFAULT = "";
	public static final String MODE_STRATEGY_BRAINSTORM = "StrategyBrainstorm";

	public static final String OBJECT_NAME = "ViewData";
	
	private IntegerData currentTab;
	private StringData currentMode;
	private ORefListData chainModeFactorRefs;
	private ORefData currentResultsChainRef;
	private ORefData currentConceptualModelRef;
	private ORefListData expandedNodesList;
	private CodeListData diagramHiddenTypes;
	private StringData planningStyleChoice;
	private StringData planningSingleLevelChoice;
	private ORefData planningCustomChoiceRef;
}
