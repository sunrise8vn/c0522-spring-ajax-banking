package com.cg.controller.rest;


import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Withdraw;
import com.cg.model.dto.CustomerCreateDTO;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.DepositDTO;
import com.cg.model.dto.WithdrawDTO;
import com.cg.service.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {

        List<Customer> customers = customerService.findAll();


        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getById(@PathVariable long customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Customer customer = customerOptional.get();

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerCreateDTO> create(@RequestBody CustomerCreateDTO customerDTO) {

        customerDTO.setId(0L);
        Customer customer = customerDTO.toCustomer();

        Customer newCustomer = customerService.save(customer);

        customerDTO.setId(newCustomer.getId());
        customerDTO.setBalance("0");

        return new ResponseEntity<>(newCustomer.toCustomerCreateDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/deposit")
    public ResponseEntity<CustomerDTO> create(@RequestBody DepositDTO depositDTO) {

        long customerId = depositDTO.getCustomerId();

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Deposit deposit = new Deposit();
        BigDecimal transactionAmount = new BigDecimal(depositDTO.getTransactionAmount());
        deposit.setTransactionAmount(transactionAmount);
        deposit.setCustomer(customerOptional.get());

        Customer newCustomer = customerService.deposit(customerOptional.get(), deposit);

        return new ResponseEntity<>(newCustomer.toCustomerDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<CustomerDTO> create(@RequestBody WithdrawDTO withdrawDTO) {

        long customerId = withdrawDTO.getCustomerId();

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        BigDecimal transactionAmount = new BigDecimal(Long.parseLong(withdrawDTO.getTransactionAmount()));

        if (customerOptional.get().getBalance().compareTo(transactionAmount) < 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setTransactionAmount(transactionAmount);
        withdraw.setCustomer(customerOptional.get());

        Customer newCustomer = customerService.withdraw(customerOptional.get(), withdraw);

        return new ResponseEntity<>(newCustomer.toCustomerDTO(), HttpStatus.CREATED);
    }
}
