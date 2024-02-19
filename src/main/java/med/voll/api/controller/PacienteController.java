package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.paciente.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pacientes")
@SecurityRequirement(name = "bearer-Leo")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriCadastro) {
        var paciente = new Paciente(dados);
        repository.save(paciente);

        var uri = uriCadastro.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body( new DadosPacienteDetalhado(paciente));
    }


    @GetMapping
    public ResponseEntity <Page<DadosListagemPaciente>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);

        return ResponseEntity.ok(page);

    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados){
        var  paciente = repository.getReferenceById(dados.id());
        paciente.altualizarInformacoes(dados);

        return  ResponseEntity.ok(new DadosPacienteDetalhado(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir( @PathVariable Long id){
        var  paciente = repository.getReferenceById(id);
        paciente.excluir();

        return ResponseEntity.noContent().build();

    }
    @GetMapping("/{id}")
    public  ResponseEntity detalharCadastroPaciente(@PathVariable Long id){
        var paciente = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosPacienteDetalhado(paciente));
    }


}
