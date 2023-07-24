package com.geekter.ExpenseTracker.service;

import com.geekter.ExpenseTracker.model.*;
import com.geekter.ExpenseTracker.repository.IExpenseRepo;
import com.geekter.ExpenseTracker.repository.IUserRepo;
import com.geekter.ExpenseTracker.service.emailUtility.EmailHandler;
import com.geekter.ExpenseTracker.service.hashingUtility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    IUserRepo userRepo;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    ExpenseService expenseService;
    @Autowired
    IExpenseRepo expenseRepo;

    public SignUpOutput signUpUser(User user) {
        String newEmail = user.getUserEmail();
        String signUpStatusMessage=null;
        boolean signUpStatus=true;
        if(newEmail == null){
            signUpStatusMessage="Invalid Email";
            signUpStatus=false;

            return new SignUpOutput(signUpStatusMessage,signUpStatus);
        }
        //check if this user email already exists ??

        User existingUser = userRepo.findByUserEmail(newEmail);
try {
    if (existingUser != null) {
        signUpStatusMessage = "Email already registered!!!";
        signUpStatus = false;
        return new SignUpOutput(signUpStatusMessage, signUpStatus);
    }

    //hash the password: encrypt the password

    String encryptedPassword = PasswordEncrypter.encryptPassword(user.getUserPassword());
    user.setUserPassword(encryptedPassword);
    userRepo.save(user);

    return new SignUpOutput("User Registered Successfully!!!", signUpStatus);
}

      catch (Exception e){
        signUpStatusMessage = "Internal error occurred during sign up";
        signUpStatus = false;
        return new SignUpOutput(signUpStatusMessage,signUpStatus);
}




    }

    public String signInUser(SignInInput signInInput) {
        String signInEmail = signInInput.getEmail();
        String signInStatusMessage = null;

        if(signInEmail == null)
        {
            signInStatusMessage = "Invalid email";
            return signInStatusMessage;


        }

        //check if this user email already exists ??
        User existingUser = userRepo.findFirstByUserEmail(signInEmail);

        if(existingUser == null)
        {
            signInStatusMessage = "Email not registered!!!";
            return signInStatusMessage;

        }


        try {
            String encryptedPassword = PasswordEncrypter.encryptPassword(signInInput.getPassword());
            if(existingUser.getUserPassword().equals(encryptedPassword))
            {
                //session should be created since password matched and user id is valid
                AuthenticationToken authToken  = new AuthenticationToken(existingUser);
                authenticationService.saveAuthToken(authToken);

                EmailHandler.sendEmail(existingUser.getUserEmail(), "email testing",authToken.getTokenValue());
                return "Token sent to your email";
            }
            else {
                signInStatusMessage = "Invalid credentials!!!";
                return signInStatusMessage;
            }
        }
        catch(Exception e)
        {
            signInStatusMessage = "Internal error occurred during sign in";
            return signInStatusMessage;
        }

    }

    public String signOutUser(String email) {

        User user = userRepo.findFirstByUserEmail(email);
        AuthenticationToken token = authenticationService.findFirstByUser(user);
        authenticationService.removeToken(token);
        return "User Signed out successfully";
    }


    public String createExpense(Expense expense, String email) {

        User expenseUser = userRepo.findFirstByUserEmail(email);
        expense.setUser(expenseUser);
        return expenseService.createExpense(expense);

    }

    public String getExpensesByExpenseCreatedDate(String email, LocalDate date) {

          return String.valueOf(expenseService.getExpensesByExpenseCreatedDate(date));


    }

    public String updateExpenseById(Long id, Double price) {
        return String.valueOf(expenseService.updateExpenseById(id,price));
    }

    public String deleteExpenseById(Long id) {
        return expenseService.deleteExpenseById(id);
    }

    public String generateTotalExpenditureForMonth(String month,String userEmail) {
        return expenseService.generateTotalExpenditureForMonth(month,userEmail);
    }
}

