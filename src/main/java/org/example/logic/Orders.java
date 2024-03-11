package org.example.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Класс отвечает за представление заказа в магазине, имеет постоянный serialVersionUID
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Orders")
public class Orders implements Serializable {
    static final long serialVersionUID = 42L;
    @Getter
    private static Integer identifier = 0;
    private static Random rand = new Random();
    private static List<String> availableFood = new ArrayList<>();
    @Id
    private Integer id;
    private String clientName;
    private String type;
    private Integer price;
    private String payBy;

    public Orders(String clientName, String type, String payBy) {
        this.price = rand.nextInt(100, 1000);
        this.clientName = clientName;
        this.id = identifier;
        this.type = type;
        this.payBy = payBy;
        identifier += 1;
    }

    /**
     * Проверка на корректность заказа (доступен ли заказанный продукт в магазине)
     * @param orders заказ сделанный пользователем
     * @return true/false в зависимости от результата
     */
    public static boolean isOrderCorrect(Orders orders) {
        return availableFood.contains(orders.getType());
    }

    /**
     * Функция заполняет массив доступных продуктов данными из файла, это происходит 1 раз при запуске программы
     */
    public static void fillAvailableFoodList() {
        try {
            File file = ResourceUtils.getFile("classpath:available_food.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineWords = line.split(" ");
                availableFood.addAll(Arrays.asList(lineWords));
            }

            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
