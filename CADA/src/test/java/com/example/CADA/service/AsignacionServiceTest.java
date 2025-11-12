package com.example.CADA.service;

import com.example.CADA.model.Asignacion;
import com.example.CADA.model.EstadoAsignacion;
import com.example.CADA.model.RolArbitro;
import com.example.CADA.repository.AsignacionRepository;
import com.example.CADA.service.impl.JpaAsignacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de AsignacionService")
class AsignacionServiceTest {

    @Mock
    private AsignacionRepository repository;

    @InjectMocks
    private JpaAsignacionService asignacionService;

    private Asignacion asignacion1;
    private Asignacion asignacion2;
    private Asignacion asignacion3;

    @BeforeEach
    void setUp() {
        asignacion1 = Asignacion.builder()
                .id(1L)
                .partidoId(10L)
                .arbitroId(1L)
                .rol(RolArbitro.PRINCIPAL)
                .estado(EstadoAsignacion.PENDIENTE)
                .build();

        asignacion2 = Asignacion.builder()
                .id(2L)
                .partidoId(10L)
                .arbitroId(2L)
                .rol(RolArbitro.ASISTENTE_1)
                .estado(EstadoAsignacion.ACEPTADA)
                .build();

        asignacion3 = Asignacion.builder()
                .id(3L)
                .partidoId(20L)
                .arbitroId(1L)
                .rol(RolArbitro.PRINCIPAL)
                .estado(EstadoAsignacion.RECHAZADA)
                .build();
    }

    @Test
    @DisplayName("Debería retornar todas las asignaciones")
    void testFindAll() {
        // Given
        List<Asignacion> asignaciones = Arrays.asList(asignacion1, asignacion2, asignacion3);
        when(repository.findAll()).thenReturn(asignaciones);

        // When
        List<Asignacion> resultado = asignacionService.findAll();

        // Then
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertTrue(resultado.contains(asignacion1));
        assertTrue(resultado.contains(asignacion2));
        assertTrue(resultado.contains(asignacion3));
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería encontrar asignaciones por ID de árbitro")
    void testFindByArbitroId() {
        // Given
        Long arbitroId = 1L;
        List<Asignacion> asignacionesArbitro = Arrays.asList(asignacion1, asignacion3);
        when(repository.findByArbitroId(arbitroId)).thenReturn(asignacionesArbitro);

        // When
        List<Asignacion> resultado = asignacionService.findByArbitroId(arbitroId);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(a -> a.getArbitroId().equals(arbitroId)));
        assertTrue(resultado.contains(asignacion1));
        assertTrue(resultado.contains(asignacion3));
        verify(repository, times(1)).findByArbitroId(arbitroId);
    }

    @Test
    @DisplayName("Debería encontrar asignaciones por ID de partido")
    void testFindByPartidoId() {
        // Given
        Long partidoId = 10L;
        List<Asignacion> asignacionesPartido = Arrays.asList(asignacion1, asignacion2);
        when(repository.findByPartidoId(partidoId)).thenReturn(asignacionesPartido);

        // When
        List<Asignacion> resultado = asignacionService.findByPartidoId(partidoId);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(a -> a.getPartidoId().equals(partidoId)));
        assertTrue(resultado.contains(asignacion1));
        assertTrue(resultado.contains(asignacion2));
        verify(repository, times(1)).findByPartidoId(partidoId);
    }

    @Test
    @DisplayName("Debería crear una nueva asignación correctamente")
    void testCreate() {
        // Given
        Asignacion nuevaAsignacion = Asignacion.builder()
                .partidoId(30L)
                .arbitroId(3L)
                .rol(RolArbitro.ASISTENTE_2)
                .estado(EstadoAsignacion.PENDIENTE)
                .build();

        Asignacion asignacionGuardada = Asignacion.builder()
                .id(4L)
                .partidoId(30L)
                .arbitroId(3L)
                .rol(RolArbitro.ASISTENTE_2)
                .estado(EstadoAsignacion.PENDIENTE)
                .build();

        when(repository.save(any(Asignacion.class))).thenReturn(asignacionGuardada);

        // When
        Asignacion resultado = asignacionService.create(nuevaAsignacion);

        // Then
        assertNotNull(resultado);
        assertEquals(4L, resultado.getId());
        assertEquals(30L, resultado.getPartidoId());
        assertEquals(3L, resultado.getArbitroId());
        assertNull(nuevaAsignacion.getId()); // Verifica que se limpia el ID antes de guardar
        verify(repository, times(1)).save(any(Asignacion.class));
    }

    @Test
    @DisplayName("Debería aceptar una asignación pendiente")
    void testAceptar() {
        // Given
        Long asignacionId = 1L;
        when(repository.findById(asignacionId)).thenReturn(Optional.of(asignacion1));
        when(repository.save(any(Asignacion.class))).thenAnswer(invocation -> {
            Asignacion a = invocation.getArgument(0);
            a.setEstado(EstadoAsignacion.ACEPTADA);
            return a;
        });

        // When
        Asignacion resultado = asignacionService.aceptar(asignacionId);

        // Then
        assertNotNull(resultado);
        assertEquals(EstadoAsignacion.ACEPTADA, resultado.getEstado());
        verify(repository, times(1)).findById(asignacionId);
        verify(repository, times(1)).save(any(Asignacion.class));
    }

    @Test
    @DisplayName("Debería rechazar una asignación pendiente")
    void testRechazar() {
        // Given
        Long asignacionId = 1L;
        when(repository.findById(asignacionId)).thenReturn(Optional.of(asignacion1));
        when(repository.save(any(Asignacion.class))).thenAnswer(invocation -> {
            Asignacion a = invocation.getArgument(0);
            a.setEstado(EstadoAsignacion.RECHAZADA);
            return a;
        });

        // When
        Asignacion resultado = asignacionService.rechazar(asignacionId);

        // Then
        assertNotNull(resultado);
        assertEquals(EstadoAsignacion.RECHAZADA, resultado.getEstado());
        verify(repository, times(1)).findById(asignacionId);
        verify(repository, times(1)).save(any(Asignacion.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción al aceptar asignación inexistente")
    void testAceptar_NoExiste() {
        // Given
        Long asignacionId = 999L;
        when(repository.findById(asignacionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(Exception.class, () -> asignacionService.aceptar(asignacionId));
        verify(repository, times(1)).findById(asignacionId);
        verify(repository, never()).save(any(Asignacion.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción al rechazar asignación inexistente")
    void testRechazar_NoExiste() {
        // Given
        Long asignacionId = 999L;
        when(repository.findById(asignacionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(Exception.class, () -> asignacionService.rechazar(asignacionId));
        verify(repository, times(1)).findById(asignacionId);
        verify(repository, never()).save(any(Asignacion.class));
    }

    @Test
    @DisplayName("Debería eliminar una asignación por ID")
    void testDelete() {
        // Given
        Long asignacionId = 1L;
        doNothing().when(repository).deleteById(asignacionId);

        // When
        asignacionService.delete(asignacionId);

        // Then
        verify(repository, times(1)).deleteById(asignacionId);
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay asignaciones para un árbitro")
    void testFindByArbitroId_SinResultados() {
        // Given
        Long arbitroId = 999L;
        when(repository.findByArbitroId(arbitroId)).thenReturn(List.of());

        // When
        List<Asignacion> resultado = asignacionService.findByArbitroId(arbitroId);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findByArbitroId(arbitroId);
    }
}
