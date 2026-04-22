package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAgeBetween(int min, int max);

    @Query(value = "select count(*) from student;", nativeQuery = true)
    Integer getCount();

    @Query(value = "select avg(age) from student;", nativeQuery = true)
    Double getSrAge();

    @Query(value = "select * from student order by id desc limit 5;", nativeQuery = true)
    List<Student> getStudents();

}

