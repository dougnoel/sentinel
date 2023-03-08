package steps;

import com.dougnoel.sentinel.configurations.Configuration;
import io.cucumber.java.en.When;

public class InitializationSteps {
    /**
     * Adds the parsed string keys and values in Configuration for later use
     *
     * @param values String the string with keys and values
     * Example:
     *  When I initialize the configuration values as follows
     *     """
     *     id: 10
     *     category_name: puppies
     *     """
     *
     */
    @When("I initialize the configuration values as follows")
    public void iInitializeTheData(String values) {
        var items = values.replace(" ","").split("\n");
        for (var item:items
             ) {
            Configuration.update(item.split(":")[0],  item.split(":")[1]);
        }
    }
}
