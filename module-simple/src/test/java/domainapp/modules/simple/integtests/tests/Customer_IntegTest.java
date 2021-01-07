package domainapp.modules.simple.integtests.tests;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.services.wrapper.DisabledException;
import org.apache.isis.applib.services.wrapper.InvalidException;
import org.apache.isis.persistence.jdo.datanucleus5.jdosupport.mixins.Persistable_datanucleusIdLong;

import lombok.Getter;

import domainapp.modules.simple.dom.so.Customer;
import domainapp.modules.simple.fixture.SimpleObject_persona;
import domainapp.modules.simple.integtests.SimpleModuleIntegTestAbstract;

@Transactional
public class Customer_IntegTest extends SimpleModuleIntegTestAbstract {

    Customer customer;

    @BeforeEach
    public void setUp() {
        // given
        customer = fixtureScripts.runPersona(SimpleObject_persona.FOO);
    }

    public static class name extends Customer_IntegTest {

        @Test
        public void accessible() {
            // when
            final String name = wrap(customer).getName();

            // then
            assertThat(name).isEqualTo(customer.getName());
        }

        @Test
        public void not_editable() {

            // expect
            assertThrows(DisabledException.class, ()->{

                // when
                wrap(customer).setName("new name");
            });
        }

    }

    public static class updateName extends Customer_IntegTest {

        @DomainService
        public static class UpdateNameListener {

            @Getter
            List<Customer.UpdateNameActionDomainEvent> events = new ArrayList<>();

            @EventListener(Customer.UpdateNameActionDomainEvent.class)
            public void on(Customer.UpdateNameActionDomainEvent ev) {
                events.add(ev);
            }
        }

        @Inject
        UpdateNameListener updateNameListener;


        @Test
        public void can_be_updated_directly() {

            // given
            updateNameListener.getEvents().clear();

            // when
            wrap(customer).updateName("new name");
            transactionService.flushTransaction();

            // then
            assertThat(wrap(customer).getName()).isEqualTo("new name");
            assertThat(updateNameListener.getEvents()).hasSize(5);
        }

        @Test
        public void fails_validation() {

            // expect
            InvalidException cause = assertThrows(InvalidException.class, ()->{

                // when
                wrap(customer).updateName("new name&");
            });

            // then
            assertThat(cause.getMessage()).contains("Character '&' is not allowed");
        }
    }

    public static class dataNucleusId extends Customer_IntegTest {

        @Test
        public void should_be_populated() {
            // when
            final Long id = mixin(Persistable_datanucleusIdLong.class, customer).prop();

            // then
            assertThat(id).isGreaterThanOrEqualTo(0);
        }
    }

}
