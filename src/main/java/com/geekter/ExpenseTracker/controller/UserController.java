package com.geekter.ExpenseTracker.controller;

import com.geekter.ExpenseTracker.model.Expense;
import com.geekter.ExpenseTracker.model.SignInInput;
import com.geekter.ExpenseTracker.model.SignUpOutput;
import com.geekter.ExpenseTracker.model.User;
import com.geekter.ExpenseTracker.service.AuthenticationService;
import com.geekter.ExpenseTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("user/signup")
    public SignUpOutput signUpUser(@RequestBody User user) {
        return userService.signUpUser(user);


    }

    @PostMapping("user/signin")
    public String signInUser(@RequestBody SignInInput signInInput) {
        return userService.signInUser(signInInput);

    }


    @DeleteMapping("user/signOut")
    public String signOutUser(String email, String token) {
        if (authenticationService.authenticate(email, token)) {
            return userService.signOutUser(email);
        } else {
            return "Sign out not allowed for non authenticated user.";
        }

    }


    @PostMapping("expense")
    public String createExpense(@RequestBody Expense expense, @RequestParam String email, @RequestParam String token) {
        if (authenticationService.authenticate(email, token)) {
            return userService.createExpense(expense, email);
        } else {
            return "Not an Authenticated user activity!!!";
        }
    }

    @GetMapping("expense/date/{date}")

    public String getExpensesByExpenseCreatedDate(@PathVariable LocalDate date, @RequestParam String email, @RequestParam String token) {
        if (authenticationService.authenticate(email, token)) {
            return userService.getExpensesByExpenseCreatedDate(email, date);
        } else {
            return "Not an Authenticated user activity!!!";
        }

    }

    @PutMapping("expense/{id}/{price}")

    public String updateExpenseById(@PathVariable Long id, @RequestParam String email, @RequestParam String token, @PathVariable Double price) {
        if (authenticationService.authenticate(email, token)) {
            return userService.updateExpenseById(id, price);
        } else {
            return "Not an Authenticated user activity!!!";
        }
    }


    @DeleteMapping("expense/{id}")

    public String deleteExpenseById(@PathVariable Long id, @RequestParam String email, @RequestParam String token) {
        if (authenticationService.authenticate(email, token)) {
            return userService.deleteExpenseById(id);
        } else {
            return "Not an Authenticated user activity!!!";
        }

    }

    @GetMapping("expense/reports/monthly/expenditure")//monthformat-yyyy-mm
    public String generateTotalExpenditureForMonth(@RequestParam String userEmail, @RequestParam String token, @RequestParam String month){
        if (authenticationService.authenticate(userEmail, token)) {
            return userService.generateTotalExpenditureForMonth(month,userEmail);
        } else {
            return "Not an Authenticated user activity!!!";
        }
}
}
