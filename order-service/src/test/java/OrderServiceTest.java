import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkeycrew.Order;
import com.turkeycrew.OrderItem;
import com.turkeycrew.OrderStatus;
import com.turkeycrew.OrderRepository;
import com.turkeycrew.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void processOrderTest() {
        // Prepare test data
        OrderItem item1 = OrderItem.builder().itemName("Item1").quantity(2).price(10.0).build();
        OrderItem item2 = OrderItem.builder().itemName("Item2").quantity(3).price(15.0).build();

        Order orderRequest = Order.builder()
                .userId(1)
                .items(Arrays.asList(item1, item2))
                .build();

        // Mock the save operation
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setOrderId(1); // Set a mock order ID
            return savedOrder;
        });

        // Invoke the method
        orderService.processOrder(orderRequest);

        // Verify that the order was saved with any instance of Order
        verify(orderRepository).save(any(Order.class));
    }

}