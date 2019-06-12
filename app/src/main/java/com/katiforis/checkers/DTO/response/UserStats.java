package com.katiforis.checkers.DTO.response;


import com.katiforis.checkers.DTO.UserDto;

import java.util.ArrayList;
import java.util.List;

public class UserStats extends BaseResponse {
    UserDto userDto;

    public UserStats(String status) {
        super(status);
    }

    public UserStats() {
        super(ResponseState.USER_STATS.getState());
    }
    public UserStats(String gameId, String status) {
        super(gameId, status);
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
