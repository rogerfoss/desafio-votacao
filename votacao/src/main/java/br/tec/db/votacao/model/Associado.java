package br.tec.db.votacao.model;

import br.tec.db.votacao.enums.AssociadoStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "associado")
@EqualsAndHashCode
public class Associado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @Enumerated(EnumType.STRING)
    private AssociadoStatusEnum status;

}