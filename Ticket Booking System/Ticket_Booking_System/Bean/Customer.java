package Ticket_Booking_System.Bean;

import java.util.Objects;

public class Customer {

	private String customerName;
    private String email;
    private String phoneNumber;

    public Customer(String customerName, String email, String phoneNumber) {
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    

	public String getCustomerName() { return customerName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }

    @Override
    public String toString() {
        return customerName + " | " + email + " | " + phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return email.equals(customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }



	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
