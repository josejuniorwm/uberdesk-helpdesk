package com.uberdesk.helpdesk.service;

import com.uberdesk.helpdesk.Chamado;
import com.uberdesk.helpdesk.Usuario;
import com.uberdesk.helpdesk.dtos.ChamadoDTO;
import com.uberdesk.helpdesk.repository.ChamadoRepository;
import org.springframework.stereotype.Service;
import com.uberdesk.helpdesk.Prioridade;
import com.uberdesk.helpdesk.Status;


import java.util.List;
import java.util.Optional;

/**
 * /@Service: Avisa ao Spring que esta é a classe que contém as regras de negócio dos Chamados.
 */

@Service
public class ChamadoService {

    // 1. O Operário do Banco de Dados para os chamados
    private final ChamadoRepository repository;

    // 2. A COMUNICAÇÃO ENTRE SERVIÇOS (Boas Práticas)
    // Por que não chamamos o UsuarioRepository direto?
    // Porque o UsuarioService já tem o mét0do "findById" pronto, que inclusive
    // já lança a nossa exceção de "Objeto não encontrado" se o ID não existir!

    private final UsuarioService usuarioService;

    // 3. Construtor: O Spring injeta as duas dependências automaticamente.

    public ChamadoService(ChamadoRepository repository, UsuarioService usuarioService){
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    /**
     * 4. BUSCAR POR ID
     */

    public Chamado findById(Long id){
        Optional<Chamado> obj = repository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Chamado nao encontrado! ID: " + id));
    }

    /**
     * 5. BUSCAR TODOS
     */

    public List<Chamado> findAll() {
        return repository.findAll();
    }

    /**
     * 6. CRIAR NOVO CHAMADO
     * Recebemos o DTO (seguro) da internet e precisamos transformá-lo numa Entidade (Chamado)
     * para salvar no banco de dados.
     */

    public Chamado create(ChamadoDTO objDTO) {
        return repository.save(newChamado(objDTO));

    }

    /**
     * 7. O MÉT0DO FÁBRICA (O Segredo da Conversão)
     * Este mét0do pega os IDs de técnico e cliente que vieram no DTO, busca esses usuários
     * de verdade no banco, e monta o objeto Chamado completo.
     */

    private Chamado newChamado(ChamadoDTO obj) {
        // Vai ao banco buscar quem é o técnico e o cliente pelos IDs que vieram no JSON
        Usuario tecnico = usuarioService.findById(obj.getTecnico());
        Usuario cliente = usuarioService.findById(obj.getCliente());

    // Cria um Chamado vazio
    Chamado chamado = new Chamado();

    // Se for uma atualização (ID não nulo), mantém o ID. Se for criação, fica nulo para gerar um novo.
    if (obj.getId() != null) {
        chamado.setId(obj.getId());
    }
    // Preenche os dados básicos usando o próprio Enum para garantir os valores
    // Nota: Se o seu Enum for diferente (ex: Status.toEnum(codigo)), ajustamos isso!

    chamado.setPrioridade(Prioridade.values()[obj.getPrioridade()]);
    chamado.setStatus(Status.values()[obj.getStatus()]);
    chamado.setObservacoes(obj.getObservacoes());
    chamado.setTitulo(obj.getTitulo());
    // O mais importante: Atribui os OBJETOS Usuario completos ao invés de apenas os IDs

    chamado.setTecnico(tecnico);
    chamado.setCliente(cliente);

    return chamado;
    }

    /**
     * 8. ATUALIZAR CHAMADO
     * Reutilizamos a nossa "Fábrica" (newChamado) para converter o DTO e validar os usuários.
     */

    public Chamado update(Long id, ChamadoDTO objDTO) {
        // 1. Garante que o DTO tem o mesmo ID que foi passado na URL
        objDTO.setId(id);
        // 2. Busca o chamado antigo no banco. Se não existir, já estoura o Erro 404 aqui!
        Chamado oldObj = findById(id);
        // 3. Fabrica o novo chamado com os dados atualizados que vieram da internet
        Chamado newObj = newChamado(objDTO);
        // 4. Detalhe de ouro: Preserva a data de abertura original (para não ser sobrescrita por "hoje")
        newObj.setDataAbertura(oldObj.getDataAbertura());
        // 5. Salva no banco (Como o objeto já tem um ID, o JPA faz um UPDATE em vez de INSERT)
        return repository.save(newObj);
        }

        /**
         * 9. DELETAR CHAMADO
         */

        public void delete(Long id){

            // Busca primeiro para garantir que existe (e lançar o 404 se não existir)
            Chamado obj = findById(id);
            // Manda o banco apagar
            repository.delete(obj);
        }

    }


