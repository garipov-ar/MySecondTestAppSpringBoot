package ru.garipov.MySecondTestAppSpringBoot.model;

import lombok.Getter;

@Getter
public enum Positions {
    DEV(2.2, false),
    HR(1.2, false),
    TL(2.6, false),
    MANAGER(3.0, true), // Дополнительная позиция: Менеджер
    QA(2.0, false),     // Дополнительная позиция: QA-инженер
    MARKETING(1.8, false); // Дополнительная позиция: Маркетолог

    private final double positionCoefficient;
    private final boolean isManager;

    Positions(double positionCoefficient, boolean isManager) {
        this.positionCoefficient = positionCoefficient;
        this.isManager = isManager;
    }

    public boolean isManager() {
        return isManager;
    }
}