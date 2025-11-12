package ru.don_polesie.back_end.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.don_polesie.back_end.model.Role;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDtoResponse {
    public int id;
    public String name;
    public String surname;
    public String email;
    public String password;
    public String phoneNumber;
    public Set<Role> roles;
}
