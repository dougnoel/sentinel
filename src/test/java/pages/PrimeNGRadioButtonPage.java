package pages;

import com.dougnoel.sentinel.elements.PrimeNGRadioButton;
import com.dougnoel.sentinel.pages.Page;

public class PrimeNGRadioButtonPage extends Page {
	public PrimeNGRadioButton option_one_radio_buton() { return new PrimeNGRadioButton(XPATH, "//p-radiobutton[@inputid='opt1']"); }
	public PrimeNGRadioButton option_two_radio_buton() { return new PrimeNGRadioButton(XPATH, "//*[@id='opt2']"); }
	public PrimeNGRadioButton option_three_radio_buton() { return new PrimeNGRadioButton(XPATH, "//*[@id='opt3']"); }
}
