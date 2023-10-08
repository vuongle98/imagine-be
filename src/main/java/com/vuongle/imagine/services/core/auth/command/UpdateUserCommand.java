package com.vuongle.imagine.services.core.auth.command;

import com.vuongle.imagine.models.embeded.FriendShipData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserCommand extends CreateUserCommand implements Serializable {

    private ObjectId id;

    private Boolean enabled;

    private Boolean locked;

    private Boolean online;

    private List<FriendShipData> friendShipData;
}
