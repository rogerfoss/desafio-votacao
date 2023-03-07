package br.tec.db.votacao.model;

import br.tec.db.votacao.enums.AssociadoStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Getter
@Setter
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
    @CPF(message = "CPF inválido")
    private String cpf;

    @Enumerated(EnumType.STRING)
    private AssociadoStatusEnum status;

}