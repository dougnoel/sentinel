package sentinel.elements;

import org.openqa.selenium.support.ui.Select;

import sentinel.exceptions.NoSuchElementException;
import sentinel.exceptions.NoSuchSelectorException;
import sentinel.utils.SelectorType;
import sentinel.utils.StringUtils;

/**
 * Extends PageElement. Is intended to be a a base class for Radiobutton and
 * Dropdown.
 */
public class PageSelectElement extends PageElement {

    /**
     * Implementation of a WebElement to initialize how an element is going to be
     * found when it is worked on by the WebDriver class. Takes a reference to the
     * WebDriver class that will be exercising its functionality.
     * 
     * @param selectorType
     *            SelectorType
     * @param selectorValue
     *            String
     */
    public PageSelectElement(SelectorType selectorType, String selectorValue) {
        super(selectorType, selectorValue);
    }

    public PageSelectElement select(String selectText) throws NoSuchElementException, NoSuchSelectorException{
        Select selectElement = new Select(this.element());
        try {
            selectElement.selectByVisibleText(selectText);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.err.println(
                    "PageSelectElement: Attempting to select a value from a list that does not exist. Check your Cucumber test to ensure what you are trying to select exists.");
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public PageSelectElement select(int index) throws  NoSuchSelectorException, NoSuchElementException{
        Select selectElement = new Select(this.element());
        try {
            selectElement.selectByIndex(index);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.err.println(
                    "PageSelectElement: Attempting to select a value from a list that does not exist. Check your Cucumber test to ensure what you are trying to select exists.");
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public PageSelectElement select(SelectorType selectorType, String selectText) throws NoSuchSelectorException, NoSuchElementException, NumberFormatException{
        Select selectElement = new Select(this.element());
        switch (selectorType) {
        case INDEX:
            return select(Integer.parseInt(selectText));
        case VALUE:
            selectElement.selectByValue(selectText);
            break;
        case TEXT:
            return select(selectText);
        default:
            // This is here in case a new type is added to SelectorType and has not been
            // implemented yet here.
            String errorMessage = StringUtils.format(
                    "Unhandled selector type \"{}\" passed to Page Select Element class. Could not resolve the reference. Refer to the Javadoc for valid options.",
                    selectorType);
            throw new NoSuchSelectorException(errorMessage);
        }

        return this;
    }

    public String getText(int index) throws NoSuchSelectorException, NoSuchElementException{
        Select selectElement = new Select(this.element());
        return selectElement.getOptions().get(index).getText();
    }
    
    public String getSelectedText() throws NoSuchSelectorException, NoSuchElementException{
        Select selectElement = new Select(this.element());
        return selectElement.getFirstSelectedOption().getText();
    }
}
