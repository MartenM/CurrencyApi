package nl.martenm.currency.api.data.sources;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import nl.martenm.currency.CurrencyPlugin;
import nl.martenm.currency.api.data.AbstractDataSource;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

/**
 * The MySql DataSource for the currencyApi.
 * @author MartenM
 * @since 30-3-2018.
 */
public class MySql extends AbstractDataSource {

    private MysqlDataSource dataSource;
    private static final String MAIN_TALBE = "currencies";

    public MySql(CurrencyPlugin currencyPlugin){
        super(currencyPlugin);
        setup();
    }

    /**
     * Initial setup. Create data source etc..
     */
    private void setup(){
        dataSource = new MysqlDataSource();
        dataSource.setUser(plugin.getConfig().getString("mysql.user"));
        dataSource.setPassword(plugin.getConfig().getString("mysql.password"));
        dataSource.setServerName(plugin.getConfig().getString("mysql.address"));
        dataSource.setDatabaseName(plugin.getConfig().getString("mysql.database"));

        if(!simpleSqlUpdate("CREATE TABLE IF NOT EXISTS " + MAIN_TALBE + " ( `id` INT(6) NOT NULL AUTO_INCREMENT , " +
                "`uuid` VARCHAR(36) NOT NULL , " +
                "`name` VARCHAR(32), " +
                "PRIMARY KEY (`id`));")){
            throw new Error("Could not create currencies table.");
        }
    }

    /**
     * Method used for creating prepared statements and executing those on the database.
     * @param sql A normal SQL query string.
     * @param args Params that replace the ? in the query.
     * @return result
     */
    private boolean simpleSqlUpdate(String sql, Object... args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for(int i = 0; i < args.length; i++){
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            if(CurrencyPlugin.DEBUG)
                e.printStackTrace();
            return false;
        } finally {
            try{
                if(connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return true;
    }

    @Override
    public void registerCurrency(String currency) {
        if(!simpleSqlUpdate("ALTER TABLE " + MAIN_TALBE + " ADD " + currency + " INT(16) NOT NULL DEFAULT '0'")){
            // Collum already existed. No major changes.
        }
        // A new currency row has been created.
    }

    @Override
    public int getBalance(String currency, UUID player) {
        validate(currency);

        int balance = 0;

        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;

        try {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("select ? from " + MAIN_TALBE + " where uuid = ?");
            preparedStatement.setString(1, currency);
            preparedStatement.setString(2, player.toString());

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                balance = resultSet.getInt(currency);
            }

        } catch (SQLException e) {
           throw new Error(e);
        }

        return balance;
    }

    @Override
    public boolean giveBalance(String currency, UUID player, int amount) {
        validate(currency);

        return simpleSqlUpdate("INSERT INTO " + MAIN_TALBE + " (uuid, " + currency + ") VALUES(?, ?) ON DUPLICATE KEY UPDATE " + currency + " = " + currency + "+ ?;", player.toString(), amount, amount);

        }

    @Override
    public boolean payBalance(String currency, UUID player, int amount) {
        validate(currency);

        return simpleSqlUpdate("INSERT INTO " + MAIN_TALBE + " (uuid, " + currency + ") VALUES(?, ?) ON DUPLICATE KEY UPDATE " + currency + " = " + currency + "- ?;", player.toString(), amount, amount);
    }

    @Override
    public boolean setBalance(String currency, UUID player, int amount) {
        validate(currency);

        return simpleSqlUpdate("INSERT INTO " + MAIN_TALBE + " (uuid, " + currency + ") VALUES(?, ?) ON DUPLICATE KEY UPDATE " + currency + " = ?;", player.toString(), amount, amount);
    }

    @Override
    public void updateName(UUID player, String name) {
        simpleSqlUpdate("UPDATE " + MAIN_TALBE + " SET name = ? where uuid = ?", name, player.toString());
    }
}
