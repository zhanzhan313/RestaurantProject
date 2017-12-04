package com.example.myrestaurant.Model;

import java.util.ArrayList;

/**
 * Created by 站站 on 2017/12/2.
 */

public class CustomerList {
    private ArrayList<Customer> customers;
    private static  CustomerList ourInstance = new CustomerList();

    public static CustomerList getInstance() {
        return ourInstance;
    }

    private CustomerList() {
        customers=new ArrayList<>();
    }
    public void addCustomer(Customer customer)
    {
        customers.add(customer);
    }
    public void deleteCustomer(Customer customer)
    {
        customers.remove(customer);
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }
    public boolean containsCustomer(String username) {
        for(Customer customer:customers)
        {
            if(customer.getUserName().equals(username))
                return true;
        }
        return false;
    }
    public Customer getCustomer(String username) {
        for(Customer customer:customers)
        {
            if(customer.getUserName().equals(username))
                return customer;
        }
        return null;
    }
    public Customer verifyCustomer(String username,String userpass) {
        for(Customer customer:customers)
        {
            if(customer.getUserName().equals(username)&&customer.getPassWord().equals(userpass))
                return customer;
        }
        return null;
    }
}
