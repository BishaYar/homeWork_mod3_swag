package ru.hogwarts.school.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FacultyRepository repository;

    @Test
    public void getFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("VVT");
        faculty.setColor("red");

        repository.deleteAll();
        faculty = repository.save(faculty);

        Faculty forObject = testRestTemplate.getForObject("http://localhost:" + port + "/faculties/" + faculty.getId(), Faculty.class);

        Assertions.assertNotNull(forObject);
        Assertions.assertEquals(faculty.getId(), forObject.getId());
        Assertions.assertEquals(faculty.getName(), forObject.getName());
        Assertions.assertEquals(faculty.getColor(), forObject.getColor());
    }

    @Test
    public void createFacultyTest() {
        repository.deleteAll();

        Faculty faculty = new Faculty();
        faculty.setName("VVT");
        faculty.setColor("red");

        Faculty forObject = testRestTemplate.postForObject("http://localhost:" + port + "/faculties", faculty, Faculty.class);

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
                forObject.getColor(),
                repository.findById(forObject.getId()).get().getColor()
        );
    }

    @Test
    public void editFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("VVT");
        faculty.setColor("red");

        faculty = repository.save(faculty);

        faculty.setName("FVT");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<Faculty> request = new HttpEntity<>(faculty, headers);

        ResponseEntity<Faculty> update = testRestTemplate.exchange(
                "http://localhost:" + port + "/faculties/{id}",
                HttpMethod.PUT,
                request,
                Faculty.class,
                faculty.getId()
        );

        Assertions.assertNotNull(update.getBody());
        Assertions.assertEquals(faculty.getName(), repository.findById(update.getBody().getId()).get().getName());
        Assertions.assertEquals(faculty.getColor(), repository.findById(update.getBody().getId()).get().getColor());
    }

    @Test
    public void removeFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("VVT");
        faculty.setColor("red");

        faculty = repository.save(faculty);

        var delete = testRestTemplate.exchange(
                "http://localhost:" + port + "/faculties/" +  faculty.getId(),
                HttpMethod.DELETE,
                null,
                Faculty.class);

        Assertions.assertEquals(HttpStatus.OK, delete.getStatusCode());
        Assertions.assertTrue(repository.findById(faculty.getId()).isEmpty());

    }

    @Test
    public void returnNotFoundTest() {
        var notFaculty = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/123",
                Void.class
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, notFaculty.getStatusCode());
    }

    @Test
    public void getAllFacultyOneColorTest() {
        repository.deleteAll();

        Faculty faculty = new Faculty();
        faculty.setName("VVT");
        faculty.setColor("red");

        Faculty faculty1 = new Faculty();
        faculty1.setName("RTG");
        faculty1.setColor("red");

        Faculty faculty2= new Faculty();
        faculty2.setName("RTG");
        faculty2.setColor("green");

        List<Faculty> facultyList = new ArrayList<>(List.of(faculty, faculty1, faculty2));

        List<Faculty> listSave = repository.saveAll(facultyList);

        Assertions.assertEquals(2, listSave.stream()
                .filter(item->item.getColor().equals("red")).count());
    }
}
