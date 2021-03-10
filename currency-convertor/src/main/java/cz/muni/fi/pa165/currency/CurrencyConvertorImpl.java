package cz.muni.fi.pa165.currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Currency;


/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private final ExchangeRateTable exchangeRateTable;
    private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount){
        logger.trace("CurrencyConvertorImpl.convert() was called");

        // Verify parameters (sourceCurrency and targetCurrency verified by exchangeRateTable)
        verifyParams(sourceAmount);

        // get exchange rate
        BigDecimal rate;
        try{
            rate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);
        }catch (ExternalServiceFailureException e){
            logger.error("Conversion failure between currencies {} and {}", sourceCurrency, targetCurrency);
            throw new UnknownExchangeRateException(e.getMessage(), e);
        }

        if(rate == null){
            String message = String.format("Unknown exchange rate for currencies %s and %s", sourceCurrency.getCurrencyCode(), targetCurrency.getCurrencyCode());
            logger.warn(message);
            throw new UnknownExchangeRateException(message);
        }

        return rate.multiply(sourceAmount);
    }

    private void verifyParams( BigDecimal sourceAmount){
        if(sourceAmount == null){
            throw new IllegalArgumentException("sourceAmount cannot be null");
        }

        boolean sourceAmountIsNegative = (sourceAmount.compareTo(new BigDecimal("0")) < 0);
        if(sourceAmountIsNegative){
            throw new IllegalArgumentException("sourceAmount cannot be a negative value");
        }
    }

}
