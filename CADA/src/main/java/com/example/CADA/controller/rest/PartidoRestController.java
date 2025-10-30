package com.example.CADA.controller.rest;

import com.example.CADA.model.Partido;
import com.example.CADA.service.PartidoService;
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
 * Controlador REST para la gestión de partidos.
 * 
 * Proporciona endpoints para realizar operaciones CRUD sobre los partidos,
 * incluyendo la obtención de listados, creación, actualización y eliminación.
 */
@RestController
@RequestMapping("/api/v1/partidos")
@Tag(name = "Partidos", description = "API para la gestión de partidos")
public class PartidoRestController {

	private final PartidoService partidoService;

	public PartidoRestController(PartidoService partidoService) {
		this.partidoService = partidoService;
	}

	/**
	 * Obtiene todos los partidos registrados en el sistema.
	 * 
	 * @return lista de todos los partidos
	 */
	@Operation(
		summary = "Obtener todos los partidos",
		description = "Retorna una lista completa de todos los partidos registrados en el sistema"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Lista de partidos obtenida exitosamente",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Partido.class)
			)
		)
	})
	@GetMapping
	public ResponseEntity<List<Partido>> getAllPartidos() {
		List<Partido> partidos = partidoService.findAll();
		return ResponseEntity.ok(partidos);
	}

	/**
	 * Obtiene un partido específico por su ID.
	 * 
	 * @param id identificador único del partido
	 * @return el partido encontrado o error 404 si no existe
	 */
	@Operation(
		summary = "Obtener partido por ID",
		description = "Retorna un partido específico buscado por su identificador único"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Partido encontrado",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Partido.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "Partido no encontrado"
		)
	})
	@GetMapping("/{id}")
	public ResponseEntity<Partido> getPartidoById(
		@Parameter(description = "ID del partido a buscar", required = true)
		@PathVariable Long id
	) {
		return partidoService.findById(id)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Crea un nuevo partido en el sistema.
	 * 
	 * @param partido datos del partido a crear
	 * @return el partido creado con su ID asignado
	 */
	@Operation(
		summary = "Crear nuevo partido",
		description = "Registra un nuevo partido en el sistema"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "Partido creado exitosamente",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Partido.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "Datos de entrada inválidos"
		)
	})
	@PostMapping
	public ResponseEntity<Partido> createPartido(
		@Parameter(description = "Datos del partido a crear", required = true)
		@Valid @RequestBody Partido partido
	) {
		Partido created = partidoService.create(partido);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	/**
	 * Actualiza un partido existente.
	 * 
	 * @param id identificador del partido a actualizar
	 * @param partido datos actualizados del partido
	 * @return el partido actualizado o error 404 si no existe
	 */
	@Operation(
		summary = "Actualizar partido",
		description = "Actualiza la información de un partido existente"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "Partido actualizado exitosamente",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = Partido.class)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "Partido no encontrado"
		),
		@ApiResponse(
			responseCode = "400",
			description = "Datos de entrada inválidos"
		)
	})
	@PutMapping("/{id}")
	public ResponseEntity<Partido> updatePartido(
		@Parameter(description = "ID del partido a actualizar", required = true)
		@PathVariable Long id,
		@Parameter(description = "Datos actualizados del partido", required = true)
		@Valid @RequestBody Partido partido
	) {
		return partidoService.findById(id)
			.map(existing -> {
				Partido updated = partidoService.update(id, partido);
				return ResponseEntity.ok(updated);
			})
			.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Elimina un partido del sistema.
	 * 
	 * @param id identificador del partido a eliminar
	 * @return respuesta sin contenido si se eliminó correctamente
	 */
	@Operation(
		summary = "Eliminar partido",
		description = "Elimina un partido del sistema de forma permanente"
	)
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "204",
			description = "Partido eliminado exitosamente"
		),
		@ApiResponse(
			responseCode = "404",
			description = "Partido no encontrado"
		)
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePartido(
		@Parameter(description = "ID del partido a eliminar", required = true)
		@PathVariable Long id
	) {
		if (partidoService.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		partidoService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
