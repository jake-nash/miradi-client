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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.objectdata.RelevancyOverrideSetData;
import org.miradi.objecthelpers.*;
import org.miradi.questions.StrategyStatusQuestion;
import org.miradi.schemas.ProgressReportSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.CommandVector;

public class TestStrategy extends AbstractObjectWithBudgetDataToDeleteTestCase
{
	public TestStrategy(String name)
	{
		super(name);
	}
		
	@Override
	protected int getType()
	{
		return ObjectType.STRATEGY;
	}
	
	@Override
	protected BaseObject createParentObject() throws Exception
	{
		return getProject().createStrategy();
	}
	
	public void testFields() throws Exception
	{
		verifyFields(getType());
	}
	
	public void testBasics() throws Exception
	{
		FactorId strategyId = new FactorId(17);
		Strategy strategy = new Strategy(getObjectManager(), strategyId);
		assertEquals("already has activities?", 0, strategy.getActivityIds().size());
		
		IdList afterAdd1 = strategy.getActivityIds();
		afterAdd1.add(new BaseId(838));
		strategy.setData(Strategy.TAG_ACTIVITY_IDS, afterAdd1.toString());
		assertEquals("didn't add?", afterAdd1.toString(), strategy.getData(Strategy.TAG_ACTIVITY_IDS));
		
		strategy.setData(Strategy.TAG_ACTIVITY_IDS, "");
		assertEquals("didn't remove?", "", strategy.getData(Strategy.TAG_ACTIVITY_IDS));
	}
	
	public void testActivityIds() throws Exception
	{
		FactorId strategyId = new FactorId(66);
		Strategy strategy = new Strategy(getObjectManager(), strategyId);
		IdList empty = new IdList(TaskSchema.getObjectType(), strategy.getData(Strategy.TAG_ACTIVITY_IDS));
		assertEquals("not empty to start?", 0, empty.size());
		
		BaseId activityId = new BaseId(828);
		IdList oneItem = new IdList(TaskSchema.getObjectType());
		oneItem.add(activityId);
		strategy.setData(Strategy.TAG_ACTIVITY_IDS, oneItem.toString());
		
		IdList got = new IdList(TaskSchema.getObjectType(), strategy.getData(Strategy.TAG_ACTIVITY_IDS));
		assertEquals("round trip failed?", oneItem, got);
	}
	
	public void testStatus() throws Exception
	{
		FactorId strategyId = new FactorId(91);
		Strategy strategy = new Strategy(getObjectManager(), strategyId);
		assertTrue("didn't default to real status?", strategy.isStatusReal());
		assertFalse("defaulted to draft status?", strategy.isStatusDraft());
		strategy.setData(Strategy.TAG_STATUS, StrategyStatusQuestion.STATUS_DRAFT_CODE);
		assertEquals("set/get didn't work?", StrategyStatusQuestion.STATUS_DRAFT_CODE, strategy.getData(Strategy.TAG_STATUS));
		assertFalse("didn't unset real status?", strategy.isStatusReal());
		assertTrue("didn't set to draft status?", strategy.isStatusDraft());
		strategy.setData(Strategy.TAG_STATUS, StrategyStatusQuestion.STATUS_REAL_CODE);
		assertTrue("didn't restore to real status?", strategy.isStatusReal());
		assertFalse("didn't unset draft status?", strategy.isStatusDraft());
		strategy.setData(Strategy.TAG_STATUS, "OIJFW*FJJF");
		assertTrue("didn't treat unknown as real?", strategy.isStatusReal());
		assertFalse("treated unknown as draft?", strategy.isStatusDraft());
		
	}
	
