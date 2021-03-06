package org.ubicuosoft.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.ubicuosoft.Datos;
import org.ubicuosoft.models.Examen;
import org.ubicuosoft.repositories.ExamenRepository;
import org.ubicuosoft.repositories.ExamenRepositoryImpl;
import org.ubicuosoft.repositories.PreguntaRepository;
import org.ubicuosoft.repositories.PreguntaRepositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Forma 1. Habilitar las anotaciones de mockito.
@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {
    @Mock
    //ExamenRepository examenRepository;
    ExamenRepositoryImpl examenRepository;

    @Mock
    //PreguntaRepository preguntaRepository;
    PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

    @BeforeEach
    void setUp() {
//--Forma 2. Habilitar las anotaciones de mockito.
//        MockitoAnnotations.openMocks(this);

//--Crear los mocks.
//        repository=mock(ExamenRepository.class);//Objeto simulado de repository
//        preguntaRepository=mock(PreguntaRepository.class);//Objeto simulado de repository
//        service=new ExamenServiceImpl(repository, preguntaRepository);
    }

    @Test
    void find_examen_por_nombre() {
        //GIVEN
//        ExamenRepository repository=new ExamenRepositoryImpl();
        ExamenRepository repository=mock(ExamenRepository.class);
        ExamenService service=new ExamenServiceImpl(repository, preguntaRepository);

        //Que pasa si quieremos hacer un test para comprobar si el servicio devuelve una lista vacia?
        //R. Sin mockito se tendria que modificar ExamenRepositoryImpl-findall para que devuelva una lista vacia.
        //WHEN
        Examen examen= service.findExamenPorNombre("Matematicas");

        //THEN
        assertNotNull(examen);
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas",examen.getNombre());
    }

    @Test
    void find_examen_por_nombre_con_mockito() {
        //GIVEN
        //--Inicio Mock--
        //Mockeando el repositorio con datos preestablecidos.
        ExamenRepository repository= mock(ExamenRepository.class);
//        List<Examen> examenes= Arrays.asList(
//                new Examen(5L,"Matematicas"),
//                new Examen(6L,"Lenguaje"),
//                new Examen(7L,"Historia"));
        //El metodo Repository-FindAll() retorna una lista de examenes 'List<Examen>'
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //--Fin Mock--

        //WHEN
        ExamenService service=new ExamenServiceImpl(repository, preguntaRepository);
        Examen examen= service.findExamenPorNombre("Matematicas");

        //THEN
        assertNotNull(examen);
        assertEquals(1L, examen.getId());
        assertEquals("Matematicas",examen.getNombre());
    }

    @Test
    void find_examen_por_nombre_lista_vacia_con_mockito() {
        //GIVEN
        //--Inicio Mock--
        ExamenRepository repository=mock(ExamenRepository.class);
        List<Examen> examenes= Collections.emptyList();
        when(repository.findAll()).thenReturn(examenes);
        //--Fin Mock---
        //WHEN
        ExamenService service=new ExamenServiceImpl(repository,preguntaRepository);
        Examen examen=service.findExamenPorNombre("Lenguaje");
        //THEN
        assertNull(examen);
    }

    @Test
    void find_examen_por_nombre_lista_vacia_con_mockito_v2() {
        //GIVEN
        List<Examen> examenes= Collections.emptyList();
        when(examenRepository.findAll()).thenReturn(examenes);
        //WHEN
        Examen examen=service.findExamenPorNombre("Lenguaje");
        //THEN
        assertNull(examen);
    }

    @Test
    void buscar_un_examen_por_nombre_con_preguntas() {
        //GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(1L)).thenReturn(Datos.PREGUNTAS);
        //WHEN
        Examen examen=service.findExamenPorNombreConPreguntas("Matematicas");
        //THEN
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Pregunta 1"));
        assertTrue(examen.getNombre().equals("Matematicas"));
    }

    @Test
    void buscar_un_examen_por_nombre_con_preguntas_con_any() {
        //GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        //WHEN
        Examen examen=service.findExamenPorNombreConPreguntas("Historia");
        //THEN
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Pregunta 1"));
    }

    @Test
    void buscar_un_examen_por_nombre_con_preguntas_con_verify() {
        //GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        //WHEN
        Examen examen=service.findExamenPorNombreConPreguntas("Historia");
        //THEN
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Pregunta 1"));
        //Verificar si se han llamados metodos.
        verify(examenRepository).findAll();//Verificar si se ha llamado el metodo findAll().
        verify(preguntaRepository).findPreguntasPorExamenId(3L); //Verifica si se ha llmado el metodo findPreguntasPorExamenId().
    }

    @Test
    @Disabled
    void buscar_un_examen_por_nombre_con_preguntas_con_verify_v2() {
        //GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        //WHEN
        Examen examen=service.findExamenPorNombreConPreguntas("Historiaaa");//
        //THEN
        assertNull(examen);
        //Verificar si se han llamados metodos.
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong()); //Aqui fallara porque no fue invocado
    }

    @Test
    void guardar_examen_con_preguntas() {
        //GIVEN
        Examen examenConPreguntas=Datos.EXAMEN;
        examenConPreguntas.setPreguntas(Datos.PREGUNTAS);
        when(examenRepository.guardar(any(Examen.class))).thenReturn(Datos.EXAMEN);

        //WHEN
        Examen examen=service.guardar(examenConPreguntas);

        //THEN
        assertNotNull(examen.getId());
        assertEquals(4L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(examenRepository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void guardar_examen_con_preguntas_autoincrementable() {
        //---GIVEN (Dado. Un entorno de prueba)---
        Examen examenConPreguntas=Datos.EXAMEN;
        examenConPreguntas.setPreguntas(Datos.PREGUNTAS);
        when(examenRepository.guardar(any(Examen.class))).then(new Answer<Examen>(){
            Long secuencia=4L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen=invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });
        //---WHEN (Cuando. Ejecutamos el metodo a probar)---
        Examen examen=service.guardar(examenConPreguntas);
        //---THEN (Entonces. Validar el resultado)---
        assertNotNull(examen.getId());
        assertEquals(4L, examen.getId());
        assertEquals("Fisica", examen.getNombre());
        verify(examenRepository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void manejo_excepcion() {
        //GIVE
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);//Lista de examenes con ID=null
        when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);//Al pasar un parametro diferente de Long (ID=null) deberia generar una excepcion.
        //WHEN - THEN
        Exception exception=assertThrows(IllegalArgumentException.class,()->{
            service.findExamenPorNombreConPreguntas("Matematicas");
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());//Se cumple con la excepcion esperada.
        verify(examenRepository).findAll();//verificar si se ha invocado al parametro findAll()
        verify(preguntaRepository).findPreguntasPorExamenId(isNull());//verificar si se ha invocado al parametro findPregundasPorExamenId con parametro null
    }

    @Test
    void argument_matchers() {
        //GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        //WHEN
        service.findExamenPorNombreConPreguntas("Matematicas");
        //THEN
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg->arg!=null && arg>=1));
    }

    @Test
    void argument_matchers_personalizado() {
        //GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        //WHEN
        service.findExamenPorNombreConPreguntas("Matematicas");
        //THEN
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgMatchers()));
    }

    @Test
    void argument_matchers_con_labmdas() {
        //GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        //WHEN
        service.findExamenPorNombreConPreguntas("Matematicas");
        //THEN
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(argThat((argument)->argument!=null && argument>0));
    }

    public static class MiArgMatchers implements ArgumentMatcher<Long>{

        private Long argument;
        @Override
        public boolean matches(Long argument) {
            this.argument=argument;
            return argument!=null && argument>0;
        }
    }

    @Test
    void capturar_el_argumento() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenPorNombreConPreguntas("Matematicas");

        //ArgumentCaptor<Long> captor=ArgumentCaptor.forClass(Long.class);
        verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());

        assertEquals(1L, captor.getValue());
    }

    @Test
    void test_do_throw() {
        //GIVEN
        //doAlgo.
        //doThrow, cuando se lanza una excepcion.(usar cuando el metodo "void" no devuele nada).
        Examen examen=Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());//Simular la invocacion del metodo "void" guardarPreguntas

        //WHEN - THEN
        assertThrows(IllegalArgumentException.class,()->{
            service.guardar(examen);
        });
    }

    @Test
    void test_do_answer() {
        //GIVEN
        //Modelar la respuesta del metodo findAll.
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        //Modelar la respuesta del metodo findExamenPorNombreConPreguntas.
        doAnswer(invocation->{
            Long id=invocation.getArgument(0);
            return id==1L? Datos.PREGUNTAS:Collections.emptyList();
        }).when(preguntaRepository).findPreguntasPorExamenId(anyLong());
        //WHEN
        Examen examen= service.findExamenPorNombreConPreguntas("Matematicas");
        //THEN
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Pregunta 1"));
        assertEquals(1L, examen.getId());
        assertEquals("Matematicas",examen.getNombre());

        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void guardar_examen_con_preguntas_autoincrementable_do_answer() {
        //---GIVEN (Dado. Un entorno de prueba)---
        Examen examenConPreguntas=Datos.EXAMEN;
        examenConPreguntas.setPreguntas(Datos.PREGUNTAS);

        doAnswer(new Answer<Examen>(){
            Long secuencia=4L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen=invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(examenRepository).guardar(any(Examen.class));

        //---WHEN (Cuando. Ejecutamos el metodo a probar)---
        Examen examen=service.guardar(examenConPreguntas);
        //---THEN (Entonces. Validar el resultado)---
        assertNotNull(examen.getId());
        assertEquals(4L, examen.getId());
        assertEquals("Fisica", examen.getNombre());
        verify(examenRepository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void test_do_call_real_method() {
        //GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(anyLong()); //Invocacion a un metodo real (No interface, sino implementacion)
        //WHEN
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        //THEN
        assertEquals(1L,examen.getId());
        assertEquals("Matematicas", examen.getNombre());
    }

    @Test
    void test_spy() {
        //Spy, no es un 100% mock, es un hibrido.
        //En Spy, cuando invocamos un metodo va ir al metodo real.
        //Un Spy es un clon del objeto real.
        //No es recomendable usar espias (spy)
        //El mock es 100% simulado.

        //GIVEN
        ExamenRepository examenRepository=spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository=spy(PreguntaRepositoryImpl.class);
        ExamenService examenService=new ExamenServiceImpl(examenRepository, preguntaRepository);

        List<String> preguntas= Arrays.asList("Pregunta N");
        //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong()); //(Hibrido)Aqui se mockea el metodO findPreguntasPorId
        //WHEN
        Examen examen=examenService.findExamenPorNombreConPreguntas("Matematicas");
        //THEN
        assertEquals(1L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Pregunta N"));
    }

    @Test
    void test_orden_invocaciones_mocks() {
        //GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        //WHEN
        service.findExamenPorNombreConPreguntas("Matematicas");
        service.findExamenPorNombreConPreguntas("Lenguaje");
        //THEN
        //Veriificar el orden de ejecucion de los metodos mockeados
        InOrder inOrder=inOrder(examenRepository, preguntaRepository);
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(1L);
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(2L);
    }

    @Test
    void test_verificar_numero_invocaciones_mock() {
        //GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        //WHEN
        service.findExamenPorNombreConPreguntas("Matematicas");
        //THEN
        verify(preguntaRepository).findPreguntasPorExamenId(1L);
        verify(preguntaRepository,times(1)).findPreguntasPorExamenId(1L);
        verify(preguntaRepository, atLeast(1)).findPreguntasPorExamenId(1L);
        verify(preguntaRepository, atLeastOnce()).findPreguntasPorExamenId(1L);
        verify(preguntaRepository, atMost(1)).findPreguntasPorExamenId(1L);
        verify(preguntaRepository, atMostOnce()).findPreguntasPorExamenId(1L);

        //verifyNoInteractions(preguntaRepository);
    }
}