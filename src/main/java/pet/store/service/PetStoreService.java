package pet.store.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	@Autowired
	private PetStoreDao petStoreDao;
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private CustomerDao customerDao;
	
	//inserts or modifies pet store data
	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		
		PetStore petStore = findOrCreatePetStore(petStoreId);
		
		copyPetStoreFields(petStore, petStoreData);
		
		return new PetStoreData(petStoreDao.save(petStore));
	}

	private static void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		if (Objects.isNull(petStore) || Objects.isNull(petStoreData)) {
			throw new IllegalArgumentException("Both petStore and petStoreData must be non-null");
		}
		
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
		
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;
		
		if(Objects.isNull(petStoreId)) {	
			petStore = new PetStore();
		
		} else {
			petStore = findPetStoreById(petStoreId);
		}
		
		return petStore;
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
			.orElseThrow(() -> new NoSuchElementException(
				"Pet Store with ID="+ petStoreId + " not found."));
	}
	
	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		
		PetStore petStore = findPetStoreById(petStoreId);
		Employee employee = findOrCreateEmployee(petStoreId, petStoreEmployee.getEmployeeId());
		
		copyEmployeeFields(employee, petStoreEmployee);
		
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);
		
		
		return new PetStoreEmployee(employeeDao.save(employee));
	}
	
	//Copies the data in the pet store employee parameter to the Employee object.
	private static void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		if (Objects.isNull(employee) || Objects.isNull(petStoreEmployee)) {
			throw new IllegalArgumentException("Both employee and petStoreEmployee must be non-null");
		}
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
	}
	
	//Retrieves an existing employee or creates a new one
	private Employee findOrCreateEmployee(Long petStoreId,Long employeeId) {
		
		if(Objects.isNull(employeeId)) {	
			
			return new Employee();
		} else {
			return findEmployeeById(petStoreId, employeeId);
		}
	}
		
	//Fetch's the Employee object by it's ID
	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
	  Employee employee = employeeDao.findById(employeeId)
	  	.orElseThrow(() -> new NoSuchElementException(
	  			"Employee with ID=" + employeeId + " not found."));
		
		if (!employee.getPetStore().getPetStoreId().equals(petStoreId)) {
			throw new IllegalArgumentException("Employee with ID=" + employeeId + " does not work at PetStore with ID=" + petStoreId);
		}
		return employee;
	}
	
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		
		PetStore petStore = findPetStoreById(petStoreId);
		Customer customer = findOrCreateCustomer(petStoreId, petStoreCustomer.getCustomerId());
		
		copyCustomerFields(customer, petStoreCustomer);
		
		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);
		
		
		return new PetStoreCustomer(customerDao.save(customer));
	}
	
	//Copies the data in the pet store customer parameter to the Customer object.
	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		if (Objects.isNull(customer) || Objects.isNull(petStoreCustomer)) {
			throw new IllegalArgumentException("Both customer and petStoreCustomer must be non-null");
		}
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
	}
	
	//Retrieves an existing customer or creates a new one
	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if(Objects.isNull(customerId)) {	
			
			return new Customer();
		} else {
			return findCustomerById(petStoreId, customerId);
		}
	}
	
	//Fetch's the Customer object by it's ID
	private Customer findCustomerById(Long petStoreId, Long customerId) {
	  Customer customer = customerDao.findById(customerId)
	    .orElseThrow(() -> new NoSuchElementException(
	    		"Customer with ID=" + customerId + " not found."));
	  
	  for(PetStore petStore : customer.getPetStores()) {
		  if(petStore.getPetStoreId().equals(petStoreId)) {
			  
			  return customer;
		  }
	  }
	  
	  throw new IllegalArgumentException("Customer with ID=" + customerId + " does not work at PetStore with ID=" + petStoreId);
	}
	
	// Returns summary data for all pet stores
	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		
		//Fetches ALL pet store entities
		List<PetStore> petStores = petStoreDao.findAll();
		
		//Converts List of PetStore objects to List of PetStoreData objects
		List<PetStoreData> result = new ArrayList<>();
        for (PetStore petStore : petStores) {
            PetStoreData petStoreData = new PetStoreData(petStore);

            // Clear the list of customers and employees
            petStoreData.getCustomers().clear();
            petStoreData.getEmployees().clear();

            // Add to result list
            result.add(petStoreData);
        }
        return result;
	}
	
	@Transactional(readOnly = true)
	public PetStoreData retrieveSinglePetStore(Long petStoreId) {
		PetStore petStore = petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException("Pet Store with ID="+ petStoreId + " not found."));
		
		return new PetStoreData(petStore);
	}

	public void deletePetStoreById(Long petStoreId) {
	  PetStore petStore = findPetStoreById(petStoreId);	  
		petStoreDao.delete(petStore);
	}
}
