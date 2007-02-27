/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import org.conservationmeasures.eam.actions.ActionAbout;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.FutureVersionException;
import org.conservationmeasures.eam.exceptions.OldVersionException;
import org.conservationmeasures.eam.exceptions.UnknownCommandException;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectRepairer;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;
import org.conservationmeasures.eam.views.Doer;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.budget.BudgetView;
import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.images.ImagesView;
import org.conservationmeasures.eam.views.map.MapView;
import org.conservationmeasures.eam.views.monitoring.MonitoringView;
import org.conservationmeasures.eam.views.noproject.NoProjectView;
import org.conservationmeasures.eam.views.schedule.ScheduleView;
import org.conservationmeasures.eam.views.strategicplan.StrategicPlanView;
import org.conservationmeasures.eam.views.summary.SummaryView;
import org.conservationmeasures.eam.views.targetviability.TargetViabilityView;
import org.conservationmeasures.eam.views.threatmatrix.ThreatMatrixView;
import org.conservationmeasures.eam.views.umbrella.Definition;
import org.conservationmeasures.eam.views.umbrella.DefinitionCommonTerms;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.workplan.WorkPlanView;
import org.conservationmeasures.eam.wizard.WizardManager;
import org.martus.swing.ResourceImageIcon;
import org.martus.util.DirectoryLock;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.BrowserLauncherRunner;

public class MainWindow extends JFrame implements CommandExecutedListener, ClipboardOwner, SplitterPositionSaverAndGetter
{
	public MainWindow() throws IOException
	{
		this(new Project());
	}
	
	public MainWindow(Project projectToUse)
	{
		preferences = new AppPreferences();
		project = projectToUse;
		setFocusCycleRoot(true);
		wizardManager = new WizardManager();
	}
	
	public void start(String[] args) throws Exception
	{
		File appPreferencesFile = getPreferencesFile();
		preferences.load(appPreferencesFile);
		project.addCommandExecutedListener(this);
		
		ToolTipManager.sharedInstance().setInitialDelay(TOOP_TIP_DELAY_MILLIS);
		setIconImage(new ResourceImageIcon("images/appIcon.png").getImage());
		
		actions = new Actions(this);
		mainMenuBar = new MainMenuBar(actions);
		toolBarBox = new ToolBarContainer();
		mainStatusBar = new MainStatusBar();
		updateTitle();
		setSize(new Dimension(900, 700));
		setJMenuBar(mainMenuBar);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(toolBarBox, BorderLayout.BEFORE_FIRST_LINE);
		getContentPane().add(mainStatusBar, BorderLayout.AFTER_LAST_LINE);

		addWindowListener(new WindowEventHandler());

		noProjectView = new NoProjectView(this);
		summaryView = new SummaryView(this);
		diagramView = new DiagramView(this);
		threatMatrixView = new ThreatMatrixView(this);
		budgetView = new BudgetView(this);
		workPlanView = new WorkPlanView(this);
		mapView = new MapView(this);
		calendarView = new ScheduleView(this);
		imagesView = new ImagesView(this);
		strategicPlanView = new StrategicPlanView(this);
		monitoringView = new MonitoringView(this);
		targetViabilityView = new TargetViabilityView(this);

		viewHolder = new JPanel();
		viewHolder.setLayout(new CardLayout());
		viewHolder.add(createCenteredView(noProjectView), noProjectView.cardName());
		viewHolder.add(summaryView, summaryView.cardName());
		viewHolder.add(diagramView, diagramView.cardName());
		viewHolder.add(threatMatrixView, threatMatrixView.cardName());
		viewHolder.add(budgetView, budgetView.cardName());
		viewHolder.add(workPlanView, workPlanView.cardName());
		viewHolder.add(mapView, mapView.cardName());
		viewHolder.add(calendarView, calendarView.cardName());
		viewHolder.add(imagesView, imagesView.cardName());
		viewHolder.add(strategicPlanView, strategicPlanView.cardName());
		viewHolder.add(monitoringView, monitoringView.cardName());
		viewHolder.add(targetViabilityView, targetViabilityView.cardName());
		
		getContentPane().add(viewHolder, BorderLayout.CENTER);
		
		setCurrentView(noProjectView);
		actions.updateActionStates();

		if(!Arrays.asList(args).contains("--nosplash"))
		{
			Doer aboutDoer = diagramView.getDoer(ActionAbout.class);
			aboutDoer.doIt();
		}
		
		setVisible(true);
		if(preferences.getIsMaximized())
			setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
	}

	public AppPreferences getAppPreferences()
	{
		return preferences;
	}
	
	private File getPreferencesFile()
	{
		File appPreferencesFile = new File(EAM.getHomeDirectory(), APP_PREFERENCES_FILENAME);
		return appPreferencesFile;
	}

	private JComponent createCenteredView(JComponent viewToCenter)
	{
		Box centered = Box.createHorizontalBox();
		centered.add(Box.createHorizontalGlue());
		centered.add(viewToCenter);
		centered.add(Box.createHorizontalGlue());
		return centered;
	}
	
