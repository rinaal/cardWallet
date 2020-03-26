package no.cardwallet.card.AppUser;

import no.cardwallet.card.GiftCard.GiftCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.security.Principal;


@Controller
public class AppUserController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    GiftCardRepository giftCardRepository;

    final
    AppUserRepository appUserRepository;

    public AppUserController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    private void getAppUserByEmailAddModelAttribute(Model model, Principal principal) {
        String email = principal.getName();
        AppUser appUser = appUserRepository.findByEmail(email);
        model.addAttribute(appUser);
    }


    @GetMapping("/sign-up")
    public String signUp(@ModelAttribute AppUser appUser) {
        return "signUp";
    }

    @PostMapping("/save-user")
    public String validateUser(@ModelAttribute AppUser appUser, BindingResult bindingResult, @RequestParam String email, @RequestParam String password, @RequestParam String repeatPassword) {
        AppUserValidator appUserValidator = new AppUserValidator();
        appUser = new AppUser(email, password, repeatPassword);
        if (appUserValidator.supports(appUser.getClass())) {
            appUserValidator.validate(appUser, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            return "signUp";
        }
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);
        return "login";//send to log in page
    }

    @GetMapping("/login")
    public String userLogin() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }

    @GetMapping("/settings")
    public String userSettings(@ModelAttribute AppUser appUser) {
        return "userSettings";
    }

    @GetMapping("/change-email")
    public String changeEmail(Model model, Principal principal) {
        getAppUserByEmailAddModelAttribute(model, principal);
        return "changeEmail";
    }

    @PostMapping("/save-changed-email")
    public String saveChangedEmail (Model model, Principal principal, @ModelAttribute AppUser appUserPosting, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException {
        String email = principal.getName();
        AppUser appUser = appUserRepository.findByEmail(email);
        model.addAttribute(appUser);
        appUser.setEmail(appUserPosting.getEmail());
        appUserRepository.save(appUser);
        new SecurityContextLogoutHandler().logout(httpRequest,  httpResponse, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login"; //add to antMatcher.permittedAll()
    }

    @GetMapping("/change-password")
    public String changePassword(Model model, Principal principal) {
        getAppUserByEmailAddModelAttribute(model, principal);
        return "changePassword";
    }

    @PostMapping("/save-changed-password")
    public String saveChangedPassword(Model model, Principal principal, @ModelAttribute AppUser appUserPosting) {
        String email = principal.getName();
        AppUser appUser = appUserRepository.findByEmail(email);
        appUser.setPassword(passwordEncoder.encode(appUserPosting.getPassword()));
        appUserRepository.save(appUser);
        model.addAttribute(appUser);
        return "successfullyChangedPassword";
    }

    @GetMapping("/terms-and-conditions")
    public String termsAndConditions() {
        return "termsAndConditions";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgotPassword";
    }

    @PostMapping("/successfully-reset-password")
    public String passwordReset(@ModelAttribute AppUser appUser, @RequestParam String email) {
        if (appUserRepository.findAppUserByEmail(email) != null) {
            appUser = appUserRepository.findAppUserByEmail(email);
            appUser.setPassword(passwordEncoder.encode("abc"));
            appUserRepository.save(appUser);
        } else {
            return "redirect:/sign-up";
        }
        return "successfullyResetPassword";
    }


    @GetMapping ("/sureYouWantToDeleteAccount")
    public String sureYouWantToDeleteAccount () {
        return "deleteAccount";
    }

    @Transactional
    @GetMapping("/delete-app-user")
    public String deleteAppUser(Principal principal) {
        String email = principal.getName();
        Long appUserId = appUserRepository.findByEmail(email).getId();
        giftCardRepository.deleteByAppUserId(appUserId);
        appUserRepository.deleteAppUserByEmail(email);
        return "redirect:/sign-up";
    }

}
