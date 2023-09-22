package ru.garipov.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.garipov.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.garipov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.garipov.MySecondTestAppSpringBoot.model.Request;
import ru.garipov.MySecondTestAppSpringBoot.model.Response;
import ru.garipov.MySecondTestAppSpringBoot.service.ValidateService;

import java.beans.SimpleBeanInfo;
import java.text.SimpleDateFormat;

@RestController
public class MyController {
    private final ValidateService validateService;

    @Autowired
    public MyController(ValidateService validateService) {
        this.validateService = validateService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) throws UnsupportedCodeException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Проверяем поле uid на равенство "123" и выбрасываем исключение, если условие выполняется
        if ("123".equals(request.getUid())) {
            throw new UnsupportedCodeException();
        }
        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(request.getSystemTime())
                .code("success")
                .errorCode("")
                .errorMessage("")
                .build();
        try {
            validateService.isValid(bindingResult);
        } catch (ValidationFailedException e) {
            // Обработка ValidationFailedException
            response.setCode("failed");
            response.setErrorCode("ValidationException");
            response.setErrorMessage("Ошибка валидации: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setCode("failed");
            response.setErrorMessage("Произошла непредвиденная ошибка");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<Response> handleValidationFailedException(ValidationFailedException ex) {
        Response response = Response.builder()
                .code("failed")
                .errorCode("ValidationException")
                .errorMessage(ex.getMessage())  // Используйте сообщение из исключения
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedCodeException.class)
    public ResponseEntity<Response> handleUnsupportedCodeException(UnsupportedCodeException ex) {
        Response response = Response.builder()
                .code("failed")
                .errorCode("UnsupportedCodeException")
                .errorMessage(ex.getMessage())  // Используйте сообщение из исключения
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
