package pages;

import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.pages.Page;

public class TablePage extends Page {
	public Table example_table() { return new Table(ID, "mytable"); }
}
