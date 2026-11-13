package com.sprint.mission.discodeit.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ApiError {
    private String code;
    private String message;
    private List<ValidationError> details;
}
