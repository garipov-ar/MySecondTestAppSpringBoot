package ru.garipov.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.garipov.MySecondTestAppSpringBoot.exception.ValidationFailedException;

@Service
public interface ValidateService {
    void isValid(BindingResult bindingResult) throws ValidationFailedException;
}
