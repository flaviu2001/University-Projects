package core.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@NoArgsConstructor
@Entity
public class Address extends BaseEntity<Long>{
    private String city;
    private String street;

    public Address(Long id, String city, String street) {
        this.setId(id);
        this.city = city;
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Address && this.getId().equals(((Address) obj).getId());
    }
}
