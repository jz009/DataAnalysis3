public class Address {
    String street;
    String state;
    String city;
    String zip;

    public Address(String street, String city, String state, String zip) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String toString() {
        return street + " " + city + ", " + state + " " + zip;
    }
}
