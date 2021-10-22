package com.bits.demo.sticky.service;

import com.bits.demo.sticky.model.Notes;
import com.bits.demo.sticky.model.NotesDTO;
import com.bits.demo.sticky.model.TaskDTO;

import java.util.List;
import java.util.Optional;

public interface NotesService {

    List<Notes> getAllNotesBoards();

    Optional<Notes> getNotesById(Long id);

    Optional<Notes> getNotesByTitle(String title);

    Notes saveNewNotes(NotesDTO notesDTO);

    Notes updateNotes(Notes oldNotes, NotesDTO newNotesDTO);

    void deleteNotes(Notes notes);

    Notes addNewTaskToNotes(Long notesId, TaskDTO taskDTO);
}
