package pet.store.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PetStoreCustomer {
	
	private int customerId;
	
	private String customerFirstName;
	private String customerLastName;
	private String customerEmail;
}
