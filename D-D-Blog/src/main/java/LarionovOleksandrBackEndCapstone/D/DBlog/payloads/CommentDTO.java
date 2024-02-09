package LarionovOleksandrBackEndCapstone.D.DBlog.payloads;

import jakarta.validation.constraints.NotEmpty;

public record CommentDTO(
        @NotEmpty
        String comment) {
}
