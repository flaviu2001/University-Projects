package domain;

public class Cat extends BaseEntity<Long>{
    String name, owner;
    Integer catYears;

    public Cat(Long id, String name, String owner, Integer catYears) {
        this.setId(id);
        this.name = name;
        this.owner = owner;
        this.catYears = catYears;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public Integer getCatYears() {
        return catYears;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
                "; owner: " + this.owner +
                "; catYears: " + this.catYears +
                "}";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Cat))
            return false;
        return this.getId().equals(((Cat) obj).getId());
    }
}
