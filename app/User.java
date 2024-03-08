public class User {
    private int id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;

    // Add other fields as needed

    // Constructors (empty and parameterized)

    // Getters and setters for all fields

    // Inner classes for Address and Company
    public static class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;

        // Constructors, getters, and setters
    }

    public static class Geo {
        private String lat;
        private String lng;

        // Constructors, getters, and setters
    }

    public static class Company {
        private String name;
        private String catchPhrase;
        private String bs;

        // Constructors, getters, and setters
    }
}
