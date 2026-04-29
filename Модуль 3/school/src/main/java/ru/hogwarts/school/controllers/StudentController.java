package ru.hogwarts.school.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.findStudent(id);
    }

    @GetMapping("/all")
    public Collection<Student> getAllStudent() {
        return studentService.getAllStudents();
    }

    @GetMapping("/all_A")
    public Collection<String> getAllStudentForA() {
        return studentService.getAllStudentsForA();
    }

    @GetMapping
    public Collection<Student> findByAgeBetween(@RequestParam int min_age, @RequestParam int max_age) {
        return studentService.findByAgeBetween(min_age, max_age);
    }

    @GetMapping("/count")
    public Integer getCountStudents() {
        return studentService.getCount();
    }

    @GetMapping("/srAge")
    public Double getSrAgeStudents() {
        return studentService.getSrAge();
    }

    @GetMapping("/srAgeTwo")
    public Double getSrAgeStudentsTwo() {
        return studentService.getSrAgeTwo();
    }

    @GetMapping("/limit")
    public List<Student> getStudentsLimit() {
        return studentService.getStudentsLimit();
    }

    @GetMapping("/age")
    public Collection<Student> getAllStudentOneAge(@RequestParam int age) {
        return studentService.getAllStudentsOneAge(age);
    }

    @GetMapping("/{id}/faculty")
    public Faculty findFacultyByStudentId(@PathVariable Long id) {
        return studentService.findFacultyByStudentId(id);
    }

    @GetMapping("/all/{faculty_id}")
    public Collection<Student> findAllStudentByFacultyId(@RequestParam Long faculty_id) {
        return studentService.findAllStudentByFacultyId(faculty_id);
    }

    @GetMapping("/sum")
    public Integer getSum() {
        return studentService.getSum();
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student){
        return studentService.createStudent(student);
    }

    @PutMapping("{id}")
    public Student editStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.editStudent(id, student);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

}
