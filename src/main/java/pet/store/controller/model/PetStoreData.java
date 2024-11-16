package pet.store.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Data
@NoArgsConstructor
public class PetStoreData {
	private Long petStoreId;
	
	private String petStoreName;
	private String petStoreAddress;
	private String petStoreCity;
	private String petStoreState;
	private String petStoreZip;
	private String petStorePhone;
	private Set<PetStoreCustomer> customers;
	private Set<PetStoreEmployee> employees;
	

	public PetStoreData(PetStore save) {
		petStoreId = save.getPetStoreId();
		petStoreName = save.getPetStoreName();
		petStoreAddress = save.getPetStoreAddress();
		petStoreCity = save.getPetStoreCity();
		petStoreState = save.getPetStoreState();
		petStoreZip = save.getPetStoreZip();
		petStorePhone = save.getPetStorePhone();
	
	
		customers = new HashSet<>();
		for(Customer customer : save.getCustomers()) {
			customers.add(new PetStoreCustomer(customer));
		}
		employees = new HashSet<>();
		for(Employee employee : save.getEmployees()) {
			employees.add(new PetStoreEmployee(employee));
	    }
	}

	public class PetStoreCustomer {
		private int customerId;
		private String customerFirstName;
		private String customerLastName;
		private String customerEmail;
	
		public PetStoreCustomer (Customer customer) {
			customerId = customer.getCustomerId();
			customerFirstName = customer.getCustomerFirstName();
			customerLastName = customer.getCustomerLastName();
			customerEmail = customer.getCustomerEmail();
		}
	}
	
	public class PetStoreEmployee {
		private int employeeId;
		private String employeeFirstName;
		private String employeeLastName;
		private String employeePhone;
		private String employeeJobTitle;
		
		public PetStoreEmployee (Employee employee) {
			employeeId = employee.getEmployeeId();
			employeeFirstName = employee.getEmployeeFirstName();
			employeeLastName = employee.getEmployeeLastName();
			employeePhone = employee.getEmployeePhone();
			employeeJobTitle = employee.getEmployeeJobTitle();
		}
	}
}