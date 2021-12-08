package telegram;

import java.util.Map;

public class Lobby {
    public String m_creator;
    public String m_pin;
    public Map<String, String> m_playerNameToChatId;

    public Lobby(String creator, String pin, Map<String, String> playerNameToChatId) {
        m_creator = creator;
        m_pin = pin;
        m_playerNameToChatId = playerNameToChatId;
    }
}
