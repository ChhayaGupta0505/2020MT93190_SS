import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material';
import { Task } from '../model/task/task';
import { MatInputModule } from '@angular/material/input';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { NotesService } from '../service/notes-service.service';
import { TaskService } from '../service/task.service';

@Component({
  selector: 'app-task-dialog',
  templateUrl: './task-dialog.component.html',
  styleUrls: ['./task-dialog.component.css']
})
export class TaskDialogComponent implements OnInit {

  dialogTitle: String;
  notesId: String;
  task: Task;

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<TaskDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data,
    private notesService: NotesService,
    private taskService: TaskService) {

    this.dialogTitle = data.title;
    this.notesId = data.notesId;
    this.task = data.task;

    this.form = fb.group({
      title: [this.task.title, Validators.required],
      description: [this.task.description, Validators.required],
      color: [this.task.color,Validators.required]
  });
   }

  ngOnInit() {
  }

  save() {
    this.mapFormToTaskModel();
    if (!this.task.id) {
      this.notesService.saveNewTaskInNotes(this.notesId, this.task).subscribe();
    } else {
      this.taskService.updateTask(this.task).subscribe();
    }
    this.dialogRef.close();
  }

  close() {
      this.dialogRef.close();
  }

  private mapFormToTaskModel(): void {
    this.task.title = this.form.get('title').value;
    this.task.description = this.form.get('description').value;
    this.task.color = this.form.get('color').value;
    this.task.status = 'TODO';
  }

}
