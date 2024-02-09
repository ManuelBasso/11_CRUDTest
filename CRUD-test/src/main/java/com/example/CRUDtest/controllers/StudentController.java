package com.example.CRUDtest.controllers;

import com.example.CRUDtest.entities.Student;
import com.example.CRUDtest.repositories.StudentRepository;
import com.example.CRUDtest.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentService studentService;


    @PostMapping("")
    public Student create(@RequestBody Student s) {
        return studentRepository.save(s);
    }

    @GetMapping("/")
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Student> getSingle(@PathVariable long id) {
        if (studentRepository.existsById(id)) {
            return studentRepository.findById(id);
        } else
            return Optional.empty();

    }

    @PutMapping("/{id}")
    public Student updateFirstName(@PathVariable Long id, @RequestParam String firstName) {
        return studentService.setNewName(id, firstName);

    }

    @PutMapping("/{id}/isWorking")
    public Student updateWorkingStatus(@PathVariable Long id, @RequestParam Boolean isWorking){
        return studentService.setWorkingStatus(id, isWorking);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        studentRepository.deleteById(id);
    }



}
