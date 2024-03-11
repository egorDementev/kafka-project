package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.logic.MessageRequest;
import org.example.logic.Orders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * Контроллер, который обрабатывает HTTP-запросы для создания заказов и отправки сообщений в Apache Kafka
 */
@RestController
@RequestMapping("api/make-order")
public class MessageController {

    public MessageController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Метод проверяет корректность данных в объекте MessageRequest
     * @param request запрос
     * @return true, если клиент, тип и способ оплаты указаны, и способ оплаты является либо "cash", либо "card"
     */
    private boolean checkData(MessageRequest request) {
        if (request.client() == null || request.type() == null || request.pay() == null)
            return false;
        return request.pay().equals("cash") || request.pay().equals("card");
    }

    /**
     * Метод обрабатывает HTTP POST запросы на эндпоинт /api/make-order, получает объект MessageRequest из тела запроса и проверяет его на корректность с помощью метода checkData.
     * Если данные корректны, он преобразует объект MessageRequest в строку JSON с помощью ObjectMapper, а затем отправляет сообщение в брокер Kafka, в зависимости от типа заказа.
     * @param request запрос
     * @return сообщение о результате операции в виде строки
     * @throws JsonProcessingException
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public String publish(@Valid @RequestBody MessageRequest request) throws JsonProcessingException {
        if (checkData(request)) {
            String message = mapper.writeValueAsString(request);

            if (Objects.equals(request.type(), "sushi")) {
                kafkaTemplate.send("sushi-bar", 0, null, message);
            } else if (Objects.equals(request.type(), "pizza")) {
                kafkaTemplate.send("sushi-bar", 1, null, message);
            } else {
                kafkaTemplate.send("sushi-bar", 2, null, message);
            }

            return "Ваш заказ №" + Orders.getIdentifier() + " успешно создан и отправлен на обработку!";
        }
        return "Данные введены не корректно, мы не можем создать ваш заказ . . .";
    }
}
