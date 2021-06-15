package com.db.awmd.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 * @Author Challa Hemanth
 *
 * Transfer Domain class. This holds the trasfer object containing
 * FromAccountId, ToAccountId and Amount to be transferred
 *
 */
@Data
public class Transfer {

    @NotNull
    @NotEmpty
    private String fromAccountId;

    @NotNull
    @NotEmpty
    private String toAccountId;

    @NotNull
    @Min(value = 0, message = "Amount to be transferred cannot be less than zero!!")
    private BigDecimal amount;

    @JsonCreator
    public Transfer(@JsonProperty("fromAccountId") String fromAccountId,
                   @JsonProperty("toAccountId") String toAccountId, @JsonProperty("amount") BigDecimal amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }
}
