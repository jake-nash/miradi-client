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
import java.awt.Font;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import org.conservationmeasures.eam.actions.ActionAbout;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.CommandSwitchView;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.FutureVersionException;
import org.conservationmeasures.eam.exceptions.InvalidDateRangeException;
import org.conservationmeasures.eam.exceptions.OldVersionException;
import org.conservationmeasures.eam.exceptions.UnknownCommandException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectRepairer;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.FontFamiliyQuestion;
import org.conservationmeasures.eam.utils.DateRange;
import org.conservationmeasures.eam.utils.DateRangeEffort;
import org.conservationmeasures.eam.utils.MiradiResourceImageIcon;
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
import org.martus.util.DirectoryLock;
import org.martus.util.MultiCalendar;

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
		actions = new Actions(this);
	}
	
	public void start(String[] args) throws Exception
	{
		File appPreferencesFile = getPreferencesFile();
		preferences.load(appPreferencesFile);
		project.addCommandExecutedListener(this);
		
		ToolTipManager.sharedInstance().setInitialDelay(TOOP_TIP_DELAY_MILLIS);
		setIconImage(new MiradiResourceImageIcon("images/appIcon.png").getImage());
		
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
		updateActionStates();

		if(!Arrays.asList(args).contains("--nosplash"))
		{
			Doer aboutDoer = diagramView.getDoer(ActionAbout.class);
			aboutDoer.setMainWindow(this);
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
		toolBarBox.clear();
		SwingUtilities.invokeLater(new ToolBarUpdater());
	}
	
	class ToolBarUpdater implements Runnable
	{
		public void run()
		{
			UmbrellaView view = getCurrentView();
			if(view == null)
				return;
			JToolBar toolBar = view.createToolBar();
			if(toolBar == null)
				throw new RuntimeException("View must have toolbar");

			toolBarBox.setToolBar(toolBar);
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
		if(diagramView == null)
			return null;
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
			fakeViewSwitchForMainWindow();

			validate();
			updateTitle();
			updateStatusBar();
			getDiagramView().updateVisibilityOfFactors();
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
		
		updateActionStates();
		
	}
	
	public DiagramView getDiagramView()
	{
		return diagramView;
	}
	
	public ThreatMatrixView getThreatView()
	{
		return threatMatrixView;
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
		setDiagramViewStatusBar();
	}

	private void setDiagramViewStatusBar()
	{
		if (!diagramView.getViewName().equals(currentView.getName()))
			return;
		
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

	public void setStatusBarIfDataExistsOutOfRange()
	{
		try
		{
			final String dataOutOfRange = EAM.text("WorkPlan/Financial data outside project begin/end dates will not be shown");
			if (isDataOutsideOfcurrentProjectDateRange())
				mainStatusBar.setStatus(dataOutOfRange);
			else
				mainStatusBar.setStatusReady();
		}
		catch (InvalidDateRangeException e)
		{
			mainStatusBar.setStatus(e.getMessage());
			EAM.logError(e.getMessage());
		}
	}

	//TODO refactor this method (nested for loops)
	private boolean isDataOutsideOfcurrentProjectDateRange() throws InvalidDateRangeException
	{
		
		ProjectMetadata metadata = getProject().getMetadata();
		String startDate = metadata.getStartDate();
		String endDate = metadata.getExpectedEndDate();

		if (startDate.trim().length() <= 0 || endDate.trim().length() <= 0)
			return false;
		
		MultiCalendar multiStartDate = MultiCalendar.createFromIsoDateString(startDate);
		MultiCalendar multiEndDate = MultiCalendar.createFromIsoDateString(endDate);
		
		if (multiStartDate.after(multiEndDate))
			throw new InvalidDateRangeException(EAM.text("WARNING: Project end date before start date."));

		try
		{
			DateRange projectDateRange = new DateRange(multiStartDate, multiEndDate);
			
			BaseId[] assignmentIds = getProject().getAssignmentPool().getIds();
			for (int i = 0; i < assignmentIds.length; i++)
			{
				Assignment assignment = (Assignment) getProject().findObject(new ORef(ObjectType.ASSIGNMENT, assignmentIds[i]));
				DateRangeEffortList effortList = assignment.getDetails();
				for (int j = 0; j < effortList.size(); j++)
				{
					DateRangeEffort effort = effortList.get(j);
					DateRange effortDateRange = effort.getDateRange();
					if (!projectDateRange.contains(effortDateRange))
						return true;
				}
			}
			
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
		
		return false;
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
			EAM.logException(e);
			EAM.errorDialog("Unexpected error switching view");
		}
		
		updateActionsAndStatusBar();
	}

	public void updateActionsAndStatusBar()
	{
		updateActionStates();
		updateStatusBar();
	}

	public void updateActionStates()
	{
		if(getProject().isInTransaction())
			return;
		SwingUtilities.invokeLater(new ActionUpdater());
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

	public void savePreferences() throws Exception
	{
		boolean isMaximized = false;
		if((getExtendedState() & MAXIMIZED_BOTH) != 0)
			isMaximized = true;
		preferences.setIsMaximized(isMaximized);
		preferences.save(getPreferencesFile());
		getCurrentView().refresh();
	}
	
	public void setBooleanPreference(String genericTag, boolean state)
	{
		preferences.setBoolean(genericTag, state);
		if (getDiagramComponent()!=null)
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

	public Font getUserDataPanelFont()
	{
		ChoiceItem fontFamily = new FontFamiliyQuestion("").findChoiceByCode(getDataPanelFontFamily());
		return new Font(fontFamily.getLabel(),Font.PLAIN, getDataPanelFontSizeWithDefault());
	}
	
	public int getDataPanelFontSizeWithDefault()
	{
		int size = preferences.getPanelFontSize();
		if (size == 0)
			return getFont().getSize();
		return size;
	}
	
	public int getDataPanelFontSize()
	{
		return preferences.getPanelFontSize();
	}
	
	public void setDataPanelFontSize(int fontSize)
	{
		preferences.setPanelFontSize(fontSize);
	}
	
	public String getDataPanelFontFamily()
	{
		return preferences.getPanelFontFamily();
	}
	
	public void setDataPanelFontFamily(String fontFamily)
	{
		preferences.setPanelFontFamily(fontFamily);
	}
	
	public int getWizardFontSize()
	{
		return preferences.getWizardFontSize();
	}
	
	public void setWizardFontSize(int fontSize)
	{
		preferences.setWizardFontSize(fontSize);
	}
	
	public String getWizardFontFamily()
	{
		return preferences.getWizardFontFamily();
	}
	
	public void setWizardFontFamily(String fontFamily)
	{
		preferences.setWizardFontFamily(fontFamily);
	}
	
	public void setTaggedInt(String name, int value)
	{
		preferences.setTaggedInt(name, value);
	}
	
	public int getTaggedInt(String name)
	{
		return preferences.getTaggedInt(name);
	}
	
	public void setTaggedString(String name, String value)
	{
		preferences.setTaggedString(name, value);
	}
	
	public String getTaggedString(String name)
	{
		return preferences.getTaggedString(name);
	}
	
	public void saveDiagramZoomSetting(String name, double setting)
	{
		preferences.setTaggedDouble(name, setting);
	}
	
	public double getDiagramZoomSetting(String name)
	{
		return preferences.getTaggedDouble(name);
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
	

	private static String HTTP_PROTOCOL = "http";
	private static String MAIL_PROTOCOL = "mailto:";
	
	private static final String APP_PREFERENCES_FILENAME = "settings";
	private static final int TOOP_TIP_DELAY_MILLIS = 0;
	
	protected Actions actions;
	private AppPreferences preferences;
	private Project project;
	
	private NoProjectView noProjectView;
	private UmbrellaView summaryView;
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
	private ToolBarContainer toolBarBox;
	private MainMenuBar mainMenuBar;
	private MainStatusBar mainStatusBar;
	
	private WizardManager wizardManager;
	
}
