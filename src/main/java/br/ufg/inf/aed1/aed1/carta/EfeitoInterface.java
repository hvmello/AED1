package br.ufg.inf.aed1.aed1.carta;

import br.ufg.inf.aed1.aed1.gameplay.Game;
import br.ufg.inf.aed1.aed1.gameplay.Player;

public interface EfeitoInterface {

    void aplicarEfeito(Game game, Player targetPlayer, int targetIndex);

}
