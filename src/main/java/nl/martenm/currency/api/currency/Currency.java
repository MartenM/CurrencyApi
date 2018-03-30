package nl.martenm.currency.api.currency;

/**
 * @author MartenM
 * @since 30-3-2018.
 */
public class Currency {

    private String name;

    public Currency(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
