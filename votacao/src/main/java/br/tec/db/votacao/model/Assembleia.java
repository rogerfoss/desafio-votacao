package br.tec.db.votacao.model;

import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assembleia")
@EqualsAndHashCode
public class Assembleia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime inicio = LocalDateTime.now();

    private LocalDateTime fim;

    @Enumerated(EnumType.STRING)
    private AssembleiaStatusEnum status;

    @OneToMany
    @JoinTable(name = "assembleia_pautas", joinColumns = @JoinColumn(name = "assembleia_id"),
            inverseJoinColumns = @JoinColumn(name = "pauta_id"))
    private List<Pauta> pautas;

}
