package com.bits.demo.sticky.controller;

import com.bits.demo.sticky.model.Notes;
import com.bits.demo.sticky.model.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotesControllerITCase extends CommonITCase {

    private String baseURL;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp(){
        baseURL = "http://localhost:" + port;
    }

    @Test
    public void whenGetAllnotes_thenReceiveSingleNotes(){

        //given
        saveSingleRandomNotes();

        //when
        ResponseEntity<List<Notes>> response = this.restTemplate.exchange(
                                                baseURL + "notes/",
                                                    HttpMethod.GET,
                                                    new HttpEntity<>(new HttpHeaders()),
                                                    new ParameterizedTypeReference<List<Notes>>() {});

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() >= 1);
    }

    @Test
    public void whenGetSingleNotesById_thenReceiveSingleNotes(){

        //given
        Notes notes = saveSingleRandomNotes();

        //when
        ResponseEntity<Notes> response = this.restTemplate.exchange(
                baseURL + "notes/" + notes.getId(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Notes.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notes.getId(), response.getBody().getId());
        assertEquals(notes.getTitle(), response.getBody().getTitle());
    }

    @Test
    public void whenGetAllTasksForNotesById_thenReceiveTasksList(){

        //given
        Notes notes = saveSingleNotesWithOneTask();

        //when
        ResponseEntity<List<Task>> response = this.restTemplate.exchange(
                baseURL + "notes/" + notes.getId() + "/tasks/",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<List<Task>>() {});

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notes.getTasks().get(0).getId(), response.getBody().get(0).getId());
        assertEquals(notes.getTasks().get(0).getTitle(), response.getBody().get(0).getTitle());
        assertEquals(notes.getTasks().get(0).getDescription(), response.getBody().get(0).getDescription());
        assertEquals(notes.getTasks().get(0).getColor(), response.getBody().get(0).getColor());
    }

    @Test
    public void whenGetSingleNotesByTitle_thenReceiveSingleNotes(){

        //given
        Notes notes = saveSingleRandomNotes();

        //when
        ResponseEntity<Notes> response = this.restTemplate.exchange(
                baseURL + "notes?title=" + notes.getTitle(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Notes.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notes.getId(), response.getBody().getId());
        assertEquals(notes.getTitle(), response.getBody().getTitle());
    }

    @Test
    public void whenPostSingleNotes_thenItIsStoredInDb(){

        //given
        Notes notes = createSingleNotes();

        //when
        ResponseEntity<Notes> response = this.restTemplate.exchange(
                baseURL + "notes/",
                HttpMethod.POST,
                new HttpEntity<>(convertNotesToDTO(notes), new HttpHeaders()),
                Notes.class);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

            // check response Notes
        Notes responseNotes = response.getBody();
        assertNotNull(responseNotes.getId());
        assertEquals(notes.getTitle(), responseNotes.getTitle());

            // check Notes saved in db
        Notes savedNotes = findNotesInDbById(responseNotes.getId()).get();
        assertEquals(notes.getTitle(), savedNotes.getTitle());
    }

    @Test
    public void whenPostSingleTaskToAlreadyCreatedNotes_thenItIsStoredInDbAndAssignedToNotes(){

        //given
        Notes notes = saveSingleRandomNotes();
        Task task = createSingleTask();

        //when
        ResponseEntity<Notes> response = this.restTemplate.exchange(
                baseURL + "notes/" + notes.getId() + "/tasks/",
                HttpMethod.POST,
                new HttpEntity<>(convertTaskToDTO(task), new HttpHeaders()),
                Notes.class);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // check response Notes
        Notes responseNotes = response.getBody();
        assertNotNull(responseNotes.getId());
        assertEquals(notes.getTitle(), responseNotes.getTitle());
        assertTrue(responseNotes.getTasks().size() == 1);

        Task responseTask = responseNotes.getTasks().get(0);
        // check response Task
        assertNotNull(responseTask.getId());
        assertEquals(task.getTitle(), responseTask.getTitle());
        assertEquals(task.getDescription(), responseTask.getDescription());
        assertEquals(task.getColor(), responseTask.getColor());
        assertEquals(task.getStatus(), responseTask.getStatus());

        // check saved Task in db
        Task savedTask = findTaskInDbById(responseTask.getId()).get();
        assertEquals(responseTask.getId(), savedTask.getId());
        assertEquals(task.getTitle(), savedTask.getTitle());
        assertEquals(task.getDescription(), savedTask.getDescription());
        assertEquals(task.getColor(), savedTask.getColor());
        assertEquals(task.getStatus(), savedTask.getStatus());
    }


    @Test
    public void whenPutSingleNotes_thenItIsUpdated(){

        //given
        Notes notes = saveSingleRandomNotes();
        notes.setTitle(notes.getTitle() + " Updated");

        //when
        ResponseEntity<Notes> response = this.restTemplate.exchange(
                baseURL + "notes/" + notes.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(convertNotesToDTO(notes), new HttpHeaders()),
                Notes.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(notes.getTitle(), findNotesInDbById(notes.getId()).get().getTitle());
    }

    @Test
    public void whenDeleteSingleNotesById_thenItIsDeletedFromDb(){

        //given
        Notes notes = saveSingleRandomNotes();

        //when
        ResponseEntity<String> response = this.restTemplate.exchange(
                baseURL + "notes/" + notes.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(new HttpHeaders()),
                String.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(String.format("Notes with id: %d was deleted", notes.getId()), response.getBody());
        assertFalse(findNotesInDbById(notes.getId()).isPresent());
    }
}
