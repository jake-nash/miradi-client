/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class ThreatGridPanel extends JPanel
{
	public ThreatGridPanel(ThreatMatrixView viewToUse,
			NonEditableThreatMatrixTableModel modelToUse)
			throws Exception
	{
		super(new BorderLayout());
		view = viewToUse;
		add(createThreatGridPanel(modelToUse));
	}

	public JScrollPane createThreatGridPanel(NonEditableThreatMatrixTableModel model) throws Exception
	{
		NonEditableRowHeaderTableModel newRowHeaderData = new NonEditableRowHeaderTableModel(model);
		JTable rowTable =  new ThreatMatrixRowHeaderTable(newRowHeaderData);
		rowHeaderTable = rowTable;
		
		ThreatMatrixTable table = new ThreatMatrixTable(model, this);
		threatTable = table;
		
		JTableHeader columnHeader = table.getTableHeader();
		targetColumnSortListener = new BundleColumnSortHandler(this);
		columnHeader.addMouseListener(targetColumnSortListener);
		columnHeader.addMouseMotionListener(targetColumnSortListener);

		JTableHeader rowHeader = rowTable.getTableHeader();
		threatColumnSortListener = new ThreatNameColumnHandler(this);
		rowHeader.addMouseListener(threatColumnSortListener);
		
		JScrollPane scrollPane = new ScrollPaneWithTableAndRowHeader(rowTable, table);
		
		return scrollPane;
	}

	
	public ThreatRatingBundle getSelectedBundle()
	{
		return highlightedBundle;
	}

	
	public void selectBundle(ThreatRatingBundle bundle) throws Exception
	{
		highlightedBundle = bundle;
		refreshCell(bundle);
	}
	
	
	private void refreshCell(ThreatRatingBundle bundle) throws Exception
	{
		repaint();
	}

	
	public Project getProject() 
	{
		return view.getProject();
	}
	
	
	public ThreatRatingFramework getThreatRatingFramework() 
	{
		return view.getThreatRatingFramework();
	}
	
	
	public ThreatMatrixView getThreatMatrixView() 
	{
		return view;
	}
	
	
	public ThreatMatrixTable getThreatMatrixTable() 
	{
		return threatTable;
	}
	
	
	public JTable getRowHeaderTable() 
	{
		return rowHeaderTable;
	}
	
	public void establishPriorSortState() throws Exception  
	{
		String currentSortBy = getProject().getViewData(
				getProject().getCurrentView()).getData(
				ViewData.TAG_CURRENT_SORT_BY);
		
		boolean hastPriorSortBy = currentSortBy.length() != 0;
		if(hastPriorSortBy)
		{
			String currentSortDirection = getProject().getViewData(
					getProject().getCurrentView()).getData(
					ViewData.TAG_CURRENT_SORT_DIRECTION);

			boolean sortOrder = currentSortDirection.equals(ViewData.SORT_ASCENDING);

			if(currentSortBy.equals(ViewData.SORT_THREATS))
			{
				threatColumnSortListener.setToggle(sortOrder);
				threatColumnSortListener.sort(0);
			}
			else
			{
				int columnToSort = threatTable.getSummaryColumn();
				if (!currentSortBy.equals(ViewData.SORT_SUMMARY)) 
				{
					FactorId nodeId = new FactorId(new Integer(currentSortBy).intValue());
					columnToSort= ((NonEditableThreatMatrixTableModel)threatTable.getModel()).findTargetIndexById(nodeId);
					if (wasTargetDeleted(columnToSort)) 
						return;
				}
				targetColumnSortListener.setToggle(sortOrder);
				targetColumnSortListener.sort(columnToSort);
			}
		}
	}


	private boolean wasTargetDeleted(int columnToSort)
	{
		return columnToSort<0;
	}

	private ThreatMatrixView view;
	private ThreatRatingBundle highlightedBundle;
	private ThreatMatrixTable threatTable;
	private ThreatNameColumnHandler threatColumnSortListener;
	private BundleColumnSortHandler targetColumnSortListener;
	private JTable rowHeaderTable;
	
	public final static int ABOUT_ONE_LINE = 20;
	public final static int ROW_HEIGHT = 2 * ABOUT_ONE_LINE;
	public final static int ABOUT_ONE_INCH = 72;
	public final static int LEFTMOST_COLUMN_WIDTH = 2 * ABOUT_ONE_INCH;
	public final static int DEFAULT_COLUMN_WIDTH = ABOUT_ONE_INCH;
	
}



