import db.Database;

import db.Entity;
import todo.entity.Step;
import todo.entity.Task;
import todo.service.StepService;
import todo.service.TaskService;
import todo.validator.StepValidator;
import todo.validator.TaskValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;


import static db.Database.get;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {

        Database.registerValidator(2, new TaskValidator()); // 2 = کد Task
        Database.registerValidator(3, new StepValidator()); // 3 = کد Step

        System.out.println("To-Do List Application");
        System.out.println("Available commands: ");
        System.out.println("add task, add step, delete, update task, update step");
        System.out.println("get task-by-id, get all-tasks, get incomplete-tasks, exit");

        while (true) {
            System.out.print("\n--> ");
            String command = scanner.nextLine().trim().toLowerCase();

            try {
                switch (command) {
                    case "add task":
                        handleAddTask();
                        break;
                    case "add step":
                        handleAddStep();
                        break;
                    case "delete":
                        handleDelete();
                        break;
                    case "update task":
                        handleUpdateTask();
                        break;
                    case "update step":
                        handleUpdateStep();
                        break;
                    case "get task-by-id":
                        handleGetTaskById();
                        break;
                    case "get all-tasks":
                        handleGetAllTasks();
                        break;
                    case "get incomplete-tasks":
                        handleGetIncompleteTasks();
                        break;
                    case "exit":
                        System.out.println("Exiting application...");
                        return;
                    default:
                        System.out.println("Invalid command. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void handleAddTask() throws Exception {
        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Due date (yyyy-MM-dd): ");
        Date dueDate = dateFormat.parse(scanner.nextLine());

        Task task = new Task(title, description, dueDate);
        Database.add(task);

        System.out.println("Task saved successfully.");
        System.out.println("ID: " + task.id);
    }

    private static void handleAddStep() throws Exception {
        System.out.print("Task ID: ");
        int taskId = scanner.nextInt();

        System.out.print("Title: ");
        String title = scanner.nextLine();

        Step step = new Step(title, taskId);
        Database.add(step);

        System.out.println("Step saved successfully.");
        System.out.println("ID: " + step.id);
        System.out.println("Creation Date: " + new Date());
    }

    private static void handleDelete() throws Exception {
        System.out.print("ID: ");
        int id = scanner.nextInt();
        Database.delete(id);

        Entity entityToRemove = get(id);
        if (entityToRemove instanceof Task) {
            Task task = (Task) entityToRemove;
            for (Entity entity : Database.getEntities()) {
                if (entity instanceof Step) {
                    Step step = (Step) entity;
                    if (step.getTaskRef() == task.getId()) {
                        Database.delete(step.id);
                    }
                }
            }
        }


        System.out.println("Entity with ID=" + id + " successfully deleted.");
    }

    private static void handleUpdateTask() throws Exception {
        System.out.print("ID: ");
        int taskId = scanner.nextInt();

        System.out.print("Field to update (title/description/dueDate/status): ");
        String field = scanner.nextLine();
        String oldValue = new String();

        System.out.print("New Value: ");
        String newValue = scanner.nextLine();

        Task task = (Task) get(taskId);
        Task updatedTask = task.copy();

        switch (field.toLowerCase()) {
            case "title":
                updatedTask.setTitle(newValue);
                oldValue = task.getTitle();
                break;
            case "description":
                updatedTask.setDescription(newValue);
                oldValue = task.getDescription();
                break;
            case "duedate":
                updatedTask.setDueDate(dateFormat.parse(newValue));
                oldValue = String.valueOf(task.getDueDate());
                break;
            case "status":
                updatedTask.setStatus(Task.Status.valueOf(newValue.toUpperCase()));
                oldValue = String.valueOf(task.getStatus());
                if (updatedTask.getStatus() == Task.Status.COMPLETED) {
                    TaskService.completeAllSteps(taskId);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid field name");
        }

        Database.update(updatedTask);
        System.out.println("Successfully updated the task.");
        System.out.println("Field: " + field);
        System.out.println("old value: " + oldValue);
        System.out.println("new value: " + newValue);
    }

    private static void handleUpdateStep() throws Exception {
        System.out.print("ID: ");
        int stepId = scanner.nextInt();

        System.out.print("Field to update (title/status): ");
        String field = scanner.nextLine();

        System.out.print("New Value: ");
        String newValue = scanner.nextLine();
        String oldValue = new String();


        Step step = (Step) get(stepId);
        Step updatedStep = step.copy();

        switch (field.toLowerCase()) {
            case "title":
                updatedStep.setTitle(newValue);
                oldValue = step.getTitle();
                break;
            case "status":
                updatedStep.setStatus(Step.Status.valueOf(newValue.toUpperCase()));
                oldValue = String.valueOf(step.getStatus());
                break;
            default:
                throw new IllegalArgumentException("Invalid field name");
        }

        Database.update(updatedStep);
        System.out.println("Successfully updated the step.");
        System.out.println("Field: " + field);
        System.out.println("old value: " + oldValue);
        System.out.println("new value: " + newValue);
    }

    private static void handleGetTaskById() throws Exception {

    }

    private static void handleGetAllTasks() throws Exception {

    }

    private static void handleGetIncompleteTasks() throws Exception {

    }
}


