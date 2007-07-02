/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import java.awt.BorderLayout;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.HtmlFormEventHandler;

public abstract class SkeletonWizardStep extends JPanel implements HtmlFormEventHandler
{
	public SkeletonWizardStep(WizardPanel wizardToUse, String viewNameToUse)
	{
		super(new BorderLayout());
		wizard = wizardToUse;
		viewName = viewNameToUse;
		controls = new Hashtable();
	}
	
	abstract public void refresh() throws Exception;

	public WizardPanel getWizard()
	{
		return wizard;
	}

	public String getViewName()
	{
		return viewName;
	}
	
	public String getTextLeft() throws Exception
	{
		if (!doesExist(LEFT))
			return getText(NEITHER);
		return getText(LEFT);
	}

	public String getTextRight() throws Exception
	{
		if (!doesExist(RIGHT))
			return "";
		return getText(RIGHT);
	}
	
	private String getText(String sideLabel) throws Exception
	{
		String resourceFileName = getResourceFileName(sideLabel);
		if(resourceFileName == null)
			return "Missing text";
		return EAM.loadResourceFile(getClass(), resourceFileName);
	}
	
	private boolean doesExist(String ext) throws Exception
	{
		URL url = EAM.getResourceURL(getClass(), getResourceFileName(ext));
		return (url!=null);
	}
	
	private String getResourceFileName(String sideLabel)
	{
		return getHtmlBaseName() + sideLabel + ".html";
	}

	public String getHtmlBaseName()
	{
		return getClass().getSimpleName();
	}

	public void buttonPressed(String buttonName)
	{
		control(buttonName);
	}

	public void linkClicked(String linkDescription)
	{
		if (getMainWindow().mainLinkFunction(linkDescription))
			return;
			
		if(linkDescription.startsWith("View:"))
		{
			control(linkDescription);
		}
	}
	
	public MainWindow getMainWindow()
	{
		return getWizard().getMainWindow();
	}

	private void control(String controlName)
	{
		try
		{
			WizardManager wizardManager = getMainWindow().getWizardManager();
			SkeletonWizardStep step = wizardManager.getCurrentStep();
			Class destinationStepClass = wizardManager.findControlTargetStep(controlName, step);
			if (destinationStepClass==null)
			{
				String errorText = "Control ("+ controlName +") not found for step: " + wizardManager.getStepName(step);
				reportError(EAM.text(errorText));
			}
			
			navigateToStep(destinationStepClass);
			getMainWindow().updateActionsAndStatusBar();
		}
		catch (Exception e)
		{
			String body = EAM.text("Wizard load of control failed:") + controlName;
			EAM.errorDialog(body);
			EAM.logException(e);
		}
	}

	private void navigateToStep(Class destinationStepClass) throws Exception
	{
		Project project = getMainWindow().getProject();
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			WizardManager wizardManager = getMainWindow().getWizardManager();
			wizardManager.setStep(destinationStepClass);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
	
	private void reportError(String msg)
	{
		EAM.logError(msg);
		EAM.errorDialog(msg);
	}


	
	SkeletonWizardStep createControl(String controlName , Class controlStep)
	{
		addControl(controlName, controlStep);
		return this;
	}
	
	SkeletonWizardStep createNextControl(Class controlStep)
	{
		return createControl("Next", controlStep);
	}
	
	SkeletonWizardStep createBackControl(Class controlStep)
	{
		return createControl("Back", controlStep);
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
	
	public Class getControl(String controlName)
	{
		return (Class) controls.get(controlName);
	}

	public void addControl(String controlName, Class targetStepp)
	{
		controls.put(controlName, targetStepp);
	}

	private static final String RIGHT = "Right";
	private static final String LEFT = "Left";
	private static final String NEITHER = "";
	
	private Hashtable controls;
	private WizardPanel wizard;
	private String viewName;
}
