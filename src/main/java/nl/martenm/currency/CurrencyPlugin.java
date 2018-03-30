package nl.martenm.currency;

import nl.martenm.currency.api.CurrencyApi;
import nl.martenm.currency.api.currency.Currency;
import nl.martenm.currency.api.data.DataSource;
import nl.martenm.currency.api.data.sources.MySql;
import nl.martenm.currency.commands.CurrencyCommand;
import nl.martenm.currency.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author MartenM
 * @since 29-3-2018.
 */
public class CurrencyPlugin extends JavaPlugin {

    private DataSource dataSource;
    private Logger logger;

    private CurrencyApi currencyApi;

    public static boolean DEBUG = true;

    private Map<String, Currency> currencies;

    @Override
    public void onEnable() {
        this.logger = getServer().getLogger();

        dataSource = new MySql(this);

        // Normal startup
        saveDefaultConfig();
        getCommand("currency").setExecutor(new CurrencyCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        currencies = new HashMap<>();
        CurrencyApi.getApi().registerCurrency("money");

        currencyApi = CurrencyApi.getApi();
        logger.info("Currency API is ready to be used.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public boolean currencyExist(String name){
        return currencies.containsKey(name);
    }

    public void addCurrency(String name){
        currencies.put(name, new Currency(name));
    }

    public CurrencyApi getCurrencyApi() {
        return currencyApi;
    }
}