	public void testIsAssignmentDataSuperseded() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		TestTask.verifyIsAssignmentDataSuperseded(getProject(), strategy, Strategy.TAG_ACTIVITY_IDS);
	}
	
	public void testCreateCommandsToDeleteChildren() throws Exception
	{
		Strategy strategy = getProject().createStrategy();		
		TestObjective.verifyAnnotationIsDeletedFromParent(getProject(), strategy, Strategy.TAG_PROGRESS_REPORT_REFS, ProgressReportSchema.getObjectType());
	}
	
	public void testStrategyIsRemovedFromObjectiveRelevancyList() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Cause cause = getProject().createCause();
		Objective objective = getProject().createObjective(cause);
		verifyRelevancyIsUpdatedAfterStrategyOrActivityIsDeleted(strategy, objective);
	}
	
	public void testStrategyIsRemovedFromGoalRelevancyListWhenDeleted() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Target goalOwner = getProject().createTarget();
		Goal goal = getProject().createGoal(goalOwner);
		verifyRelevancyIsUpdatedAfterStrategyOrActivityIsDeleted(strategy, goal);
	}
	
	public void testStrategyIsRemovedFromIndicatorRelevancyListWhenDeleted() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createIndicator(strategy);
		verifyRelevancyIsUpdatedAfterStrategyOrActivityIsDeleted(strategy, indicator);
	}

	public void testActivityIsRemovedFromObjectiveRelevancyListWhenDeleted() throws Exception
	{
		Task activity = getProject().createActivity();
		Strategy objectiveOwner = getProject().createStrategy();
		Objective objective = getProject().createObjective(objectiveOwner);
		verifyRelevancyIsUpdatedAfterStrategyOrActivityIsDeleted(activity, objective);
	}

	public void testActivityIsRemovedFromGoalRelevancyListWhenDeleted() throws Exception
	{
		Task activity = getProject().createActivity();
		Target goalOwner = getProject().createTarget();
		Goal goal = getProject().createGoal(goalOwner);
		verifyRelevancyIsUpdatedAfterStrategyOrActivityIsDeleted(activity, goal);
	}

	public void testActivityIsRemovedFromIndicatorRelevancyListWhenDeleted() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Task activity = getProject().createActivity(strategy);
		Indicator indicator = getProject().createIndicator(strategy);
		verifyRelevancyIsUpdatedAfterStrategyOrActivityIsDeleted(activity, indicator);
	}

	private void verifyRelevancyIsUpdatedAfterStrategyOrActivityIsDeleted(BaseObject itemInRelevancyListToBeDeleted, Desire desire) throws Exception
	{
		RelevancyOverrideSet relevancyOverrides = new RelevancyOverrideSet();
		relevancyOverrides.add(new RelevancyOverride(itemInRelevancyListToBeDeleted.getRef(), true));
		getProject().fillObjectUsingCommand(desire, Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancyOverrides.toString());

		ORefList relevantStrategyAndActivityRefs = new ORefList(getAllStrategyAndActivityRefsFromRelevancyOverrides(desire));
		ORefList relevantActivityRefs = relevantStrategyAndActivityRefs.getFilteredBy(itemInRelevancyListToBeDeleted.getType());
		assertEquals("Desire's activity relevancy list was not updated?", 1, relevantActivityRefs.size());
		
		CommandVector commandsToDelete = itemInRelevancyListToBeDeleted.createCommandsToDeleteChildrenAndObject();
		getProject().executeCommands(commandsToDelete);
		
		ORefList relevantStrategyAndActivityRefsAfterDelete = new ORefList(getAllStrategyAndActivityRefsFromRelevancyOverrides(desire));
		ORefList relevantRefsAfterDelete = relevantStrategyAndActivityRefsAfterDelete.getFilteredBy(itemInRelevancyListToBeDeleted.getType());
		
		assertEquals("Strategy / Activity was not removed from Desire relevancy list?", 0, relevantRefsAfterDelete.size());
	}

	private void verifyRelevancyIsUpdatedAfterStrategyOrActivityIsDeleted(BaseObject itemInRelevancyListToBeDeleted, Indicator indicator) throws Exception
	{
		RelevancyOverrideSet relevancyOverrides = new RelevancyOverrideSet();
		relevancyOverrides.add(new RelevancyOverride(itemInRelevancyListToBeDeleted.getRef(), true));
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancyOverrides.toString());

		ORefList relevantStrategyAndActivityRefs = new ORefList(getAllStrategyAndActivityRefsFromRelevancyOverrides(indicator));
		ORefList relevantRefs = relevantStrategyAndActivityRefs.getFilteredBy(itemInRelevancyListToBeDeleted.getType());
		assertEquals("Indicator's activity relevancy list was not updated?", 1, relevantRefs.size());

		CommandVector commandsToDelete = itemInRelevancyListToBeDeleted.createCommandsToDeleteChildrenAndObject();
		getProject().executeCommands(commandsToDelete);

		ORefList relevantStrategyAndActivityRefsAfterDelete = new ORefList(getAllStrategyAndActivityRefsFromRelevancyOverrides(indicator));
		ORefList relevantRefsAfterDelete = relevantStrategyAndActivityRefsAfterDelete.getFilteredBy(itemInRelevancyListToBeDeleted.getType());

		assertEquals("Strategy / Activity was not removed from Indicator relevancy list?", 0, relevantRefsAfterDelete.size());
	}

	public ORefSet getAllStrategyAndActivityRefsFromRelevancyOverrides(Desire desire) throws Exception
	{
		return ((RelevancyOverrideSetData)desire.getField(desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET)).extractRelevantRefs();
	}

	public ORefSet getAllStrategyAndActivityRefsFromRelevancyOverrides(Indicator indicator) throws Exception
	{
		return ((RelevancyOverrideSetData)indicator.getField(indicator.TAG_RELEVANT_STRATEGY_ACTIVITY_SET)).extractRelevantRefs();
	}

	public void testGetRelevantIndicatorRefList() throws Exception
	{
		ORef strategyRef = getProject().createObject(ObjectType.STRATEGY);
		Strategy strategy = Strategy.find(getProject(), strategyRef);
		BaseId indicatorId = getProject().addItemToFactorList(strategyRef, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		ORef indicatorRef = getProject().findObject(ObjectType.INDICATOR, indicatorId).getRef();
		assertEquals("wrong indicator count?", 1, strategy.getIndicatorsOnSameFactor().size());

		verifyRelevancy(indicatorRef, strategy, true, 1);
		verifyRelevancy(indicatorRef, strategy, false, 0);
	}

	private void verifyRelevancy(ORef indicatorRef, Strategy strategy, boolean overrideBoolean, int expectedValue) throws Exception
	{
		RelevancyOverrideSet relevancyOverrides = new RelevancyOverrideSet();
		relevancyOverrides.add(new RelevancyOverride(strategy.getRef(), overrideBoolean));
		Indicator indicator = Indicator.find(getProject(), indicatorRef);
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancyOverrides.toString());
		assertEquals("wrong indicator count?", expectedValue, strategy.getRelevantIndicatorRefList().size());
	}
}
