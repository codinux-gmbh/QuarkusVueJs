package net.codinux.text

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import java.time.LocalDateTime

@Path("/hello")
class GreetingResource {

    @GET
    fun getGreeting() = "from Quarkus @${LocalDateTime.now()}"

}