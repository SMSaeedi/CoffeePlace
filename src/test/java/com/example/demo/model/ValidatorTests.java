package com.example.demo.model;

import com.example.demo.dao.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ValidatorTests {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test
    void shouldNotValidateWhenNameEmpty() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Customer customer = Customer.builder()
                .name("")
                .email("invalid")
                .build();

        Validator validator = createValidator();
        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);
        assertThat(constraintViolations).hasSize(2);

        List<ConstraintViolation<Customer>> list = new ArrayList<>(constraintViolations);
        int i = 0;
        for (ConstraintViolation<Customer> c : list) {
            assertThat(list.get(i).getPropertyPath().toString()).isEqualTo(c.getPropertyPath().toString());
            assertThat(list.get(i).getMessage()).isEqualTo(c.getMessage());
            i++;
        }
    }

    @Test
    void shouldNotValidateWhenEmailIncorrect() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Customer customer = Customer.builder()
                .name("valid")
                .email("invalidFormat")
                .build();

        Validator validator = createValidator();
        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);
        assertThat(constraintViolations).hasSize(1);

        ConstraintViolation<Customer> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo(violation.getPropertyPath().toString());
        assertThat(violation.getMessage()).isEqualTo("email must be a valid email address");
    }
}
