package cz.muni.fi.pa165.currency;

import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.Currency;

public class ExchangeRateTableImplTest {

    ExchangeRateTable table = new ExchangeRateTableImpl();

    @Test
    public void testEurToCzk27(){
        Currency eur = Currency.getInstance("EUR");
        Currency czk = Currency.getInstance("CZK");

        BigDecimal rate = null;
        try{
            rate = table.getExchangeRate(eur, czk);
        }catch (Exception e){
            fail();
        }
        assertNotNull(rate);
        assertEquals(rate.compareTo(new BigDecimal("27")), 0);
    }

    @Test
    public void testNonEurToCzkNull(){
        Currency eur = Currency.getInstance("EUR");
        Currency czk = Currency.getInstance("CZK");

        BigDecimal rate = null;
        try{
            rate = table.getExchangeRate(czk, eur);
        }catch (Exception e){
            fail();
        }
        assertNull(rate);
    }
}
