package db;

import java.util.ArrayList;

import java.util.Date;

import java.util.HashMap;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;


public class Database {

    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static int newId = 1;
    private static HashMap<Integer, Validator> validators = new HashMap<>();

    public static void add(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        if (validator != null) {
            validator.validate(e);
        }

        if (e instanceof Trackable) {
            Date now = new Date();
            ((Trackable) e).setCreationDate(now);
            ((Trackable) e).setLastModificationDate(now);
        }
        e.id = newId;
        ++newId;
        entities.add(e.copy());

    }

    public static Entity get(int id) throws EntityNotFoundException {
        for (Entity e : entities) {
            if (e.id == id) {
                return e.copy();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException {
        Entity e = get(id);
        entities.remove(e);
    }

    public static void update(Entity e) throws EntityNotFoundException, InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());

        //validator.validate(e);

        if (e instanceof Trackable) {
            ((Trackable) e).setLastModificationDate(new Date());
        }

        Entity oldE = get(e.id);
        int index = entities.indexOf(oldE);
        entities.remove(oldE);
        entities.add(e.copy());

    }
    public static void registerValidator(int entityCode, Validator validator) {

        if (validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Validator already exists");
        }
        validators.put(entityCode, validator);
    }


    public static ArrayList<Entity> getAll(int entityCode) {
        ArrayList<Entity> result = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity.getEntityCode() == entityCode) {
                result.add(entity.copy());
            }
        }

        return result;
    }
    public static ArrayList<Entity> getEntities(){
        return entities;
    }

}
