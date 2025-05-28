package co.com.bancolombia.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meta {
    @JsonProperty("_messageId")
    private String messageId;
    @JsonProperty("_requestDateTime")
    private String requestDate;
    @JsonProperty("moreRegisters")
    private Boolean moreRegister;
    @JsonProperty("totalRecords")
    private Integer totalRecords;
}
