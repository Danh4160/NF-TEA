package ca.mcgill.ecse428.nftea.CucumberStepDefinitions;

import ca.mcgill.ecse428.nftea.model.UserAccount;
import ca.mcgill.ecse428.nftea.service.LoginService;
import ca.mcgill.ecse428.nftea.service.UserAccountService;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest
public class loginStepDefinitions {

    private LocalDateTime currentDateTime;

    int errorCounter = 0;
    String error = "";
    UserAccount userAccount = null;

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    LoginService loginService;

    @After
    public void teardown(){
//        userAccountService.
        int errorCounter = 0;
        String error = "";
    }

    @Given("the following users exist in the system:")
    public void theFollowingUsersExistInTheSystem(io.cucumber.datatable.DataTable dataTable) throws Exception {
        List<List<String>> rows = dataTable.asLists();
        int i = 0;
        for (List<String> columns : rows){
            if (i == 0){
                i++;
            }
            else {
                String email = columns.get(0);
                String password = columns.get(1);
                userAccount =  userAccountService.createUser(null,null, null, email, password);

            }
        }
    }

    @Given("the current date and time {string}")
    public void the_current_date_and_time(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        currentDateTime = LocalDateTime.parse(dateTimeStr, formatter);
    }

    @Given("the registered user is not logged in with {string}")
    public void the_registered_user_is_not_logged_in_with(String email) {
        UserAccount user = userAccountService.getUserAccountByEmail(email);
        user.setIsLoggedIn(false);
        userAccountService.saveAccount(user);
    }

    @Given("the registered user is logged in with {string}")
    public void the_registered_user_is_logged_in_with(String email) {
        UserAccount user = userAccountService.getUserAccountByEmail(email);
        user.setIsLoggedIn(true);
        userAccountService.saveAccount(user);
    }

    @Given("{String} has {int} attempt")
    public void the_registered_user_is_logged_in_with(String email, int attempts) {
        UserAccount user = userAccountService.getUserAccountByEmail(email);
        userAccount.setLoginAttempts(attempts);
        userAccountService.saveAccount(user);
    }


    @When("the registered user tries to log in with email {string} and password {string}")
    public void theRegisteredUserTriesToLogInWithEmailAndPassword(String email, String password) {
        try{
            userAccount = loginService.loginUserAccount(email, password);
        }
        catch (Exception e){
            error = e.getMessage();
            errorCounter++;
        }
    }

    @Then("the registered user should be successfully logged in")
    public void theRegisteredUserShouldBeSuccessfullyLoggedIn() {
        assertTrue(userAccount.getIsLoggedIn());
    }

    @Then("the registered user should not be logged in")
    public void theRegisteredUserShouldNotBeLoggedIn() {
        assertFalse(userAccount.getIsLoggedIn());
    }

    @Then("an error message {string} shall be raised")
    public void anErrorMessageShallBeRaised(String errorMsg) {
        assertTrue(error.contains(errorMsg));
    }

    @Then("{String} should have {int} attempts")
    public void should_have_attempts(String email, int attempts) {
        UserAccount user = userAccountService.getUserAccountByEmail(email);
        assertEquals(attempts, user.getLoginAttempts());
    }

    @Then("{String}'s most recent attempt should be at {String}")
    public void most_recent_attempt_should_be_at(String email, String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);

        UserAccount user = userAccountService.getUserAccountByEmail(email);
        assertEquals(dateTime, user.getLastAttempt());
    }
}
