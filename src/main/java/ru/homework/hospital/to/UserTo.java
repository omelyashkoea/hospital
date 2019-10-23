package ru.homework.hospital.to;

import org.hibernate.validator.constraints.SafeHtml;
import ru.homework.hospital.HasEmail;
import ru.homework.hospital.util.UserUtil;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class UserTo extends BaseTo implements HasEmail, Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 2, max = 100)
    @SafeHtml
    private String name;

    @Email
    @NotBlank
    @Size(max = 100)
    @SafeHtml
    private String email;

    @NotBlank
    @Size(min = 5, max = 32, message = "length must be between 5 and 32 characters")
    private String password;

    @Size(min = 5, max = 50)
    @NotNull
    private String expert = UserUtil.DEFAULT_EXPERT;

    public UserTo() {
    }

    public UserTo(Integer id, String name, String email, String password, String expert) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.expert = expert;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setExpert(String expert) {
        this.expert = expert;
    }

    public String getExpert() {
        return expert;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", expert='" + expert + '\'' +
                '}';
    }
}
