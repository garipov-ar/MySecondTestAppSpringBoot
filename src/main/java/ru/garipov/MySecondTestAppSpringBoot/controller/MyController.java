package ru.garipov.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) throws UnsupportedCodeException {
        log.info("Received request: {}", request);

        if ("123".equals(request.getUid())) {
            log.error("Received request with uid '123', throwing UnsupportedCodeException");
            throw new UnsupportedCodeException();
        }

        Response response = createResponse(request);

        try {
            validateService.isValid(bindingResult);
        } catch (ValidationFailedException e) {
            log.error("Validation failed: {}", e.getMessage());
            return new ResponseEntity<>(createErrorResponse(e), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(createErrorResponse(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifyResponseService.modify(response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Response createResponse(Request request) {
        return Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
    }

    private Response createErrorResponse(Exception e) {
        log.error("An exception occurred: {}", e.getMessage());
        Response response = Response.builder()
                .code(Codes.FAILED)
                .errorCode(ErrorCodes.UNKNOWN_EXCEPTION)
                .errorMessage(ErrorMessages.UNKNOWN)
                .build();
        if (e instanceof ValidationFailedException) {
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
        }
        return response;
    }

    @ExceptionHandler({ValidationFailedException.class, UnsupportedCodeException.class})
    public ResponseEntity<Response> handleException(Exception ex) {
        log.error("Handling exception: {}", ex.getMessage());
        return new ResponseEntity<>(createErrorResponse(ex), HttpStatus.BAD_REQUEST);
    }
}
