package neobis.mobimaket.exception.handler;

import neobis.mobimaket.exception.IncorrectLoginException;
import neobis.mobimaket.exception.NotFoundException;
import neobis.mobimaket.exception.RegistrationTokenExpiredException;
import neobis.mobimaket.exception.UserAlreadyExistException;
import jakarta.validation.ConstraintViolationException;
import neobis.mobimaket.exception.reponse.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestControllerAdvice
public class MainHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundException(NotFoundException e) { // если сущность (user,product, etc...) не была найдена
        return new ExceptionResponse(HttpStatus.NOT_FOUND, e.getClass().getName(), e.getMessage());
    }

    @ExceptionHandler(IncorrectLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse incorrectLoginException(IncorrectLoginException e) {
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getClass().getName(), e.getMessage());
    }

    @ExceptionHandler(RegistrationTokenExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse registrationTokenExpiredException(RegistrationTokenExpiredException e) {
        return new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getClass().getName(), e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.FOUND)
    public ExceptionResponse UserAlreadyExistException(UserAlreadyExistException e) { // если введенный логин уже существует
        return new ExceptionResponse(HttpStatus.FOUND, e.getClass().getName(), e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public List<ExceptionResponse> onConstraintValidationException(
            ConstraintViolationException e
    ) {
        return e.getConstraintViolations().stream()
                .map(
                        violation -> new ExceptionResponse(
                                HttpStatus.NOT_ACCEPTABLE,
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .toList();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public List<ExceptionResponse> onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        return e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ExceptionResponse(
                        HttpStatus.NOT_ACCEPTABLE,
                        error.getField(),
                        error.getDefaultMessage()))
                .collect(toList());
    }

}
