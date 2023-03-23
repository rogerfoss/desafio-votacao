package br.tec.db.votacao.model;

import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sessao_de_votacao")
public class SessaoDeVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime inicio = LocalDateTime.now();

    private LocalDateTime fim;

    @Enumerated(EnumType.STRING)
    private SessaoDeVotacaoStatusEnum status;

    @OneToOne
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    @OneToMany
    @JoinTable(name = "sessao_de_votacao_votos", joinColumns = @JoinColumn(name = "sessao_de_votacao_id"),
            inverseJoinColumns = @JoinColumn(name = "voto_id"))
    private List<Voto> votos;

}
