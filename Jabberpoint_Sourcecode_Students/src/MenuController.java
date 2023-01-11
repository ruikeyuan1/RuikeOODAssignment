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

	private void addFileMenu()
	{
		Menu fileMenu = new Menu(FILE);

		this.addOpen(fileMenu);
		this.addNew(fileMenu);
		this.addSave(fileMenu);
		fileMenu.addSeparator();
		this.addExit(fileMenu);

		add(fileMenu);
	}

	private void addOpen(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(OPEN));
		this.menuItem.addActionListener(actionEvent -> {
			this.slideViewerComponent.getPresentation().clear();

			try {
				this.xmlAccessor.loadFile(this.slideViewerComponent.getPresentation(), TESTFILE);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			this.slideViewerComponent.getPresentation().setSlideNumber(0);
			this.slideViewerComponent.update(this.slideViewerComponent.getPresentation(), this.slideViewerComponent.getPresentation().getCurrentSlide());

			parent.repaint();
		});
	}

	private void addNew(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(NEW));
		this.menuItem.addActionListener(actionEvent -> {
			this.slideViewerComponent.getPresentation().clear();
			this.parent.repaint();
		});
	}

	private void addSave(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(SAVE));
		this.menuItem.addActionListener(e -> {
			try {
				this.xmlAccessor.saveFile(this.slideViewerComponent.getPresentation(), SAVEFILE);
			} catch (IOException exc) {
				JOptionPane.showMessageDialog(parent, IOEX + exc,
						SAVEERR, JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	private void addExit(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(EXIT));
		this.menuItem.addActionListener(actionEvent -> System.exit(0));
	}

	private void addViewMenu()
	{
		Menu viewMenu = new Menu(VIEW);

		this.addNext(viewMenu);
		this.addPrev(viewMenu);
		this.addGoto(viewMenu);

		add(viewMenu);
	}

	private void addNext(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(NEXT));
		this.menuItem.addActionListener(actionEvent -> {
			this.slideViewerComponent.getPresentation().nextSlide();
			this.slideViewerComponent.update(this.slideViewerComponent.getPresentation(), this.slideViewerComponent.getPresentation().getCurrentSlide());
		});
	}

	private void addPrev(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(PREV));
		this.menuItem.addActionListener(actionEvent -> {
			this.slideViewerComponent.getPresentation().prevSlide();
			this.slideViewerComponent.update(this.slideViewerComponent.getPresentation(), this.slideViewerComponent.getPresentation().getCurrentSlide());
		});

	}

	private void addGoto(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(GOTO));
		this.menuItem.addActionListener(actionEvent -> {
			String pageNumberStr = JOptionPane.showInputDialog(PAGENR);
			int pageNumber = Integer.parseInt(pageNumberStr);

			if (pageNumber > this.slideViewerComponent.getPresentation().getSize())
			{
				this.slideViewerComponent.getPresentation().setSlideNumber(this.slideViewerComponent.getPresentation().getSlideNumber());
				this.slideViewerComponent.update(this.slideViewerComponent.getPresentation(), this.slideViewerComponent.getPresentation().getCurrentSlide());
			}
			else if (pageNumber > 0)
			{
				this.slideViewerComponent.getPresentation().setSlideNumber(pageNumber - 1);
				this.slideViewerComponent.update(this.slideViewerComponent.getPresentation(), this.slideViewerComponent.getPresentation().getCurrentSlide());
			}
		});
	}

	/**
	 * Adds the helpmenu and its element(s).
	 */
	private void addHelpMenu()
	{
		Menu helpMenu = new Menu(HELP);
		this.addAbout(helpMenu);
		setHelpMenu(helpMenu);		//Needed for portability (Motif, etc.).
	}

	private void addAbout(Menu menu)
	{
		menu.add(this.menuItem = mkMenuItem(ABOUT));
		this.menuItem.addActionListener(actionEvent -> showAboutBox());
	}
}
