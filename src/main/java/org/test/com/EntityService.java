package org.test.com;

import static org.jooq.impl.DSL.asterisk;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;

import io.quarkus.logging.Log;
import org.jooq.DSLContext;
import org.jooq.DeleteUsingStep;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.impl.DSL;

@ApplicationScoped
public class EntityService {

    public static final String TABLE_ENTITY = "entity";
    public static final String COLUMN_ENTITY_ID = "id";
    public static final String COLUMN_ENTITY_TIME = "time";

    @Inject
    DSLContext dsl;

    public List<Entity> retrieveAllEntities() {

        Log.info("Retrieving all entities");
        return dsl.select(asterisk()).from(TABLE_ENTITY).fetch().into(Entity.class);
    }

    public Entity retrieveEntity(String id) {

        Log.info("Retrieving entity with id: " + id);
        List<Entity> into = dsl.select(asterisk()).from(TABLE_ENTITY).where(COLUMN_ENTITY_ID + " = \"" + id + "\"").fetch().into(
                Entity.class);
        return into.get(0);
    }

    @Transactional
    public Entity insert(final Entity entity) {

        InsertQuery<?> insert = dsl.insertQuery(DSL.table(TABLE_ENTITY));

        insert.addValue(DSL.field(COLUMN_ENTITY_ID, String.class), entity.getId());
        insert.addValue(DSL.field(COLUMN_ENTITY_TIME, String.class), entity.getTime().toString());

        insert.execute();

        // uncomment to check how transactions handle errors
        //throw new RuntimeException("BOOOM!!");

        return entity;
    }

    public void clearAllEntities() {

        DeleteUsingStep<Record> recordDeleteUsingStep = dsl.deleteFrom(DSL.table(TABLE_ENTITY));

        Log.info("Deleting all entities from table");
        recordDeleteUsingStep.execute();
    }

}
