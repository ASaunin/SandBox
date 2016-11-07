package defective;

public class LazyClass {

    public static class Address {

        private String house;
        private String street;
        private String city;
        private Postcode postcode;
        private String country;

        public String getAddressSummary() {
            return house + ", " +
                    street + ", " +
                    city + ", " +
                    postcode.getPostcodeNumber() + ", " +
                    country;
        }

        public Address(String house, String street, String city, Postcode postcode, String country) {
            this.house = house;
            this.street = street;
            this.city = city;
            this.postcode = postcode;
            this.country = country;
        }

    }

    private static class Postcode {

        private String postcode;

        public String getPostcode() {
            return postcode;
        }

        public String getPostcodeNumber() {
            return postcode.split(" ")[1];
        }

        public String getPostcodeArea() {
            return postcode.split(" ")[0];
        }

        public Postcode(String postcode) {
            this.postcode = postcode;
        }

    }

    public static void main(String[] args) {
        final Address address = new Address("45", "Park Avenue", "New York", new Postcode("NY 10016"), "USA");
        System.out.println(address.getAddressSummary());
    }

}
