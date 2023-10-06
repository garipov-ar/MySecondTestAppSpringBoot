package ru.garipov.MySecondTestAppSpringBoot.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.garipov.MySecondTestAppSpringBoot.model.Request;

@Service
@Primary
public class ModifySourceRequestService implements ModifyRequestService{
    @Override
    public void modify(Request request) {

        request.setSource("Новое значение для source");
    }
}
