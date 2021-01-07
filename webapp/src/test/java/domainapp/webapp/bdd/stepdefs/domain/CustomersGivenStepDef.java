package domainapp.webapp.bdd.stepdefs.domain;

import java.util.List;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import lombok.val;

import domainapp.modules.simple.dom.so.Customer;
import domainapp.modules.simple.dom.so.Customers;
import domainapp.webapp.bdd.CucumberTestAbstract;
import io.cucumber.java.en.Given;

public class CustomersGivenStepDef extends CucumberTestAbstract {

    @Inject protected CustomersContext context;
    @Inject protected Customers customers;

    @Given("a customer")
    public void a_customer() {
        final List<Customer> list = wrap(customers).listAll();
        val customer = list.get(0);
        context.setCustomer(customer);
        context.setOriginalName(customer.getLastName());
    }

}
