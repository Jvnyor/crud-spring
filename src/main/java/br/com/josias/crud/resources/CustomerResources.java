package br.com.josias.crud.resources;

import java.util.List;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.josias.crud.model.Customer;
import br.com.josias.crud.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Crud of Customers")
@RequestMapping("/customers")
public class CustomerResources {

	@Autowired
	private CustomerService customerService;
	
	@GetMapping
	@ApiResponse(responseCode = "200", description = "List paged of customers saved in DB")
	@ApiResponse(responseCode = "400", description = "Bad Request")
	@ApiResponse(responseCode = "500", description = "Internal error")
	@Operation(summary="List of all Customers paginated",description="List of all customers paginated")
	public ResponseEntity<Page<Customer>> listAllCustomersPageable(@ParameterObject Pageable pageable) {
		return ResponseEntity.ok(customerService.listAllPageable(pageable));
	}
	
	@GetMapping("/list")
	@ApiResponse(responseCode = "200", description = "List of customers saved in DB")
	@ApiResponse(responseCode = "400", description = "Bad Request")
	@ApiResponse(responseCode = "500", description = "Internal error")
	@Operation(summary="List of all Customers",description="List of all customers")
	public ResponseEntity<List<Customer>> listAllCustomersNonPageable() {
		return ResponseEntity.ok(customerService.listAllNonPageable());
	}
	
	@GetMapping("/find/{id}")
	@Operation(summary="Find customer by ID (CPF)",description="Find customer by ID (CPF)")
	@ApiResponse(responseCode = "200", description = "Customer by ID finded in DB")
	@ApiResponse(responseCode = "400", description = "Bad Request")
	@ApiResponse(responseCode = "500", description = "Internal error")
	public ResponseEntity<Customer> findCustomerByCpf(@PathVariable String id) {
		return ResponseEntity.ok(customerService.findById(id));
	}
	
	@GetMapping("/find")
	@Operation(summary="Find customers by name",description="Find customer by name (first name or last name)")
	@ApiResponse(responseCode = "200", description = "Customer by name finded in DB")
	@ApiResponse(responseCode = "400", description = "Bad Request")
	@ApiResponse(responseCode = "500", description = "Internal error")
	public ResponseEntity<List<Customer>> findCustomerNameWithLike(@RequestParam String name) {
		List<Customer> customerNameWithLike = customerService.findNameWithLike(name);
		return ResponseEntity.ok(customerNameWithLike);
	}
	
	@PostMapping("/create")
	@Transactional
	@Operation(summary="Create customers",description="create customers (have cpf verification)")
	@ApiResponse(responseCode = "201", description = "Customer registered in DB")
	@ApiResponse(responseCode = "400", description = "Bad Request")
	@ApiResponse(responseCode = "500", description = "Internal error")
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
		return new ResponseEntity<>(customerService.save(customer), HttpStatus.CREATED);
	}
	
	@PutMapping("/replace")
	@ApiResponse(responseCode = "200", description = "Customer replaced by CPF in DB")
	@ApiResponse(responseCode = "400", description = "Bad Request")
	@ApiResponse(responseCode = "500", description = "Internal error")
	@Operation(summary="Replace customers",description="replace customers (have cpf verification)")
	public ResponseEntity<Customer> replaceCustomer(@RequestBody Customer customer) {
		return ResponseEntity.ok(customerService.replace(customer));
	}
	
	@DeleteMapping("/delete/{id}")
	@ApiResponse(responseCode = "200", description = "Customer deleted by ID in DB")
	@ApiResponse(responseCode = "400", description = "Bad Request")
	@ApiResponse(responseCode = "500", description = "Internal error")
	@Operation(summary="Delete customers by ID",description="delete customers by ID")
	public ResponseEntity<Void> removeCustomer(@PathVariable String id) {
		customerService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
