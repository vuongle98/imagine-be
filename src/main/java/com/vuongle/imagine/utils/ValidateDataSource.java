package com.vuongle.imagine.utils;

import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.exceptions.NoPermissionException;
import com.vuongle.imagine.exceptions.UserNotFoundException;
import com.vuongle.imagine.models.embeded.Creator;

import java.lang.reflect.Field;
import java.util.Objects;

public class ValidateDataSource {

    public static <T> boolean isOwnData(T dataSource, Class<T> classType) {
        if (Objects.isNull(dataSource)) {
            throw new DataNotFoundException(classType.getSimpleName() + " not found");
        }

        if (Objects.isNull(Context.getCreator())) throw new UserNotFoundException("Logged in user not found");


        try {
            Field creatorField = dataSource.getClass().getDeclaredField("creator");
            creatorField.setAccessible(true);

            Creator creator = (Creator) creatorField.get(dataSource);
            if (!Context.getCreator().getId().equals(creator.getId())) {
                throw new NoPermissionException("logged in user must be owner");
            }

        } catch (NoSuchFieldException e) {
            throw new DataNotFoundException(classType.getName() + " not found");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        return true;
    }

}
