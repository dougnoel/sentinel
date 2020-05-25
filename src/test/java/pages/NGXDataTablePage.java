package pages;

import com.dougnoel.sentinel.elements.Span;
import com.dougnoel.sentinel.elements.tables.NGXDataTable;
import com.dougnoel.sentinel.pages.Page;

public class NGXDataTablePage extends Page {
	public NGXDataTable example_table() { return new NGXDataTable(XPATH, "/html/body/app-root/div/content/basic-auto-demo/div/ngx-datatable"); }
	public Span name_header() { return new Span(XPATH, "//datatable-header-cell[1]/div/span[1]/span"); }
}
