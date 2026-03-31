package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
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
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student){
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
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
