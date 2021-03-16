package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainAnnotations {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("cz.muni.fi.pa165");

        CurrencyConvertor convertor = context.getBean(CurrencyConvertor.class);

        BigDecimal result = convertor.convert(Currency.getInstance("EUR"), Currency.getInstance("CZK"), new BigDecimal("1"));
        System.out.println(result);
    }
}
