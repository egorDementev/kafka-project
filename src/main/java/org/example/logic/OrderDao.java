package org.example.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс представляет объект доступа к данным для работы с объектами заказов Orders. Используется для взаимодействия с базой данных Redis, где хранятся объекты класса Orders
 */
@Repository
public class OrderDao {

    private static final String HASH_KEY = "Orders";
    @Autowired
    private RedisTemplate<String, Object> template;

    /**
     * Метод сохраняет объект заказа Orders в базу данных Redis, использует RedisTemplate, который предоставляет доступ к различным операциям Redis, таким как сохранение объектов в виде хэш-значений
     * @param orders заказ
     */
    public void saveToDB(Orders orders) {
        template.opsForHash().put(HASH_KEY, orders.getId(), orders);
    }

    /**
     * Метод возвращает список всех объектов заказов, хранящихся в базе данных Redis, использует операцию values() для получения всех значений из хэша, где ключом является HASH_KEY
     * @return лист всех сделанных заказов
     */
    public List<Object> findAll() {
        return template.opsForHash().values(HASH_KEY);
    }

    /**
     * Метод ищет объект заказа по его идентификатору id, использует операцию get() для получения значения из хэша по указанному ключу
     * @param id уникальный идентификатор заказа
     * @return объект класса Orders - заказ соответствующий данному идентификатору
     */
    public Orders findProductById(Integer id) {
        return (Orders) template.opsForHash().get(HASH_KEY, id);
    }

    /**
     * Метод удаляет объект заказа из базы данных Redis по его идентификатору id, использует операцию delete() для удаления указанного ключа и его значения из хэша
     * @param id уникальный идентификатор заказа
     * @return строку с успешным результатом
     */
    public String deleteProduct(Integer id) {
        template.opsForHash().delete(HASH_KEY, id);
        return "Order was successfully removed!";
    }

}
