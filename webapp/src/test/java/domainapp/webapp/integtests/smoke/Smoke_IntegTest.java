package domainapp.webapp.integtests.smoke;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.services.wrapper.InvalidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import org.apache.isis.applib.services.xactn.TransactionService;

import static org.assertj.core.api.Assertions.assertThat;

import domainapp.webapp.integtests.ApplicationIntegTestAbstract;
import domainapp.modules.simple.dom.so.Customer;
import domainapp.modules.simple.dom.so.Customers;

@Transactional
class Smoke_IntegTest extends ApplicationIntegTestAbstract {

    @Inject
    Customers menu;
    @Inject TransactionService transactionService;

    @Test
    void happy_case() {

        // when
        List<Customer> all = wrap(menu).listAll();

        // then
        assertThat(all).isEmpty();


        // when
        final Customer fred = wrap(menu).create("Fred");
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(1);
        assertThat(all).contains(fred);


        // when
        final Customer bill = wrap(menu).create("Bill");
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(2);
        assertThat(all).contains(fred, bill);


        // when
        wrap(fred).updateName("Freddy");
        transactionService.flushTransaction();

        // then
        assertThat(wrap(fred).getLastName()).isEqualTo("Freddy");


        // when
        wrap(fred).setNotes("These are some notes");
        transactionService.flushTransaction();

        // then
        assertThat(wrap(fred).getNotes()).isEqualTo("These are some notes");


        // when
        Assertions.assertThrows(InvalidException.class, () -> {
            wrap(fred).updateName("New name !!!");
            transactionService.flushTransaction();
        }, "Exclamation mark is not allowed");

        // then
        assertThat(wrap(fred).getNotes()).isEqualTo("These are some notes");


        // when
        wrap(fred).delete();
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(1);
    }

}

