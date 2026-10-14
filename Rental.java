package kz.rental;

/**
 * Активная выдача: оборудование + клиент + плановый срок.
 */
public class Rental {
    private final Equipment equipment;
    private final String customerId;
    private final int plannedDays;

    public Rental(Equipment equipment, String customerId, int plannedDays) {
        this.equipment = equipment;
        this.customerId = customerId;
        this.plannedDays = plannedDays;
    }

    public Equipment getEquipment() { return equipment; }
    public String getCustomerId() { return customerId; }
    public int getPlannedDays() { return plannedDays; }
}