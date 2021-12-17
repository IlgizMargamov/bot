import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.common.player.BasePlayer;
import com.games.fool.FoolLogic;
import org.junit.jupiter.api.Test;

class TestFool {

    @Test
    public void TestGame(){
        BasePlayer player1 = new BasePlayer();
        BasePlayer player2 = new BasePlayer();
        var players = new BasePlayer[]{player1,player2};
        FoolLogic game = new FoolLogic(players,new Deck(DeckType.MEDIUM));
        game.startGame();
    }
}
