# Graphics

Coroutines, Retrofit, Gson, MPAndroidChart, Runtime-permission-kotlin, ViewModel

Тестовое задание для Android разработчика
Требуется написать на Kotlin или Java мобильное приложение для Android, которое запрашивает у сервера определённое количество координат точек (x, y), а затем отображает полученный ответ в виде таблицы и графика. 
На главном экране имеется блок информационного текста, поле для ввода числа точек и одна кнопка «Поехали». По нажатию на кнопку осуществляется POST запрос на сервер(https://demo.bankplus.ru/mobws/json/pointsList), внутри которого содержится информация о количестве запрашиваемых точек (count) и параметр “version=1.1”. Сервер выдаёт ответ в JSON формате, пример:
{"result":0,"response":{"points":[{"x":1.23, "y":2.44},{"x":2.17, "y":3.66}]}}
"result":0  - означает, что запрос обработан без ошибок, -100 – неверные параметры запроса, -1 – при остальных ошибках. При ошибке с кодом -1 в response содержится объект message с текстом причины ошибки, закодированным в base64. Ошибочную ситуацию нужно уметь обрабатывать и выводить всплывающее окно с текстом ошибки.
Если ответ от сервера получен, то на новом экране должна отобразиться таблица с полученными координатами точек. Ниже должен быть отображен график с точками, соединёнными прямыми линиями. Точки на графике должны следовать по возрастанию координаты x.
Дополнительно можно осуществить следующие возможности работы с графиком:
изменения масштаба пользователем
соединение точек не ломаной линией, а сглаженной
работа в портретной и ландшафтной ориентации экрана
сохранение изображения графика в файл
