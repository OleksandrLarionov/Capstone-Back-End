package LarionovOleksandrBackEndCapstone.D.DBlog.payloads.errorsPayloads;

import java.util.List;

public record ErrorsPayloadWhitList(String message, String date, List<String> errorsList) {
}
