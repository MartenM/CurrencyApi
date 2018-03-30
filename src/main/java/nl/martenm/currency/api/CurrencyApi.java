package nl.martenm.currency.api;

import nl.martenm.currency.CurrencyPlugin;
import nl.martenm.currency.api.data.DataSource;
import nl.martenm.currency.api.data.actions.DataAction;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * The CurrencyAPI. This is the class you should be using for all interactions with the CurrencyApi.
 * The methods for getting and setting balances here are done Async and the result is returned Synced.
 * @author MartenM
 * @since 29-3-2018.
 */
public class CurrencyApi {

    private CurrencyPlugin plugin;
    private CurrencyApi (CurrencyPlugin plugin){
        this.plugin = plugin;
    }

    protected DataSource getDataSource(){
        return plugin.getDataSource();
    }

    /**
     * Register a new currency. This should always be done before using new currencies.
     * @param currency The name of the currency.
     * @return Returns false if exists already.
     */
    public boolean registerCurrency(String currency){
        if(plugin.currencyExist(currency)){
            return false;
        }
        getDataSource().registerCurrency(currency);
        plugin.addCurrency(currency);
        return true;
    }

    /**
     * Get a players balance using the DataAction object. Calling .queue(Consumer) will make execute the query async. The result can be use in the consumer.
     * @param currency
     * @param player
     * @return
     */
    public DataAction<Integer> getBalance(String currency, UUID player) {
        return new DataAction<Integer>(plugin) {
            @Override
            public void queue(Consumer<Integer> succes) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        int balance = getDataSource().getBalance(currency, player);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                succes.accept(balance);
                            }
                        }.runTask(plugin);
                    }
                }.runTaskAsynchronously(plugin);
            }
        };
    }

    /**
     * Set a players balance Async.
     * @param currency
     * @param player
     * @param amount
     * @return
     */
    public DataAction<Boolean> setBalance(String currency, UUID player, int amount){
        return new DataAction<Boolean>(plugin) {
            @Override
            public void queue(Consumer<Boolean> succes) {
                // Go off-sync and execute the update.
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        boolean result = getDataSource().setBalance(currency, player, amount);

                        // Go back to synced and consume the result.
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                succes.accept(result);
                            }
                        }.runTask(plugin);
                    }
                }.runTaskAsynchronously(plugin);
            }
        };
    }

    /**
     * Add currency to a players balance.
     * @param currency
     * @param player
     * @param amount
     * @return
     */
    public DataAction<Boolean> addBalance(String currency, UUID player, int amount){
        return new DataAction<Boolean>(plugin) {
            @Override
            public void queue(Consumer<Boolean> succes) {
                // Go off-sync and execute the update.
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        boolean result = getDataSource().payBalance(currency, player, amount);

                        // Go back to synced and consume the result.
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                succes.accept(result);
                            }
                        }.runTask(plugin);
                    }
                }.runTaskAsynchronously(plugin);
            }
        };
    }

    /**
     * Remove currency from a players balance.
     * @param currency
     * @param player
     * @param amount
     * @return
     */
    public DataAction<Boolean> removeBalance(String currency, UUID player, int amount){
        return new DataAction<Boolean>(plugin) {
            @Override
            public void queue(Consumer<Boolean> succes) {
                // Go off-sync and execute the update.
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        boolean result = getDataSource().setBalance(currency, player, amount);

                        // Go back to synced and consume the result.
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                succes.accept(result);
                            }
                        }.runTask(plugin);
                    }
                }.runTaskAsynchronously(plugin);
            }
        };
    }

    /**
     * An easy method to grab this API.
     * @return
     */
    public static CurrencyApi getApi(){
        if(Bukkit.getServer().getPluginManager().getPlugin("CurrencyApi") == null){
            throw new RuntimeException("Could not get the currencyAPI. The plugin could not be found!");
        }

        CurrencyPlugin plugin = (CurrencyPlugin) Bukkit.getServer().getPluginManager().getPlugin("CurrencyApi");
        return new CurrencyApi(plugin);
    }
}
