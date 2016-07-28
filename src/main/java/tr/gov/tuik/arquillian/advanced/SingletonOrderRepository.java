package tr.gov.tuik.arquillian.advanced;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by studentA on 7/28/2016.
 */
@Singleton
@Lock(LockType.READ)
public class SingletonOrderRepository implements OrderRepository {
    private List<List<String>> orders;

    @Override
    @Lock(LockType.WRITE)
    public void addOrder(List<String> order) {
        orders.add(order);
    }

    @Override
    public List<List<String>> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    @Override
    public int getOrderCount() {
        return orders.size();
    }

    @PostConstruct
    void initialize() {
        orders = new ArrayList<>();
    }
}
