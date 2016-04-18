package main;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Created by vladislav on 18.04.16.
 */
public class ValidationHelper {

    public static final String EMPTY_JSON = "{}";
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> boolean isInvalid(T object) {
        final Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(object);

        final int size = constraintViolations.size();
        if (size > 0) {
            System.out.println(object);
            System.out.println(String.format("Error count: %d", size));

            for (ConstraintViolation<T> cv : constraintViolations)
                System.out.println(String.format(
                        "ERROR! property: [%s], value: [%s], message: [%s]",
                        cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage()));

            System.out.println();
        }
        return !constraintViolations.isEmpty();
    }

}
