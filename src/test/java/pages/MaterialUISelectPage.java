package pages;

import com.dougnoel.sentinel.elements.MaterialUISelect;
import com.dougnoel.sentinel.pages.Page;

public class MaterialUISelectPage extends Page {
	public MaterialUISelect age_dropdown() { return new MaterialUISelect(XPATH, "//*[@id=\"demo-simple-select\"]"); }
}
