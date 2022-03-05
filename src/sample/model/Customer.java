package sample.model;

/** This is a class that models customers. */
public class Customer {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String division;
    private String country;

    /** This is the constructor for the Customer class.
     @param id a customer id
     @param name a name
     @param address an address
     @param postalCode a pastal code
     @param phoneNumber a phone number
     @param division a state/region/province
     @param country a country */
    public Customer(int id, String name, String address, String postalCode, String phoneNumber, String division, String country) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.division = division;
        this.country = country;
    }

    /** A method returns a customer id.
     @return Returns an id */
    public int getId() { return id; }

    /** A method returns a name.
     @return Returns a name */
    public String getName() { return name; }

    /** A method returns an address.
     @return Returns an address */
    public String getAddress() { return address; }

    /** A method returns a postal code.
     @return Retuns a postal code */
    public String getPostalCode() { return postalCode; }

    /** A method returns a phone number.
     @return Returns a phone number */
    public String getPhoneNumber() { return phoneNumber; }

    /** A method returns a state/region/province.
     @return Returns a state/region/province */
    public String getDivision() { return division; }

    /** A method returns a country.
     @return Returns a country */
    public String getCountry() { return country; }

    /** A method sets the customer id.
     @param id a customer id */
    public void setId(int id) { this.id = id; }

    /** A method sets the customer name.
     @param name a name */
    public void setName(String name) { this.name = name; }

    /** This method overrides the toString() method. It returns a customer name.
     @return Returns a name */
    @Override
    public String toString() { return name; }
}
