package ru.suleymanovtat.graphics.model

//TODO -100 – неверные параметры запроса, -1 – при остальных ошибках.
//{
//    "response": {
//    "result": -100,
//    "message": "wrong params"
//}
//}
//todo "result":0  - означает, что запрос обработан без ошибок
//{
//    "result": 0,
//    "response": {
//    "points": [
//    {
//        "x": "6.0692",
//        "y": "46.8902"
//    },
//    {
//        "x": "26.6",
//        "y": "-16.78"
//    },


data class WrapperPoint(
    val result: Int? = null,
    val message: String? = null,
    val response: Response? = null
)

data class Response(
    val points: List<Point>? = null,
    val result: Int? = null,
    val message: String? = null
)

data class Point(
    val x: Float? = null,
    val y: Float? = null
)