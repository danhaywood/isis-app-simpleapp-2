package domainapp.webapp.bdd.stepdefs.domain;

import java.util.List;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.fail;

import org.apache.isis.applib.services.wrapper.InvalidException;

import lombok.Data;

import domainapp.modules.simple.dom.so.Customer;
import domainapp.modules.simple.dom.so.Customers;
import domainapp.webapp.bdd.CucumberTestAbstract;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.ScenarioScope;

@Service
@ScenarioScope
@Data
public class CustomersContext {

    private Customer customer;
    private String originalName;
    private InvalidException ex;
}
