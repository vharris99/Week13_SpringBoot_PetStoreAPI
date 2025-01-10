package pet.store.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Customer;
import pet.store.entity.Employee;

@Data
@NoArgsConstructor
public class PetStoreCustomer {
	
	private Long customerId;
	
	private String customerFirstName;
	private String customerLastName;
	private String customerEmail;
	
	public PetStoreCustomer(Customer customer) {
		if(customer != null) {
			this.customerId = customer.getCustomerId();
			this.customerFirstName = customer.getCustomerFirstName();
            this.customerLastName = customer.getCustomerLastName();
            this.customerEmail = customer.getCustomerEmail();
		}
	}
}
