import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.common.player.BasePlayer;
import com.games.fool.FoolLogic;
import org.junit.jupiter.api.Test;

class TestFool {

    @Test
    public void TestFool1(){
        BasePlayer player1 = new BasePlayer("player1");
        BasePlayer player2 = new BasePlayer("player2");
        BasePlayer player3 = new BasePlayer("player3");
        BasePlayer[] players = new BasePlayer[]{player1,player2,player3};
        FoolLogic game = new FoolLogic(players,new Deck(DeckType.MEDIUM));
        game.startGame();
    }
}
