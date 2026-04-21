package ru.hogwarts.school.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository repository;

    @Test
    public void getStudentTest() {
        Student student = new Student();
        student.setName("Pupkin");
        student.setAge(15);

        repository.deleteAll();
        student = repository.save(student);

        Student forObject = testRestTemplate.getForObject("http://localhost:" + port + "/students/" + student.getId(), Student.class);

        Assertions.assertNotNull(forObject);
        Assertions.assertEquals(student.getId(), forObject.getId());
        Assertions.assertEquals(student.getName(), forObject.getName());
        Assertions.assertEquals(student.getAge(), forObject.getAge());
    }

    @Test
    public void createStudentTest() {
        repository.deleteAll();

        Student student = new Student();
        student.setName("Pupkin");
        student.setAge(15);

        Student forObject = testRestTemplate.postForObject("http://localhost:" + port + "/students", student, Student.class);

        Assertions.assertFalse(repository.findAll().isEmpty());
        Assertions.assertEquals(
                forObject.getId(),
                repository.findById(forObject.getId()).get().getId()
        );
        Assertions.assertEquals(
                forObject.getName(),
                repository.findById(forObject.getId()).get().getName()
        );
        Assertions.assertEquals(
                forObject.getAge(),
                repository.findById(forObject.getId()).get().getAge()
        );
    }

    @Test
    public void editStudentTest() {
        Student student = new Student();
        student.setName("Semenov");
        student.setAge(15);

        student = repository.save(student);

        student.setName("Kukunov");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<Student> request = new HttpEntity<>(student, headers);

        ResponseEntity<Student> update = testRestTemplate.exchange(
                "http://localhost:" + port + "/students/{id}",
                HttpMethod.PUT,
                request,
                Student.class,
                student.getId()
        );

        Assertions.assertNotNull(update.getBody());
        Assertions.assertEquals(student.getName(), repository.findById(update.getBody().getId()).get().getName());
        Assertions.assertEquals(student.getAge(), repository.findById(update.getBody().getId()).get().getAge());
    }

    @Test
    public void removeStudentTest() {
        Student student = new Student();
        student.setName("Lorin");
        student.setAge(15);

        student = repository.save(student);

        var delete = testRestTemplate.exchange(
                "http://localhost:" + port + "/students/" +  student.getId(),
                HttpMethod.DELETE,
                null,
                Student.class);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, delete.getStatusCode());
        Assertions.assertTrue(repository.findById(student.getId()).isEmpty());

    }

    @Test
    public void returnNotFoundTest() {
        var notStudent = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/123",
                Void.class
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, notStudent.getStatusCode());
    }

    @Test
    public void getAllStudentOneAgeTest() {
        repository.deleteAll();

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

        List<Student> studentList = new ArrayList<>(List.of(student, student1, student2, student3));

        List<Student> listSave = repository.saveAll(studentList);

        Assertions.assertEquals(2, listSave.stream()
                .filter(item->item.getAge() == 15).count());
    }

}

