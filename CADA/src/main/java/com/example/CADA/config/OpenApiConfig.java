package com.example.CADA.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API REST.
 * 
 * Esta clase define la información general de la API, incluyendo título,
 * descripción, versión, contacto y servidores disponibles.
 */
@Configuration
public class OpenApiConfig {

	/**
	 * Configura la documentación OpenAPI de la aplicación.
	 * 
	 * @return objeto OpenAPI con la configuración completa
	 */
	@Bean
	public OpenAPI cadaOpenAPI() {
		Server localServer = new Server();
		localServer.setUrl("http://localhost:8080");
		localServer.setDescription("Servidor de desarrollo local");

		Contact contact = new Contact();
		contact.setName("Equipo CADA");
		contact.setEmail("soporte@cada.com");

		License license = new License()
			.name("Apache 2.0")
			.url("https://www.apache.org/licenses/LICENSE-2.0.html");

		Info info = new Info()
			.title("CADA - Sistema de Gestión de Torneos y Árbitros")
			.version("1.0.0")
			.description("API REST para la gestión de torneos, partidos, árbitros y asignaciones")
			.contact(contact)
			.license(license);

		return new OpenAPI()
			.info(info)
			.servers(List.of(localServer));
	}
}
