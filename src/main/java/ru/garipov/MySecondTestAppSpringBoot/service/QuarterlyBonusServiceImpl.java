package ru.garipov.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.garipov.MySecondTestAppSpringBoot.model.Positions;

@Service
public class QuarterlyBonusServiceImpl implements QuarterlyBonusService {

    @Override
    public double calculate(Positions position, double salary, double bonus) {
        // Проверяем, является ли позиция управленческой
        if (position.isManager()) {
            // Для управленцев вычисляем квартальную премию
            // В данном примере, предполагаем, что квартальная премия составляет 10% от годового бонуса
            double annualBonus = salary * bonus * 365 * position.getPositionCoefficient() / 365; // Годовой бонус
            return annualBonus * 0.1;
        } else {
            // Если позиция не является управленческой, возвращаем 0
            return 0.0;
        }
    }
}