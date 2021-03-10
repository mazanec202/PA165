package cz.muni.fi.pa165.currency;

import java.math.BigDecimal;
import java.util.Currency;


/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private final ExchangeRateTable exchangeRateTable;
    //private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }

    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount){

        // Verify parameters (sourceCurrency and targetCurrency verified by exchangeRateTable)
        verifyParams(sourceAmount);

        // get exchange rate
        BigDecimal rate;
        try{
            rate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);
        }catch (ExternalServiceFailureException e){
            throw new UnknownExchangeRateException(e.getMessage(), e);
        }

        if(rate == null){
            throw new UnknownExchangeRateException("Unknown exchange rate for currencies "
                    + sourceCurrency.getCurrencyCode() + " and " + targetCurrency.getCurrencyCode());
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
