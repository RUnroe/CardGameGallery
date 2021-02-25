package sample;

import blackjack.controllers.BlackjackEngine;
import javafx.event.ActionEvent;

public class Controller {
    public void onActionBtnPlayBlackjack(ActionEvent actionEvent) {
        new BlackjackEngine().setup();
    }
}
