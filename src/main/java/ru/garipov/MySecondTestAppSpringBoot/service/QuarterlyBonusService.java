package ru.garipov.MySecondTestAppSpringBoot.service;

import ru.garipov.MySecondTestAppSpringBoot.model.Positions;

public interface QuarterlyBonusService {
    /**
     * Рассчитываем квартальную премию для управленцев.
     *
     * @param position Позиция сотрудника.
     * @param salary   Зарплата сотрудника.
     * @param bonus    Бонус сотрудника.
     * @return Квартальная премия.
     */
    double calculate(Positions position, double salary, double bonus);
}