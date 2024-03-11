# Produser и consumer для Kafka + хранилище Redis

Для успешной работы системы необходимо обеспечить работу:
- zookeeper на порту: 2181
- kafka на порту: 9092
- redis на порту 6379

## Описание приложения

Приложение эмулирует работу магазина, принимающего заказы на еду. Заказы на суши поступают в количестве 1/3 от всех заказов, как и заказы на пиццу, а заказы на все остальные товары в сумме составляют 1/3 от всех заказов.
- 1 topic kafka
- 3 partitions (для заказов на суши, пиццу и других товаров соответственно)

## Описание работы приложения

1. Перед формированием заказа производится проверка его на корректность, только после этого данные отправляются в очередь kafka
2. Как только данные о заказе приходят, производится проверка на наличие продукта.
3. Если продукт доступен для заказа, данные о нем отправляются в хранилище Redis

## Тестирование

Написан producer на Jmeter для эмулирования нагрузки (файлы находятся в resourses)

![TPS](https://github.com/egorDementev/kafka-project/assets/57751210/190403ec-ea0d-4d78-a92e-b0678e25269b)
