package cz.muni.fi.pa165.currency;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Currency;



public class CurrencyConvertorImplTest {

    ExchangeRateTable mockExchangeTable = mock(ExchangeRateTable.class);
    CurrencyConvertor convertor = new CurrencyConvertorImpl(mockExchangeTable);
    Currency from = Currency.getInstance("CZK");
    Currency to = Currency.getInstance("GBP");

    @Test
    public void testConvert() throws ExternalServiceFailureException{
        testConvertStandard();
        testConvertDoubleMax();
        testConvertDoubleMin();
        testConvertProblematicRounding();
        testConvertInvalidAmount();
    }

    @Test
    public void testConvertStandard() throws ExternalServiceFailureException{
       testValidConvert(new BigDecimal("30.58"), new BigDecimal("2"), new BigDecimal("61.16"));
    }

    @Test
    public void testConvertDoubleMax() throws ExternalServiceFailureException{
        testValidConvert(new BigDecimal(Double.MAX_VALUE), new BigDecimal("2"));
    }

    @Test
    public void testConvertDoubleMin() throws ExternalServiceFailureException{
        testValidConvert(new BigDecimal(Double.MIN_VALUE), new BigDecimal("0.5"));
    }

    @Test
    public void testConvertProblematicRounding() throws ExternalServiceFailureException{
        testValidConvert(new BigDecimal("2.2"), new BigDecimal("3.0"), new BigDecimal("6.60"));
    }

    @Test
    public void testConvertInvalidAmount(){
        assertThatIllegalArgumentException().isThrownBy(() -> convertor.convert(from, to, new BigDecimal("-3.0")));
    }

    private void testValidConvert(BigDecimal rate, BigDecimal amount) throws ExternalServiceFailureException{
        when(mockExchangeTable.getExchangeRate(from, to)).thenReturn(rate);

        BigDecimal result = convertor.convert(from, to, amount);
        BigDecimal expected = rate.multiply(amount);
        assertThat(result).isEqualTo(expected);
    }

    private void testValidConvert(BigDecimal rate, BigDecimal amount, BigDecimal expected) throws ExternalServiceFailureException{
        when(mockExchangeTable.getExchangeRate(from, to)).thenReturn(rate);

        BigDecimal result = convertor.convert(from, to, amount);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testConvertWithNullSourceCurrency() throws ExternalServiceFailureException{
        when(mockExchangeTable.getExchangeRate((Currency) isNull(), any(Currency.class))).thenThrow(new IllegalArgumentException());
        assertThatIllegalArgumentException().isThrownBy(() -> convertor.convert(null, to, new BigDecimal("3.0")));
    }

    @Test
    public void testConvertWithNullTargetCurrency() throws ExternalServiceFailureException {
        when(mockExchangeTable.getExchangeRate(any(Currency.class), (Currency) isNull())).thenThrow(new IllegalArgumentException());
        assertThatIllegalArgumentException().isThrownBy(() -> convertor.convert(from, null, new BigDecimal("3.0")));
    }

    @Test
    public void testConvertWithNullSourceAmount() {
        assertThatIllegalArgumentException().isThrownBy(() -> convertor.convert(from, to, null));
    }

    @Test
    public void testConvertWithUnknownCurrency() throws ExternalServiceFailureException{
        Currency unknown = Currency.getInstance("EUR");
        when(mockExchangeTable.getExchangeRate(from, to)).thenReturn(new BigDecimal("2.3"));

        assertThatExceptionOfType(UnknownExchangeRateException.class).isThrownBy(() -> convertor.convert(unknown, to, new BigDecimal("3.0")));
    }

    @Test
    public void testConvertWithExternalServiceFailure() throws  ExternalServiceFailureException{
        when(mockExchangeTable.getExchangeRate(from, to)).thenThrow(new ExternalServiceFailureException("Testing error", new IllegalArgumentException()));

        assertThatExceptionOfType(UnknownExchangeRateException.class).isThrownBy(() -> convertor.convert(from, to, new BigDecimal("2")));
    }

}
