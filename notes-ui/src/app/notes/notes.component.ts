import { Component, OnInit } from '@angular/core';
import { NotesService } from '../service/notes-service.service';
import { ActivatedRoute } from '@angular/router';
import { Notes } from '../model/notes/notes';
import { Task } from '../model/task/task';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { TaskDialogComponent } from '../task-dialog/task-dialog.component';
import { TaskService } from '../service/task.service';

@Component({
  selector: 'app-notes',
  templateUrl: './notes.component.html',
  styleUrls: ['./notes.component.css']
})
export class NotesComponent implements OnInit {

  notes: Notes;
  todos: Task[] = [];
  inprogress: Task[] = [];
  dones: Task[] = [];

  constructor(
    private notesService: NotesService,
    private taskService: TaskService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) { }

  ngOnInit() {
    this.getNotes();
  }

  drop(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      this.updateTaskStatusAfterDragDrop(event);
      transferArrayItem(event.previousContainer.data,
                        event.container.data,
                        event.previousIndex,
                        event.currentIndex);
    }
  }

  openDialogForNewTask(): void {
    this.openDialog('Create New Task', new Task());
  }

  openTaskDialog(event): void {
    let taskId = event.srcElement.id;

    this.taskService.getTaskById(taskId).subscribe(
      response => {
        this.openDialog('Update Task', response);
      }
    );
  }

  private getNotes(): void {
    const id = this.route.snapshot.paramMap.get('id');

    this.notesService.retrieveNotesById(id).subscribe(
      response => {
        this.notes = response;
        this.splitTasksByStatus(response);
      }
    )
  }

  private splitTasksByStatus(notes: Notes): void {
    this.todos = notes.tasks.filter(t=>t.status==='TODO');
    this.inprogress = notes.tasks.filter(t=>t.status==='INPROGRESS');
    this.dones = notes.tasks.filter(t=>t.status==='DONE');
  }

  private updateTaskStatusAfterDragDrop(event: CdkDragDrop<string[], string[]>) {
    let taskId = event.item.element.nativeElement.id;
    let containerId = event.container.id;

    this.taskService.getTaskById(taskId).subscribe(
        response => {
          this.updateTaskStatus(response, containerId);
        }
    );
  }

  private updateTaskStatus(task: Task, containerId: string): void {
    if (containerId === 'todo'){
      task.status = 'TODO'
    } else if (containerId === 'inpro'){
      task.status = 'INPROGRESS'
    } else {
      task.status = 'DONE'
    }
    this.taskService.updateTask(task).subscribe();
  }

  private openDialog(title: string, task: Task): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.data = {
      title: title,
      task: task,
      notesId: this.notes.id
    };
    this.dialog.open(TaskDialogComponent, dialogConfig)
  }
}
