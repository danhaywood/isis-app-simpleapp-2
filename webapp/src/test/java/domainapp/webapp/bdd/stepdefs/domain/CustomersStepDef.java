package domainapp.webapp.bdd.stepdefs.domain;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import domainapp.modules.simple.dom.so.Customer;
import domainapp.modules.simple.dom.so.Customers;
import domainapp.webapp.bdd.CucumberTestAbstract;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class CustomersStepDef extends CucumberTestAbstract {

    public CustomersStepDef() {
        System.out.println("CustomersStepDef");
    }

    @Given("^there (?:is|are).* (\\d+) customer[s]?$")
    public void there_are_N_customer(int n) {

        final List<Customer> list = wrap(customers).listAll();
        assertThat(list.size(), is(n));
    }

    @When("^.*create (?:a|another) .*customer$")
    public void create_a_customer() {

        wrap(customers).create(UUID.randomUUID().toString());
    }

    @Inject protected Customers customers;

}
