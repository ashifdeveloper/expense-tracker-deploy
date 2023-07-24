package com.geekter.ExpenseTracker.service;

import com.geekter.ExpenseTracker.model.Expense;
import com.geekter.ExpenseTracker.model.User;
import com.geekter.ExpenseTracker.repository.IExpenseRepo;
import com.geekter.ExpenseTracker.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    @Autowired
    IExpenseRepo expenseRepo;
    @Autowired
    IUserRepo userRepo;
    @Autowired
    AuthenticationService authenticationService;



    public String createExpense(Expense expense) {
        expense.setExpenseCreatedDate(LocalDate.now());

        // Set the expiration date to 3 months from the current date
         LocalDate expirationDate = LocalDate.now().plusMonths(3);
         expense.setExpenseExpirationDate(expirationDate);
         expenseRepo.save(expense);

//         Remove expired expenses from the database
        LocalDate currentDate = LocalDate.now();
        List<Expense> expiredExpenses = expenseRepo.findByExpenseExpirationDateLessThan(currentDate);
        expenseRepo.deleteAll(expiredExpenses);

        return "Expense Created Successfully!!!!";
    }


    public List<Expense> getExpensesByExpenseCreatedDate(LocalDate date) {
        return expenseRepo.findByExpenseCreatedDate(date);

    }


    public String updateExpenseById(Long id, Double price) {
        Optional<Expense> myExpense = expenseRepo.findById(id);
        if (myExpense.isPresent()) {
            Expense expense = myExpense.get();
            expense.setExpensePrice(price);
            expenseRepo.save(expense);

            return "Updated Expense for " + id + " with price " + price;
        } else {
            return "Expense with ID " + id + " not found.";
        }
    }

    public String deleteExpenseById(Long id) {
        Optional<Expense> myExpense = expenseRepo.findById(id);
        if (myExpense.isPresent()) {
            Expense expense = myExpense.get();
            expenseRepo.deleteById(id);

            return "Deleted Expense for id " + id;
        } else {
            return "Expense with ID " + id + " not found.";
        }
    }

    public String generateTotalExpenditureForMonth(String month,String userEmail) {
        // Parse the input month string to LocalDate
        LocalDate startDate = LocalDate.parse(month + "-01");
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        User user = userRepo.findByUserEmail(userEmail);
        // Fetch expenses within the given month
        List<Expense> expenses = expenseRepo.findByUserAndExpenseCreatedDateBetween(user, startDate, endDate);

        // Calculate the total expenditure for the month
        Double totalExpenditure = 0.0;
        for (Expense expense : expenses) {
            totalExpenditure += expense.getExpensePrice();
        }

        String message = "Total expenditure for " + month + " is " + totalExpenditure;
        return message;

    }

    }
