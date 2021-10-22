package com.bits.demo.sticky.service;

import com.bits.demo.sticky.config.H2DatabaseConfig4Test;
import com.bits.demo.sticky.model.Notes;
import com.bits.demo.sticky.model.NotesDTO;
import com.bits.demo.sticky.repository.NotesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { H2DatabaseConfig4Test.class })
public class NotesServiceITCase {

    @Autowired
    private NotesRepository notesRepository;
    private NotesService notesService;


    @Before
    public void init() {
        notesService = new NotesServiceImpl(notesRepository);
    }


    @Test
    public void whenNewNotesCreated_thenNotesIsSavedInDb() {
        //given
        NotesDTO notesDTO = NotesDTO.builder()
                                    .title("Test Notes")
                                .build();

        //when
        notesService.saveNewNotes(notesDTO);

        //then
        List<Notes> notes = (List<Notes>) notesRepository.findAll();

        assertNotNull(notes.get(0));
        assertEquals("Test Notes", notes.get(0).getTitle());
    }
}
