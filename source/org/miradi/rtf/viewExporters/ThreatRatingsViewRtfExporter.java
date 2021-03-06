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
package org.miradi.rtf.viewExporters;

import java.util.Set;

import org.miradi.diagram.ThreatTargetChainWalker;
import org.miradi.dialogs.threatrating.upperPanel.TargetThreatLinkTableModel;
import org.miradi.dialogs.threatrating.upperPanel.ThreatRatingMultiTablePanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.questions.ReportTemplateContentQuestion;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.CodeList;
import org.miradi.utils.SimpleThreatRatingDetailsTableExporter;
import org.miradi.utils.TableExporter;
import org.miradi.utils.ThreatStressRatingDetailsTableExporter;

public class ThreatRatingsViewRtfExporter extends RtfViewExporter
{
	public ThreatRatingsViewRtfExporter(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	@Override
	public void exportView(RtfWriter writer, CodeList reportTemplateContent) throws Exception
	{
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.THREAT_RATING_VIEW_CODE))
			exportThreatRating(writer);
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.THREAT_RATING_DETAILS_CODE))
			exportThreatRatingDetails(writer);
	}
	
	private void exportThreatRatingDetails(RtfWriter writer) throws Exception
	{
		writeHeader(writer, EAM.text("Threat Rating Details"));
		if (getProject().isStressBaseMode())
			exportStressBasedThreatRatingDetails(writer);
		else
			exportSimpleThreatRatingDetails(writer);
	}

	private void exportStressBasedThreatRatingDetails(RtfWriter writer) throws Exception
	{
		Set<Target> targets = TargetThreatLinkTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject());
		Cause[] threats =  getProject().getCausePool().getDirectThreats();
		for(Target target : targets)
		{
			if (target.getStressRefs().hasRefs())
				exportStressBasedThreatRatingDetailsRow(writer, target, threats);
		}
	}

	private void exportStressBasedThreatRatingDetailsRow(RtfWriter writer, Target target, Cause[] threats) throws Exception
	{
		for (int index = 0; index < threats.length; ++index)
		{
			Cause threat = threats[index];
			ThreatStressRatingDetailsTableExporter exporter = new ThreatStressRatingDetailsTableExporter(getProject(), target, threat);
			exportTable(writer, exporter, target.getFullName());
		}
	}

	private void exportSimpleThreatRatingDetails(RtfWriter writer) throws Exception
	{
		Set<Target> targets = TargetThreatLinkTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject());
		ThreatTargetChainWalker chain = new ThreatTargetChainWalker(getProject());
		for(Target target : targets)
		{
			ORefSet upstreamThreats = chain.getUpstreamThreatRefsFromTarget(target);
			SimpleThreatRatingDetailsTableExporter exporter = new SimpleThreatRatingDetailsTableExporter(getProject(), target, upstreamThreats.toRefList());
			exportTable(writer, exporter, target.getFullName());		
		}
	}

	private void exportThreatRating(RtfWriter writer) throws Exception
	{
		ThreatRatingMultiTablePanel threatRatingMultiTablePanel = new ThreatRatingMultiTablePanel(getMainWindow());
		TableExporter tableExporter = threatRatingMultiTablePanel.createTableForExporting();
		exportTableWithPageBreak(writer, tableExporter, ReportTemplateContentQuestion.getThreatRatingsLabel());
		threatRatingMultiTablePanel.dispose();
	}
}
