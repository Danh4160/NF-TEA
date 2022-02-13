package ca.mcgill.ecse428.nftea.controller;

import ca.mcgill.ecse428.nftea.dto.UserAccountDto;
import ca.mcgill.ecse428.nftea.model.UserAccount;
import ca.mcgill.ecse428.nftea.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;


    @PostMapping(value = {"/user-account","/user-account/"})
    public ResponseEntity CreateCustomerAccount(@RequestParam String firstname,String lastName, String userName, String userEmail, String password) {
        UserAccount user;
        try{
            user=userAccountService.createUser(firstname,lastName,userName,userEmail,password);
        } catch (Exception msg) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg.getMessage());
        }
        return new ResponseEntity<>(covertDto(user), HttpStatus.OK);
    }
    
    private UserAccountDto covertDto(UserAccount userAccount){
    	return new UserAccountDto(
    			userAccount.getId(),
    			userAccount.getFirstName(),
    			userAccount.getLastName(),
    			userAccount.getUserEmail(),
    			userAccount.getUsername(),
    			userAccount.getPassword(),
    			userAccount.getIsLoggedIn(),
    			userAccount.getLoginAttempts(),
    			userAccount.getLastAttempt(),
    			userAccount.getUserRole()
			);
    }

}
