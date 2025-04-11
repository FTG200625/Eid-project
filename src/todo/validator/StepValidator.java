package todo.validator;

import db.Entity;
import db.Validator;
import db.exception.InvalidEntityException;
import db.Database;
import todo.entity.Step;

public class StepValidator implements Validator {

    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Step)) {
            throw new IllegalArgumentException("Entity must be of type Step");
        }

        Step step = (Step) entity;
        if (step.getTitle() == null) {
            throw new InvalidEntityException("Step title can not be empty");
        }

        try {
            Database.get(step.getTaskRef());
        } catch (Exception e) {
            throw new InvalidEntityException("Can not find task by ID");
        }
    }
}
