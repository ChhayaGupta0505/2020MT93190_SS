import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { NotesService } from '../service/notes-service.service';
import { Notes } from '../model/notes/notes';

@Component({
  selector: 'app-notes-dialog',
  templateUrl: './notes-dialog.component.html',
  styleUrls: ['./notes-dialog.component.css']
})
export class NotesDialogComponent implements OnInit {

  title : string;
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<NotesDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data,
    private notesService: NotesService) {

      this.form = fb.group({
        title: [this.title, Validators.required]
    });
    }

  ngOnInit() {
  }

  close() {
    this.dialogRef.close();
  }

  save() {
    this.title = this.form.get('title').value;
    if (this.title) {
      this.notesService.saveNewNotes(this.title).subscribe(

        response => {
          console.log(response)
        }
      )
    }
    this.dialogRef.close();
  }

}
