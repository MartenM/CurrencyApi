package nl.martenm.currency.listeners;

import nl.martenm.currency.CurrencyPlugin;
import nl.martenm.currency.api.currency.Currency;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author MartenM
 * @since 30-3-2018.
 */
public class PlayerJoinListener implements Listener {

    private CurrencyPlugin currencyPlugin;

    public PlayerJoinListener(CurrencyPlugin currencyPlugin){
        this.currencyPlugin = currencyPlugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void joinListener(PlayerJoinEvent event){
        new BukkitRunnable() {
            @Override
            public void run() {
                currencyPlugin.getDataSource().updateName(event.getPlayer().getUniqueId(), event.getPlayer().getName());
            }
        }.runTaskAsynchronously(currencyPlugin);
    }
}
