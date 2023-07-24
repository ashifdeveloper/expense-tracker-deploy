package com.geekter.ExpenseTracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    private String expenseTitle;

    private String expenseDescription;

    private Double expensePrice;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // hide this in json but not in database table column

    private LocalDate expenseCreatedDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate expenseExpirationDate;


    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    @JsonIgnore
    private User user;
}
