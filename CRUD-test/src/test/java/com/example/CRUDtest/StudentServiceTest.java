package com.example.CRUDtest;

import com.example.CRUDtest.entities.Student;
import com.example.CRUDtest.repositories.StudentRepository;
import com.example.CRUDtest.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles(value = "test")

public class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void checkStudentWorkingStatus(){
        Student student = new Student();
        student.setFirstName("Manuel");
        student.setLastName("Basso");
        student.setWorking(false);


        Student studentFromDb= studentRepository.save(student);
        assertThat(studentFromDb).isNotNull();
        assertThat(studentFromDb.getId()).isNotNull();

        Student studentFromService = studentService.setWorkingStatus(student.getId(), true);
        assertThat(studentFromService).isNotNull();
        assertThat(studentFromService.getId()).isNotNull();
        assertThat(studentFromService.isWorking()).isTrue();

        Student studentFromFind = studentRepository.findById(student.getId()).get();
        assertThat(studentFromFind).isNotNull();
        assertThat(studentFromFind.getId()).isNotNull();
        assertThat(studentFromFind.getId()).isEqualTo(studentFromDb.getId());
        assertThat(studentFromFind.isWorking()).isTrue();
    }
}
