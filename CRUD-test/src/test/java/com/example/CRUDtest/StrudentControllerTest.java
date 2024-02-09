package com.example.CRUDtest;

import com.example.CRUDtest.controllers.StudentController;
import com.example.CRUDtest.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class StrudentControllerTest {

    @Autowired
    StudentController studentController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private Student getStudentFromId(long id) throws Exception {

        MvcResult result = this.mockMvc.perform(get("/student/"+id))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        try {
            String userJSON = result.getResponse().getContentAsString();
            return objectMapper.readValue(userJSON, Student.class);
        } catch (Exception e){
            return null;
        }

    }

    private Student createStudent() throws Exception {
        Student student = new Student();
        student.setFirstName("Manuel");
        student.setLastName("Basso");
        student.setWorking(false);
        return createStudent(student);
    }

    private Student createStudent(Student student) throws Exception {
        MvcResult result = createStudentRequest(student);
        Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        return studentFromResponse;
    }


    private MvcResult createStudentRequest() throws Exception {

        Student student = new Student();
        student.setFirstName("Manuel");
        student.setLastName("Basso");
        student.setWorking(false);
        return createStudentRequest(student);
    }

    private MvcResult createStudentRequest(Student student) throws Exception {
        String studentJSON = objectMapper.writeValueAsString(student);

        return this.mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJSON)).andDo(print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void studentControllerNotNull() {
        assertThat(studentController).isNotNull();
    }

    @Test
    void createStudentTest() throws Exception {
        Student studentFromResponse = createStudent();
        assertThat(studentFromResponse.getId()).isNotNull();
    }

    @Test
    void readStudentList() throws Exception {
        createStudent();

        MvcResult result = this.mockMvc.perform(get("/student/"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        List<Student> studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertThat(studentFromResponse.size()).isNotZero();
    }




    @Test
    void readStudent() throws Exception {
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();

        MvcResult result = this.mockMvc.perform(get("/student/"+student.getId()))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        Student studentFromRespone = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);

        assertThat(studentFromRespone.getId()).isEqualTo(student.getId());
    }

    @Test
    void updateStudent() throws Exception {
        Student s = createStudent();
        assertThat(s.getId()).isNotNull();

        String newFirstName = "modificato";
        s.setFirstName(newFirstName);

        String studentJSON = objectMapper.writeValueAsString(s);

        MvcResult result = this.mockMvc.perform(put("/student/" + s.getId() + "?firstName=modificato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJSON)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        Student studentFromRespone = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);

        assertThat(studentFromRespone.getFirstName()).isEqualTo(newFirstName);

    }

    @Test
    void deleteStudent() throws Exception {
        Student s = createStudent();
        assertThat(s.getId()).isNotNull();

        this.mockMvc.perform(delete("/student/" + s.getId()))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        Student studentFromResponse = getStudentFromId(s.getId());
        assertThat(studentFromResponse).isNull();
    }

    @Test
    void workingStatusStudent() throws Exception{
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();

        MvcResult result = this.mockMvc.perform(put("/student/"+student.getId()+"/isWorking?isWorking=true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        assertThat(studentFromResponse).isNotNull();
        assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
        assertThat(studentFromResponse.isWorking()).isEqualTo(true);

        Student studentFromResponseGet = getStudentFromId(student.getId());
        assertThat(studentFromResponseGet).isNotNull();
        assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());
        assertThat(studentFromResponseGet.isWorking()).isEqualTo(true);
    }





}
