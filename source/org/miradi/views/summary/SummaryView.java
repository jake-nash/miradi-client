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
package org.miradi.views.summary;


import org.martus.util.MultiCalendar;
import org.miradi.actions.ActionCreateOrganization;
import org.miradi.actions.ActionDeleteOrganization;
import org.miradi.actions.ActionDeleteTeamMember;
import org.miradi.actions.ActionTeamCreateMember;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.organization.OrganizationManagementPanel;
import org.miradi.dialogs.summary.TeamManagementPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.main.MiradiToolBar;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.schemas.*;
import org.miradi.views.TabbedView;
import org.miradi.views.summary.doers.CreateOranizationDoer;
import org.miradi.views.summary.doers.DeleteOrganizationDoer;
import org.miradi.views.summary.doers.TeamCreateMemberDoer;
import org.miradi.views.umbrella.DeleteResourceDoer;

import java.util.HashSet;

public class SummaryView extends TabbedView
{
	public SummaryView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		tabPanels = new HashSet<DisposablePanel>();
		addSummaryDoersToMap();
	}

	@Override
	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.SUMMARY_VIEW_NAME;
	}

	@Override
	public MiradiToolBar createToolBar()
	{
		return new SummaryToolBar(getMainWindow().getActions());
	}
	
	@Override
	public void createTabs() throws Exception
	{
		ProjectMetadata metadata = getProject().getMetadata();
		
		teamManagementPanel = new TeamManagementPanel(getMainWindow());
		organizationManagementPanel = new OrganizationManagementPanel(getMainWindow());

		ORef[] allRelatedRefs = new ORef[] {
			metadata.getRef(),
			getProject().getSingletonObjectRef(TncProjectDataSchema.getObjectType()),
			getProject().getSingletonObjectRef(WwfProjectDataSchema.getObjectType()),
			getProject().getSingletonObjectRef(WcsProjectDataSchema.getObjectType()),
			getProject().getSingletonObjectRef(RareProjectDataSchema.getObjectType()),
			getProject().getSingletonObjectRef(FosProjectDataSchema.getObjectType()),
			getProject().getSingletonObjectRef(WcpaProjectDataSchema.getObjectType()),
		};
		
		addSummaryTab(new SummaryProjectTabPanel(getMainWindow(), metadata.getRef()));

		if (getProject().getMetadata().isMiradiShareProject())
			addSummaryTab(new SummaryMiradiSharePanel(getProject()));
		else
			addNonScrollingTab(new SummaryNonSharedMiradiSharePanel(getMainWindow()));
		
		addNonScrollingTab(teamManagementPanel);
		addNonScrollingTab(new OrganizationalTabPanel(getMainWindow(), organizationManagementPanel));
		addSummaryTab(new SummaryScopeTabPanel(getProject(), allRelatedRefs));
		addSummaryTab(new SummaryLocationPanel(getProject(), metadata.getRef()));
		addSummaryTab(new SummaryPlanningPanel(getMainWindow(), metadata.getRef()));
		addMemberOrgTab("TNCPanel.html", new TNCSummaryPanel(getProject(), metadata));
		addMemberOrgTab("WWFPanel.html", new WWFSummaryPanel(getProject(), metadata));
		addMemberOrgTab("WCSPanel.html", new WCSSummaryPanel(getProject()));
		addMemberOrgTab("RAREPanel.html", new RARESummaryPanel(getProject()));
		addMemberOrgTab("FOSPanel.html", new FOSSummaryPanel(getProject()));
	}
	
	private void addSummaryTab(AbstractObjectDataInputPanel tabPanel)
	{
		tabPanels.add(tabPanel);
		addScrollingTab(tabPanel);
	}
	
	private void addMemberOrgTab(String htmlResourceName, AbstractObjectDataInputPanel dataPanel) throws Exception
	{
		MemberOrgTabPanel tabPanel = new MemberOrgTabPanel(getMainWindow(), htmlResourceName, dataPanel);
		tabPanels.add(tabPanel);
		addScrollingTab(tabPanel);
	}
	
	@Override
	public void deleteTabs() throws Exception
	{
		teamManagementPanel.dispose();
		teamManagementPanel = null;
		
		organizationManagementPanel.dispose();
		organizationManagementPanel = null;
		
		for(DisposablePanel panel : tabPanels)
		{
			panel.dispose();
		}
		
		tabPanels.clear();
		
		super.deleteTabs();
	}
	
	private void addSummaryDoersToMap()
	{
		addDoerToMap(ActionTeamCreateMember.class, new TeamCreateMemberDoer());
		addDoerToMap(ActionDeleteTeamMember.class, new DeleteResourceDoer());
		
		addDoerToMap(ActionCreateOrganization.class, new CreateOranizationDoer());
		addDoerToMap(ActionDeleteOrganization.class, new DeleteOrganizationDoer());
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_WORKPLAN_START_DATE) ||
			event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_START_DATE))
			handleWorkPlanStartDate();
	}
	
	private void handleWorkPlanStartDate()
	{
		MultiCalendar workPlanStartDate = getProject().getMetadata().getWorkPlanStartDate();
		MultiCalendar projectStartDate = getProject().getMetadata().getProjectStartDate();
		if (workPlanStartDate == null || projectStartDate == null)
			return;
		
		if (workPlanStartDate.before(projectStartDate))
			EAM.errorDialog(EAM.text("Work plan start date is before project start date"));	
	}
	
	private HashSet<DisposablePanel> tabPanels;

	private TeamManagementPanel teamManagementPanel;
	private OrganizationManagementPanel organizationManagementPanel;
}
