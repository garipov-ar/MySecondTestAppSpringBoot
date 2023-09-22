package ru.garipov.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.garipov.MySecondTestAppSpringBoot.model.Response;

@Service
public interface ModifyResponseService {
    Response modify(Response response);
}
