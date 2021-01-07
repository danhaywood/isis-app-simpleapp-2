package domainapp.modules.simple.dom.so;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

@ExtendWith(MockitoExtension.class)
class Customer_Test {

    @Mock TitleService mockTitleService;
    @Mock MessageService mockMessageService;
    @Mock RepositoryService mockRepositoryService;

    Customer object;

    @BeforeEach
    public void setUp() throws Exception {
        object = Customer.withLastName("Foo");
        object.titleService = mockTitleService;
        object.messageService = mockMessageService;
        object.repositoryService = mockRepositoryService;
    }

    @Nested
    public class updateName {

        @Test
        void happy_case() {
            // given
            assertThat(object.getLastName()).isEqualTo("Foo");

            // when
            object.updateName("Bar", null);

            // then
            assertThat(object.getLastName()).isEqualTo("Bar");
        }

    }
    @Nested
    class delete {

        @Test
        void last_name_only() throws Exception {

            // given
            assertThat(object).isNotNull();
            assertThat(object.getLastName()).isEqualTo("Foo");
            assertThat(object.getFirstName()).isNull();

            // then
            assertThat(object.title()).isEqualTo("Foo");
        }

        @Test
        void with_first_name() throws Exception {

            // given
            assertThat(object).isNotNull();
            assertThat(object.getLastName()).isEqualTo("Foo");

            // when
            object.setFirstName("Fred");

            // then
            assertThat(object.title()).isEqualTo("Foo, Fred");
        }
    }

    @Nested
    class title {

        @Test
        void no_first_name() throws Exception {

            // given
            assertThat(object).isNotNull();

            // expecting
            when(mockTitleService.titleOf(object)).thenReturn("Foo");

            // when
            object.delete();

            // then
            verify(mockMessageService).informUser("'Foo' deleted");
            verify(mockRepositoryService).removeAndFlush(object);
        }
    }
}
