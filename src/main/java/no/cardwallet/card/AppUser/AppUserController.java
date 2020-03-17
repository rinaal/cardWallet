package no.cardwallet.card.AppUser;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AppUserController {

    final
    AppUserRepository appUserRepository;

    public AppUserController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/signup")
    public String signUp(@ModelAttribute AppUser appUser) {
            return "signUp";
    }

    /*There was an unexpected error (type=Internal Server Error, status=500).
An Errors/BindingResult argument is expected to be declared immediately after the model attribute,
the @RequestBody or the @RequestPart arguments to which they apply:
public java.lang.String no.cardwallet.card.AppUser.AppUserController.validateUser(java.lang.String,java.lang.String,java.lang.String,org.springframework.ui.Model,
org.springframework.validation.BindingResult)*/
    @PostMapping("/saveuser")
    public String validateUser(Model model, BindingResult bindingResult, @RequestParam String email, @RequestParam String password, @RequestParam String repeatPassword) {
        AppUserValidator appUserValidator = new AppUserValidator();
        AppUser appUser = new AppUser(email, password, repeatPassword);
        if (appUserValidator.supports(appUser.getClass())) {
            appUserValidator.validate(appUser, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            return "signUp";
        }
        model.addAttribute(appUser);
        appUserRepository.save(appUser);
        return "redirect:/signup";//send to log in page
    }
}
