package telegram;

import com.games.fool.FoolPlayer;

import java.util.List;

public class Lobby {
    public String m_creator;
    public String m_pin;
    public List<FoolPlayer> m_playerList;

    public Lobby(String creator, String pin, List<FoolPlayer> playersList) {
        m_creator = creator;
        m_pin = pin;
        m_playerList = playersList;
    }
}
