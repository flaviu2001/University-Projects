package common.domain;

public class Cat extends BaseEntity<Long>{
    String name, breed;
    Integer catYears;


    public Cat(Long id, String name, String breed, Integer catYears) {
        this.setId(id);
        this.name = name;
        this.breed = breed;
        this.catYears = catYears;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public Integer getCatYears() {
        return catYears;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setCatYears(Integer catYears) {
        this.catYears = catYears;
    }

    public Integer getHumanYears(){
        return catYears*15;
    }

    @Override
    public String toString() {
        return super.toString() +
                " Cat{name: " + this.name +
                "; breed: " + this.breed +
                "; catYears: " + this.catYears +
                "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Cat && this.getId().equals(((Cat) obj).getId());
    }
}
