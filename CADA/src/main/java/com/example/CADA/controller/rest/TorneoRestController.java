package com.example.CADA.controller.rest;

import com.example.CADA.model.Torneo;
import com.example.CADA.service.TorneoService;
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
 * Controlador REST para la gestión de torneos.
 * 
 * Proporciona endpoints para realizar operaciones CRUD sobre los torneos,
 * incluyendo filtros para obtener torneos activos.
 */
@RestController
@RequestMapping("/api/v1/torneos")
@Tag(name = "Torneos", description = "API para la gestión de torneos")
public class TorneoRestController {

	private final TorneoService torneoService;

	public TorneoRestController(TorneoService torneoService) {
		this.torneoService = torneoService;
	}

	/**
	 * Obtiene todos los torneos registrados en el sistema.
	 * 
	 * @return lista de todos los torneos
	 */
	@Operation(
		summary = "Obtener todos los torneos",
		description = "Retorna una lista completa de todos los torneos registrados en el sistema"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Lista de torneos obtenida exitosamente",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Torneo.class)
			)
		)
	})
	@GetMapping
	public ResponseEntity<List<Torneo>> getAllTorneos() {
		List<Torneo> torneos = torneoService.findAll();
		return ResponseEntity.ok(torneos);
	}

	/**
	 * Obtiene un torneo específico por su ID.
	 * 
	 * @param id identificador único del torneo
	 * @return el torneo encontrado o error 404 si no existe
	 */
	@Operation(
		summary = "Obtener torneo por ID",
		description = "Retorna un torneo específico buscado por su identificador único"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Torneo encontrado",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Torneo.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "Torneo no encontrado"
		)
	})
	@GetMapping("/{id}")
	public ResponseEntity<Torneo> getTorneoById(
		@Parameter(description = "ID del torneo a buscar", required = true)
		@PathVariable Long id
	) {
		return torneoService.findById(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Crea un nuevo torneo en el sistema.
	 * 
	 * @param torneo datos del torneo a crear
	 * @return el torneo creado con su ID asignado
	 */
	@Operation(
		summary = "Crear nuevo torneo",
		description = "Registra un nuevo torneo en el sistema"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Torneo creado exitosamente",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Torneo.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "Datos de entrada inválidos"
		)
	})
	@PostMapping
	public ResponseEntity<Torneo> createTorneo(
		@Parameter(description = "Datos del torneo a crear", required = true)
		@Valid @RequestBody Torneo torneo
	) {
		Torneo created = torneoService.create(torneo);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	/**
	 * Actualiza un torneo existente.
	 * 
	 * @param id identificador del torneo a actualizar
	 * @param torneo datos actualizados del torneo
	 * @return el torneo actualizado o error 404 si no existe
	 */
	@Operation(
		summary = "Actualizar torneo",
		description = "Actualiza la información de un torneo existente"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Torneo actualizado exitosamente",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Torneo.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "Torneo no encontrado"
		),
		@ApiResponse(
			responseCode = "400",
			description = "Datos de entrada inválidos"
		)
	})
	@PutMapping("/{id}")
	public ResponseEntity<Torneo> updateTorneo(
		@Parameter(description = "ID del torneo a actualizar", required = true)
		@PathVariable Long id,
		@Parameter(description = "Datos actualizados del torneo", required = true)
		@Valid @RequestBody Torneo torneo
	) {
		return torneoService.findById(id)
			.map(existing -> {
				Torneo updated = torneoService.update(id, torneo);
				return ResponseEntity.ok(updated);
			})
			.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Elimina un torneo del sistema.
	 * 
	 * @param id identificador del torneo a eliminar
	 * @return respuesta sin contenido si se eliminó correctamente
	 */
	@Operation(
		summary = "Eliminar torneo",
		description = "Elimina un torneo del sistema de forma permanente"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Torneo eliminado exitosamente"
		),
		@ApiResponse(
			responseCode = "404",
			description = "Torneo no encontrado"
		)
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTorneo(
		@Parameter(description = "ID del torneo a eliminar", required = true)
		@PathVariable Long id
	) {
		if (torneoService.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		torneoService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
