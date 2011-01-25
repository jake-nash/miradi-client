/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.planning;

import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.RowManager;

public class ObjectsOnlyRowColumnProvider extends PlanningViewRowColumnProvider
{
	public ObjectsOnlyRowColumnProvider(Project project)
	{
		super(project);
	}
	
	public CodeList getColumnCodesToShow() throws Exception
	{
		return new CodeList(ColumnManager.getVisibleColumnsForSingleType(getCurrentViewData()));
	}

	public CodeList getRowCodesToShow() throws Exception
	{
		return RowManager.getVisibleRowsForSingleType(getCurrentViewData());
	}
}
