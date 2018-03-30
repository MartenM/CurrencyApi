package nl.martenm.currency.api.data;

import java.util.UUID;

/**
 * @author MartenM
 * @since 29-3-2018.
 */
public interface DataSource{

    /**
     * Register an currency. This should always be done before trying to acces it.
     * @param currency
     */
    void registerCurrency(String currency);

    /**
     * Get the balance of a certain currency for a certain player.
     * @param currency The id of the currency.
     * @param player
     * @return
     */
    int getBalance(String currency, UUID player);

    /**
     * Give a certain amount of currency to a player.
     * @param currency The id of the currency
     * @param player
     * @param amount
     */
    boolean giveBalance(String currency, UUID player, int amount);

    /**
     * Subtract a certain amount of currency from a player.
     * @param currency
     * @param player
     * @param amount
     */
    boolean payBalance(String currency, UUID player, int amount);

    /**
     * Set the balance for a certain currency for a player.
     * Use with caution!
     * @param currency
     * @param player
     * @param amount
     */
    boolean setBalance(String currency, UUID player, int amount);

    /**
     * Update the players name in the database.
     * Usually only used on the PlayerJoinEvent.
     *
     * <STRONG>NOTE:</STRONG> This feature is currently not being used yet but I aiming to make it possible to set/get players currencies using this later on.
     * @param player
     * @param name
     */
    void updateName(UUID player, String name);
}
