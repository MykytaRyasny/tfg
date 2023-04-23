package com.tfg.proyect.service;

import com.tfg.proyect.dto.EmployeeDTO;
import com.tfg.proyect.mapper.EmployeeMapper;
import com.tfg.proyect.model.EmployeeEntity;
import com.tfg.proyect.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {

  private EmployeeRepository employeeRepository;

  private EmployeeMapper employeeMapper;

  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return employeeRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
  }

  @Override
  public Optional<EmployeeEntity> findByUsername(String username) {
    return this.employeeRepository.findByUsername(username);
  }

  @Override
  public EmployeeDTO getEmployeeById(String dni) {
    Optional<EmployeeEntity> employee = employeeRepository.findById(dni);
    return employeeMapper.employeeToEmployeeDTO(employee.orElse(null));
  }

  @Override
  public EmployeeDTO save(EmployeeDTO employeeDTO) {
    EmployeeEntity employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
    employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
    employee.setRole(employee.getRole());
    employeeRepository.save(employee);
    return employeeDTO;
  }

  @Override
  public List<EmployeeDTO> getAllEmployees() {
    List<EmployeeEntity> employees = employeeRepository.findAll();
    return employeeMapper.employeeToEmployeeDTOList(employees);
  }

  @Override
  public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO, String password) {
    EmployeeEntity employeeToEdit = employeeRepository.getReferenceById(employeeDTO.getDni());
    employeeToEdit.setFirstName(employeeDTO.getFirstName());
    employeeToEdit.setLastName(employeeDTO.getLastName());
    employeeToEdit.setRole(employeeToEdit.getRole());
    if (password != null) {
      employeeToEdit.setPassword(password);
    }
    employeeToEdit.setUsername(employeeDTO.getUsername());
    employeeRepository.save(employeeToEdit);
    return employeeDTO;
  }

  @Override
  public void deleteEmployee(String dni) {
    employeeRepository.deleteById(dni);
  }


}
