package todo.service;

import db.Database;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.*;
import todo.entity.Task;
import todo.entity.Task.Status;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    public static void createTask(String title, String description, Date dueDate) throws InvalidEntityException {
        Task task = new Task(title, description, dueDate);
        Database.add(task);
    }

    public static void setTaskStatus(int taskId, Status newStatus) throws EntityNotFoundException, InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(newStatus);
        Database.update(task);

        if (newStatus == Status.COMPLETED) {
            completeAllSteps(taskId);
        }
    }

    public static void completeAllSteps(int taskId)
            throws EntityNotFoundException, InvalidEntityException {
        List<Step> steps = StepService.getStepsByTask(taskId);
        for (Step step : steps) {
            if (step.getStatus() != Step.Status.COMPLETED) {
                StepService.setStepStatus(step.getId(), Step.Status.COMPLETED);
            }
        }
    }
}
