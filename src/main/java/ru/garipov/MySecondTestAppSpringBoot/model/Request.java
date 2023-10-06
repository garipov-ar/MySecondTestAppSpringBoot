package ru.garipov.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @NotBlank(message = "Уникальный идентификатор сообщения обязателен")
    @Size(max = 32, message = "Уникальный идентификатор сообщения должен содержать не более 32 символов")
    private String uid;

    @NotBlank(message = "Уникальный идентификатор операции обязателен")
    @Size(max = 32, message = "Уникальный идентификатор операции должен содержать не более 32 символов")
    private String operationUid;

    private String systemName;  // Изменили тип на перечисление Systems

    @NotBlank(message = "Время создания сообщения обязательно")
    private String systemTime;

    private String source;

    @Min(value = 1, message = "Уникальный идентификатор коммуникации должен быть не менее 1")
    @Max(value = 100000, message = "Уникальный идентификатор коммуникации должен быть не более 100000")
    private int communicationId;

    private int templateId;

    private int productCode;

    private int smsCode;

    @Override
    public String toString() {
        return "{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName='" + systemName + '\'' +  // Изменили тип на перечисление Systems
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                '}';
    }
}



