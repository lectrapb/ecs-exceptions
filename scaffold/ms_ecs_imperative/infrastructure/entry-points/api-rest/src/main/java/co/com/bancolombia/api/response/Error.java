package co.com.bancolombia.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Error {
    private String code;
    private String detail;
}
