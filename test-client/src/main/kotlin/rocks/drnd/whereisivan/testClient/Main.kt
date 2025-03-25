package rocks.drnd.whereisivan.testClient

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

fun main(args: Array<String>) {
    if (args.isEmpty() || args.size < 2) {
        println("Usage: Provide the path to the GPX file as a first argument and the backend URL as the second argument")
        return
    }

    val inputFilePath = args[0]
    val inputFile = File(inputFilePath)
    val url = args[1]

    if (!inputFile.exists() || !inputFile.isFile) {
        println("The provided input path is not a valid file: $inputFilePath")
        return
    }

    println("Processing file: $inputFilePath")
    parseGpxFile(inputFile, url)
}


fun toEpochMillis(time: String): Long {
    val pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val dateTime = LocalDateTime.parse(time, formatter)
    return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
}

suspend fun createActivity(client: HttpClient, backendUrl: String): String {

    val response: HttpResponse = client.post("$backendUrl/activity") {
        contentType(ContentType.Application.Json)

        accept(ContentType.Application.Json)
        setBody(
            "{" +
                    "\"startTime\": ${System.currentTimeMillis()}}"
        )
    }
    return response.body()
}

fun sendLocation(lat: Double, lon: Double, client: HttpClient, url: String) {
    val response = runBlocking() {
        client.post(url) {
            contentType(ContentType.Application.Json)

            accept(ContentType.Application.Json)
            setBody(
                "[{" +
                        "\"latitude\": $lat," +
                        "\"longitude\": $lon," +
                        "\"timeStamp\": ${System.currentTimeMillis()}}]"
            )
        }
    }
    println("Location sent: lat=$lat, lon=$lon, url=$url response=${response.status}")
}

fun parseGpxFile(file: File, backendUrl: String) {
    val client = HttpClient(CIO)
    val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val document = documentBuilder.parse(file)
    document.documentElement.normalize()
    val activityId = runBlocking() {
        createActivity(client, backendUrl)
    }
    println("Activity created: $activityId")
    val nodeList = document.getElementsByTagName("trkpt")
    var prevMillis = 0L
    for (i in 0 until nodeList.length) {
        val node = nodeList.item(i)
        if (node.nodeType == Node.ELEMENT_NODE) {
            val element = node as Element
            val lat = element.getAttribute("lat")
            val lon = element.getAttribute("lon")
            val time = toEpochMillis(element.getElementsByTagName("time").item(0).textContent)
            if (prevMillis != 0L) {
                val diff = time - prevMillis
                prevMillis = time
                Thread.sleep(diff)
            } else {
                prevMillis = time
            }
            sendLocation(lat.toDouble(), lon.toDouble(), client, "$backendUrl/activity/$activityId/track")
        }
    }
    stopActivity(client, backendUrl, activityId)
    println("The Activity is finished")
    client.close()
}

fun stopActivity(client: HttpClient, backendUrl: String, activityId: String) {
    runBlocking() {
        val response = client.post("$backendUrl/activity/stop") {
            contentType(ContentType.Application.Json)

            accept(ContentType.Application.Json)
            setBody(
                "{" +
                        "\"activityId\": \"$activityId\"}"
            )
        }
        println("Activity stopped: $response")
    }

}
