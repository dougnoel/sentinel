package pages;

import com.dougnoel.sentinel.elements.Div;
import com.dougnoel.sentinel.elements.Link;
import com.dougnoel.sentinel.pages.Page;

public class GuineaPigPage extends Page {
	public Link example_link() { return new Link(TEXT, "i am a link"); }
	public Div invisible_div() { return new Div(ID, "invisible div"); }
	public Div example_div() { return new Div(ID, "i_am_an_id"); }
	public Div fake_div() { return new Div(ID, "fake div"); }	
}
