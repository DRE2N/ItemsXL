package de.erethon.itemsxl.recipe;

import de.erethon.caliburn.recipe.CustomRecipe;

public class EditSession {

    private final String id;
    private final CustomRecipe existing;
    private boolean shaped = true;

    public EditSession(String id, CustomRecipe existing) {
        this.id = id;
        this.existing = existing;
    }

    public void setShaped(boolean shaped) {
        this.shaped = shaped;
    }

    public String getId() {
        return id;
    }

    public CustomRecipe getExisting() {
        return existing;
    }

    public boolean isExisting() {
        return existing != null;
    }

    public boolean isShaped() {
        return shaped;
    }
}
