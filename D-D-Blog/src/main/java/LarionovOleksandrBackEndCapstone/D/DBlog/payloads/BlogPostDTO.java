package LarionovOleksandrBackEndCapstone.D.DBlog.payloads;

import java.util.UUID;

public record BlogPostDTO(
        String category,
        String title,
        String cover,
        String content,
        UUID userId
) {
}
