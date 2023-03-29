package br.tec.db.votacao.validation;

import br.tec.db.votacao.enums.AssembleiaStatusEnum;
import br.tec.db.votacao.enums.PautaStatusEnum;
import br.tec.db.votacao.enums.SessaoDeVotacaoStatusEnum;
import br.tec.db.votacao.exception.BadRequestException;
import br.tec.db.votacao.model.Assembleia;
import br.tec.db.votacao.model.Associado;
import br.tec.db.votacao.model.Pauta;
import br.tec.db.votacao.model.SessaoDeVotacao;

public class ValidaServices {

    public static void validaStatusAssembleia(Assembleia assembleia, String mensagem) {
        if (assembleia.getStatus().equals(AssembleiaStatusEnum.ENCERRADA)) {
            throw new BadRequestException(mensagem);
        }
    }

    public static void validaStatusPauta(Pauta pauta) {
        if (pauta.getStatus().equals(PautaStatusEnum.APROVADA) ||
                pauta.getStatus().equals(PautaStatusEnum.REPROVADA)) {
            throw new BadRequestException("Não foi possível criar a sessão de votação, pauta já definida.");
        } else if (pauta.getStatus().equals(PautaStatusEnum.AGUARDANDO_VOTACAO)) {
            throw new BadRequestException("Não foi possível criar a sessão de votação, " +
                    "pauta já possui sessão de votação.");
        }
    }

    public static void validaStatusSessaoDeVotacao(SessaoDeVotacao sessaoDeVotacao) {
        if (sessaoDeVotacao.getStatus().equals(SessaoDeVotacaoStatusEnum.ENCERRADA)) {
            throw new BadRequestException("Sessão de votação já encerrada");
        }
    }

    public static void validaAssociadoJaVotou(SessaoDeVotacao sessaoDeVotacao, Associado associado) {
        if (sessaoDeVotacao.getVotos().stream()
                .anyMatch(voto -> voto.getAssociado().getId().equals(associado.getId()))) {

            throw new BadRequestException("Associado já votou nesta sessão");
        }
    }

}
