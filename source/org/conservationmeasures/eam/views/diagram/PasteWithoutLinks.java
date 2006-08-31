/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.main.TransferableEamList;
import org.conservationmeasures.eam.project.NodeCommandHelper;

public class PasteWithoutLinks extends Paste 
{
	public void pasteCellsIntoProject(TransferableEamList list) throws Exception 
	{
		new NodeCommandHelper(getProject()).pasteNodesOnlyIntoProject(list, getLocation());
	}
}
