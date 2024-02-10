package LarionovOleksandrBackEndCapstone.D.DBlog.payloads;

import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record CommentDTO(
        @NotEmpty
        String comment,
        @NotEmpty
        UUID userId,
        @NotEmpty
        UUID blogId) {
}
