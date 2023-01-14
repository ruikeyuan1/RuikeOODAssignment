import java.awt.MenuBar;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

/** <p>The controller for the menu</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class MenuController extends MenuBar {
	private Frame parent; //The frame, only used as parent for the Dialogs
	private SlideViewerComponent slideViewerComponent;

	private static final long serialVersionUID = 227L;
	protected static final String ABOUT = "About";
	protected static final String FILE = "File";
	protected static final String EXIT = "Exit";
	protected static final String GOTO = "Go to";
	protected static final String HELP = "Help";
	protected static final String NEW = "New";
	protected static final String NEXT = "Next";
	protected static final String OPEN = "Open";
	protected static final String PAGENR = "Page number?";
	protected static final String PREV = "Prev";
	protected static final String SAVE = "Save";
	protected static final String VIEW = "View";
	protected static final String TESTFILE = "testPresentation.xml";
	protected static final String SAVEFILE = "savedPresentation.xml";
	protected static final String IOEX = "IO Exception: ";

	protected static final String LOADERR = "Load Error";
	protected static final String SAVEERR = "Save Error";
	private MenuItem menuItem;
	private final XMLAccessor xmlAccessor = new XMLAccessor();

	/**
	 * the para of type Presentation is changed to SlideViewerComponent
	 * As SlideViewerComponent controls actions of Presentation so
	 * there is no longer a need to keep the para Presentation
	 * @param frame MenuController needs a frame to operate itself
	 * @param slideViewerComponent MenuController needs a SlideViewerComponent to operate itself
	 */
	public MenuController(Frame frame,SlideViewerComponent slideViewerComponent) {
		this.parent = frame;
		this.slideViewerComponent = slideViewerComponent;
		this.addFileMenu();
		this.addViewMenu();
		this.addHelpMenu();
	}

//Creating a menu-item
	public MenuItem mkMenuItem(String name) {
		return new MenuItem(name, new MenuShortcut(name.charAt(0)));
	}

	/**
	 * AboutBox class were deleted and the content are moved to here, as that separated class was only for showing the text content.
	 */
	private void showAboutBox()
	{
		JOptionPane.showMessageDialog(parent,
				"JabberPoint is a primitive slide-show program in Java(tm). It\n" +
						"is freely copyable as long as you keep this notice and\n" +
						"the splash screen intact.\n" +
						"Copyright (c) 1995-1997 by Ian F. Darwin, ian@darwinsys.com.\n" +
						"Adapted by Gert Florijn (version 1.1) and " +
						"Sylvia Stuurman (version 1.2 and higher) for the Open" +
						"University of the Netherlands, 2002 -- now.\n" +
						"Author's version available from http://www.darwinsys.com/",
				"About JabberPoint",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	/**
	 * Adds the addFileMenu and its element(s).
	 */
	private void addFileMenu()
	{
		Menu fileMenu = new Menu(FILE);

		this.addOpenMenu(fileMenu);
		this.addNewMenu(fileMenu);
		this.addSaveManu(fileMenu);
		fileMenu.addSeparator();
		this.addExitMenu(fileMenu);

		add(fileMenu);
	}

	/**
	 * Adds the  addOpenMenu and its element(s).
	 */
	private void addOpenMenu(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(OPEN));
		this.menuItem.addActionListener(actionEvent -> {
			this.slideViewerComponent.clear();

			try {
				this.xmlAccessor.loadFile(this.slideViewerComponent.getPresentation(), TESTFILE);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			this.slideViewerComponent.getPresentation().setSlideNumber(0);
			this.slideViewerComponent.update(this.slideViewerComponent.getPresentation(),this.slideViewerComponent.getPresentation().getCurrentSlide());
			parent.repaint();
		});
	}

	/**
	 * Adds the addNewMenu and its element(s).
	 */
	private void addNewMenu(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(NEW));
		this.menuItem.addActionListener(actionEvent -> {
			this.slideViewerComponent.clear();
			this.parent.repaint();
		});
	}

	/**
	 * Adds the addSaveManu and its element(s).
	 */
	private void addSaveManu(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(SAVE));
		this.menuItem.addActionListener(e -> {
			String inputDataFromUser = JOptionPane.showInputDialog("Give a name to the file to be saved:", null);
			try {
				this.xmlAccessor.saveFile(this.slideViewerComponent.getPresentation(), SAVEFILE);
				if (!(inputDataFromUser.equals("") || inputDataFromUser == null)){
					String fileSaveName = inputDataFromUser + ".xml";
					xmlAccessor.saveFile(this.slideViewerComponent.getPresentation(), fileSaveName);
				} else {
					JOptionPane.showMessageDialog(parent, "File not saved!",
							SAVEERR, JOptionPane.ERROR_MESSAGE);
				}
			} catch (IOException exc) {
				JOptionPane.showMessageDialog(parent, IOEX + exc,
						SAVEERR, JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	/**
	 * Adds the addExitMenu and its element(s).
	 */
	private void addExitMenu(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(EXIT));
		this.menuItem.addActionListener(actionEvent -> System.exit(0));
	}

	/**
	 * Adds the addViewMenu and its element(s).
	 */
	private void addViewMenu()
	{
		Menu viewMenu = new Menu(VIEW);

		this.addNextMenu(viewMenu);
		this.addPrevMenu(viewMenu);
		this.addGotoMenu(viewMenu);

		add(viewMenu);
	}

	/**
	 * Adds the addNextMenu and its element(s).
	 */
	private void addNextMenu(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(NEXT));
		this.menuItem.addActionListener(actionEvent -> {
			this.slideViewerComponent.nextSlide();
		});
	}

	/**
	 * Adds the addPrevMenu and its element(s).
	 */
	private void addPrevMenu(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(PREV));
		this.menuItem.addActionListener(actionEvent -> {
			this.slideViewerComponent.prevSlide();
		});

	}

	/**
	 * Adds the addGotoMenu and its element(s).
	 */
	private void addGotoMenu(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(GOTO));
		this.menuItem.addActionListener(actionEvent -> {
			String pageNumberStr = JOptionPane.showInputDialog(PAGENR);
			int pageNumber = Integer.parseInt(pageNumberStr);

			if (pageNumber > this.slideViewerComponent.getPresentation().getSize())
			{
				this.slideViewerComponent.setSlideNumber(this.slideViewerComponent.getPresentation().getSlideNumber());
			}
			else if (pageNumber > 0)
			{
				this.slideViewerComponent.setSlideNumber(pageNumber - 1);
			}
		});
	}

	/**
	 * Adds the helpMenu and its element(s).
	 */
	private void addHelpMenu()
	{
		Menu helpMenu = new Menu(HELP);
		this.addAboutMenu(helpMenu);
		setHelpMenu(helpMenu);		//Needed for portability (Motif, etc.).
	}

	private void addAboutMenu(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(ABOUT));
		this.menuItem.addActionListener(actionEvent -> showAboutBox());
	}
}
