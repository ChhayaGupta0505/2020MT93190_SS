import { Component, OnInit } from '@angular/core';
import { Notes } from '../model/notes/notes';
import { NotesService } from '../service/notes-service.service';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { NotesDialogComponent } from '../notes-dialog/notes-dialog.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  notesList: Notes[];

  constructor(
    private notesService: NotesService,
    private dialog: MatDialog
  ) { }

  ngOnInit() {
    this.retrieveAllNotesBoards();
  }

  openDialogForNewNotes(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.data = {
      notes: new Notes()
    };
    this.dialog.open(NotesDialogComponent, dialogConfig)
  }

  private retrieveAllNotesBoards(): void {
    this.notesService.retrieveAllNotesBoards().subscribe(

      response => {
        this.notesList = response;
      }
    )
  }

}
