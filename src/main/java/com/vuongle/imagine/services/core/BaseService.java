package com.vuongle.imagine.services.core;

import org.bson.types.ObjectId;

public interface BaseService<Q, E, T> {

    T create(Q command);

    T update(E command);

    void delete(ObjectId id);
}
