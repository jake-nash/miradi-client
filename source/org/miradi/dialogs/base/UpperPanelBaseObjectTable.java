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
package org.miradi.dialogs.base;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBox;
import org.miradi.dialogs.tablerenderers.*;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Vector;

abstract public class UpperPanelBaseObjectTable extends EditableBaseObjectTable implements RowColumnSelectionProvider
{
	public UpperPanelBaseObjectTable(MainWindow mainWindowToUse, UpperPanelBaseObjectTableModel modelToUse)
	{
		super(mainWindowToUse, modelToUse, modelToUse.getUniqueTableModelIdentifier());
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		resizeTable(4);
		
		DefaultFontProvider fontProvider = new DefaultFontProvider(getMainWindow());
		statusQuestionRenderer = new ChoiceItemTableCellRendererFactory(this, fontProvider);
		codeListRenderer = new CodeListRendererFactory(mainWindowToUse, this, fontProvider);
		booleanRendererFactory = new BooleanTableCellRendererFactoryWithPreferredHeight();
	}
	
	protected CodeList getCodesToDisable()
	{
		return new CodeList();
	}
	
	@Override
	public boolean isCellEditable(int row, int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		UpperPanelBaseObjectTableModel model = getObjectTableModel();
		if (model.isCodeListColumn(modelColumn))
			return false;
		if(model.isMultiLineTextCell(row, tableColumn))
			return false;
		
		return super.isCellEditable(row, tableColumn);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		if (getObjectTableModel().isCodeListColumn(modelColumn))
		{
			codeListRenderer.setQuestion(getObjectTableModel().getColumnQuestion(modelColumn));
			return codeListRenderer;
		}
		
		if (getObjectTableModel().isChoiceItemColumn(modelColumn))
		{
			return statusQuestionRenderer;
		}

		if (getObjectTableModel().isCheckboxCell(row, modelColumn))
		{
			TableCellRenderer r = super.getCellRenderer(row, tableColumn);
			r.getTableCellRendererComponent(this, false, false, false, row, tableColumn);
			return booleanRendererFactory;
		}

		return getSafeSingleLineRendererOrEditorFactory();
	}

	private SingleLineObjectTableCellEditorOrRendererFactory getSafeSingleLineRendererOrEditorFactory()
	{
		if (singleLineRendererOrEditorFactory == null)
			singleLineRendererOrEditorFactory = new SingleLineObjectTableCellEditorOrRendererFactory(this, new DefaultFontProvider(getMainWindow()));

		return singleLineRendererOrEditorFactory;
	}
	
	@Override
	public TableCellEditor getCellEditor(int row, int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		UpperPanelBaseObjectTableModel model = getObjectTableModel();
		if (model.isChoiceItemColumn(modelColumn))
		{
			ChoiceQuestion question = getObjectTableModel().getColumnQuestion(modelColumn);
			ChoiceItemComboBox comboBox = new ChoiceItemComboBox(question);
			
			return new DefaultCellEditor(comboBox);
		}
		
		if (model.isSingleLineTextCell(row, modelColumn))
		{
			return singleLineRendererOrEditorFactory;
		}
		
		EAM.logWarning("This cell is not yet editable: " + row + "," + tableColumn + ": " + model.getColumnTag(modelColumn));
		return null;
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getObjectTableModel().getObjectFromRow(row);
	}
	
	public void scrollToAndSelectRow(int row)
	{
		if(row < 0 || row >= getRowCount())
			return;
		
		Rectangle rect = getCellRect(row, 0, true);
		scrollRectToVisible(rect);
		setRowSelectionInterval(row, row);
	}

	public void setSelectedRow(ORef ref)
	{
		int rowToSelect = getObjectTableModel().findRowObject(ref.getObjectId());
		scrollToAndSelectRow(rowToSelect);
	}
	
	public UpperPanelBaseObjectTableModel getObjectTableModel()
	{
		return (UpperPanelBaseObjectTableModel)getModel();
	}
	
	@Override
	public BaseObject[] getSelectedObjects()
	{
		ORefList[] selectedHierarchies = getSelectedHierarchies();
		Vector<BaseObject> selectedObjects = new Vector<BaseObject>();
		for (int i = 0; i < selectedHierarchies.length; ++i)
		{
			ORefList thisSelectionHierarchy = selectedHierarchies[i];
			if (thisSelectionHierarchy.size() == 0)
				continue;
			
			BaseObject foundObject = getProject().findObject(thisSelectionHierarchy.get(0));
			selectedObjects.add(foundObject);		
		}
		
		return selectedObjects.toArray(new BaseObject[0]);
	}
	
	@Override
	public ORefList getSelectionHierarchy()
	{
		ORefList[] selectedHierarchies = getSelectedHierarchies();
		if(selectedHierarchies.length == 0)
			return new ORefList();
		return selectedHierarchies[0];
	}
	
	@Override
	public ORefList[] getSelectedHierarchies()
	{
		int[] rows = getSelectedRows();
		ORefList[] selectedHierarchies = new ORefList[rows.length];
		for(int i = 0; i < rows.length; ++i)
		{
			BaseObject objectFromRow = getObjectFromRow(rows[i]);
			ORefList selectedObjectRefs = new ORefList();
			if (objectFromRow != null)
				selectedObjectRefs.add(objectFromRow.getRef());
			
			selectedHierarchies[i] = selectedObjectRefs;
		}
		
		return selectedHierarchies;
	}

	@Override
	public void ensureOneCopyOfObjectSelectedAndVisible(ORef ref)
	{
		setSelectedRow(ref);
	}

	private BaseObject getObjectFromRow(int row)
	{
		return getObjectTableModel().getObjectFromRow(row);
	}
	
	private int findRowObject(BaseId id)
	{
		return getObjectTableModel().findRowObject(id);
	}
	
	public void addListSelectionListener(ListSelectionListener listener)
	{
		getSelectionModel().addListSelectionListener(listener);
	}
	
	public void updateTableAfterCommand(CommandSetObjectData cmd)
	{
		updateIfRowObjectWasModified(cmd.getObjectType(), cmd.getObjectId());
	}
	
	private void updateIfRowObjectWasModified(int type, BaseId id)
	{
		if(type != getObjectTableModel().getRowObjectType())
			return;
		
		int row = findRowObject(id);
		if(row >= 0)
			getObjectTableModel().fireTableRowsUpdated(row, row);
	}
	
	void updateTableAfterObjectCreated(ORef createdRef)
	{
		
	}
	
	void updateTableAfterObjectDeleted(ORef deletedRef)
	{
		
	}
	
	public ORefList getObjectHierarchy(int row, int column)
	{
		throw new RuntimeException("Method is currently unused and has no implementation");
	}
	
	private SingleLineObjectTableCellEditorOrRendererFactory singleLineRendererOrEditorFactory;
	private ChoiceItemTableCellRendererFactory statusQuestionRenderer;
	private CodeListRendererFactory codeListRenderer;
	private BooleanTableCellRendererFactoryWithPreferredHeight booleanRendererFactory;
}
