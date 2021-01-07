package domainapp.webapp.bdd.stepdefs.domain;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.fail;

import org.apache.isis.applib.services.wrapper.InvalidException;

import domainapp.webapp.bdd.CucumberTestAbstract;
import io.cucumber.java.en.When;

public class CustomersWhenStepDef extends CucumberTestAbstract {

    @Inject protected CustomersContext context;

    @When("I modify the name to {string}")
    public void modify_the_name_to(String newName) {
        wrap(context.getCustomer()).updateName(newName, null);
    }

    @When("I attempt to modify the name to {string}")
    public void i_attempt_to_modify_the_name_to(String newName) {

        try {
            wrap(context.getCustomer()).updateName(newName, null);
            fail();
        } catch(InvalidException ex) {
            this.context.setEx(ex);
        }

    }


}
