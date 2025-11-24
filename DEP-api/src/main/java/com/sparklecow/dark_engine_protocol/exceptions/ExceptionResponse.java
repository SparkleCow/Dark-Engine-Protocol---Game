package com.sparklecow.dark_engine_protocol.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String message;
    private int businessErrorCode;
    private String businessErrorDescription;
    private Set<String> validationErrors;
    private Map<String, String> errorDetails;
}