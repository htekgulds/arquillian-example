package tr.gov.tuik.ejb.bean.advanced;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by studentA on 7/28/2016.
 */
@SessionScoped
public class Basket implements Serializable {

    private List<String> items;

    @EJB
    private OrderRepository repo;

    public void addItem(String item) {
        items.add(item);
    }

    public List<String> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int getItemCount() {
        return items.size();
    }

    public void placeOrder() {
        repo.addOrder(items);
        items.clear();
    }

    @PostConstruct
    void initialize() {
        items = new ArrayList<>();
    }
}
