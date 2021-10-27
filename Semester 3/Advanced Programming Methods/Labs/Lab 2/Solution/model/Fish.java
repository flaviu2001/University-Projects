package model;

public class Fish implements IAnimal{
    int ageMonths, weight;

    public Fish(int _ageMonths, int _weight) {
        ageMonths = _ageMonths;
        weight = _weight;
    }

    @Override
    public int getAgeMonths() {
        return ageMonths;
    }

    @Override
    public String toString() {
        return String.format("Fish of %d months and weight %d.", ageMonths, weight);
    }
}
