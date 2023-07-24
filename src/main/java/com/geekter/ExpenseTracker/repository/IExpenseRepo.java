package com.geekter.ExpenseTracker.repository;

import com.geekter.ExpenseTracker.model.Expense;
import com.geekter.ExpenseTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IExpenseRepo extends JpaRepository<Expense,Long> {

    @Query("SELECT e FROM Expense e WHERE e.expenseCreatedDate = :date")
    List<Expense> findByExpenseCreatedDate(LocalDate date);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.expenseCreatedDate BETWEEN :startDate AND :endDate")
    List<Expense> findByUserAndExpenseCreatedDateBetween(User user, LocalDate startDate, LocalDate endDate);

    @Query("SELECT e FROM Expense e WHERE e.expenseExpirationDate < :currentDate")
    List<Expense> findByExpenseExpirationDateLessThan(LocalDate currentDate);


}