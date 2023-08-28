package com.vuongle.imagine.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vuongle.imagine.constants.FriendStatus;
import com.vuongle.imagine.constants.UserRole;
import com.vuongle.imagine.dto.auth.BaseUser;
import com.vuongle.imagine.models.embeded.FriendShipData;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("user")
public class User extends BaseUser implements Serializable, UserDetails {

    @NotEmpty(message = "Please provide your password")
    @JsonIgnore
    private String password;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant updatedDate;

    private boolean locked;

    private boolean enabled = true;

    private boolean online;

    private Instant lastActive;

    private List<FriendShipData> friendship;

    public User(
            String username,
            String fullName,
            String email,
            String password,
            List<UserRole> roles
    ) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.roles = new HashSet<>(roles);
    }

    public User(ObjectId id, String username, String email, String password, String fullName,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.roles = authorities.stream().map(item -> UserRole.valueOf(item.getAuthority())).collect(Collectors.toSet());
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = new HashSet<>(roles);
    }

    // user hiện tại request
    public void addFriend(ObjectId friendId) {
        if (Objects.isNull(friendship))
            friendship = new ArrayList<>();
        friendship.add(new FriendShipData(friendId, FriendStatus.REQUESTED, Instant.now()));
    }

    // user được request
    public void pendingFriend(ObjectId friendId) {
        if (Objects.isNull(friendship))
            friendship = new ArrayList<>();
        friendship.add(new FriendShipData(friendId, FriendStatus.PENDING, Instant.now()));
    }

    public void acceptFriend(ObjectId friendId) {
        // find in friendship
        if (friendship.stream().noneMatch(friend -> friend.getId().equals(friendId))) {
            throw new RuntimeException("Friend not found");
        }

        // update
        for (FriendShipData friend : friendship) {
            if (friend.getId().equals(friendId)) {
                friend.setStatus(FriendStatus.ACCEPTED);
                friend.setUpdateTime(Instant.now());
            }
        }
    }

    public void declineFriend(ObjectId friendId) {
        // find in friendship
        if (friendship.stream().noneMatch(friend -> friend.getId().equals(friendId))) {
            throw new RuntimeException("Friend not found");
        }

        // update
        for (FriendShipData friend : friendship) {
            if (friend.getId().equals(friendId)) {
                friend.setStatus(FriendStatus.REJECTED);
                friend.setUpdateTime(Instant.now());
            }
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(id, user.getId());
    }
}
