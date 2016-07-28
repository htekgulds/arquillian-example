package tr.gov.tuik.arquillian.advanced;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by studentA on 7/28/2016.
 */
@Local
public interface OrderRepository {

    void addOrder(List<String> order);

    List<List<String>> getOrders();

    int getOrderCount();
}
