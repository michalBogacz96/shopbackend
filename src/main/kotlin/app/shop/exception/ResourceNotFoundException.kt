package app.shop.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(resourceName: String?, fieldName: String?, fieldValue: Any?) :
    RuntimeException(
        String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue)
    )