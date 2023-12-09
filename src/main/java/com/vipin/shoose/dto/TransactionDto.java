package com.vipin.shoose.dto;

import com.vipin.shoose.model.TransactionStatus;
import com.vipin.shoose.model.TransactionType;
import com.vipin.shoose.model.Wallet;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDto {
    private String amount;

    private String wallet;
    private String transactionStatus;
    private String date;
    private String time;
    private String transactionType;
}
