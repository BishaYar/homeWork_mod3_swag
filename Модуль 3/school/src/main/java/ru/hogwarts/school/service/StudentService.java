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
import java.util.stream.Stream;

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

    public Collection<String> getAllStudentsForA() {
        logger.info("Was invoked method for find all students name A");
        return studentRepository.findAll()
                .stream()
                .map(item->item.getName().toUpperCase())
                .filter(item->item.startsWith("А"))
                .sorted()
                .collect(Collectors.toList());
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

    public Double getSrAgeTwo() {
        logger.info("Was invoked method for get avg(age) students Stream API");

        return studentRepository.findAll()
                .stream()
                .mapToInt(item-> item.getAge())
                .average().orElse(0.0);
    }

    public List<Student> getStudentsLimit() {
        logger.info("Was invoked method for get limit students");
        return studentRepository.getStudents();
    }

    public Integer getSum() {
        return Stream.iterate(1, a -> a + 1)
                .parallel()
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
    }

    public void printParallel() {

        Collection<String> printTwo = studentRepository.findAll()
                .stream()
                .map(item->item.getName())
                .limit(2)
                .collect(Collectors.toList());

        Collection<String> printForTh = studentRepository.findAll()
                .stream()
                .skip(2)
                .map(item->item.getName())
                .limit(2)
                .collect(Collectors.toList());

        Collection<String> printForFive = studentRepository.findAll()
                .stream()
                .skip(4)
                .map(item->item.getName())
                .limit(2)
                .collect(Collectors.toList());

        printNameStudent(printTwo);

        new Thread(()->
                printNameStudent(printForTh)).start();


        new Thread(()->
                printNameStudent(printForFive)).start();

    }

    public void printSynchronized() {

        Collection<String> printTwo = studentRepository.findAll()
                .stream()
                .map(item->item.getName())
                .limit(2)
                .collect(Collectors.toList());

        synchronized(printTwo) {
            printNameStudent(printTwo);
        }

        Collection<String> printForTh = studentRepository.findAll()
                .stream()
                .skip(2)
                .map(item->item.getName())
                .limit(2)
                .collect(Collectors.toList());

        synchronized(printForTh) {
            new Thread(() ->
                    printNameStudent(printForTh)).start();
        }

        Collection<String> printForFive = studentRepository.findAll()
                .stream()
                .skip(4)
                .map(item->item.getName())
                .limit(2)
                .collect(Collectors.toList());

        synchronized(printForFive) {
            new Thread(() ->
                    printNameStudent(printForFive)).start();
        }

    }

    public void printNameStudent(Collection<String> collection) {

        System.out.println(collection);
    }
}
