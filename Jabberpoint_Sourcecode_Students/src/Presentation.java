import java.util.ArrayList;


/**
 * <p>Presentations keeps track of the slides in a presentation.</p>
 * <p>Only one instance of this class is available.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class Presentation {
	private String showTitle; //The title of the presentation
	private ArrayList<Slide> listOfSlides = null; //An ArrayList with slides
	private int currentSlideNumber = 0; //The number of the current slide

	public Presentation() {
		listOfSlides = new ArrayList<>();
	}

	public int getSize() {
		return this.listOfSlides.size();
	}

	public String getTitle() {
		return this.showTitle;
	}

	public void setTitle(String nt) {
		this.showTitle = nt;
	}

	public ArrayList<Slide> getListOfSlides()
	{
		return this.listOfSlides;
	}

	//Returns the number of the current slide
	public int getSlideNumber() {
		return currentSlideNumber;
	}

	//Change the current slide number and report it the window
	public void setSlideNumber(int number) {
		this.currentSlideNumber = number;
	}

	//Add a slide to the presentation
	public void append(Slide slide) {
		this.listOfSlides.add(slide);
	}

	//Return a slide with a specific number
	public Slide getSlide(int number) {
		if (number < 0 || number >= getSize()){
			return null;
	    }
		return this.listOfSlides.get(number);
	}

	//Return the current slide
	public Slide getCurrentSlide() {
		return getSlide(this.currentSlideNumber);
	}
}
