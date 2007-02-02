/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.HtmlFormEventHandler;

public abstract class SkeletonWizardStep extends JPanel implements HtmlFormEventHandler
{
	public SkeletonWizardStep(WizardPanel wizardToUse)
	{
		super(new BorderLayout());
		wizard = wizardToUse;

	}
	
	abstract public void refresh() throws Exception;

	public WizardPanel getWizard()
	{
		return wizard;
	}

	public String getText() throws Exception
	{
		String resourceFileName = getResourceFileName();
		if(resourceFileName == null)
			return "Missing text";
		
		return EAM.loadResourceFile(getClass(), resourceFileName);
	}

	public String getResourceFileName()
	{
		return null;
	}
	
	public void buttonPressed(String buttonName)
	{
		control(buttonName);
	}

	public void linkClicked(String linkDescription)
	{
		if (linkDescription.startsWith("Definition:"))
		{
			Definition def = DefinitionCommonTerms.getDefintion(linkDescription);
			EAM.okDialog(def.term, new String[] {def.definition});
		} 
		else if(linkDescription.startsWith("View:"))
		{
			control(linkDescription);
		}
	}
	

	private void control(String controlName)
	{
		try
		{
			getWizard().control(controlName);
		}
		catch (Exception e)
		{
			String body = EAM.text("Wizard load of control failed:") + controlName;
			EAM.errorDialog(body);
			EAM.logException(e);
		}
	}
	

	public JPopupMenu getRightClickMenu(String url)
	{
		return null;
	}

	public void valueChanged(String widget, String newValue)
	{
	}

	public void setComponent(String name, JComponent component)
	{
	}



	private WizardPanel wizard;
}
