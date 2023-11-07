package com.vuongle.imagine.models;

import com.vuongle.imagine.models.embeded.Creator;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseModelData implements Serializable {

    @NotNull
    private Creator creator;

//    @CreatedBy
//    @Field("createdBy")
//    private String createdBy;
//
//    @CreatedDate
//    @Field("createdAt")
//    private Instant createdAt;
//
//    @LastModifiedDate
//    @Field("lastModifiedDate")
//    private Instant lastModifiedDate;
//
//    @LastModifiedBy
//    @Field("lastModifiedBy")
//    private String lastModifiedBy;
}
