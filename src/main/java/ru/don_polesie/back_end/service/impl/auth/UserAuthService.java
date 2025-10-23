package ru.don_polesie.back_end.service.impl.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.don_polesie.back_end.dto.auth.JwtAuthResponse;
import ru.don_polesie.back_end.model.Role;
import ru.don_polesie.back_end.model.User;
import ru.don_polesie.back_end.repository.RoleRepository;
import ru.don_polesie.back_end.repository.UserRepository;
import ru.don_polesie.back_end.utils.SmsSenderHttpClient;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class UserAuthService {
    private final Random random = new Random();
    private final Map<String, CodeEntry> codes = new ConcurrentHashMap<>();
    private final JWTGeneratorService jwtGeneratorService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Отправка временного пароля
     */
    public void sendTemporaryPassword(String number) {
        String code = generate4LetterCode();
        codes.put(number, new CodeEntry(code, System.currentTimeMillis() + 5 * 60 * 1000));
        SmsSenderHttpClient.sendSms(number, "Ваш код для входа: " + code);
    }

    /**
     * Проверка временного пароля
     */
    public JwtAuthResponse checkTemporaryPassword(String number, String code) {
        CodeEntry entry = codes.get(number);
        if (entry != null && entry.code.equals(code) && System.currentTimeMillis() < entry.expireAt) {
            // Если номера нет в бд - сохраняем нового пользователя
            if (userRepository.findByPhoneNumber(number).isEmpty()) {
                System.out.println("SAVE USER");
                save(number, code);
            }
            System.out.println(number + " " + code);
            // codes.remove(number);
            return jwtGeneratorService.generateJWT(number, code); // пример, подставь свою реализацию
        }
        // codes.entrySet().removeIf(e -> System.currentTimeMillis() > e.getValue().expireAt);
        return null;
    }

    private final PasswordEncoder passwordEncoder;

    /**
     * Регистрация нового пользователя
     */
    @Transactional
    public void save(String phoneNumber, String code) {
        var user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setPassword(passwordEncoder.encode(code));

        // Назначение роли USER по умолчанию
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        user.setRoles(roles);


        userRepository.save(user);
    }

    private String generate4LetterCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            char letter = (char) ('A' + random.nextInt(26));
            code.append(letter);
        }
        return code.toString();
    }

    private static class CodeEntry {
        String code;
        long expireAt;
        CodeEntry(String code, long expireAt) {
            this.code = code;
            this.expireAt = expireAt;
        }
    }
}

