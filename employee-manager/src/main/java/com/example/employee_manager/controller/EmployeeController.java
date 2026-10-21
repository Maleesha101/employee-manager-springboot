
package com.example.ems.controller;

import com.example.ems.entity.Employee;
import com.example.ems.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // GET /employees - display form and employee table
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute(
                "employees", employeeService.getAllEmployees());
        return "employees";
    }

    // POST /employees - save a new employee
    @PostMapping
    public String saveEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute(
                    "employees", employeeService.getAllEmployees());
            return "employees";
        }

        employeeService.saveEmployee(employee);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Employee added successfully!");

        return "redirect:/employees";
    }

    // GET /employees/{id}/edit - show employee details in edit form
    @GetMapping("/{id}/edit")
    public String editEmployee(
            @PathVariable Long id, Model model) {

        model.addAttribute(
                "employee", employeeService.getEmployeeById(id));
        return "edit-employee";
    }

    // POST /employees/{id}/update - update employee
    @PostMapping("/{id}/update")
    public String updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            // Keep the URL ID authoritative.
            employee.setId(id);
            model.addAttribute("employee", employee);
            return "edit-employee";
        }

        employeeService.updateEmployee(id, employee);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Employee updated successfully!");

        return "redirect:/employees";
    }

    // POST /employees/{id}/delete - delete employee
    @PostMapping("/{id}/delete")
    public String deleteEmployee(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute(
                "successMessage", "Employee deleted successfully!");

        return "redirect:/employees";
    }
}
