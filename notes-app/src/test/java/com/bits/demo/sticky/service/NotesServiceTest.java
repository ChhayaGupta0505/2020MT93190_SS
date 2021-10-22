package com.bits.demo.sticky.service;

import com.bits.demo.sticky.model.Notes;
import com.bits.demo.sticky.repository.NotesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotesServiceTest {

    NotesService notesService;
    @Mock
    NotesRepository notesRepository;

    @Before
    public void init() {
        notesService = new NotesServiceImpl(notesRepository);
    }

    @Test
    public void when2NotessInDatabase_thenGetListWithAllOfThem() {
        //given
        mockNotesInDatabase(2);

        //when
        List<Notes> notes = notesService.getAllNotesBoards();

        //then
        assertEquals(2, notes.size());
    }

    private void mockNotesInDatabase(int notesCount) {
        when(notesRepository.findAll())
                .thenReturn(createNotesList(notesCount));
    }

    private List<Notes> createNotesList(int notesCount) {
        List<Notes> notes = new ArrayList<>();
        IntStream.range(0, notesCount)
                .forEach(number ->{
                    Notes note = new Notes();
                    note.setId(Long.valueOf(number));
                    note.setTitle("Notes " + number);
                    note.setTasks(new ArrayList<>());
                    notes.add(note);
                });
        return notes;
    }
}
