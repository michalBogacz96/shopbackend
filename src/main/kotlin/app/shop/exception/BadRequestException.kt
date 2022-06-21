package app.shop.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException : RuntimeException {
    constructor(message: String?) : super(message)
}