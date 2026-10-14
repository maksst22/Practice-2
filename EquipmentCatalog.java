package kz.rental;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Обязанность: регистрация оборудования и поиск по идентификатору.
 * Не управляет активными выдачами.
 */
public class EquipmentCatalog {

    private final Map<String, Equipment> equipmentById = new HashMap<>();

    public void add(Equipment e) {
        if (equipmentById.containsKey(e.getId())) {
            throw new IllegalArgumentException("Дубликат id: " + e.getId());
        }
        equipmentById.put(e.getId(), e);
    }

    public Equipment getRequired(String id) {
        Equipment e = equipmentById.get(id);
        if (e == null) {
            throw new NoSuchElementException("Неизвестный id: " + id);
        }
        return e;
    }
}