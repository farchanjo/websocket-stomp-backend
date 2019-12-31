
package br.eti.archanjo.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = 5001106180231042170L;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("destinationName")
    private String destinationName;

    @JsonProperty("serverDate")
    private Date serverDate;

    @JsonProperty("clientDate")
    private Date clientDate;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("isPrivate")
    private boolean individual;

    @JsonProperty("message")
    private String message;

}
