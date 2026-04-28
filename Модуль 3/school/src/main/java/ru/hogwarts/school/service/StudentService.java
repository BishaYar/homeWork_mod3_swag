package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student findStudent(Long id) {
        logger.info("Was invoked method for find student on id");
        return studentRepository.findById(id).orElseThrow(()->{
            logger.error("There is not student with id = {}", id);
            return new EntityNotFoundException("Not found student");
        });
    }

    public Student editStudent(Long id, Student student) {
        logger.info("Was invoked method for edit student on id");
        Student student1 = findStudent(id);

        student1.setName(student.getName());
        student1.setAge(student.getAge());

        return studentRepository.save(student1);
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student on id");
        studentRepository.findById(id)
                .orElseThrow(()->{
                    logger.error("There is not faculty with id = {}", id);
                    return new EntityNotFoundException("Not found student");
                });
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for find all students");
        return studentRepository.findAll();
    }

    public Collection<Student> getAllStudentsOneAge(int age) {
        logger.info("Was invoked method for find all students one age");
        return studentRepository.findAll()
                .stream()
                .filter(item->item.getAge() == age)
                .collect(Collectors.toList());
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find all student between min and max age");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty findFacultyByStudentId(Long id) {
        logger.info("Was invoked method for find faculty by student on id");
        if (!studentRepository.existsById(id)) {
            return null;
        }
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .orElse(null);
    }

    public Collection<Student> findAllStudentByFacultyId(Long faculty_id){
        logger.info("Was invoked method for find all students on faculty_id");
        return studentRepository.findAll().stream()
                .filter(item-> Objects.equals(item.getFaculty().getId(), faculty_id))
                .collect(Collectors.toList());
    }

    public Integer getCount(){
        logger.info("Was invoked method for get count student");
        return studentRepository.getCount();
    }

    public Double getSrAge() {
        logger.info("Was invoked method for get avg(age) students");
        Double avgAge = studentRepository.getSrAge();
        return avgAge != null ? avgAge : 0.0;
    }

    public List<Student> getStudentsLimit() {
        logger.info("Was invoked method for get limit students");
        return studentRepository.getStudents();
    }
}
