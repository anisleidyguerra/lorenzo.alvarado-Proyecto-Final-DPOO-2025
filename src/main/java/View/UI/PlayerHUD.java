package View.UI;

import Domain.Entity.Characters.Players.PlayerEntity;
import Domain.Entity.Characters.Players.Hero;
import Domain.Entity.Elements.ObjetosMagicos.GlobalInventory;
import Domain.Entity.Elements.ObjetosMagicos.Item;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;

public class PlayerHUD {

    public PlayerEntity playerEntity;

    private VBox hudContainer;

    private Text lifeValueText;
    private StackPane lifeBar;
    private StackPane lifeBarContainer;

    private VBox specialAbilityContainer;
    private Text specialAbilityStatusText;
    private Text specialAbilityCooldownText;

    private VBox inventoryContainer;
    private HBox inventoryImagesRow;

    public PlayerHUD(PlayerEntity playerEntity, int index) {
        this.playerEntity = playerEntity;
        initHUD(index);
    }

    private void initHUD(int index) {
        hudContainer = new VBox(10);
        hudContainer.setId("hud-container");

        hudContainer.setTranslateX(20);
        hudContainer.setTranslateY(20 + index * 120);

        lifeBarContainer = new StackPane();
        lifeBarContainer.getStyleClass().add("life-bar-container");
        lifeBarContainer.setPrefSize(300, 30);

        lifeBar = new StackPane();
        lifeBar.getStyleClass().add("life-bar");

        Pane galaxyEffect = new Pane();
        galaxyEffect.getStyleClass().add("life-bar-galaxy-effect");
        galaxyEffect.setPrefSize(300, 30);

        Text lifeText = new Text("BARRA DE VIDA GALAXIA");
        lifeText.setId("life");
        StackPane.setAlignment(lifeText, javafx.geometry.Pos.TOP_LEFT);
        StackPane.setMargin(lifeText, new javafx.geometry.Insets(2, 5, 0, 0));

        lifeValueText = new Text();
        lifeValueText.setId("life");
        StackPane.setAlignment(lifeValueText, javafx.geometry.Pos.TOP_RIGHT);
        StackPane.setMargin(lifeValueText, new javafx.geometry.Insets(2, 5, 0, 0));

        lifeBarContainer.getChildren().addAll(lifeBar, galaxyEffect, lifeText, lifeValueText);

        specialAbilityContainer = new VBox(5);
        specialAbilityContainer.getStyleClass().add("special-ability-container");

        specialAbilityStatusText = new Text("HABILIDAD ESPECIAL");
        specialAbilityStatusText.setId("specialAbility");

        specialAbilityCooldownText = new Text("RECARGA: 0 TURNOS");
        specialAbilityCooldownText.setId("cooldown");

        specialAbilityContainer.getChildren().addAll(specialAbilityStatusText, specialAbilityCooldownText);

        inventoryContainer = new VBox(5);
        inventoryContainer.getStyleClass().add("inventory-container");

        Text inventoryTitle = new Text("INVENTARIO");
        inventoryTitle.setId("inventory-title");

        inventoryImagesRow = new HBox(5);
        inventoryImagesRow.setId("inventory-images-row");

        inventoryContainer.getChildren().addAll(inventoryTitle, inventoryImagesRow);

        hudContainer.getChildren().addAll(lifeBarContainer, specialAbilityContainer, inventoryContainer);


        updateHUD();
    }

    public void updateHUD() {
        if (playerEntity == null || playerEntity.getHero() == null) {
            setDeadState();
            return;
        }

        Hero hero = playerEntity.getHero();

        updateHealthBar(hero);
        updateSpecialAbility(hero);
        updateInventoryImages();
    }

    private void updateHealthBar(Hero hero) {
        int currentHealth = Math.max(0, hero.health);
        int maxHealth = getMaxHealth(hero);

        double healthPercentage = (maxHealth > 0) ? (double) currentHealth / maxHealth : 0;
        healthPercentage = Math.max(0, Math.min(1, healthPercentage));

        lifeBar.setPrefWidth(300 * healthPercentage);
        lifeValueText.setText(currentHealth + "/" + maxHealth);
    }

    private void updateSpecialAbility(Hero hero) {
        String status = hero.canUseSpecial() ? "DISPONIBLE" : "EN RECARGA";
        specialAbilityStatusText.setText("HABILIDAD: " + status);

        int cooldown = Math.max(0, hero.currentCooldown);
        specialAbilityCooldownText.setText("RECARGA: " + cooldown + " TURNOS");
    }



    private void setDeadState() {
        lifeBar.setPrefWidth(0);
        lifeValueText.setText("0/0");
        specialAbilityStatusText.setText("HABILIDAD: INACTIVA");
        specialAbilityCooldownText.setText("JUGADOR DERROTADO");
        inventoryContainer.setVisible(false);
    }

    private int getMaxHealth(Hero hero) {
        return 20; // Ajusta si tienes maxHealth en Hero
    }

    public void setVisible(boolean visible) {
        hudContainer.setVisible(visible);
    }
    public void updateInventoryImages() {
        inventoryImagesRow.getChildren().clear();

        List<Item> items = GlobalInventory.getItems();

        if (items.isEmpty()) {
            inventoryContainer.setVisible(false);
            return;
        }

        inventoryContainer.setVisible(true);

        for (Item item : items) {
            ImageView icon = new ImageView(item.getImage());
            icon.setFitWidth(32); // Usamos solo una configuración coherente
            icon.setFitHeight(32);
            icon.getStyleClass().add("inventory-icon");
            inventoryImagesRow.getChildren().add(icon);
        }
    }

    public Node getRoot() {
        return hudContainer;
    }




}
