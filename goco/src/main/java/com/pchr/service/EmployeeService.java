package com.pchr.service;

import java.util.Optional;

import com.pchr.entity.Employee;

public interface EmployeeService {
    public Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);
}
