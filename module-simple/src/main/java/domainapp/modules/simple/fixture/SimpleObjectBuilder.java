package domainapp.modules.simple.fixture;

import javax.inject.Inject;

import org.apache.isis.testing.fixtures.applib.fixturescripts.BuilderScriptWithResult;

import domainapp.modules.simple.dom.so.Customer;
import domainapp.modules.simple.dom.so.Customers;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class SimpleObjectBuilder extends BuilderScriptWithResult<Customer> {

    @Getter @Setter
    private String name;

    @Override
    protected Customer buildResult(final ExecutionContext ec) {

        checkParam("name", ec, String.class);

        return wrap(customers).create(name);
    }

    // -- DEPENDENCIES

    @Inject
    Customers customers;

}
