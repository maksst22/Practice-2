package kz.rental;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор тестов, фиксирующий предметные правила из задания.
 * Тесты написаны ДО исправления дефектов — они должны падать на исходной версии.
 */
class RentalDeskTest {

    private RentalDesk desk;
    private Equipment camera;

    @BeforeEach
    void setUp() {
        desk = new RentalDesk();
        camera = new Equipment("CAM-1", "Камера", 5_000);
        desk.addEquipment(camera);
    }

    // ---------- Этап 1: расчёт суммы ----------

    @Test
    @DisplayName("Сумма возврата = ставка × фактическое число дней")
    void returnUsesActualNumberOfDays() {
        desk.rent("CAM-1", "C-17", 2);
        long sum = desk.returnEquipment("CAM-1", 3);
        assertEquals(15_000, sum);
    }

    // ---------- Этап 2: граница срока ----------

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Плановый срок должен быть положительным (0 и -1 недопустимы)")
    void rentalPeriodMustBePositive(int plannedDays) {
        assertThrows(IllegalArgumentException.class,
                () -> desk.rent("CAM-1", "C-17", plannedDays));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("После отказа по сроку состояние не меняется")
    void rejectedRentalKeepsStateIntact(int plannedDays) {
        assertThrows(IllegalArgumentException.class,
                () -> desk.rent("CAM-1", "C-17", plannedDays));

        assertAll(
                () -> assertTrue(camera.isAvailable(), "оборудование должно остаться доступным"),
                () -> assertEquals(0, desk.getActiveRentalCount(), "активных выдач быть не должно")
        );
    }

    // ---------- Этап 3: повторная выдача ----------

    @Test
    @DisplayName("Недоступное оборудование нельзя выдать повторно")
    void unavailableEquipmentCannotBeRentedAgain() {
        desk.rent("CAM-1", "C-17", 2);

        assertThrows(IllegalStateException.class,
                () -> desk.rent("CAM-1", "C-18", 2));

        assertAll(
                () -> assertEquals(1, desk.getActiveRentalCount(),
                        "вторая запись не должна появиться"),
                () -> assertFalse(camera.isAvailable(),
                        "камера должна остаться недоступной")
        );
    }

    // ---------- Дополнительная защита поведения ----------

    @Test
    @DisplayName("Неизвестный id при выдаче -> NoSuchElementException")
    void unknownEquipmentIdThrows() {
        assertThrows(NoSuchElementException.class,
                () -> desk.rent("UNKNOWN", "C-17", 2));
        assertEquals(0, desk.getActiveRentalCount());
    }

    @Test
    @DisplayName("Возврат делает оборудование доступным и удаляет выдачу")
    void returnMakesEquipmentAvailable() {
        desk.rent("CAM-1", "C-17", 2);
        desk.returnEquipment("CAM-1", 2);

        assertAll(
                () -> assertTrue(camera.isAvailable()),
                () -> assertEquals(0, desk.getActiveRentalCount())
        );
    }

    @Test
    @DisplayName("Нулевой фактический срок отклоняется без изменения состояния")
    void zeroActualDaysRejected() {
        desk.rent("CAM-1", "C-17", 2);

        assertThrows(IllegalArgumentException.class,
                () -> desk.returnEquipment("CAM-1", 0));

        assertAll(
                () -> assertEquals(1, desk.getActiveRentalCount(),
                        "выдача должна остаться активной"),
                () -> assertFalse(camera.isAvailable(),
                        "оборудование должно остаться недоступным")
        );
    }

    @Test
    @DisplayName("Дубликат id оборудования отклоняется")
    void duplicateEquipmentIdRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> desk.addEquipment(new Equipment("CAM-1", "Другая", 7_000)));
    }

    @Test
    @DisplayName("Корректная выдача сохраняет клиента и плановый срок")
    void rentalStoresCustomerAndPlannedDays() {
        desk.rent("CAM-1", "C-17", 5);
        assertAll(
                () -> assertEquals(1, desk.getActiveRentalCount()),
                () -> assertFalse(camera.isAvailable())
        );
    }

    @Test
    @DisplayName("Возврат без активной выдачи -> NoSuchElementException")
    void returnWithoutActiveRentalThrows() {
        assertThrows(NoSuchElementException.class,
                () -> desk.returnEquipment("CAM-1", 3));
    }
}