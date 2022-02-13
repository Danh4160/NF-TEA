package ca.mcgill.ecse428.nftea.service;

import ca.mcgill.ecse428.nftea.dao.UserAccountRepository;
import ca.mcgill.ecse428.nftea.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserAccountService {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Transactional
    public UserAccount createUser(String firstName,String lastName,String userName,String userEmail,String password) throws Exception {
        if (isValidEmailAddress(userEmail)==false){
            throw new Exception("Wrong Email");
        }
        if (password.length()<8){
            throw new Exception("More then 8 chars are required");
        }
        if (firstName.equals(null)||lastName.equals(null)||userEmail.equals(null)||password.equals(null)||userName.equals(null)){
            throw new Exception("All textboxes need to be completed");
        }
        UserAccount myUser= new UserAccount(firstName,lastName,userEmail,userName,password,false, 0, null, UserAccount.UserRole.Customer);
        userAccountRepository.save(myUser);
        return myUser;
    }

    @Transactional
    public UserAccount getAccountByUsername(String username) {
        return userAccountRepository.findUserAccountByUsername(username);
    }

    @Transactional
    public UserAccount setUserOnline(String username) throws Exception {
        UserAccount user = userAccountRepository.findUserAccountByUsername(username);
        if (user == null) throw new Exception("Invalid username");
        user.setIsLoggedIn(true);
        userAccountRepository.save(user);
        return user;
    }

    @Transactional
    public UserAccount changeUserInfo(String username, String newPassWord, String confirmPassword, String userEmail) throws Exception {
        if (username.length() == 0) throw new Exception("Invalid username");
        if (!isValidEmailAddress(userEmail)) throw new Exception("Invalid Email");
        if (newPassWord.length()<8 || confirmPassword.length() < 8) throw new Exception("Passwords cannot be empty");
        if (!newPassWord.equals(confirmPassword)) throw new Exception("Passwords do not match");
        UserAccount user = userAccountRepository.findUserAccountByUsername(username);
        user.setUsername(username);
        user.setPassword(newPassWord);
        user.setUserEmail(userEmail);
        userAccountRepository.save(user);
        return user;
    }

    @Transactional
    public void clear() {
        userAccountRepository.deleteAll();
    }
    
    @Transactional
    public void deleteUser(Long id, String password) throws Exception {
    	if(id == null) {
    		throw new Exception("User Id required to delete user.");
    	}
    	if(password == null || password.length() < 8) {
    		throw new Exception("Valid password required to delete user.");
    	}
    	
    	UserAccount user = userAccountRepository.findUserAccountById(id);
    	
    	// User is not in the Database, as is requested by the callee
    	if(user == null) {
    		return;
    	} else if (!user.getPassword().equals(password)) {
    		throw new Exception("Invalid password for user " + id + ".");
    	} else {
    		userAccountRepository.delete(user);
    	}
    }
    

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
