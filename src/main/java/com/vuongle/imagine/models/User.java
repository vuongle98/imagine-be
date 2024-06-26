package com.vuongle.imagine.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vuongle.imagine.constants.FriendStatus;
import com.vuongle.imagine.constants.UserRole;
import com.vuongle.imagine.dto.auth.BaseUser;
import com.vuongle.imagine.exceptions.DataNotFoundException;
import com.vuongle.imagine.models.embeded.FriendShipData;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
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

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;

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

        if (friendship.stream().noneMatch(friend -> friend.getId().equals(friendId))) {
            friendship.add(new FriendShipData(friendId, FriendStatus.REQUESTED, Instant.now()));
        } else {

            for (FriendShipData friend : friendship) {
                if (friend.getId().equals(friendId) &&
                        (friend.getStatus().equals(FriendStatus.REJECTED_REQUEST) || friend.getStatus().equals(FriendStatus.REJECTED))) {
                    friend.setStatus(FriendStatus.REQUESTED);
                    friend.setUpdateTime(Instant.now());
                }
            }
        }
    }

    // user được request
    public void pendingFriend(ObjectId friendId) {
        if (Objects.isNull(friendship))
            friendship = new ArrayList<>();

        if (friendship.stream().noneMatch(friend -> friend.getId().equals(friendId))) {
            friendship.add(new FriendShipData(friendId, FriendStatus.PENDING, Instant.now()));
        } else {

            for (FriendShipData friend : friendship) {
                if (friend.getId().equals(friendId) &&
                        (friend.getStatus().equals(FriendStatus.REJECTED) || friend.getStatus().equals(FriendStatus.REJECTED_REQUEST))) {
                    friend.setStatus(FriendStatus.PENDING);
                    friend.setUpdateTime(Instant.now());
                }
            }
        }
    }

    public void acceptFriend(ObjectId friendId) {
        // find in friendship
        if (friendship.stream().noneMatch(friend -> friend.getId().equals(friendId))) {
            throw new DataNotFoundException("Friend not found");
        }

        // nếu đã có pending mới accept
        if (friendship.stream()
                .anyMatch(friend ->
                        friend.getId().equals(friendId) &&
                                (friend.getStatus().equals(FriendStatus.PENDING) || friend.getStatus().equals(FriendStatus.REQUESTED)))) {
            // update
            for (FriendShipData friend : friendship) {
                if (friend.getId().equals(friendId)) {
                    friend.setStatus(FriendStatus.ACCEPTED);
                    friend.setUpdateTime(Instant.now());
                }
            }
        }
    }

    public void declineFriend(ObjectId friendId) {
        // find in friendship
        if (friendship.stream().noneMatch(friend -> friend.getId().equals(friendId))) {
            throw new DataNotFoundException("Friend not found");
        }
        // bên gửi thì bij huyr
        if (friendship.stream()
                .anyMatch(friend ->
                        friend.getId().equals(friendId) && friend.getStatus().equals(FriendStatus.REQUESTED))) {
            // update
            for (FriendShipData friend : friendship) {
                if (friend.getId().equals(friendId)) {
                    friend.setStatus(FriendStatus.REJECTED_REQUEST);
                    friend.setUpdateTime(Instant.now());
                }
            }
        }

        // ben nhan thi huy
        if (friendship.stream()
                .anyMatch(friend ->
                        friend.getId().equals(friendId) &&
                                friend.getStatus().equals(FriendStatus.PENDING))) {
            // update
            for (FriendShipData friend : friendship) {
                if (friend.getId().equals(friendId)) {
                    friend.setStatus(FriendStatus.REJECTED);
                    friend.setUpdateTime(Instant.now());
                }
            }
        }
    }

    public void removeFriend(ObjectId friendId) {
        if (Objects.isNull(friendship)) return;

        if (friendship.stream().anyMatch(friend -> friend.getId().equals(friendId))) {
            friendship.removeIf(friend -> friend.getId().equals(friendId));
        }
    }

    public boolean isFriend(ObjectId friendId) {
        return friendship.stream().anyMatch(friend -> friend.getId().equals(friendId) && friend.getStatus().equals(FriendStatus.ACCEPTED));
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

    public boolean isAdmin() {
        return roles.contains(UserRole.ADMIN);
    }

    public boolean isModerator() {
        return roles.contains(UserRole.MODERATOR);
    }

    public boolean hasModifyPermission() {
        return isModerator() || isAdmin();
    }

    private void lockUser(boolean locked) {
        this.setLocked(locked);
    }
}
