package org.example.logic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Представление запроса сообщения
 * @param client информация о клиенте (его имя)
 * @param type тип заказанной продукции
 * @param pay способ платежа
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageRequest(String client, String type, String pay) {
}