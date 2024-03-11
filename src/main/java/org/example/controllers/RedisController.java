package org.example.controllers;

import org.example.logic.Orders;
import org.example.logic.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер, который обрабатывает HTTP-запросы, связанные с операциями для объектов заказов в базе данных Redis
 */
@RestController
@RequestMapping("api/data-base")
public class RedisController {
    @Autowired
    private OrderDao dao;

    /**
     * Метод обрабатывает HTTP GET запросы на эндпоинт /api/data-base/get-all-orders, вызывает метод findAll() у OrderDao для получения списка всех заказов из базы данных Redis
     * @return возвращает этот список в виде ответа на запрос.
     */
    @GetMapping("/get-all-orders")
    public List<Object> getAllOrders() {
        return dao.findAll();
    }

    /**
     * Этот метод обрабатывает HTTP GET запросы на эндпоинт /api/data-base/find-{id}, вызывает метод findProductById(id) у OrderDao, чтобы найти конкретный заказ по его идентификатору
     * @param id это переменная пути, содержащая идентификатор заказа
     * @return возвращает заказ в виде ответа
     */
    @GetMapping("/find-{id}")
    public Orders findOrder(@PathVariable(name = "id") Integer id) {
        return dao.findProductById(id);
    }

    /**
     * Метод обрабатывает HTTP DELETE запросы на эндпоинт /api/data-base/delete-{id}, вызывает метод deleteProduct(id) у OrderDao, чтобы удалить конкретный заказ из базы данных Redis
     * @param id это переменная пути, содержащая идентификатор заказа
     * @return сообщение об успешном удалении заказа
     */
    @DeleteMapping("/delete-{id}")
    public String remove(@PathVariable(name = "id") Integer id) {
        return dao.deleteProduct(id);
    }

}
