package LarionovOleksandrBackEndCapstone.D.DBlog.payloads;

import jakarta.validation.constraints.NotEmpty;

public record CommentResponseDTO(
        @NotEmpty
        String comment
) {
}
