package com.dougnoel.sentinel.elements.tables;
import java.util.Map;

public class MetabolonPortalResultsTable extends Table {
    
    public MetabolonPortalResultsTable(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}

    protected String tableHeaderTag = "ul"; //"//ul[contains(@class,'TableHeader')]";
	protected String tableRowTag = "li";
	protected String tableCellDataTag = "td";
	protected String tableDataCellLocator = ".//" + tableCellDataTag;
	protected String tableSiblingCellLocator = ".//..//*";
}