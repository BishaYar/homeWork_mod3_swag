package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.info("Was invoked method for find faculty on id");
        logger.debug("find on id = {}", id);
        return facultyRepository.findById(id).orElseThrow(()->{
            logger.error("There (find) is not faculty with id = {}", id);
            return new EntityNotFoundException("Not found faculty");
        });
    }

    public Faculty editFaculty(long id, Faculty faculty) {
        logger.info("Was invoked method for edit faculty on id");
        Faculty faculty1 = findFaculty(id);

        logger.debug("faculty id = {}", id);

        faculty1.setName(faculty.getName());
        faculty1.setColor(faculty.getColor());

        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty on id");
        facultyRepository.findById(id)
                .orElseThrow(()->{
                    logger.error("There (delete) is not faculty with id = {}", id);
                    return new EntityNotFoundException("Not found faculty");
                });
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for find all faculties");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getAllFacultiesOneColor(String color) {
        logger.info("Was invoked method for get all faculties one color");
        return facultyRepository.findAll().stream()
                .filter(item->item.getColor().equals(color))
                .collect(Collectors.toList());
    }

    public Faculty findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String name, String color){
        logger.info("Was invoked method for find faculty by name or color");
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(name, color);
    }

}
