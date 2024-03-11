package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.logic.MessageRequest;
import org.example.logic.Orders;
import org.example.logic.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

/**
 * Компонент Spring, содержит методы, которые прослушивают сообщения из темы Kafka и выполняют определенные действия при получении сообщений
 */
@Component
public class KafkaListeners {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private OrderDao dao;

    /**
     * Метод прослушивает сообщения из темы "sushi-bar", размещенные на разделах с номерами "0" и "1"
     * @param data данные в формате строки, десериализует их в объект MessageRequest
     * @throws JsonProcessingException
     */
    @KafkaListener(topics = "sushi-bar", groupId = "groupId",
            topicPartitions = {@TopicPartition(topic = "sushi-bar", partitions = {"0", "1"})})
    void listenerSushiAndPizza(String data) throws JsonProcessingException {
        MessageRequest result = mapper.readValue(data, MessageRequest.class);
        Orders orders = new Orders(result.client(), result.type(), result.pay());

        dao.saveToDB(orders);
        System.out.println("Заказ №" + orders.getId() + " успешно обработан! Повар уже приступил к его изготовлению.\n" + "Клиент: " + orders.getClientName() + ", Товар: " + orders.getType() + "🍣🍕");
    }

    /**
     * Метод прослушивает сообщения из темы "sushi-bar", размещенные на разделах с номерами "2"
     * @param data данные в формате строки, десериализует их в объект MessageRequest
     * @throws JsonProcessingException
     */
    @KafkaListener(topics = "sushi-bar", groupId = "groupId",
            topicPartitions = {@TopicPartition(topic = "sushi-bar", partitions = {"2"})})
    void listenerDifferentOrders(String data) throws JsonProcessingException {
        MessageRequest result = mapper.readValue(data, MessageRequest.class);
        Orders orders = new Orders(result.client(), result.type(), result.pay());

        if (Orders.isOrderCorrect(orders)) {
            dao.saveToDB(orders);
            System.out.println("Заказ №" + orders.getId() + " успешно обработан! Повар уже приступил к его изготовлению.\n" + "Клиент: " + orders.getClientName() + ", Товар: " + orders.getType() + "✨");
        }
        else {
            System.out.println("К сожалению, данный товар больше не доступен в нашем магазине . . .");
        }
    }
}
