package com.example.CADA.service;

import com.example.CADA.model.Arbitro;
import com.example.CADA.model.Especialidad;
import com.example.CADA.model.Escalafon;
import com.example.CADA.repository.ArbitroRepository;
import com.example.CADA.service.impl.JpaArbitroService;
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
@DisplayName("Pruebas unitarias de ArbitroService")
class ArbitroServiceTest {

    @Mock
    private ArbitroRepository repository;

    @InjectMocks
    private JpaArbitroService arbitroService;

    private Arbitro arbitro1;
    private Arbitro arbitro2;
    private Arbitro arbitroInactivo;

    @BeforeEach
    void setUp() {
        arbitro1 = Arbitro.builder()
                .id(1L)
                .username("juan.perez")
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan.perez@example.com")
                .especialidad(Especialidad.CAMPO)
                .escalafon(Escalafon.FIBA)
                .activo(true)
                .build();

        arbitro2 = Arbitro.builder()
                .id(2L)
                .username("maria.garcia")
                .nombre("María")
                .apellido("García")
                .email("maria.garcia@example.com")
                .especialidad(Especialidad.MESA)
                .escalafon(Escalafon.PRIMERA)
                .activo(true)
                .build();

        arbitroInactivo = Arbitro.builder()
                .id(3L)
                .username("carlos.lopez")
                .nombre("Carlos")
                .apellido("López")
                .email("carlos.lopez@example.com")
                .especialidad(Especialidad.CAMPO)
                .escalafon(Escalafon.SEGUNDA)
                .activo(false)
                .build();
    }

    @Test
    @DisplayName("Debería retornar todos los árbitros")
    void testFindAll() {
        // Given
        List<Arbitro> arbitros = Arrays.asList(arbitro1, arbitro2, arbitroInactivo);
        when(repository.findAll()).thenReturn(arbitros);

        // When
        List<Arbitro> resultado = arbitroService.findAll();

        // Then
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertTrue(resultado.contains(arbitro1));
        assertTrue(resultado.contains(arbitro2));
        assertTrue(resultado.contains(arbitroInactivo));
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería retornar solo árbitros activos")
    void testFindActivos() {
        // Given
        List<Arbitro> arbitrosActivos = Arrays.asList(arbitro1, arbitro2);
        when(repository.findByActivoTrue()).thenReturn(arbitrosActivos);
        when(repository.countByActivo(true)).thenReturn(2L);

        // When
        List<Arbitro> resultado = arbitroService.findActivos();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(Arbitro::isActivo));
        assertTrue(resultado.contains(arbitro1));
        assertTrue(resultado.contains(arbitro2));
        assertFalse(resultado.contains(arbitroInactivo));
        verify(repository, times(1)).findByActivoTrue();
    }

    @Test
    @DisplayName("Debería encontrar árbitro por ID cuando existe")
    void testFindById_Existe() {
        // Given
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(arbitro1));

        // When
        Optional<Arbitro> resultado = arbitroService.findById(id);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(arbitro1.getId(), resultado.get().getId());
        assertEquals(arbitro1.getUsername(), resultado.get().getUsername());
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debería retornar Optional vacío cuando el árbitro no existe")
    void testFindById_NoExiste() {
        // Given
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Arbitro> resultado = arbitroService.findById(id);

        // Then
        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Debería encontrar árbitro por username cuando existe")
    void testFindByUsername_Existe() {
        // Given
        String username = "juan.perez";
        when(repository.findByUsername(username)).thenReturn(Optional.of(arbitro1));

        // When
        Optional<Arbitro> resultado = arbitroService.findByUsername(username);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(arbitro1.getUsername(), resultado.get().getUsername());
        verify(repository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Debería crear un nuevo árbitro correctamente")
    void testCreate() {
        // Given
        Arbitro nuevoArbitro = Arbitro.builder()
                .username("nuevo.arbitro")
                .nombre("Nuevo")
                .apellido("Árbitro")
                .email("nuevo@example.com")
                .especialidad(Especialidad.CAMPO)
                .escalafon(Escalafon.TERCERA)
                .activo(true)
                .build();

        Arbitro arbitroGuardado = Arbitro.builder()
                .id(4L)
                .username("nuevo.arbitro")
                .nombre("Nuevo")
                .apellido("Árbitro")
                .email("nuevo@example.com")
                .especialidad(Especialidad.CAMPO)
                .escalafon(Escalafon.TERCERA)
                .activo(true)
                .build();

        when(repository.save(any(Arbitro.class))).thenReturn(arbitroGuardado);

        // When
        Arbitro resultado = arbitroService.create(nuevoArbitro);

        // Then
        assertNotNull(resultado);
        assertEquals(4L, resultado.getId());
        assertEquals("nuevo.arbitro", resultado.getUsername());
        assertNull(nuevoArbitro.getId()); // Verifica que se limpia el ID antes de guardar
        verify(repository, times(1)).save(any(Arbitro.class));
    }

    @Test
    @DisplayName("Debería actualizar un árbitro existente")
    void testUpdate() {
        // Given
        Long id = 1L;
        Arbitro arbitroActualizado = Arbitro.builder()
                .id(id)
                .username("juan.perez")
                .nombre("Juan Carlos")
                .apellido("Pérez")
                .email("juan.perez@example.com")
                .especialidad(Especialidad.CAMPO)
                .escalafon(Escalafon.FIBA)
                .activo(true)
                .build();

        when(repository.save(any(Arbitro.class))).thenReturn(arbitroActualizado);

        // When
        Arbitro resultado = arbitroService.update(id, arbitroActualizado);

        // Then
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Juan Carlos", resultado.getNombre());
        verify(repository, times(1)).save(any(Arbitro.class));
    }

    @Test
    @DisplayName("Debería eliminar un árbitro por ID")
    void testDelete() {
        // Given
        Long id = 1L;
        doNothing().when(repository).deleteById(id);

        // When
        arbitroService.delete(id);

        // Then
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Debería cambiar el estado activo de un árbitro")
    void testToggleActivo() {
        // Given
        Long id = 1L;
        boolean estadoOriginal = arbitro1.isActivo();
        when(repository.findById(id)).thenReturn(Optional.of(arbitro1));
        when(repository.save(any(Arbitro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Arbitro resultado = arbitroService.toggleActivo(id);

        // Then
        assertNotNull(resultado);
        assertEquals(!estadoOriginal, resultado.isActivo());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(any(Arbitro.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción al cambiar estado de árbitro inexistente")
    void testToggleActivo_NoExiste() {
        // Given
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(Exception.class, () -> arbitroService.toggleActivo(id));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(Arbitro.class));
    }
}
