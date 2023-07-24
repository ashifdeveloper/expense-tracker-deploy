package com.geekter.ExpenseTracker.repository;

import com.geekter.ExpenseTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepo extends JpaRepository<User,Long>{
    User findByUserEmail(String newEmail);

    User findFirstByUserEmail(String signInEmail);
}
