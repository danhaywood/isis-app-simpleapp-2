package domainapp.webapp.bdd.stepdefs.domain;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;

import static org.junit.jupiter.api.Assertions.fail;

import domainapp.webapp.bdd.CucumberTestAbstract;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class CustomersThenStepDef extends CucumberTestAbstract {

    @Inject private CustomersContext context;

    @Then("the name is now {string}")
    public void the_name_is_now(String name) {
        Assertions.assertThat(context.getCustomer().getLastName()).isEqualTo(name);
    }

    @Then("the name is unchanged")
    public void the_name_is_unchanged() {
        Assertions.assertThat(context.getCustomer().getLastName()).isEqualTo(context.getOriginalName());
    }

    @And("a warning is raised")
    public void a_warning_is_raised() {
        Assertions.assertThat(context.getEx()).isNotNull();
    }

}
