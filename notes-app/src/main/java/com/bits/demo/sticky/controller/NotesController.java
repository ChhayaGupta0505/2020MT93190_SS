package com.bits.demo.sticky.controller;

import com.bits.demo.sticky.model.Notes;
import com.bits.demo.sticky.model.NotesDTO;
import com.bits.demo.sticky.model.Task;
import com.bits.demo.sticky.model.TaskDTO;
import com.bits.demo.sticky.service.NotesService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4201")
public class NotesController {

    private final NotesService notesService;

    @GetMapping("/")
    @ApiOperation(value="View a list of all Notes=", response = Notes.class, responseContainer = "List")
    public ResponseEntity<?> getAllNotes(){
        try {
            return new ResponseEntity<>(
                    notesService.getAllNotesBoards(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value="Find a notes info by its id", response = Notes.class)
    public ResponseEntity<?> getNotes(@PathVariable Long id){
        try {
            Optional<Notes> optNotes = notesService.getNotesById(id);
            if (optNotes.isPresent()) {
                return new ResponseEntity<>(
                        optNotes.get(),
                        HttpStatus.OK);
            } else {
                return noNotesFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("")
    @ApiOperation(value="Find a Notes info by its title", response = Notes.class)
    public ResponseEntity<?> getNotesByTitle(@RequestParam String title){
        try {
            Optional<Notes> optNotes = notesService.getNotesByTitle(title);
            if (optNotes.isPresent()) {
                return new ResponseEntity<>(
                        optNotes.get(),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No Notes found with a title: " + title, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/")
    @ApiOperation(value="Save new Notes board", response = Notes.class)
    public ResponseEntity<?> createNotes(@RequestBody NotesDTO notesDTO){
        try {
            return new ResponseEntity<>(
                    notesService.saveNewNotes(notesDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value="Update a Notes with specific id", response = Notes.class)
    public ResponseEntity<?> updateNotes(@PathVariable Long id, @RequestBody NotesDTO notesDTO){
        try {
            Optional<Notes> optNotes = notesService.getNotesById(id);
            if (optNotes.isPresent()) {
                return new ResponseEntity<>(
                        notesService.updateNotes(optNotes.get(), notesDTO),
                        HttpStatus.OK);
            } else {
                return noNotesFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="Delete Notes with specific id", response = String.class)
    public ResponseEntity<?> deleteNotes(@PathVariable Long id){
        try {
            Optional<Notes> optNotes = notesService.getNotesById(id);
            if (optNotes.isPresent()) {
                notesService.deleteNotes(optNotes.get());
                return new ResponseEntity<>(
                        String.format("Notes with id: %d was deleted", id),
                        HttpStatus.OK);
            } else {
                return noNotesFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/{notesId}/tasks/")
    @ApiOperation(value="View a list of all tasks for a Notes with provided id", response = Task.class, responseContainer = "List")
    public ResponseEntity<?> getAllTasksInNotes(@PathVariable Long notesId){
         try {
            Optional<Notes> optNotes = notesService.getNotesById(notesId);
            if (optNotes.isPresent()) {
                return new ResponseEntity<>(
                        optNotes.get().getTasks(),
                        HttpStatus.OK);
            } else {
                return noNotesFoundResponse(notesId);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/{notesId}/tasks/")
    @ApiOperation(value="Save new Task and assign it to Notes", response = Notes.class)
    public ResponseEntity<?> createTaskAssignedToNotes(@PathVariable Long notesId, @RequestBody TaskDTO taskDTO){
        try {
            return new ResponseEntity<>(
                    notesService.addNewTaskToNotes(notesId, taskDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    private ResponseEntity<String> errorResponse(){
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> noNotesFoundResponse(Long id){
        return new ResponseEntity<>("No Notes found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
