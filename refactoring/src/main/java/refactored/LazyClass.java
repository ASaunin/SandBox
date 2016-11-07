package refactored;

import java.util.StringJoiner;

public class LazyClass {

    public static class Address {

        private String house;
        private String street;
        private String city;
        private String postcode;
        private String country;

        public String getAddressSummary() {
            final StringJoiner stringJoiner = new StringJoiner(", ");
            return new StringJoiner(", ")
                    .add(house)
                    .add(street)
                    .add(city)
                    .add(getPostcodeNumber())
                    .add(country)
                    .toString();
        }

        private String getPostcodeNumber() {
            return postcode.split(" ")[1];
        }

        private String getPostcodeArea() {
            return postcode.split(" ")[0];
        }

        public Address(String house, String street, String city, String postcode, String country) {
            this.house = house;
            this.street = street;
            this.city = city;
            this.postcode = postcode;
            this.country = country;
        }

    }

    public static void main(String[] args) {
        final Address address = new Address("45", "Park Avenue", "New York", "NY 10016", "USA");
        System.out.println(address.getAddressSummary());
    }

}
