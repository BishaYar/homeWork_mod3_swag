package ru.hogwarts.school.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("faculties")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public Faculty getFaculty(@RequestParam Long id) {
        return facultyService.findFaculty(id);
    }

    @GetMapping
    public Collection<Faculty> getAllFaculty() {
        return facultyService.getAllFaculties();
    }

    @GetMapping("/color/{color}")
    public Collection<Faculty> getAllFacultyOneColor(String color) {
        return facultyService.getAllFacultiesOneColor(color);
    }

    @PostMapping
    public Faculty createFaculty(Faculty faculty){
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public Faculty editFaculty(Faculty faculty){
        return facultyService.editFaculty(faculty);
    }

    @DeleteMapping("{id}")
    public void removeFaculty(@RequestParam Long id){
        facultyService.deleteFaculty(id);
    }

}
