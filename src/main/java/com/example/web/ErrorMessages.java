package com.example.web;

import jakarta.transaction.SystemException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import javax.transaction.xa.XAException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class ErrorMessages {
    private static final Logger LOG = Logger.getLogger(ErrorMessages.class.getName());
    private static final String DB_UNAVAILABLE = "База данных недоступна. Попробуйте позже";

    private ErrorMessages() {
    }

    public static String describe(Throwable e) {
        LOG.log(Level.SEVERE, "Необработанная ошибка", e);

        for (Throwable t = e; t != null; t = next(t)) {
            if (t instanceof ConstraintViolationException cve) {
                return "Некорректные данные: " + cve.getConstraintViolations().stream()
                        .map(ErrorMessages::violationText)
                        .collect(Collectors.joining("; "));
            }
            if (t instanceof SQLException sql && sql.getSQLState() != null) {
                String text = sqlStateText(sql.getSQLState());
                if (text != null) {
                    return text;
                }
            }
            if (t instanceof SystemException || t instanceof XAException) {
                return DB_UNAVAILABLE;
            }
        }
        return "Внутренняя ошибка сервера. Попробуйте позже";
    }

    private static Throwable next(Throwable t) {
        Throwable cause = t.getCause();
        return cause == t ? null : cause;
    }

    private static String violationText(ConstraintViolation<?> v) {
        return v.getPropertyPath() + " — " + v.getMessage();
    }

    private static String sqlStateText(String state) {
        if (state.startsWith("08")) {
            return DB_UNAVAILABLE;
        }
        return switch (state) {
            case "23505" -> "Такой объект уже существует";
            case "23503" -> "Связанный объект не найден или используется другими объектами";
            case "23502" -> "Не заполнено обязательное поле";
            case "23514" -> "Значение не прошло проверку базы данных";
            default -> null;
        };
    }
}
