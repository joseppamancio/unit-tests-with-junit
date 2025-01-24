package com.algaworks.junit.blog.negocio;

import com.algaworks.junit.blog.armazenamento.ArmazenamentoEditor;
import com.algaworks.junit.blog.exception.EditorNaoEncontradoException;
import com.algaworks.junit.blog.exception.RegraNegocioException;
import com.algaworks.junit.blog.modelo.Editor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class CadastroEditorMockTest {

    Editor editor;

    @Spy
    Editor editorSpy;

    @Captor
    ArgumentCaptor<Mensagem> mensagemArgumentCaptor;

    @Mock
    ArmazenamentoEditor armazenamentoEditor;

    @Mock
    GerenciadorEnvioEmail gerenciadorEnvioEmail;

    @InjectMocks
    CadastroEditor cadastroEditor;  // armazenamentoEditor e gerenciadorEnvioEmail serão injetados nele

    @Nested
    class CadastroComEditorValido {
        /*
        // PARAMETRO FIXO DEFINIDO
        @BeforeEach
        void beforeEach() {
            editor = new Editor(null, "Alex", "alex@email.com", BigDecimal.TEN, true);
            Mockito.when(armazenamentoEditor.salvar(editor))
                    .thenReturn( new Editor(1L, "Alex", "alex@email.com", BigDecimal.TEN, true));
        }
        */

        /*
        // PARAMETRO OBTIDO PELA INVOCACAO
        @BeforeEach
        void beforeEach() {
            editor = new Editor(null, "Alex", "alex@email.com", BigDecimal.TEN, true);
            Mockito.when(armazenamentoEditor.salvar(editor))
                    .thenAnswer(invocacaoMetodo ->{
                        int posicaoDoParametro = 0;
                        Editor editorPassadoPorParametro = invocacaoMetodo.getArgument(posicaoDoParametro, Editor.class);
                        editorPassadoPorParametro.setId(1L);
                        return editorPassadoPorParametro;
                    });
        }
        */

        // PARAMETRO DINAMICO EM MOCK
        @BeforeEach
        void beforeEach() {
            editor = new Editor(null, "Alex", "alex@email.com", BigDecimal.TEN, true);
            Mockito.when(armazenamentoEditor.salvar(Mockito.any(Editor.class)))
                    .thenAnswer(invocacaoMetodo -> {
                        int posicaoDoParametro = 0;
                        Editor editorPassadoPorParametro = invocacaoMetodo.getArgument(posicaoDoParametro, Editor.class);
                        editorPassadoPorParametro.setId(1L);
                        return editorPassadoPorParametro;
                    });
        }

        @Test
        void Dado_um_editor_valido_Quando_criar_Entao_deve_retornar_um_id_de_cadastro() {
            // cadastroEditor = new CadastroEditor(armazenamentoEditor, gerenciadorEnvioEmail); // Alternativa manual ao @InjectMocks

            Editor editorSalvo = cadastroEditor.criar(editor);
            assertEquals(1L, editorSalvo.getId());
        }

        @Test
        void Dado_um_editor_valido_Quando_criar_Entao_deve_chamar_metodo_salvar_do_armazenamento() {
            cadastroEditor.criar(editor);
            verify(armazenamentoEditor, times(1))
                    .salvar(Mockito.eq(editor));
        }

        @Test
        void Dado_um_editor_valido_Quando_criar_e_lancar_excecao_ao_salvar_Entao_nao_deve_enviar_email() {
            Mockito.when(armazenamentoEditor.salvar(editor)).thenThrow(new RuntimeException());
            assertAll("Não deve enviar e-mail, quando lançar Exception do armazenamento",
                    () -> assertThrows(RuntimeException.class, () -> cadastroEditor.criar(editor)),
                    () -> verify(gerenciadorEnvioEmail, never()).enviarEmail(Mockito.any())
            );
        }

        //Capturando um argumento de um metodo
        @Test
        void Dado_um_editor_valido_Quando_cadastrar_Entao_deve_enviar_email_com_destino_ao_editor() {
            //ArgumentCaptor<Mensagem> mensagemArgumentCaptor = ArgumentCaptor.forClass(Mensagem.class); // Alternativa manual ao @Captor

            Editor editorSalvo = cadastroEditor.criar(editor);
            verify(gerenciadorEnvioEmail).enviarEmail(mensagemArgumentCaptor.capture());

            Mensagem mensagemPassadaPorParametro = mensagemArgumentCaptor.getValue();
            assertEquals(editorSalvo.getEmail(), mensagemPassadaPorParametro.getDestinatario());
        }

        @Test
        void Dado_um_editor_valido_Quando_cadastrar_Entao_deve_verificar_o_email() {
            //Editor editorSpy = Mockito.spy(editor); // Alternativa manual ao @Spy
            cadastroEditor.criar(editorSpy);
            verify(editorSpy, Mockito.atLeast(1)).getEmail();
        }

        //Alterando retorno de um metodo após chamadas consecutivas
        @Test
        void Dado_um_editor_com_email_existente_Quando_cadastrar_Entao_deve_lancar_excecao() {
            Mockito.when(armazenamentoEditor.encontrarPorEmail("alex@email.com"))
                    .thenReturn(Optional.empty()) // Primeira chamada
                    .thenReturn(Optional.of(editor)); // Segunda Chamada

            Editor editorComEmailExistente = new Editor(null, "Alex", "alex@email.com", BigDecimal.TEN, true);
            cadastroEditor.criar(editor);
            assertThrows(RegraNegocioException.class, () -> cadastroEditor.criar(editorComEmailExistente));
        }

        // Verificando ordem de chamada de metodos
        @Test
        void Dado_um_editor_valido_Quando_cadastrar_Entao_deve_enviar_email_apos_salvar() {
            cadastroEditor.criar(editor);

            InOrder inOrder = Mockito.inOrder(armazenamentoEditor, gerenciadorEnvioEmail);
            inOrder.verify(armazenamentoEditor, times(1)).salvar(editor);
            inOrder.verify(gerenciadorEnvioEmail, times(1)).enviarEmail(Mockito.any(Mensagem.class));
        }
    }

    @Nested
    class CadastroComEditorNulo{
        @Test
        void Dado_um_editor_nulo_Quando_cadastrar_Entao_deve_lancar_excecao(){
            assertThrows(NullPointerException.class, () -> cadastroEditor.criar(null));
            verify(armazenamentoEditor, never()).salvar(Mockito.any());
            verify(gerenciadorEnvioEmail, never()).enviarEmail(Mockito.any());
        }
    }

    @Nested
    class EdicaoComEditor{

        @Spy
        Editor editor = new Editor(null, "Alex", "alex@email.com", BigDecimal.TEN, true);

        @BeforeEach
        void init(){
            Mockito.when(armazenamentoEditor.salvar(editor)).thenAnswer(invocacao -> invocacao.getArgument(0, Editor.class));
            Mockito.when(armazenamentoEditor.encontrarPorId(1L)).thenReturn(Optional.of(editor));
        }

        @Test
        void dado_um_editor_valido_Quando_editar_Entao_deve_alterar_editor_salvo() {
            Editor editorAtualizado = new Editor(1L, "Alex Silva", "alex.silva@email.com", BigDecimal.ZERO, false);
            cadastroEditor.editar(editorAtualizado);
            verify(editor, times(1)).atualizarComDados(editorAtualizado);

            InOrder inOrder = Mockito.inOrder(editor, armazenamentoEditor);
            inOrder.verify(editor).atualizarComDados(editorAtualizado);
            inOrder.verify(armazenamentoEditor).salvar(editor);
        }
    }

    @Nested
    class EdicaoComEditorInexistente {

        Editor editor = new Editor(99L, "Alex", "alex@email.com", BigDecimal.TEN, true);

        @BeforeEach
        void init() {
            Mockito.when(armazenamentoEditor.encontrarPorId(99L)).thenReturn(Optional.empty());
        }

        @Test
        void Dado_um_editor_que_nao_exista_Quando_editar_Entao_deve_lancar_exception() {
            assertThrows(EditorNaoEncontradoException.class, ()-> cadastroEditor.editar(editor));
            verify(armazenamentoEditor, never()).salvar(Mockito.any(Editor.class));
        }

    }
}