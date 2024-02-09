package com.example.CRUDtest.services;

import com.example.CRUDtest.entities.Student;
import com.example.CRUDtest.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;


    public Student setWorkingStatus(Long id, Boolean isWorking) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            student.get().setWorking(isWorking);
            return studentRepository.save(student.get());

        } else {
            return null;
        }
    }

    public Student setNewName(Long id, String firstName) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            student.get().setFirstName(firstName);
            return studentRepository.save(student.get());
        } else {
            return null;
        }
    }
}
