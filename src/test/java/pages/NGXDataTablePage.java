package pages;

import com.dougnoel.sentinel.elements.Table;
import com.dougnoel.sentinel.pages.Page;

public class NGXDataTablePage extends Page {
	public Table example_table() { return new Table(XPATH, "/html/body/app-root/div/content/basic-auto-demo/div/ngx-datatable"); }
}
