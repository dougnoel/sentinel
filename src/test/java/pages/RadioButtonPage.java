package pages;

import com.dougnoel.sentinel.elements.radiobuttons.Radiobutton;
import com.dougnoel.sentinel.pages.Page;

public class RadioButtonPage extends Page {
	public Radiobutton male_radio_button() { return new Radiobutton(ID, "male"); }
	public Radiobutton female_radio_button() { return new Radiobutton(ID, "female"); }
	public Radiobutton other_radio_button() { return new Radiobutton(ID, "other"); }
	public Radiobutton zero_to_thirty_radio_button() { return new Radiobutton(ID, "age1"); }
	public Radiobutton thirty_one_to_sixty_radio_button() { return new Radiobutton(ID, "age2"); }
	public Radiobutton sixty_one_to_one_hundred_radio_button() { return new Radiobutton(ID, "age3"); }
}
