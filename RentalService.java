package kz.rental;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Обязанность: выполнение выдачи и возврата, хранение активных выдач.
 * Атомарность: все проверки выполняются ДО изменения состояния.
 */
public class RentalService {

    private final EquipmentCatalog catalog;
    private final RentalFeeCalculator feeCalculator;
    private final List<Rental> activeRentals = new ArrayList<>();

    public RentalService(EquipmentCatalog catalog, RentalFeeCalculator feeCalculator) {
        this.catalog = catalog;
        this.feeCalculator = feeCalculator;
    }

    public void rent(String equipmentId, String customerId, int plannedDays) {
        // 1. Проверки — состояние не меняем
        if (plannedDays <= 0) {
            throw new IllegalArgumentException("Плановый срок должен быть > 0");
        }
        Equipment e = catalog.getRequired(equipmentId);
        if (!e.isAvailable()) {
            throw new IllegalStateException("Оборудование уже выдано: " + equipmentId);
        }

        // 2. Изменение состояния — только после успешных проверок
        activeRentals.add(new Rental(e, customerId, plannedDays));
        e.markRented();
    }

    public long returnEquipment(String equipmentId, int actualDays) {
        // 1. Находим активную выдачу
        Rental r = findActive(equipmentId);

        // 2. Считаем сумму — может бросить исключение, состояние не тронуто
        long sum = feeCalculator.calculate(r, actualDays);

        // 3. Только после успешного расчёта меняем состояние
        activeRentals.remove(r);
        r.getEquipment().markReturned();
        return sum;
    }

    public Rental findActive(String equipmentId) {
        return activeRentals.stream()
                .filter(x -> x.getEquipment().getId().equals(equipmentId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Нет активной выдачи: " + equipmentId));
    }

    public int activeCount() { return activeRentals.size(); }
}