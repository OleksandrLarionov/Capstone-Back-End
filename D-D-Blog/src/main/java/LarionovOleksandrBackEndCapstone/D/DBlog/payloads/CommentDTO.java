package LarionovOleksandrBackEndCapstone.D.DBlog.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CommentDTO(
        @NotEmpty
        String comment,
        @NotNull
        UUID blogId) {
}
