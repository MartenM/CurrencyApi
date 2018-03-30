package nl.martenm.currency.api.data;

import nl.martenm.currency.CurrencyPlugin;

/**
 * This class contains all the standard actions that should be undertaken when a call is made on a dataSource.
 * Implementations handle the storage of the balance.
 * Classes that want to
 * @author MartenM
 * @since 30-3-2018.
 */
public abstract class AbstractDataSource implements DataSource{

    protected CurrencyPlugin plugin;
    public AbstractDataSource(CurrencyPlugin plugin){
        this.plugin = plugin;
    }

    protected void validate(String currency){
        if(!plugin.currencyExist(currency)){
            throw new Error("Unregistered currency. Please register a currency before using it!");
        }
    }
}
