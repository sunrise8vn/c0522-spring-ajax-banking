package com.cg.controller.rest;


import com.cg.model.Customer;
import com.cg.model.dto.CustomerCreateDTO;
import com.cg.service.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private ICustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<CustomerCreateDTO> create(@RequestBody CustomerCreateDTO customerDTO) {

        customerDTO.setId(0L);
        Customer customer = customerDTO.toCustomer();

        Customer newCustomer = customerService.save(customer);

        customerDTO.setId(newCustomer.getId());
        customerDTO.setBalance("0");

        return new ResponseEntity<>(newCustomer.toCustomerCreateDTO(), HttpStatus.CREATED);
    }
}
