package domainapp.modules.simple.dom.so;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import org.apache.isis.applib.services.repository.RepositoryService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Customers_Test {

    @Mock RepositoryService mockRepositoryService;
    @Mock IsisJdoSupport_v3_2 mockIsisJdoSupport_v3_2;

    Customers objects;

    @BeforeEach
    public void setUp() {
        objects = new Customers(mockRepositoryService, mockIsisJdoSupport_v3_2);
    }

    @Nested
    class create {

        @Test
        void happyCase() {

            // given
            final String someName = "Foobar";

            // expect
            when(mockRepositoryService.persist(
                    argThat((ArgumentMatcher<Customer>) simpleObject -> Objects.equals(simpleObject.getLastName(), someName)))
            ).then((Answer<Customer>) invocation -> invocation.getArgument(0));

            // when
            final Customer obj = objects.create(someName);

            // then
            assertThat(obj).isNotNull();
            assertThat(obj.getLastName()).isEqualTo(someName);
        }
    }

    @Nested
    class ListAll {

        @Test
        void happyCase() {

            // given
            final List<Customer> all = new ArrayList<>();

            // expecting
            when(mockRepositoryService.allInstances(Customer.class))
                .thenReturn(all);

            // when
            final List<Customer> list = objects.listAll();

            // then
            assertThat(list).isEqualTo(all);
        }
    }
}
