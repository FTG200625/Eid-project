package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Step.Status;
import todo.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class StepService {
    public static void createStep(int taskRef, String title) throws InvalidEntityException {
        Step step = new Step(title, taskRef);
        Database.add(step);
    }

    public static void setStepStatus(int stepId, Status newStatus) throws EntityNotFoundException, InvalidEntityException {
        Step step = (Step) Database.get(stepId);
        step.setStatus(newStatus);
        Database.update(step);
        checkParentTaskStatus(step.getTaskRef());
    }

    private static void checkParentTaskStatus(int taskId) throws EntityNotFoundException, InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        ArrayList<Step> steps = getStepsByTask(taskId);

        boolean allCompleted = true;
        boolean anyCompleted = false;

        for (Step step : steps) {
            if (step.getStatus() != Step.Status.COMPLETED) {
                allCompleted = false;
            } else {
                anyCompleted = true;
            }
        }

        if (allCompleted) {
            TaskService.setTaskStatus(taskId, Task.Status.COMPLETED);
        } else if (anyCompleted) {
            TaskService.setTaskStatus(taskId, Task.Status.IN_PROGRESS);
        }
    }

    public static ArrayList<Step> getStepsByTask(int taskId) {
        ArrayList<Entity> allSteps = Database.getAll(3);
        ArrayList<Step> result = new ArrayList<>();

        for (Entity entity : allSteps) {
            Step step = (Step) entity;
            if (step.getTaskRef() == taskId) {
                result.add(step);
            }
        }

        return result;
    }
}
