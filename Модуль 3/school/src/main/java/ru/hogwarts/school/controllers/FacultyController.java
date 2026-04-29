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
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.findFaculty(id);
    }

    @GetMapping("/all")
    public Collection<Faculty> getAllFaculty() {
        return facultyService.getAllFaculties();
    }

    @GetMapping("/maxLengthName")
    public String getMaxNameFaculty() {
        return facultyService.getMaxNameFaculty();
    }

    @GetMapping("color")
    public Collection<Faculty> getAllFacultyOneColor(@RequestParam String color) {
        return facultyService.getAllFacultiesOneColor(color);
    }

    @GetMapping
    public Faculty findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(@RequestParam(required = false) String name, @RequestParam(required = false) String color) {
        return facultyService.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, color);
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty){
        return facultyService.createFaculty(faculty);
    }

    @PutMapping("{id}")
    public Faculty editFaculty(@PathVariable Long id, @RequestBody Faculty faculty){
        return facultyService.editFaculty(id, faculty);
    }

    @DeleteMapping("{id}")
    public void removeFaculty(@PathVariable Long id){
        facultyService.deleteFaculty(id);
    }

}
