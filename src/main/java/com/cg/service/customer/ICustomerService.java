package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.IGeneralService;

import java.util.List;

public interface ICustomerService extends IGeneralService<Customer> {

    List<Customer> findAllByIdNot(long id);

    Boolean existsByIdEquals(long id);

    Customer deposit(Customer customer, Deposit deposit);

    Customer withdraw(Customer customer, Withdraw withdraw);

    Customer transfer(Transfer transfer);
}