	public UmbrellaView getCurrentView()
	{
		return currentView;
	}
	
	private void setCurrentView(UmbrellaView view) throws Exception
	{
		if(currentView != null)
			currentView.becomeInactive();
		CardLayout layout = (CardLayout)viewHolder.getLayout();
		layout.show(viewHolder, view.cardName());
		currentView = view;
		currentView.becomeActive();
		updateToolBar();
	}

	public void updateToolBar()
	{
		SwingUtilities.invokeLater(new ToolBarUpdater());
	}
	
	class ToolBarUpdater implements Runnable
	{
		public void run()
		{
			UmbrellaView view = getCurrentView();
			if(view == null)
				return;
			JComponent toolBar = view.getToolBar();
			if(toolBar == null)
				throw new RuntimeException("View must have toolbar");
			toolBarBox.removeAll();
			toolBarBox.add(toolBar);
			toolBarBox.invalidate();
			toolBarBox.validate();
			invalidate();
			validate();
			repaint();
		}
	}

	public Project getProject()
	{
		return project;
	}
	
	public WizardManager getWizardManager()
	{
		return wizardManager;
	}
	
	public DiagramComponent getDiagramComponent()
	{
		return diagramView.getDiagramComponent();
	}
	
	public Actions getActions()
	{
		return actions;
	}
	
	public void createOrOpenProject(File projectDirectory)
	{
		try
		{
			project.createOrOpen(projectDirectory);
			ProjectRepairer.repairAnyProblems(project);
			project.getDiagramModel().updateProjectScopeBox();
			fakeViewSwitchForMainWindow();

			validate();
			updateTitle();
			updateStatusBar();
			getDiagramComponent().setModel(project.getDiagramModel());
			getDiagramComponent().setGraphLayoutCache(project.getGraphLayoutCache());
			getProject().updateVisibilityOfFactors();
			getDiagramComponent().requestFocus();
			
			setCommandLogFile(project);
		}
		catch(UnknownCommandException e)
		{
			EAM.errorDialog(EAM.text("Unknown Command\nYou are probably trying to load an old project " +
					"that contains obsolete commands that are no longer supported"));
		}
		catch(DirectoryLock.AlreadyLockedException e)
		{
			EAM.errorDialog(EAM.text("That project is in use by another copy of this application"));
		}
		catch(FutureVersionException e)
		{
			EAM.errorDialog(EAM.text("That project cannot be opened because it was created by a newer version of this application"));
		}
		catch(OldVersionException e)
		{
			EAM.errorDialog(EAM.text("That project cannot be opened until it is migrated to the current data format"));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unknown error prevented opening that project"));
		}
		
		actions.updateActionStates();
		
	}
	
	private void fakeViewSwitchForMainWindow()
	{
		String currentProjectView = project.getCurrentView();
		if(!project.isLegalViewName(currentProjectView))
			currentProjectView = project.DEFAULT_VIEW_NAME;
		
		project.forceMainWindowToSwitchViews(currentProjectView);
	}

	public void closeProject() throws Exception
	{
		project.close();
		updateTitle();
		mainStatusBar.setStatus("");
	}
	
	public void updateStatusBar()
	{
		if(getProject().getLayerManager().areAllNodesVisible())
			mainStatusBar.setStatusAllLayersVisible();
		else
			mainStatusBar.setStatusHiddenLayers();
	}

	public void exitNormally()
	{
		try
		{
			closeProject();
			savePreferences();
			System.exit(0);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			System.exit(1);
		}
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		updateAfterCommand(event);
	}
	
	private void updateAfterCommand(CommandExecutedEvent event)
	{
		try
		{
			if(event.getCommand().getCommandName().equals(CommandSwitchView.COMMAND_NAME))
				updateView();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			EAM.errorDialog("Unexpected error switching view");
		}
		
		SwingUtilities.invokeLater(new ActionUpdater());
		updateStatusBar();
	}
	
	class ActionUpdater implements Runnable
	{
		public void run()
		{
			actions.updateActionStates();
		}
	}
	
	public void updateView() throws Exception
	{
		String viewName = getProject().getCurrentView();
		if(viewName.equals(summaryView.cardName()))
			setCurrentView(summaryView);
		else if(viewName.equals(diagramView.cardName()))
			setCurrentView(diagramView);
		else if(viewName.equals(noProjectView.cardName()))
			setCurrentView(noProjectView);
		else if(viewName.equals(threatMatrixView.cardName()))
			setCurrentView(threatMatrixView);
		else if(viewName.equals(budgetView.cardName()))
			setCurrentView(budgetView);
		else if(viewName.equals(workPlanView.cardName()))
			setCurrentView(workPlanView);
		else if(viewName.equals(mapView.cardName()))
			setCurrentView(mapView);
		else if(viewName.equals(calendarView.cardName()))
			setCurrentView(calendarView);
		else if(viewName.equals(imagesView.cardName()))
			setCurrentView(imagesView);
		else if(viewName.equals(strategicPlanView.cardName()))
			setCurrentView(strategicPlanView);
		else if(viewName.equals(monitoringView.cardName()))
			setCurrentView(monitoringView);
		else if (viewName.equals(targetViabilityView.cardName()))
			setCurrentView(targetViabilityView);
		else
		{
			EAM.logError("MainWindow.switchToView: Unknown view: " + viewName);
			setCurrentView(summaryView);
		}
	}
	
