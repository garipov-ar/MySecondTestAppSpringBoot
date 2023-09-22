package ru.garipov.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import ru.garipov.MySecondTestAppSpringBoot.model.*;
import ru.garipov.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.garipov.MySecondTestAppSpringBoot.service.ValidateService;
import ru.garipov.MySecondTestAppSpringBoot.util.DateTimeUtil;

import java.beans.SimpleBeanInfo;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
public class MyController {
    private final ValidateService validateService;
    private final ModifyResponseService modifyResponseService;

    @Autowired
    public MyController(ValidateService validateService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService) {
        this.validateService = validateService;
        this.modifyResponseService = modifyResponseService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) throws UnsupportedCodeException {

        log.info("Received request: {}", request);

        // Проверяем поле uid на равенство "123" и выбрасываем исключение, если условие выполняется
        if ("123".equals(request.getUid())) {
            log.error("Received request with uid '123', throwing UnsupportedCodeException");
            throw new UnsupportedCodeException();
        }

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        log.info("Generated response: {}", response);

        try {
            validateService.isValid(bindingResult);
        } catch (ValidationFailedException e) {
            log.error("Validation failed: {}", e.getMessage());
            // Обработка ValidationFailedException
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("An unknown exception occurred: {}", e.getMessage());
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifyResponseService.modify(response);
        log.info("Modified response: {}", response);

        return new ResponseEntity<>(modifyResponseService.modify(response), HttpStatus.OK);
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<Response> handleValidationFailedException(ValidationFailedException ex) {
        Response response = Response.builder()
                .code(Codes.FAILED)
                .errorCode(ErrorCodes.VALIDATION_EXCEPTION)
                .errorMessage(ErrorMessages.valueOf(ex.getMessage()))  // Используйте сообщение из исключения
                .build();
        log.error("Handling ValidationFailedException: {}", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedCodeException.class)
    public ResponseEntity<Response> handleUnsupportedCodeException(UnsupportedCodeException ex) {
        Response response = Response.builder()
                .code(Codes.FAILED)
                .errorCode(ErrorCodes.UNSUPPORTED_EXCEPTION)
                .errorMessage(ErrorMessages.valueOf(ex.getMessage()))  // Используйте сообщение из исключения
                .build();
        log.error("Handling UnsupportedCodeException: {}", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

