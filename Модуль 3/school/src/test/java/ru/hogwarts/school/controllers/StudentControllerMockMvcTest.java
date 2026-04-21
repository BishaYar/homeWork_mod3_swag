package ru.hogwarts.school.controllers;

import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.StudentService;
import ru.hogwarts.school.exception.EntityNotFoundException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(StudentService.class)
public class StudentControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentRepository repository;

    @MockitoSpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createStudentTest() throws Exception {
        Student student = new Student();
        student.setId(5L);
        student.setName("Testirov");
        student.setAge(15);

        when(studentService.createStudent(student)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));

        verify(studentService).createStudent(any(Student.class));
    }

    @Test
    public void getStudentMockMvcTest() throws Exception {
        Student student = new Student();
        student.setId(5L);
        student.setName("Testirov");
        student.setAge(15);

        when(repository.findById(5L)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/5")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @Test
    public void editStudentTest() throws Exception {
        Long id = 5L;
        Student studentEx = new Student();
        studentEx.setId(id);
        studentEx.setName("Testirov");
        studentEx.setAge(15);

        Student student1 = new Student();
        student1.setId(id);
        student1.setName("Klonov");
        student1.setAge(16);

        when(repository.findById(5L)).thenReturn(Optional.of(studentEx));
        when(repository.save(student1)).thenAnswer(inv->inv.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students/" + id)
                        .content(objectMapper.writeValueAsString(student1))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Klonov"))
                .andExpect(jsonPath("$.age").value(16));
    }

    @Test
    public void removeStudentTest() throws Exception {
        Long id = 5L;

        doNothing().when(repository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/{id}", id))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).deleteStudent(id);
    }

    @Test
    public void returnNotFoundTest() throws Exception {
        Long id = 1L;

        doThrow(new EntityNotFoundException("Not found student"))
                .when(studentService).deleteStudent(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/{id}", id))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).deleteStudent(id);
    }

    @Test
    public void getAllStudentsOneAgeTest() throws Exception {
        int age = 17;

        Student student = new Student();
        student.setName("Lorin");
        student.setAge(15);

        Student student1 = new Student();
        student1.setName("Gorin");
        student1.setAge(17);

        Student student2 = new Student();
        student2.setName("Sorin");
        student2.setAge(16);

        Student student3 = new Student();
        student3.setName("Torin");
        student3.setAge(15);

        when(studentService.getAllStudentsOneAge(age)).thenReturn(List.of(student1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/age").param("age", String.valueOf(age)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(student1.getId()))
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[0].age").value(student1.getAge()));

    }
}
