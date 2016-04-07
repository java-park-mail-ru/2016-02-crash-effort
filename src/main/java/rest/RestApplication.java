package rest;

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

    public static <T> boolean validate(T object) {
        Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(object);

        int size = constraintViolations.size();
        if (size > 0) {
            System.out.println(object);
            System.out.println(String.format("Error count: %d", size));

            for (ConstraintViolation<T> cv : constraintViolations)
                System.out.println(String.format(
                        "ERROR! property: [%s], value: [%s], message: [%s]",
                        cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage()));

            System.out.println();
        }
        return constraintViolations.isEmpty();
    }

    @Override
    public Set<Object> getSingletons() {
        final HashSet<Object> objects = new HashSet<>();
        objects.add(new Users());
        objects.add(new Session());
        return objects;
    }
}
