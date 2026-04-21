package ru.hogwarts.school.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.exception.EntityNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
@Import(FacultyService.class)
public class FacultyControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyRepository repository;

    @MockitoSpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("VVT");
        faculty.setColor("red");

        when(facultyService.createFaculty(faculty)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculties")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));

        verify(facultyService).createFaculty(any(Faculty.class));
    }

    @Test
    public void getFacultyMockMvcTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("VVT");
        faculty.setColor("red");

        when(repository.findById(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties/1")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));

        //verify(facultyService).findFaculty(1L);
    }

    @Test
    public void editFacultyTest() throws Exception {
        Long id = 5L;
        Faculty facultyEx = new Faculty();
        facultyEx.setId(id);
        facultyEx.setName("VVT");
        facultyEx.setColor("red");

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName("RTF");
        faculty.setColor("green");

        when(repository.findById(id)).thenReturn(Optional.of(facultyEx));
        when(repository.save(faculty)).thenAnswer(inv->inv.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculties/" + id)
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("RTF"))
                .andExpect(jsonPath("$.color").value("green"));
    }

    @Test
    public void removeFacultyTest() throws Exception {
        Long id = 5L;

        doNothing().when(repository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculties/{id}", id))
                .andExpect(status().isNotFound());

        verify(facultyService, times(1)).deleteFaculty(id);
    }

    @Test
    public void returnNotFoundTest() throws Exception {
        Long id = 1L;

        doThrow(new EntityNotFoundException("Not found faculty"))
                .when(facultyService).deleteFaculty(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculties/{id}", id))
                .andExpect(status().isNotFound());

        verify(facultyService, times(1)).deleteFaculty(id);
    }

    @Test
    public void getAllFacultyOneColorTest() throws Exception {
        String color = "red";

        Faculty facultyEx = new Faculty();
        facultyEx.setId(1L);
        facultyEx.setName("VVT");
        facultyEx.setColor("red");

        Faculty faculty = new Faculty();
        faculty.setId(2L);
        faculty.setName("RTF");
        faculty.setColor("green");

        when(facultyService.getAllFacultiesOneColor(color)).thenReturn(List.of(facultyEx));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculties/color").param("color", String.valueOf(color)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(facultyEx.getId()))
                .andExpect(jsonPath("$[0].name").value(facultyEx.getName()))
                .andExpect(jsonPath("$[0].color").value(facultyEx.getColor()));
    }

}
