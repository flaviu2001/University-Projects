package model;

public class Turtle implements IAnimal {
    int ageMonths, numberOfChildren;

    public Turtle(int _ageMonths, int _numberOfChildren) {
        ageMonths = _ageMonths;
        numberOfChildren = _numberOfChildren;
    }

    @Override
    public int getAgeMonths() {
        return ageMonths;
    }

    @Override
    public String toString() {
        return String.format("Turtle of %d months and %d children.", ageMonths, numberOfChildren);
    }
}
