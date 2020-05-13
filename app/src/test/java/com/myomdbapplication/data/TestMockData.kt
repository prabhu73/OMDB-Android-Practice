package com.myomdbapplication.data

import com.myomdbapplication.repository.api.OMDBRemoteServices
import java.io.File
import java.lang.IllegalStateException
import kotlin.reflect.KClass

/**
 * @relativePath: json file path
 * @myClass: get mock service class
 */
fun getJson(relativePath: String, myClass: KClass<OMDBRemoteServices>) : String {
    // Load the JSON response
    return "{\n" +
            "  \"Search\": [\n" +
            "    {\n" +
            "      \"Title\": \"Seeking a Friend for the End of the World\",\n" +
            "      \"Year\": \"2012\",\n" +
            "      \"imdbID\": \"tt1307068\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTk4MDQ1NzE3N15BMl5BanBnXkFtZTcwMjA0MDkzNw@@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"Friend Request\",\n" +
            "      \"Year\": \"2016\",\n" +
            "      \"imdbID\": \"tt3352390\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNGFhZjQ5OTEtY2FlMy00OWM5LTlkYjAtZGUyNTE2YmVkYjc0XkEyXkFqcGdeQXVyNjkwMzU3NDI@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"My Friend Dahmer\",\n" +
            "      \"Year\": \"2017\",\n" +
            "      \"imdbID\": \"tt2291540\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BN2UxZDJlYjAtNGQ0OC00MWE4LTkzMjktMDAyNTg2ZTVkZjQ1XkEyXkFqcGdeQXVyMTc2MDc0Nw@@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"The American Friend\",\n" +
            "      \"Year\": \"1977\",\n" +
            "      \"imdbID\": \"tt0075675\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BOWFlZDA1YTQtMGJjYy00OTVlLTkxODUtNTgxNWNiMTZkYmExXkEyXkFqcGdeQXVyNjc1NTYyMjg@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"Who Finds a Friend Finds a Treasure\",\n" +
            "      \"Year\": \"1981\",\n" +
            "      \"imdbID\": \"tt0085327\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNWM4OTJjODYtNjgxZC00ZGYwLTkxZGMtMjRjODIyMTkyMDIwXkEyXkFqcGdeQXVyNzY3MTc5NTE@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"With a Friend Like Harry...\",\n" +
            "      \"Year\": \"2000\",\n" +
            "      \"imdbID\": \"tt0216800\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTgxMTE4MTYzNl5BMl5BanBnXkFtZTYwNTE4OTI5._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"My Brilliant Friend\",\n" +
            "      \"Year\": \"2018–\",\n" +
            "      \"imdbID\": \"tt7278862\",\n" +
            "      \"Type\": \"series\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BODU3Nzg5NGMtOTE2MS00YWQ0LTgyYzktMWM3YjNmYjlmZTliXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"Deadly Friend\",\n" +
            "      \"Year\": \"1986\",\n" +
            "      \"imdbID\": \"tt0090917\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNjQ0M2Y3ODktOTZhOC00Yzg4LWE5NGQtMDQyMmRjOWQzYTYxXkEyXkFqcGdeQXVyNTQ4ODA2NzQ@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"My Best Friend\",\n" +
            "      \"Year\": \"2006\",\n" +
            "      \"imdbID\": \"tt0778784\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTc4MzQ4Mzk4Ml5BMl5BanBnXkFtZTcwNDYzODA1MQ@@._V1_SX300.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"Title\": \"Your Friend the Rat\",\n" +
            "      \"Year\": \"2007\",\n" +
            "      \"imdbID\": \"tt1134859\",\n" +
            "      \"Type\": \"movie\",\n" +
            "      \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTcyOTU4MTM0OF5BMl5BanBnXkFtZTgwODQzMDgwMjE@._V1_SX300.jpg\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"totalResults\": \"1103\",\n" +
            "  \"Response\": \"True\"\n" +
            "}"
    /*val path = "json/$relativePath"
    myClass.java.classLoader?.getResource(path)?.let {
        val file = File(it.path)
        return String(file.readBytes())
    }
    throw IllegalStateException("Resource not found")*/
}