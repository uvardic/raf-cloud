package njp.raf.cloud.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class RestUtilities {

    private RestUtilities() {}

    public static ResponseEntity<Map<String, String>> createErrorMap(BindingResult bindingResult) {
        Map<String, String> errorMap = bindingResult.getFieldErrors()
                .stream()
                .collect(toMap(FieldError::getField, RestUtilities::getErrorMessage, (a, b) -> b));

        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    private static String getErrorMessage(FieldError fieldError) {
        if (fieldError.getDefaultMessage() == null)
            return "Error message was't provided";

        return fieldError.getDefaultMessage();
    }

}
