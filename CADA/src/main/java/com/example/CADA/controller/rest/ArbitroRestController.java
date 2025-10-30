package com.example.CADA.controller.rest;

import com.example.CADA.model.Arbitro;
import com.example.CADA.service.ArbitroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de árbitros.
 * 
 * Proporciona endpoints para realizar operaciones CRUD sobre los árbitros,
 * incluyendo filtros para obtener árbitros activos.
 */
@RestController
@RequestMapping("/api/v1/arbitros")
@Tag(name = "Árbitros", description = "API para la gestión de árbitros")
public class ArbitroRestController {

	private final ArbitroService arbitroService;

	public ArbitroRestController(ArbitroService arbitroService) {
		this.arbitroService = arbitroService;
	}

	/**
	 * Obtiene todos los árbitros registrados en el sistema.
	 * 
	 * @return lista de todos los árbitros
	 */
	@Operation(
		summary = "Obtener todos los árbitros",
		description = "Retorna una lista completa de todos los árbitros registrados en el sistema"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Lista de árbitros obtenida exitosamente",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Arbitro.class)
			)
		)
	})
	@GetMapping
	public ResponseEntity<List<Arbitro>> getAllArbitros() {
		List<Arbitro> arbitros = arbitroService.findAll();
		return ResponseEntity.ok(arbitros);
	}

	/**
	 * Obtiene todos los árbitros activos.
	 * 
	 * @return lista de árbitros activos
	 */
	@Operation(
		summary = "Obtener árbitros activos",
		description = "Retorna una lista de árbitros que están marcados como activos"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Lista de árbitros activos obtenida exitosamente",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Arbitro.class)
			)
		)
	})
	@GetMapping("/activos")
	public ResponseEntity<List<Arbitro>> getArbitrosActivos() {
		List<Arbitro> arbitros = arbitroService.findActivos();
		return ResponseEntity.ok(arbitros);
	}

	/**
	 * Obtiene un árbitro específico por su ID.
	 * 
	 * @param id identificador único del árbitro
	 * @return el árbitro encontrado o error 404 si no existe
	 */
	@Operation(
		summary = "Obtener árbitro por ID",
		description = "Retorna un árbitro específico buscado por su identificador único"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Árbitro encontrado",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Arbitro.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "Árbitro no encontrado"
		)
	})
	@GetMapping("/{id}")
	public ResponseEntity<Arbitro> getArbitroById(
		@Parameter(description = "ID del árbitro a buscar", required = true)
		@PathVariable Long id
	) {
		return arbitroService.findById(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Crea un nuevo árbitro en el sistema.
	 * 
	 * @param arbitro datos del árbitro a crear
	 * @return el árbitro creado con su ID asignado
	 */
	@Operation(
		summary = "Crear nuevo árbitro",
		description = "Registra un nuevo árbitro en el sistema"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Árbitro creado exitosamente",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Arbitro.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "Datos de entrada inválidos"
		)
	})
	@PostMapping
	public ResponseEntity<Arbitro> createArbitro(
		@Parameter(description = "Datos del árbitro a crear", required = true)
		@Valid @RequestBody Arbitro arbitro
	) {
		Arbitro created = arbitroService.create(arbitro);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	/**
	 * Actualiza un árbitro existente.
	 * 
	 * @param id identificador del árbitro a actualizar
	 * @param arbitro datos actualizados del árbitro
	 * @return el árbitro actualizado o error 404 si no existe
	 */
	@Operation(
		summary = "Actualizar árbitro",
		description = "Actualiza la información de un árbitro existente"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Árbitro actualizado exitosamente",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Arbitro.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "Árbitro no encontrado"
		),
		@ApiResponse(
			responseCode = "400",
			description = "Datos de entrada inválidos"
		)
	})
	@PutMapping("/{id}")
	public ResponseEntity<Arbitro> updateArbitro(
		@Parameter(description = "ID del árbitro a actualizar", required = true)
		@PathVariable Long id,
		@Parameter(description = "Datos actualizados del árbitro", required = true)
		@Valid @RequestBody Arbitro arbitro
	) {
		return arbitroService.findById(id)
			.map(existing -> {
				Arbitro updated = arbitroService.update(id, arbitro);
				return ResponseEntity.ok(updated);
			})
			.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Elimina un árbitro del sistema.
	 * 
	 * @param id identificador del árbitro a eliminar
	 * @return respuesta sin contenido si se eliminó correctamente
	 */
	@Operation(
		summary = "Eliminar árbitro",
		description = "Elimina un árbitro del sistema de forma permanente"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Árbitro eliminado exitosamente"
		),
		@ApiResponse(
			responseCode = "404",
			description = "Árbitro no encontrado"
		)
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteArbitro(
		@Parameter(description = "ID del árbitro a eliminar", required = true)
		@PathVariable Long id
	) {
		if (arbitroService.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		arbitroService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
