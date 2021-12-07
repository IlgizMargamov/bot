import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.games.fool.FoolPlayer;
import org.junit.jupiter.api.Test;
import com.games.fool.FoolLogic;

class TestFool {

    @Test
    public void TestFool1(){
        FoolPlayer player1 = new FoolPlayer();
        FoolPlayer player2 = new FoolPlayer();
        var players = new FoolPlayer[]{player1,player2};
        FoolLogic game = new FoolLogic(players,new Deck(DeckType.MEDIUM));
        game.startGame();
    }
}
