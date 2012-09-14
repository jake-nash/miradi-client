/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;


import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.schemas.DashboardSchema;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.XmlUtilities2;

public class DashboardCommentsField extends AbstractDashboardClickableField
{
	public DashboardCommentsField(Project projectToUse, ORef refToUse,String stringMapCodeToUse)
	{
		super(projectToUse, refToUse, stringMapCodeToUse);
	}

	@Override
	protected void updateLabelComponent(PanelTitleLabel labelComponentToUse, Dashboard dashboard) throws Exception
	{
		CodeToUserStringMap map = dashboard.getCommentsMap();
		String mapValue = map.getUserString(stringMapCode);
		String firstLine = HtmlUtilities.getFirstLineWithTruncationIndicated(mapValue);
		
		labelComponentToUse.setText(XmlUtilities2.convertXmlTextToPlainText(firstLine));
	}

	public boolean hasComments() throws Exception
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(DashboardSchema.getObjectType());
		Dashboard dashboard = Dashboard.find(getProject(), dashboardRef);
		CodeToUserStringMap commentsMap = dashboard.getCommentsMap();
		String comment = commentsMap.getUserString(stringMapCode);
		return comment.length() > 0;
	}
}
