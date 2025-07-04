package com.wu.duplicatecheck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for incoming profile update/duplicate check requests.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileUpdateRequest {

    @NotBlank(message = "cpfNumber is mandatory")
    @JsonProperty("cpfNumber")
    private String cpfNumber;

    @NotBlank(message = "sourceSystem is mandatory")
    @JsonProperty("sourceSystem")
    private String sourceSystem;

    @NotBlank(message = "sourceId is mandatory")
    @JsonProperty("sourceId")
    private String sourceId;

    @NotBlank(message = "eventName is mandatory")
    @JsonProperty("eventName")
    private String eventName;

    @JsonProperty("externalRefId")
    private String externalRefId;

    @JsonProperty("eventId")
    private String eventId;

    @JsonProperty("productCode")
    private String productCode;

    @JsonProperty("eventTimestamp")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}", message = "eventTimestamp must be in ISO 8601 format")
    private String eventTimestamp;

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("messagePayload")
    private Object messagePayload;

    @NotBlank(message = "customerUmn is mandatory")
    @JsonProperty("customerUmn")
    private String customerUmn;

    @NotBlank(message = "firstName is mandatory")
    @JsonProperty("firstName")
    private String firstName;

    @NotBlank(message = "lastName is mandatory")
    @JsonProperty("lastName")
    private String lastName;

    @Email(message = "email must be a valid address")
    @JsonProperty("email")
    private String email;

    /** Phone digits only, no ‘+’.  ISD code comes in <code>phoneCountryCode</code>. */
    @JsonProperty("phone")
    private String phone;

    /** ISO-3166 numeric or alpha country code for <code>phone</code>. */
    @JsonProperty("phoneCountryCode")
    private String phoneCountryCode;

    @JsonProperty("address")
    private String address;

}