	public void jump(Class stepMarker) throws CommandFailedException
	{
		try
		{
			getCurrentView().jump(stepMarker);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void savePreferences() throws IOException
	{
		boolean isMaximized = false;
		if((getExtendedState() & MAXIMIZED_BOTH) != 0)
			isMaximized = true;
		preferences.setIsMaximized(isMaximized);
		preferences.save(getPreferencesFile());
	}
	
	public void setBooleanPreference(String genericTag, boolean state)
	{
		preferences.setBoolean(genericTag, state);
		getDiagramComponent().updateDiagramComponent(this);
		repaint();
	}
	
	public boolean getBooleanPreference(String genericTag)
	{
		return preferences.getBoolean(genericTag);
	}
	
	public Color getColorPreference(String colorTag)
	{
		return preferences.getColor(colorTag);
	}
	
	public void setColorPreference(String colorTag, Color colorToUse)
	{
		preferences.setColor(colorTag, colorToUse);
		repaint();
	}

	public void saveSplitterLocation(String name, int location)
	{
		preferences.setTaggedInt(name, location);
	}
	
	public int getSplitterLocation(String name)
	{
		return preferences.getTaggedInt(name);
	}
	
	public void lostOwnership(Clipboard clipboard, Transferable contents) 
	{
	}
	
	private void updateTitle()
	{
		setTitle("Miradi - " + project.getFilename());
	}
	
	class WindowEventHandler extends WindowAdapter
	{
		public void windowClosing(WindowEvent event)
		{
			try
			{
				exitNormally();
			}
			catch (Exception e)
			{
				EAM.logException(e);
				System.exit(1);
			}
		}
	}
	
	public boolean mainLinkFunction(String linkDescription)
	{	
		if (linkDescription.startsWith("Definition:"))
		{
			Definition def = DefinitionCommonTerms.getDefintion(linkDescription);
			EAM.okDialog(def.term, new String[] {def.definition});
		} 
		else if (isBrowserProtocol(linkDescription))
		{
	        launchBrowser(linkDescription);
		}
		else
			return false;
		
        return true;
	}
	
	private void launchBrowser(String linkDescription)
	{
		try 
		{
		    BrowserLauncherRunner runner = new BrowserLauncherRunner(
		    		new BrowserLauncher(null),
		            "",
		            linkDescription,
		            null);
		    new Thread(runner).start();
		}
		catch (Exception e) 
		{
			EAM.logException(e);
		}
	}

	private boolean isBrowserProtocol(String linkDescription)
	{
		return linkDescription.startsWith(HTTP_PROTOCOL) || linkDescription.startsWith(MAIL_PROTOCOL);
	}
	
	
	private File createCommandLogFile(Project projectToUse) throws IOException
	{
		File thisProjectDirectory = new File(EAM.getHomeDirectory(), projectToUse.getFilename());
		File commandLogFile = new File(thisProjectDirectory, COMMAND_LOG_FILE_NAME);
		if (commandLogFile.exists())
			commandLogFile.delete();
		return commandLogFile;
	}
	
	public PrintStream getCommandLogFile() throws FileNotFoundException
	{
		//TODO: need to handle no project calles
		if (commandLog==null)
			return null;
		FileOutputStream os =  new FileOutputStream(commandLog, true);
		return new PrintStream(os);
	}
	
	private void setCommandLogFile(Project projectToUse) throws IOException
	{
		commandLog = createCommandLogFile(projectToUse);
		getCommandLogFile().println("LOG ENTRY: Project Opened");
	}
	
	private static final String COMMAND_LOG_FILE_NAME = "command.log";
	
	private static String HTTP_PROTOCOL = "http";
	private static String MAIL_PROTOCOL = "mailto:";
	
	private static final String APP_PREFERENCES_FILENAME = "settings";
	private static final int TOOP_TIP_DELAY_MILLIS = 0;
	
	protected Actions actions;
	private AppPreferences preferences;
	private Project project;
	
	private NoProjectView noProjectView;
	private SummaryView summaryView;
	private DiagramView diagramView;
	private ThreatMatrixView threatMatrixView;
	private BudgetView budgetView;
	private WorkPlanView workPlanView;
	private MapView mapView;
	private ScheduleView calendarView;
	private ImagesView imagesView;
	private StrategicPlanView strategicPlanView;
	private TabbedView monitoringView;
	private TargetViabilityView targetViabilityView;
	
	private UmbrellaView currentView;
	private JPanel viewHolder;
	private JPanel toolBarBox;
	private MainMenuBar mainMenuBar;
	private MainStatusBar mainStatusBar;
	
	private WizardManager wizardManager;
	private File commandLog;
	
}
