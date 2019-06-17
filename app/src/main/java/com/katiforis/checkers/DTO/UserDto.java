package com.katiforis.checkers.DTO;

import java.io.Serializable;

public class UserDto implements Serializable {
    static final long serialVersionUID = -4596596853482889445L;
    private long id;
    private String userId;
    private String username;
    private String email;
    private String imageUrl;
    private PlayerDetailsDto playerDetails;
    private String color;
    private Long secondsRemaining;
    private Boolean isCurrent;

    public UserDto() {
    }

    public UserDto(String username) {
        this.username = username;
    }


    public UserDto(long id, String username, String imageUrl) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public PlayerDetailsDto getPlayerDetails() {
        return playerDetails;
    }

    public void setPlayerDetails(PlayerDetailsDto playerDetails) {
        this.playerDetails = playerDetails;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getSecondsRemaining() {
        return secondsRemaining;
    }

    public void setSecondsRemaining(Long secondsRemaining) {
        this.secondsRemaining = secondsRemaining;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return this.userId.equals(userDto.userId);
    }
}
