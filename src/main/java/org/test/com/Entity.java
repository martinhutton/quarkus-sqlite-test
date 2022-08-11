package org.test.com;

import java.time.Instant;

public class Entity {

    private String id;
    private Instant time;

    public Entity(final String id, final Instant time) {

        this.id = id;
        this.time = time;
    }

    public String getId() {

        return id;
    }

    public void setId(final String id) {

        this.id = id;
    }

    public Instant getTime() {

        return time;
    }

    public void setTime(final Instant time) {

        this.time = time;
    }
}
