package serie06.sentence;

import java.util.EventListener;

public interface SentenceListener extends EventListener {
    void sentenceSaid(SentenceEvent e);
}
