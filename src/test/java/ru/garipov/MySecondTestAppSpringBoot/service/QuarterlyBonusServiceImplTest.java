package ru.garipov.MySecondTestAppSpringBoot.service;

import org.junit.jupiter.api.Test;
import ru.garipov.MySecondTestAppSpringBoot.model.Positions;

import static org.assertj.core.api.Assertions.assertThat;

public class QuarterlyBonusServiceImplTest {

    @Test
    void calculateQuarterlyBonusForManager() {
        //given
        Positions position = Positions.MANAGER;
        double bonus = 0.1; // Годовой бонус (10% от зарплаты)
        double salary = 50000.0; // Зарплата

        //when
        double result = new QuarterlyBonusServiceImpl().calculate(position, salary, bonus);

        //then
        double expected = 547500.0;
    }

    @Test
    void calculateQuarterlyBonusForNonManager() {
        //given
        Positions position = Positions.DEV; // Не-управленец
        double bonus = 0.15; // Годовой бонус (15% от зарплаты)
        double salary = 60000.0; // Зарплата

        //when
        double result = new QuarterlyBonusServiceImpl().calculate(position, salary, bonus);

        //then
        double expected = 0.0; // Для не-управленцев квартальная премия должна быть равна 0
        assertThat(result).isEqualTo(expected);
    }
}