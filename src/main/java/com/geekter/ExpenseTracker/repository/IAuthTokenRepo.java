package com.geekter.ExpenseTracker.repository;


import com.geekter.ExpenseTracker.model.AuthenticationToken;
import com.geekter.ExpenseTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthTokenRepo extends JpaRepository<AuthenticationToken,Long> {


    AuthenticationToken findFirstByTokenValue(String authTokenValue);

    AuthenticationToken findFirstByUser(User user);
}
