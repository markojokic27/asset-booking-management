package de.bdr.asset.management.core.config.security;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@SecurityScheme(
  name = "Bearer Authentication",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "bearer"
)
@OpenAPIDefinition(
  info =@Info(
    title = "Asset Booking Management",
    version = "1.0",
    contact = @Contact(
      name = "Mladen Banović", email = "mladen.banovic@maurer-electronics.hr", url = "https://www.maurer-electronics.hr/"
    ),
    description = "This is the OpenAPI documentation for documenting and testing endpoints of the Asset Booking Management Project."
  ),
  servers = @Server(
    url = "TBD",
    description = "Development"
  )
)
public class OpenAPISecurityConfig {}