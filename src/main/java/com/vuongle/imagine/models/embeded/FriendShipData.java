package com.vuongle.imagine.models.embeded;

import com.vuongle.imagine.constants.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendShipData implements Serializable {

    @Id
    private ObjectId id;

    private FriendStatus status;

    private Instant updateTime;
}
