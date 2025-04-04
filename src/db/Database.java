package db;

import java.util.ArrayList;
import db.exception.EntityNotFoundException;

public class Database {

    private static final ArrayList<Entity> entities = new ArrayList<>();
    private static int newId = 1;

    public static void add(Entity e) {
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

    public static void update(Entity e) throws EntityNotFoundException {
        Entity oldE = get(e.id);
        int index = entities.indexOf(oldE);
        entities.set(index, e.copy());
    }
}
