/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.cellviews;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;

public class CellViewFactory extends DefaultCellViewFactory
{
	protected VertexView createVertexView(Object cell)
	{
		EAMGraphCell eamGraphCell = (EAMGraphCell)cell;
		if(eamGraphCell.isFactor())
			return createNodeView(eamGraphCell);
		
		return new ProjectScopeView(eamGraphCell);
	}

	private VertexView createNodeView(EAMGraphCell cell)
	{
		FactorCell diagramFactor = (FactorCell)cell;
		if(diagramFactor.isTarget())
		{
			return new EllipseFactorView(diagramFactor);
		}
		if(diagramFactor.isDirectThreat())
		{
			return new RectangleFactorView(diagramFactor);
		}
		if(diagramFactor.isStress())
		{
			return new RectangleFactorView(diagramFactor);
		}
		if(diagramFactor.isContributingFactor())
		{
			return new RectangleFactorView(diagramFactor);
		}
		if(diagramFactor.isStrategy())
		{
			return new HexagonFactorView(diagramFactor);
		}
		throw new RuntimeException("Unknown node type");
	}

	protected EdgeView createEdgeView(Object edge)
	{
		return new FactorLinkView(edge);
	}
}