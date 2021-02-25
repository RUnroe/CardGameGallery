package blackjack.presenters;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BlackjackGameScene {

    //    public ImageView imageViewQueenOfHearts;
    public AnchorPane anchorPaneRoot;
    public AnchorPane anchorPanePlayerField;
    public HBox hBoxHouseHand;
    public Label lblHouseName;

    public HBox hBoxPlayerOneHand;
    public HBox hBoxPlayerTwoHand;
    public HBox hBoxPlayerThreeHand;
    public HBox hBoxPlayerFourHand;
    public HBox hBoxPlayerFiveHand;

    public Label lblPlayerOneName;
    public Label lblPlayerTwoName;
    public Label lblPlayerThreeName;
    public Label lblPlayerFourName;
    public Label lblPlayerFiveName;

    public void exampleDisplayHand(Player player) {
        AtomicInteger translation = new AtomicInteger(0);
        AtomicInteger rotation = new AtomicInteger(-2 * player.getHand().size());
        if (anchorPaneRoot == null) {
            anchorPaneRoot = new AnchorPane();
        }
        player.getHand().forEach(card -> {
//            card.getImageView().setX(card.getImageView().getX() + translation.get());
//            card.getImageView().setRotate(rotation.doubleValue());
            hBoxPlayerOneHand.getChildren().add(card.getImageView()); // adds card image view to anchor pane
//            translation.getAndAdd(15);
//            rotation.getAndAdd(5);
        });
    }

    public List<ImageView> getImageViews() {
        List<ImageView> imageViews = new ArrayList<>();
        hBoxPlayerOneHand.getChildren().forEach(node -> {
            if (node instanceof ImageView) {
                imageViews.add((ImageView) node);
            }
        });
        return imageViews;
    }
}
