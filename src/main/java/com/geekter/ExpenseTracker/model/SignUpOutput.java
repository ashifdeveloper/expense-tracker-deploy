package com.geekter.ExpenseTracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpOutput {
        private String signUpStatusMessage;
        private boolean signUpStatus;

}
