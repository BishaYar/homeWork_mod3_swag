package ru.hogwarts.school.repositories;

import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentInterface {

    Integer getCount();

    Double getSrAge();

    List<Student> getStudents();

}
