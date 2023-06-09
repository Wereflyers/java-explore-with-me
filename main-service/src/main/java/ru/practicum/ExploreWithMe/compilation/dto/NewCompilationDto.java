package ru.practicum.ExploreWithMe.compilation.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class NewCompilationDto {
    private Set<Long> events;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean pinned = false;
    @NotBlank
    private String title;
}
