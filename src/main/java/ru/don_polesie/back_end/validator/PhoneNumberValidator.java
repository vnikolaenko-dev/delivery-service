package ru.don_polesie.back_end.validator;

public class PhoneNumberValidator {

    /**
     * Проверяет корректность номера телефона в российском формате
     * @param phoneNumber номер телефона в виде строки
     * @return true если номер корректен, false если нет
     */
    public static boolean isValidRussianPhone(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }

        // Убираем все нецифровые символы
        String cleaned = phoneNumber.replaceAll("[^\\d]", "");

        // Проверяем: начинается с 7 и всего 11 цифр
        return cleaned.matches("^7\\d{10}$");
    }
}
