package domainapp.modules.simple.dom.so;

import java.util.Comparator;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import org.apache.isis.applib.jaxb.PersistentEntityAdapter;

import static org.apache.isis.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.types.Name;
import domainapp.modules.simple.types.Notes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE, schema = "customer")
@javax.jdo.annotations.DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="id")
@javax.jdo.annotations.Version(strategy= VersionStrategy.DATE_TIME, column="version")
@javax.jdo.annotations.Unique(name="Customer_lastName_UNQ", members = {"lastName"})
@DomainObject()
@DomainObjectLayout()
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Customer implements Comparable<Customer> {

    public static Customer withLastName(String name) {
        val simpleObject = new Customer();
        simpleObject.setLastName(name);
        return simpleObject;
    }

    public static class ActionDomainEvent extends SimpleModule.ActionDomainEvent<Customer> {}

    @Inject RepositoryService repositoryService;
    @Inject TitleService titleService;
    @Inject MessageService messageService;

    private Customer() {
    }

    public String title() {
        return getLastName() +
                (getFirstName() != null ? ", " + getFirstName(): "");
    }

    @Column(allowsNull = "true")
    @Getter @Setter @ToString.Include
    @Property(editing = Editing.ENABLED)
    private String firstName;

    @Name
    @Getter @Setter @ToString.Include
    private String lastName;

    @Notes
    @Getter @Setter
    private String notes;


    public static class UpdateNameActionDomainEvent extends Customer.ActionDomainEvent {}
    @Action(semantics = IDEMPOTENT,
            command = CommandReification.ENABLED, publishing = Publishing.ENABLED,
            domainEvent = UpdateNameActionDomainEvent.class)
    public Customer updateName(
            @Name final String lastName
            ,@Nullable String firstName) {
        setLastName(lastName);
        setFirstName(firstName);
        return this;
    }

    public String default0UpdateName() {
        return getLastName();
    }
    public String default1UpdateName() {
        return getFirstName();
    }

    public static class DeleteActionDomainEvent extends Customer.ActionDomainEvent {}
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE, domainEvent = DeleteActionDomainEvent.class)
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    private final static Comparator<Customer> comparator =
            Comparator.comparing(Customer::getLastName);

    @Override
    public int compareTo(final Customer other) {
        return comparator.compare(this, other);
    }

}
