package br.com.compasso.models;

import br.com.compasso.dao.StatesDao;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String street, district, city, cep;

    @ManyToOne
    private States state;

    public Address() {
    }

    public Address(String street, String district, String city, String cep, String state) {
        this.street = street;
        this.district = district;
        this.city = city;
        this.cep = cep;
        this.state = new StatesDao().readName(state);;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public States getState() { return state; }
    public void setState(States state) { this.state = state; }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", district='" + district + '\'' +
                ", city='" + city + '\'' +
                ", cep='" + cep + '\'' +
                ", state=" + state +
                '}';
    }
}
