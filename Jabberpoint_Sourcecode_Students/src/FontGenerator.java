import java.awt.Font;
import java.awt.Color;

public class FontGenerator extends Font{
    private Color color;

    /**
     * constructor for generating font of page label
     * @param font
     * @param color
     */
    public FontGenerator(Font font, Color color) {
        super(font);
        this.color = color;
    }

    /**
     * constructor for generating font of text content in different slides
     * @param font
     */
    public FontGenerator(Font font){
        super(font);
    }

    public Color getColor(){
        return this.color;
    }
}