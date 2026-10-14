package kz.rental;

import java.util.Objects;

/**
 * Оборудование пункта проката.
 * Правило: идентификатор уникален, ставка положительна.
 */
public class Equipment {
    private final String id;
    private final String name;
    private final long dailyRate;
    private boolean available = true;

    public Equipment(String id, String name, long dailyRate) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id не может быть пустым");
        }
        if (dailyRate <= 0) {
            throw new IllegalArgumentException("dailyRate должен быть > 0");
        }
        this.id = id;
        this.name = name;
        this.dailyRate = dailyRate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public long getDailyRate() { return dailyRate; }
    public boolean isAvailable() { return available; }

    void markRented() { this.available = false; }
    void markReturned() { this.available = true; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipment other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Equipment{" + id + ", " + name + ", " + dailyRate + ", available=" + available + '}';
    }
}