import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Notes } from '../model/notes/notes';
import { Task } from '../model/task/task';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NotesService {

  private notesAppUrl = environment.notesAppUrl

  constructor(private http: HttpClient) { }

  retrieveAllNotesBoards(): Observable<Notes[]> {
    return this.http.get<Notes[]>(this.notesAppUrl + '/notes/');
  }

  retrieveNotesById(id: String): Observable<Notes> {
    return this.http.get<Notes>(this.notesAppUrl + '/notes/' + id);
  }

  saveNewNotes(title: string): Observable<string> {
    let headers = new HttpHeaders({'Content-Type': 'application/json' });
    let options = { headers: headers };
    let jsonObject = this.prepareTiTleJsonObject(title);
    return this.http.post<string>(
      this.notesAppUrl + '/notes/',
      jsonObject,
      options
    );
  }

  saveNewTaskInNotes(notesId: String, task: Task): Observable<Task> {
    let headers = new HttpHeaders({'Content-Type': 'application/json' });
    let options = { headers: headers };
    return this.http.post<Task>(
      this.notesAppUrl + '/notes/' + notesId + '/tasks/',
      task,
      options);
  }

  private prepareTiTleJsonObject(title: string) {
    const object = {
      title: title
    }
    return JSON.stringify(object);
  }

}
