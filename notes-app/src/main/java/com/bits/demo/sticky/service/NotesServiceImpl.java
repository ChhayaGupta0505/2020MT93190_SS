package com.bits.demo.sticky.service;

import com.bits.demo.sticky.model.Notes;
import com.bits.demo.sticky.model.NotesDTO;
import com.bits.demo.sticky.model.Task;
import com.bits.demo.sticky.model.TaskDTO;
import com.bits.demo.sticky.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepository;

    @Override
    @Transactional
    public List<Notes> getAllNotesBoards() {
        List<Notes> notesList = new ArrayList<>();
        notesRepository.findAll().forEach(notesList::add);
        return notesList;
    }

    @Override
    @Transactional
    public Optional<Notes> getNotesById(Long id) {
        return notesRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Notes> getNotesByTitle(String title) {
        return notesRepository.findByTitle(title);
    }

    @Override
    @Transactional
    public Notes saveNewNotes(NotesDTO notesDTO) {
        return notesRepository.save(convertDTOToNotes(notesDTO));
    }

    @Override
    @Transactional
    public Notes updateNotes(Notes oldNotes, NotesDTO newNotesDTO) {
        oldNotes.setTitle(newNotesDTO.getTitle());
        return notesRepository.save(oldNotes);
    }

    @Override
    @Transactional
    public void deleteNotes(Notes notes) {
        notesRepository.delete(notes);
    }

    @Override
    @Transactional
    public Notes addNewTaskToNotes(Long notesId, TaskDTO taskDTO) {
        Notes notes = notesRepository.findById(notesId).get();
        notes.addTask(convertDTOToTask(taskDTO));
        return notesRepository.save(notes);
    }

    private Notes convertDTOToNotes(NotesDTO notesDTO){
        Notes notes = new Notes();
        notes.setTitle(notesDTO.getTitle());
        return notes;
    }

    private Task convertDTOToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setColor(taskDTO.getColor());
        task.setStatus(taskDTO.getStatus());
        return task;
    }
}
