package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Person {
    private String firstName;
    private String lastName;
    private int age;
    private String streetAddress;
    private String city;
    private String country;

    // Bad implementation
    public Person(String firstName, String lastName, int age, String streetAddress, String city, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.streetAddress = streetAddress;
        this.city = city;
        this.country = country;
    }

    // Good implementation via builder pattern
    public static class Builder {

        private String firstName;
        private String lastName;
        private int age;
        private String streetAddress;
        private String city;
        private String country;

        public Builder setName(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setAddress(String streetAddress, String city, String country) {
            this.streetAddress = streetAddress;
            this.city = city;
            this.country = country;
            return this;
        }

        public Person createPerson() {
            return new Person(firstName, lastName, age, streetAddress, city, country);
        }

    }

}