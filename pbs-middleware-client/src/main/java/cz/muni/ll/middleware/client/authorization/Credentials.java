package cz.muni.ll.middleware.client.authorization;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.validator.routines.EmailValidator;

@Getter
@Setter
final public class Credentials {

    @NonNull
    private String email;

    @NonNull
    private String password;

    public Credentials(@NonNull String email, @NonNull String password) {
        if (email == null) {
            throw new IllegalArgumentException("Email can not be NULL!");
        }
        if (email.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("Email can not be blank!");
        }
        if(!EmailValidator.getInstance(true).isValid(email)) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
        if (password == null) {
            throw new IllegalArgumentException("Password can not be NULL!");
        }
        if (password.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("Password can not be blank!");
        }
        this.email = email;
        this.password = password;
    }
}
