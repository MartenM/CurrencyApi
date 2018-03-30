package nl.martenm.currency.api.data.actions;

import nl.martenm.currency.CurrencyPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.function.Consumer;

/**
 * @author MartenM
 * @since 30-3-2018.
 */
public abstract class DataAction<T>{

    private CurrencyPlugin currencyPlugin;
    public DataAction(CurrencyPlugin currencyPlugin){
        this.currencyPlugin = currencyPlugin;
    }

    public void queue(Consumer<T> succes){
        throw new NotImplementedException();
    }
}
