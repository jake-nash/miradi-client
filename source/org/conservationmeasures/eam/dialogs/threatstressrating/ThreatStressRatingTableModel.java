/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.base.EditableObjectTableModel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.ThreatStressRating;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.utils.ColumnTagProvider;

public class ThreatStressRatingTableModel extends EditableObjectTableModel implements ColumnTagProvider
{
	public ThreatStressRatingTableModel(Project projectToUse, ORef refToUse)
	{
		super(projectToUse);
		
		ratings = new ThreatStressRating[0];
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		Target target = (Target) getProject().findObject(hierarchyToSelectedRef[0]);
		Cause cause = (Cause) getProject().findObject(hierarchyToSelectedRef[1]);
		
		ORef factorLinkRef = getProject().getFactorLinkPool().getLinkedRef(cause.getRef(), target.getRef());
		FactorLink factorLink = (FactorLink) getProject().findObject(factorLinkRef);
		ORefList threatStressRatingRefs = factorLink.getThreatStressRatingRefs();
		ratings = new ThreatStressRating[threatStressRatingRefs.size()];
		for (int i = 0; i < threatStressRatingRefs.size(); ++i)
		{
			ratings[i] = (ThreatStressRating) getProject().findObject(threatStressRatingRefs.get(i));
		}
	}

	public boolean isCellEditable(int row, int column)
	{
		if (isContributionColumn(column))
			return true;
		
		if (isIrreversibilityColumn(column))
			return true;
		
		return false;
	}
	
	public boolean isIrreversibilityColumn(int column)
	{
		return getColumnTag(column).equals(ThreatStressRating.TAG_IRREVERSIBILITY);
	}

	public boolean isContributionColumn(int column)
	{
		return getColumnTag(column).equals(ThreatStressRating.TAG_CONTRIBUTION);
	}
		
	public String getColumnName(int column)
	{
		return EAM.fieldLabel(ThreatStressRating.getObjectType(), getColumnTag(column));
	}
	
	public String getColumnTag(int column)
	{
		return getColumnTags()[column];
	}

	public int getColumnCount()
	{
		return getColumnTags().length;
	}

	public int getRowCount()
	{
		return ratings.length;
	}

	public Object getValueAt(int row, int column)
	{
		if (isContributionColumn(column))
			return getThreatStressRating(row).getContribution();
		
		if (isIrreversibilityColumn(column))
			return getThreatStressRating(row).getIrreversibility();
		
		return null;
	}
	
	public void setValueAt(Object value, int row, int column)
	{
		if (value == null)
			return;
		
		if (isContributionColumn(column) || isIrreversibilityColumn(column))
		{
			ORef ref = getBaseObjectForRow(row).getRef();
			setThreatStressRatingData(ref, getColumnTag(column), ((ChoiceItem) value).getCode());
		}
	}

	public void setThreatStressRatingData(ORef  threatStressRatingRef, String fieldTag, String valueToSave)
	{
		try
		{
			Command command = new CommandSetObjectData(threatStressRatingRef, fieldTag, valueToSave);
			getProject().executeCommand(command);
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
		}
	}
			
	public BaseObject getBaseObjectForRow(int row)
	{
		return ratings[row];
	}

	public ThreatStressRating getThreatStressRating(int row)
	{
		return (ThreatStressRating) getBaseObjectForRow(row);
	}
	
	public static String[] getColumnTags()
	{
		return new String[] {
				Stress.TAG_LABEL,
				Stress.PSEUDO_STRESS_RATING,
				ThreatStressRating.TAG_CONTRIBUTION,
				ThreatStressRating.TAG_IRREVERSIBILITY,
				ThreatStressRating.PSEUDO_TAG_THREAT_RATING,				
		};
	}
	
	private ThreatStressRating[] ratings;
}
