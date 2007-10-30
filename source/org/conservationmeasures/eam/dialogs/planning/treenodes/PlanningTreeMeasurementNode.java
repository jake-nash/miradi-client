/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeMeasurementNode extends AbstractPlanningTreeNode
{
	public PlanningTreeMeasurementNode(Project projectToUse, ORef measurementRef) throws Exception
	{
		super(projectToUse);
		measurement = (Measurement) project.findObject(measurementRef);
	}

	public BaseObject getObject()
	{
		return measurement;
	}
	
	boolean shouldSortChildren()
	{
		return false;
	}

	private Measurement measurement;
}
