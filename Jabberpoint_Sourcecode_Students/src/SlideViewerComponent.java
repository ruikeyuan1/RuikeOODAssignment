import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JFrame;


/** <p>SlideViewerComponent is a graphical component that ca display Slides.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class SlideViewerComponent extends JComponent {
		
	private Slide slide; //The current slide
	private Font labelFont = null; //The font for labels
	private Presentation presentation = null; //The presentation
	private JFrame frame = null;

	private static final long serialVersionUID = 227L;
	private static final Color BGCOLOR = Color.white;
	private static final Color COLOR = Color.black;
	private static final String FONTNAME = "Dialog";
	private static final int FONTSTYLE = Font.BOLD;
	private static final int FONTHEIGHT = 10;
	private static final int XPOS = 1100;
	private static final int YPOS = 20;

	public SlideViewerComponent(JFrame frame, Presentation pres) {
		setBackground(BGCOLOR); 
		presentation = pres;
		labelFont = new Font(FONTNAME, FONTSTYLE, FONTHEIGHT);
		this.frame = frame;
	}

	public Dimension getPreferredSize() {
		return new Dimension(Slide.WIDTH, Slide.HEIGHT);
	}

	public Presentation getPresentation() {
		return this.presentation;
	}

	public void update(Presentation presentation, Slide data) {
		if (data == null) {
			repaint();
			return;
		}

		this.slide = data;
		repaint();
		frame.setTitle(presentation.getTitle());
	}

	public void paintComponent(Graphics g)
	{
		if (this.presentation.getSlideNumber() < 0 || this.slide == null)
			return;

		preparePainting(g, this.presentation.getSlideNumber(),this.presentation.getSize());

		Rectangle area = new Rectangle(0, YPOS, getWidth(), (getHeight() - YPOS));
		this.slide.draw(g, area, this);
	}

	/**
	 * prepare defaults for painting
	 */
	private void preparePainting(Graphics g, int currentSlideNumber, int slidesSize)
	{
		g.setColor(BGCOLOR);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setFont(labelFont);
		g.setColor(COLOR);

		g.drawString("Slide " + (1 +  currentSlideNumber + " of " +  slidesSize), XPOS, YPOS);
	}
}
