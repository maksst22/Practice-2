package kz.rental;

/**
 * Фасад: сохраняет прежние внешние сигнатуры и делегирует работу
 * специализированным объектам.
 */
public class RentalDesk {

    private final EquipmentCatalog catalog = new EquipmentCatalog();
    private final RentalFeeCalculator feeCalculator = new RentalFeeCalculator();
    private final RentalService service = new RentalService(catalog, feeCalculator);

    public void addEquipment(Equipment e) {
        catalog.add(e);
    }

    public Equipment findEquipment(String id) {
        return catalog.getRequired(id);
    }

    public void rent(String equipmentId, String customerId, int plannedDays) {
        service.rent(equipmentId, customerId, plannedDays);
    }

    public long returnEquipment(String equipmentId, int actualDays) {
        return service.returnEquipment(equipmentId, actualDays);
    }

    public int getActiveRentalCount() {
        return service.activeCount();
    }

    public Rental findActiveRental(String equipmentId) {
        return service.findActive(equipmentId);
    }
}