package ru.garipov.MySecondTestAppSpringBoot.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;
import ru.garipov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.garipov.MySecondTestAppSpringBoot.model.Request;
import ru.garipov.MySecondTestAppSpringBoot.model.Systems;


@Service
public interface ValidateService {
    void isValid(BindingResult bindingResult) throws ValidationFailedException;
}