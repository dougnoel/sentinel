package pages;

import com.dougnoel.sentinel.elements.Link;
import com.dougnoel.sentinel.pages.Page;

public class GuineaPigPage extends Page {
	public Link i_am_a_link_link() { return new Link(TEXT, "i am a link"); }
}
