package kz.rental;

public class App {
    public static void main(String[] args) {
        RentalDesk desk = new RentalDesk();
        desk.addEquipment(new Equipment("CAM-1", "Камера", 5_000));

        desk.rent("CAM-1", "C-17", 2);
        long sum = desk.returnEquipment("CAM-1", 3);

        System.out.println("К оплате: " + sum + " тг");
        System.out.println("Активных выдач: " + desk.getActiveRentalCount());
    }
}