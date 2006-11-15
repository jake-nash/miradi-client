/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import java.util.Collections;
import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objectpools.DesirePool;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.views.TreeTableNode;

public class MonitoringRootNode extends MonitoringNode
{
	public MonitoringRootNode(Project projectToUse) throws Exception
	{
		project = projectToUse;
		rebuild();
	}
	
	public ORef getObjectReference()
	{
		return null;
	}
	
	public int getType()
	{
		return -1;
	}

	public String toString()
	{
		return getClass().toString();
	}
	
	public int getChildCount()
	{
		return children.size();
	}

	public TreeTableNode getChild(int index)
	{
		return (MonitoringNode)children.get(index);
	}

	public Object getValueAt(int column)
	{
		return "";
	}

	private void rebuild() throws Exception
	{
		Vector desireVector = new Vector();
		desireVector.addAll(getAllDesires(project.getGoalPool()));
		children = desireVector;
		Collections.sort(children, new IgnoreCaseStringComparator());
	}

	private Vector getAllDesires(DesirePool pool) throws Exception
	{
		BaseId[] desireIds = pool.getIds();
		Vector desires = new Vector();
		for(int i = 0; i < desireIds.length; ++i)
		{
			Goal desire = (Goal)pool.findDesire(desireIds[i]);
			desires.add(new MonitoringGoalNode(project, desire));
		}
		return desires;
	}
	
	Project project;
	Vector children;
}
