package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertor;
import cz.muni.fi.pa165.currency.ExchangeRateTable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.Currency;

public class MainXml {

    public static void main(String[] args) {
        ApplicationContext context  = new ClassPathXmlApplicationContext("applicationContext.xml");
        ExchangeRateTable exchangeRateTable = context.getBean("exchangeRateTable", ExchangeRateTable.class);
        CurrencyConvertor currencyConvertor = context.getBean("currencyConvertor", CurrencyConvertor.class);

        BigDecimal rate = currencyConvertor.convert(Currency.getInstance("EUR"), Currency.getInstance("CZK"), new BigDecimal("1"));
        System.out.println(rate);
    }

}
