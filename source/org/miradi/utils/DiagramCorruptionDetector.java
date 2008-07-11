/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.utils;

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class DiagramCorruptionDetector
{
	public static Vector<String> getCorruptedDiagrams(Project project)
	{
		Vector<String> errorMessages = new Vector();
		ORefList diagramObjectRefs = project.getAllDiagramObjectRefs();
		for (int index = 0; index < diagramObjectRefs.size(); ++index)
		{
			DiagramObject diagramObject = DiagramObject.findDiagramObject(project, diagramObjectRefs.get(index));
			errorMessages.addAll(getDiagramCorruptedErrorMessages(project, diagramObject));
		}
		
		return errorMessages;
	}


	private static Vector<String> getDiagramCorruptedErrorMessages(Project project, DiagramObject diagramObject)
	{
		Vector<String> errorMessages = new Vector();
		errorMessages.addAll(getCorruptedDiagramFactorErrorMessages(project, diagramObject));
		errorMessages.addAll(getCorruptedDiagramLinksErrorMessages(project, diagramObject));
		
		return errorMessages;
	}

	public static Vector<String> getCorruptedDiagramLinksErrorMessages(Project project, DiagramObject diagramObject)
	{
		String diagramName = diagramObject.toString();
		Vector<String> errorMessages = new Vector();
		ORefList diagramLinkRefs = diagramObject.getAllDiagramLinkRefs();
		for (int index = 0; index < diagramLinkRefs.size(); ++index)
		{
			DiagramLink diagramLink = DiagramLink.find(project, diagramLinkRefs.get(index));
			if (diagramLink ==null)
			{	
				EAM.logVerbose("Found null diagramLink ref = " + diagramLinkRefs.get(index));
				errorMessages.add("Found null diagramLink.  Diagram = " + diagramName);
				continue;
			}

			DiagramFactor fromDiagramFactor = diagramLink.getFromDiagramFactor();
			DiagramFactor toDiagramFactor = diagramLink.getToDiagramFactor();
			FactorLink factorLink = FactorLink.find(project, diagramLink.getWrappedRef());
			if (fromDiagramFactor == null || toDiagramFactor == null)
			{
				EAM.logVerbose("Found null from or to for diagram link ref = " + diagramLink.getRef() + " .  from = " + fromDiagramFactor + " to = " + toDiagramFactor);
				errorMessages.add("Found null from or to for diagram link.  Diagram = " + diagramName);
				continue;
			}
			
			if (fromDiagramFactor.getWrappedFactor() == null || toDiagramFactor.getWrappedFactor() == null)
			{
				EAM.logVerbose("Found null from wrapped factor or to wrapped factor from diagram link ref = " + diagramLink.getRef() + " from wrapped ref = " +fromDiagramFactor.getWrappedORef() +  " to wrapped ref = " + toDiagramFactor.getWrappedORef() );
				errorMessages.add("Found null from wrapped factor or to wrapped factor from diagram link.  Diagram = " + diagramName);
				continue;
			}
			
			if (factorLink == null && !diagramLink.isGroupBoxLink())
			{
				String fromLabel = fromDiagramFactor.getWrappedFactor().toString();
				String toLabel = toDiagramFactor.getWrappedFactor().toString();
				EAM.logVerbose("Found null non group box factor link  diagramLink ref = " + diagramLink.getRef() + " wrappedRef = " + diagramLink.getWrappedRef());
				errorMessages.add("Found null non group box factor link from = " + fromLabel + "to = " + toLabel + ".  Diagram = " + diagramName);
				continue;
			}
		}
		
		return errorMessages;
	}


	public static Vector<String> getCorruptedDiagramFactorErrorMessages(Project project, DiagramObject diagramObject)
	{
		Vector<String> errorMessages = new Vector();
		String diagramName = diagramObject.toString();
		ORefList diagramFactorRefs = diagramObject.getAllDiagramFactorRefs();
		for (int index = 0; index < diagramFactorRefs.size(); ++index)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(project, diagramFactorRefs.get(index));
			if (diagramFactor == null)
			{
				String errorMesssage = "Found null diagram factor. Ref = " + diagramFactorRefs.get(index);
				EAM.logVerbose(errorMesssage);
				errorMessages.add("Found null diagram factor.  Diagram = " + diagramName);
				continue;
			}
			
			final Factor factor = diagramFactor.getWrappedFactor();
			if (factor == null)
			{
				EAM.logVerbose("Found null wrapped factor.  Ref = " + diagramFactor.getWrappedORef());
				errorMessages.add("Found null underlying factor  .Diagram = " + diagramName);
				continue;
			}
			
			if (Task.is(factor) && !factor.isActivity())
			{
				EAM.logVerbose("Found non activity factor that is a task.  Diagram factor ref = " + diagramFactor.getRef());
				errorMessages.add("Found non activity factor that is a task. label = " + factor.getLabel() + ".  Diagram = " + diagramName);
				continue;
			}
		}
		
		return errorMessages;
	}
}
