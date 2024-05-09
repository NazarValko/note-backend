package org.nazar.notesbackend.entity.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {

    private Long id;

    private String name;

    private String description;

    private LocalDate createdAt;
}
