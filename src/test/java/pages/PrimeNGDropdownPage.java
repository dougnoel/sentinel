package pages;

import com.dougnoel.sentinel.elements.PrimeNGDropdown;
import com.dougnoel.sentinel.pages.Page;

public class PrimeNGDropdownPage extends Page {
	public PrimeNGDropdown city_dropdown() { return new PrimeNGDropdown(XPATH, "//p-dropdown[1]"); }
}
