package kz.rental;

/**
 * Обязанность: проверка фактического срока и расчёт суммы возврата.
 * Не зависит от хранения каталога и активных выдач.
 */
public class RentalFeeCalculator {

    public long calculate(Rental rental, int actualDays) {
        if (actualDays <= 0) {
            throw new IllegalArgumentException("Фактический срок должен быть > 0");
        }
        return rental.getEquipment().getDailyRate() * actualDays;
    }
}