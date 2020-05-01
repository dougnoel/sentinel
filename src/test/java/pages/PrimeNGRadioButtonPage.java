package pages;

import com.dougnoel.sentinel.elements.PrimeNGDropdown;
import com.dougnoel.sentinel.pages.Page;

public class PrimeNGRadioButtonPage extends Page {
	public PrimeNGDropdown option_one_radio_buton() { return new PrimeNGDropdown(XPATH, "//*[@id='opt1']"); }
	public PrimeNGDropdown option_two_radio_buton() { return new PrimeNGDropdown(XPATH, "//*[@id='opt2']"); }
	public PrimeNGDropdown option_three_radio_buton() { return new PrimeNGDropdown(XPATH, "//*[@id='opt3']"); }
}
