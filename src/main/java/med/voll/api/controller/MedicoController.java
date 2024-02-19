package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
@SecurityRequirement(name = "bearer-Leo")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;
    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriCadastoApp){

        var medico = new Medico(dados);
        repository.save(medico);

        var uri = uriCadastoApp.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosMedicoDetalhado(medico));

    }
    @GetMapping
    public ResponseEntity <Page<DadosListagemMedico>>listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        var  page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);

    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
         var  medico = repository.getReferenceById(dados.id());
         medico.altualizarInformacoes(dados);

           return ResponseEntity.ok(new DadosMedicoDetalhado(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){

        var  medico = repository.getReferenceById(id);
        medico.excluir();
          return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity detalharCadastroMedico(@PathVariable Long id){

        var  medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosMedicoDetalhado(medico));

    }

}
