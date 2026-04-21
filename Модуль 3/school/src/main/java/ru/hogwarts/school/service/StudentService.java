package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;

import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        return studentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Not found student"));
    }

    public Student editStudent(Long id, Student student) {
        Student student1 = findStudent(id);

        student1.setName(student.getName());
        student1.setAge(student.getAge());

        return studentRepository.save(student1);
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Not found student"));
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Collection<Student> getAllStudentsOneAge(int age) {
        return studentRepository.findAll()
                .stream()
                .filter(item->item.getAge() == age)
                .collect(Collectors.toList());
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty findFacultyByStudentId(Long id) {
        if (!studentRepository.existsById(id)) {
            return null; // Или выбрасываем исключение StudentNotFoundException
        }
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .orElse(null);
    }

    public Collection<Student> findAllStudentByFacultyId(Long faculty_id){
        return studentRepository.findAll().stream()
                .filter(item-> Objects.equals(item.getFaculty().getId(), faculty_id))
                .collect(Collectors.toList());
    }
}
