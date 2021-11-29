import com.common.deck.Deck;
import com.common.deck.DeckType;
import com.games.fool.FoolPlayer;
import com.games.fool.foolLogic;
import org.junit.jupiter.api.Test;

class TestFool {

    @Test
    public void TestFool(){
        FoolPlayer player1 = new FoolPlayer();
        FoolPlayer player2 = new FoolPlayer();
        var players = new FoolPlayer[]{player1,player2};
        foolLogic game = new foolLogic(players,new Deck(DeckType.MEDIUM));
        game.StartGame();
    }
}
