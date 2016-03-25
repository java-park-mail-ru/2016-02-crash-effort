package rest;

import main.AccountService;
import main.Main;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by e.shubin on 25.02.2016.
 */
@ApplicationPath("api")
public class RestApplication extends Application {
    public static final String EMPTY_JSON = "{}";
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static boolean validate(Object object) {
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(object);

        System.out.println(object);
        System.out.println(String.format("Error count: %d", constraintViolations.size()));

        for (ConstraintViolation<Object> cv : constraintViolations)
            System.out.println(String.format(
                    "ERROR! property: [%s], value: [%s], message: [%s]",
                    cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage()));

        System.out.println();

        return constraintViolations.isEmpty();
    }

    public static AccountService getAccountService() {
        return (AccountService) Main.CONTEXT.get(AccountService.class);
    }

    @Override
    public Set<Object> getSingletons() {
        final HashSet<Object> objects = new HashSet<>();
        objects.add(new Users());
        objects.add(new Session());
        return objects;
    }
}
