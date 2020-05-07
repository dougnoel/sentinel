package pages;

import com.dougnoel.sentinel.elements.tables.NGXDataTable;
import com.dougnoel.sentinel.pages.Page;

public class NGXDataTablePage extends Page {
	public NGXDataTable example_table() { return new NGXDataTable(XPATH, "/html/body/app-root/div/content/basic-auto-demo/div/ngx-datatable"); }
}
