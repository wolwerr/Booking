package com.example.host.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RebookingRequest {
    @NotNull
    private LocalDate newStartDate;
    @NotNull
    private LocalDate newEndDate;
}
