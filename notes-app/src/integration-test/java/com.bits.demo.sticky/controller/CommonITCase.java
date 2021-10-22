package com.bits.demo.sticky.controller;

import com.bits.demo.sticky.model.Notes;
import com.bits.demo.sticky.model.NotesDTO;
import com.bits.demo.sticky.model.Task;
import com.bits.demo.sticky.model.TaskDTO;
import com.bits.demo.sticky.model.TaskStatus;
import com.bits.demo.sticky.repository.NotesRepository;
import com.bits.demo.sticky.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Optional;

@TestPropertySource( properties = {
        "spring.datasource.url=jdbc:h2:mem:test",
        "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect"
})
public class CommonITCase {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private TaskRepository taskRepository;

    protected Notes createSingleNotes(){
        Notes notes = new Notes();
        int random = (int)(Math.random() * 100 + 1);
        notes.setTitle("Test Sticky Notes " + random);
        notes.setTasks(new ArrayList<>());
        return notes;
    }

    protected Task createSingleTask(){
        Task task = new Task();
        int random = (int)(Math.random() * 100 + 1);
        task.setTitle("Test Task " + random);
        task.setDescription("Description " + random);
        task.setColor("Color " + random);
        task.setStatus(TaskStatus.TODO);
        return task;
    }

    protected NotesDTO convertNotesToDTO(Notes notes) {
        return new NotesDTO().builder()
                .title(notes.getTitle())
                .build();
    }

    protected TaskDTO convertTaskToDTO(Task task) {
        return new TaskDTO().builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .color(task.getColor())
                .status(task.getStatus())
                .build();
    }

    protected Notes saveSingleRandomNotes(){
        return notesRepository.save(createSingleNotes());
    }

    protected Notes saveSingleNotesWithOneTask(){
        Notes notes = createSingleNotes();
        Task task = createSingleTask();
        notes.addTask(task);
        return notesRepository.save(notes);
    }

    protected Task saveSingleTask(){
        return taskRepository.save(createSingleTask());
    }

    protected Optional<Notes> findNotesInDbById(Long id) {
        return notesRepository.findById(id);
    }

    protected Optional<Task> findTaskInDbById(Long id) {
        return taskRepository.findById(id);
    }
}
