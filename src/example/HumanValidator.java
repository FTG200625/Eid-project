package example;

import db.*;
import db.exception.InvalidEntityException;

public class HumanValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Human)) {
            throw new IllegalArgumentException("Entity must be type of Human.");
        }

        Human human = (Human) entity;

        if (human.name == null) {
            throw new InvalidEntityException("Name can not be empty.");
        }

        if (human.age < 0) {
            throw new InvalidEntityException("Age must be a positive number.");
        }
    }
}
